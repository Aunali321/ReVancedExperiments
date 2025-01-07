package li.auna.patches.instagram.layout

import app.revanced.patcher.extensions.InstructionExtensions.addInstructions
import app.revanced.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.Opcode

import li.auna.util.indexOfFirstInstructionOrThrow

@Suppress("unused")
val enableDeveloperMenuPatch = bytecodePatch(
    name = "Enable Developer Menu",
    description = "Enables the developer menu.",
) {
    compatibleWith("com.instagram.android")

    execute {
        shouldAddPrefTTLFingerprint.method.apply {
            val isDeveloperMethodCallIndex = indexOfFirstInstructionOrThrow { opcode == Opcode.INVOKE_STATIC }


            val isDeveloperMethod = navigate(this).to(isDeveloperMethodCallIndex).stop()

            // Enable the developer menu.
            isDeveloperMethod.addInstructions(
                0,
                """
                   const v0, 0x1
                    return v0
                """,
            )
        }
    }
}