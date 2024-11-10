package li.auna.patches.telegram.typingindicator

import app.revanced.patcher.patch.bytecodePatch
import li.auna.util.returnEarly

@Suppress("unused")
val hideAdsPatch = bytecodePatch(
    name = "Hide sponsored ads",
    description = "Hides sponsored ads in channels",
) {
    compatibleWith(
        "org.telegram.messenger",
        "org.telegram.messenger.web"
    )

    execute {
        needSendTypingFingerprint.method.returnEarly()
    }
}