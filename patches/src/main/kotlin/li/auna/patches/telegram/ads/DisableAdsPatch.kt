package li.auna.patches.telegram.ads

import app.revanced.patcher.patch.bytecodePatch
import li.auna.util.returnEarly

@Suppress("unused")
val hideAdsPatch = bytecodePatch(
    name = "Hide sponsored ads",
    description = "Hides sponsored ads in channels",
) {
    compatibleWith(
        "org.telegram.messenger",
        "org.telegram.messenger.web",
        "uz.unnarsx.cherrygram"
    )

    execute {
        hideSponsoredMessagesFingerprint.method.returnEarly()
    }
}
