package li.auna.patches.instagram.interaction.bio

import app.revanced.patcher.extensions.InstructionExtensions.addInstructions
import app.revanced.patcher.extensions.InstructionExtensions.getInstruction
import app.revanced.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction

import li.auna.util.indexOfFirstInstructionOrThrow

@Suppress("unused")
val selectableBioPatch = bytecodePatch(
    name = "Selectable Bio",
    description = "Makes user's bio selectable.",
) {
    compatibleWith("com.instagram.android")

    execute {
        selectableBioFingerprint.method.apply {
            val setBioTextIndex = indexOfFirstInstructionOrThrow { opcode == Opcode.INVOKE_VIRTUAL }
            val setTextViewInstruction = getInstruction<FiveRegisterInstruction>(setBioTextIndex)
            val textViewRegister = setTextViewInstruction.registerC
            val textRegister = setTextViewInstruction.registerD

            // Make the textview selectable.
            addInstructions(
                setBioTextIndex + 1,
                """
                    const/4 v$textRegister, 0x1
                    invoke-virtual { v$textViewRegister, v$textRegister }, Landroid/widget/TextView;->setTextIsSelectable(Z)V
                """,
            )
        }
    }
}