package li.auna.patches.instagram.adblock

import app.revanced.patcher.extensions.InstructionExtensions.addInstructions
import app.revanced.patcher.patch.bytecodePatch

@Suppress("unused")
val adBlockPatch = bytecodePatch(
    name = "Hide Ads",
    description = "Hides ads in stories, discover, profile, etc. " +
            "An ad can still appear once when refreshing the home feed.",
) {
    compatibleWith("com.instagram.android")

    execute {
        adInjectorFingerprint.method.addInstructions(
            0,
            """
                const/4 v0, 0x0
                return v0
            """,
        )
    }
}