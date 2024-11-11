package li.auna.patches.telegram.bypassintegrity

import app.revanced.patcher.Fingerprint
import app.revanced.patcher.extensions.InstructionExtensions.getInstruction
import app.revanced.patcher.extensions.InstructionExtensions.replaceInstruction
import app.revanced.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction

@Suppress("unused")
val bypassIntegrityPatch = bytecodePatch(
    name = "Bypass Integrity",
    description = "Bypass integrity check to allow login",
) {
    compatibleWith(
        "org.telegram.messenger",
        "org.telegram.messenger.web"
    )

    execute {
        fun Fingerprint.patch() {
            method.apply {
                for (index in stringMatches!!.map { it.index + 2 }) {
                    val instructionRegister = getInstruction<OneRegisterInstruction>(index).registerA
                    replaceInstruction(
                        index,
                        "const/4 v$instructionRegister, 0x1",
                    )
                }
            }
        }

        bypassIntegrityFingerprint.patch()
    }
}

