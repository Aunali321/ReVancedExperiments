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
        "org.telegram.messenger.web",
        "uz.unnarsx.cherrygram"
    )

    apply {
        listOf(
            isPremiumUserMethod,
            isPremiumMethod,
            isPremiumForStoryMethod,
        ).forEach { it.returnEarly(true) }
    }
}
