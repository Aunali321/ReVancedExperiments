package li.auna.patches.all.misc.deobfuscate

import app.revanced.patcher.extensions.InstructionExtensions.getInstruction
import app.revanced.patcher.extensions.InstructionExtensions.instructions
import app.revanced.patcher.extensions.InstructionExtensions.instructionsOrNull
import app.revanced.patcher.extensions.InstructionExtensions.removeInstruction
import app.revanced.patcher.extensions.InstructionExtensions.replaceInstruction
import app.revanced.patcher.patch.bytecodePatch
import li.auna.util.getReference
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction21c
import com.android.tools.smali.dexlib2.iface.ClassDef
import com.android.tools.smali.dexlib2.iface.Method
import com.android.tools.smali.dexlib2.iface.instruction.*
import com.android.tools.smali.dexlib2.iface.reference.StringReference
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableStringReference
import com.android.tools.smali.dexlib2.util.MethodUtil
import java.util.*
import java.util.concurrent.Executors

val deobfuscateSmobPatch = bytecodePatch(
    name = "Deobfuscate Smob",
    use = false
) {
    execute {
        val remove = Collections.synchronizedList(mutableListOf<Triple<ClassDef, Method, Int>>())

        fun collectRemoveHeader() {
            val executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

            val tasks = classes.map { classDef ->
                Runnable {
                    classDef.methods.forEach methods@{ method ->
                        method.instructionsOrNull ?: return@methods

                        val s = method.instructions.first()
                            .getReference<StringReference>()?.string ?: return@methods

                        if (s.startsWith("  ~@~")) {
                            remove += Triple(classDef, method, 0)
                        }
                    }
                }
            }
            tasks.forEach { executor.execute(it) }
            executor.shutdown()
            while (!executor.isTerminated) {
            }
        }

        fun collectRemoveUnnecessaryInstructions() {
            val executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
            val tasks = classes.map { classDef ->
                Runnable {
                    classDef.methods.forEach methods@{ method ->
                        method.instructionsOrNull ?: return@methods

                        val parameterCount = method.parameters.size
                        val registerCount = method.implementation!!.registerCount
                        val isStatic = method.accessFlags and AccessFlags.STATIC.value != 0
                        val parameterRegisters = parameterCount + if (isStatic) 0 else 1
                        val possiblyUnusedRegisters = 0..<registerCount - parameterRegisters

                        val insns = method.instructions
                            .asSequence()
                            .filter { it.opcode != Opcode.CONST_4 }
                            .let {
                                if (it.first().opcode == Opcode.CONST_STRING_JUMBO || it.first().opcode == Opcode.CONST_STRING) {
                                    it.drop(1)
                                } else {
                                    it
                                }
                            }

                        fun methodUsesRegister(possiblyUnusedRegister: Int) = insns.any {
                            when (it) {
                                is RegisterRangeInstruction -> {
                                    val start = it.startRegister
                                    val end = it.startRegister + it.registerCount - 1

                                    possiblyUnusedRegister in start..end
                                }

                                is FiveRegisterInstruction ->
                                    possiblyUnusedRegister in listOf(
                                        it.registerC,
                                        it.registerD,
                                        it.registerE,
                                        it.registerF,
                                        it.registerG,
                                    ).take(it.registerCount)

                                is ThreeRegisterInstruction ->
                                    it.registerA == possiblyUnusedRegister ||
                                            it.registerB == possiblyUnusedRegister ||
                                            it.registerC == possiblyUnusedRegister

                                is TwoRegisterInstruction -> {
                                    if (it.registerA == possiblyUnusedRegister &&
                                        (it.opcode == Opcode.MOVE || it.opcode.ordinal in Opcode.ADD_INT_2ADDR.ordinal..Opcode.REM_DOUBLE_2ADDR.ordinal)
                                    ) {
                                        return@any false
                                    }

                                    it.registerA == possiblyUnusedRegister ||
                                            it.registerB == possiblyUnusedRegister
                                }

                                is OneRegisterInstruction -> {
                                    it.opcode != Opcode.CONST_STRING &&
                                            it.opcode != Opcode.CONST_STRING_JUMBO &&
                                            it.registerA == possiblyUnusedRegister
                                }

                                else -> false
                            }
                        }
                        if (method.name == "q" && "Lp/en;" == classDef.type) {
                            println()
                        }
                        possiblyUnusedRegisters.filter { !methodUsesRegister(it) }
                            .forEach { unusedRegister ->

                                method.instructions.withIndex()
                                    .filter { (_, it) ->
                                        it.opcode == Opcode.CONST_4 ||
                                                it.opcode == Opcode.MOVE ||
                                                it.opcode.ordinal in Opcode.ADD_INT_2ADDR.ordinal..Opcode.REM_DOUBLE_2ADDR.ordinal
                                    }
                                    .filter { (_, inst) -> (inst as? OneRegisterInstruction)?.registerA == unusedRegister }
                                    .forEach {
                                        remove += Triple(classDef, method, it.index)
                                    }
                            }
                    }
                }
            }
            tasks.forEach { executor.execute(it) }
            executor.shutdown()
            while (!executor.isTerminated) {
            }
        }

        fun collectRemoveDuplicateInstructions() {
            val executor = Executors.newFixedThreadPool(1)
            val tasks = classes.map { classDef ->
                Runnable {
                    classDef.methods.forEach methods@{ method ->
                        method.instructionsOrNull ?: return@methods
                        if (method.name == "b" && "Lp/fd0;" == classDef.type) {
                            println()
                        }
                        val instructions = method.instructions
                        val toRemove = mutableListOf<Int>()

                        for (i in 0 until instructions.count() - 1) {
                            val current = instructions.elementAt(i)
                            val next = instructions.elementAt(i + 1)

                            if (
                                current is ReferenceInstruction &&
                                next is ReferenceInstruction &&
                                (
                                        (
                                                current.opcode == Opcode.CONST_CLASS &&
                                                        next.opcode == Opcode.CONST_CLASS &&
                                                        current.reference == next.reference
                                                )
                                        )
                            ) {
                                toRemove.add(i)
                            } else if (current.opcode.ordinal in (Opcode.NOP.ordinal..Opcode.MOVE_EXCEPTION.ordinal) &&
                                current.opcode == next.opcode &&
                                current is TwoRegisterInstruction &&
                                next is TwoRegisterInstruction &&
                                current !is ThreeRegisterInstruction &&
                                next !is ThreeRegisterInstruction &&
                                current.registerA == next.registerA &&
                                current.registerB == next.registerB
                            ) {
                                toRemove.add(i)
                            } else if (
                                (current.opcode == Opcode.CONST_STRING_JUMBO || current.opcode == Opcode.CONST_STRING) &&
                                (next.opcode == Opcode.CONST_STRING || next.opcode == Opcode.CONST_STRING_JUMBO) &&
                                current is OneRegisterInstruction &&
                                next is OneRegisterInstruction &&
                                current.registerA == next.registerA
                            ) {
                                toRemove.add(i)
                            } else if (
                                current.opcode == next.opcode &&
                                current is OneRegisterInstruction &&
                                next is OneRegisterInstruction &&
                                current !is TwoRegisterInstruction &&
                                next !is TwoRegisterInstruction &&
                                current.registerA == next.registerA
                            ) {
                                toRemove.add(i)
                            }
                        }

                        if (toRemove.isEmpty()) return@methods

                        toRemove.forEach {
                            remove += Triple(classDef, method, it)
                        }
                    }
                }
            }
            tasks.forEach { executor.execute(it) }
            executor.shutdown()
            while (!executor.isTerminated) {
            }
        }

        collectRemoveHeader()
        collectRemoveUnnecessaryInstructions()
        collectRemoveDuplicateInstructions()

        val toRemove = remove.groupBy({ it.first }, { it.second to it.third })
            .mapValues { (_, pairs) ->
                pairs.groupBy({ it.first }, { it.second })
                    .mapValues { (_, indices) -> indices.toSet().sortedDescending() }
            }
            .entries.groupBy({ proxy(it.key).mutableClass }, { it.value }) // Group before converting to a map
            .mapValues { (_, values) ->
                values.reduce { acc, map -> acc + map } // Merge colliding entries
            }
            .mapValues { (classDef, methodIndicesPair) ->
                methodIndicesPair.mapKeys { (method, _) ->
                    classDef.methods.first {
                        MethodUtil.methodSignaturesMatch(it, method)
                    }
                }
            }

        val executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
        val tasks = toRemove.map { (_, methods) ->
            Runnable {
                methods.forEach { (method, indices) ->
                    val insnSize = method.instructions.size
                    indices.forEach ix@{ index ->
                        method.removeInstruction(index)

                        if (insnSize <= index + 1) {
                            return@ix
                        }

                        val insn = method.getInstruction(index + 1)

                        if (insn.opcode != Opcode.CONST_STRING_JUMBO) {
                            return@ix
                        }

                        method.replaceInstruction(
                            index + 1,
                            BuilderInstruction21c(
                                Opcode.CONST_STRING,
                                (insn as OneRegisterInstruction).registerA,
                                ImmutableStringReference(insn.getReference<StringReference>()!!.string),
                            ),
                        )
                    }
                }
            }
        }
        tasks.forEach { executor.execute(it) }
        executor.shutdown()
        while (!executor.isTerminated) {
        }
    }
}