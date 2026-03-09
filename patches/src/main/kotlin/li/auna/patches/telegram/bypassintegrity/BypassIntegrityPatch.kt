package li.auna.patches.telegram.bypassintegrity

import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.extensions.getInstruction
import app.revanced.patcher.extensions.replaceInstruction
import app.revanced.patcher.extensions.string
import app.revanced.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import li.auna.util.indexOfFirstInstruction

@Suppress("unused")
val bypassIntegrityPatch = bytecodePatch(
    name = "Bypass Integrity",
    description = "Bypass integrity check to allow login",
) {
    compatibleWith(
        "org.telegram.messenger",
        "org.telegram.messenger.web",
        "uz.unnarsx.cherrygram"
    )

    apply {
        bypassIntegrityMethod.apply {
            val stringsToMatch = listOf("basicIntegrity", "ctsProfileMatch")

            // Find indices of the matched strings and patch 2 instructions after each.
            implementation!!.instructions.forEachIndexed { index, instruction ->
                if (instruction.string in stringsToMatch) {
                    val patchIndex = index + 2
                    val instructionRegister = getInstruction<OneRegisterInstruction>(patchIndex).registerA
                    replaceInstruction(
                        patchIndex,
                        "const/4 v$instructionRegister, 0x1",
                    )
                }
            }
        }

        spoofSignatureMethod.apply {
            addInstructions(
                0,
                """
                   const-string v0, "49C1522548EBACD46CE322B6FD47F6092BB745D0F88082145CAF35E14DCC38E1"
                   return-object v0
                """
            )
        }
    }
}
