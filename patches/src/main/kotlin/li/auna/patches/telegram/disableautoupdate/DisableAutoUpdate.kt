package li.auna.patches.telegram.disableautoupdate

import app.revanced.patcher.extensions.InstructionExtensions.addInstruction
import app.revanced.patcher.patch.bytecodePatch
import li.auna.util.returnEarly

@Suppress("unused")
val unlockProPatch = bytecodePatch(
    name = "Disable Auto Update",
    description = "Disable Auto Update",
) {
    compatibleWith(
        "org.telegram.messenger", "org.telegram.messenger.web", "uz.unnarsx.cherrygram"
    )

    execute {
        checkAppUpdateFingerprint.apply {
            method.addInstruction(
                patternMatch!!.endIndex + 1, "const/4 v0, 0x0"
            )
        }
        setNewAppVersionAvailableFingerprint.method.returnEarly(false)
        blockViewUpdateFingerprint.method.returnEarly()
    }
}
