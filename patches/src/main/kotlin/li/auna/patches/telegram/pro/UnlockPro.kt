package li.auna.patches.telegram.pro

import app.revanced.patcher.patch.bytecodePatch
import li.auna.util.returnEarly

@Suppress("unused")
val unlockProPatch = bytecodePatch(
    name = "Unlock Pro",
    description = "Unlock client-side Pro features",
) {
    compatibleWith(
        "org.telegram.messenger",
        "org.telegram.messenger.web"
    )

    execute {
        setOf(
            isPremiumUserFingerprint,
            isPremiumFingerprint,
            isPremiumForStoryFingerprint
        ).forEach { it.method.returnEarly(true) }
    }
}