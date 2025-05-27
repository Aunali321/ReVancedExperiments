package li.auna.patches.instagram.adblock

import app.revanced.patcher.patch.bytecodePatch
import li.auna.util.returnEarly

@Suppress("unused")
val adBlockPatch = bytecodePatch(
    name = "Hide Ads",
    description = "Hides ads in stories, discover, profile, etc. " +
            "An ad can still appear once when refreshing the home feed.",
) {
    compatibleWith(
        "com.instagram.android",
        "com.instagram.barcelona",
    )

    execute {
        adInjectorFingerprint.method.returnEarly(false)
    }
}