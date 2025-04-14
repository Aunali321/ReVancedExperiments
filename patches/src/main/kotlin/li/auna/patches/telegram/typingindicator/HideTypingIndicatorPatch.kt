package li.auna.patches.telegram.typingindicator

import app.revanced.patcher.patch.bytecodePatch
import li.auna.util.returnEarly

@Suppress("unused")
val hideAdsPatch = bytecodePatch(
    name = "Hide typing indicator",
    description = "Hides your typing indicator from other users",
) {
    compatibleWith(
        "org.telegram.messenger",
        "org.telegram.messenger.web",
        "uz.unnarsx.cherrygram",
        "org.telegram.messenger.beta",
        "org.forkgram.messenger",
        "tw.nekomimi.nekogram",
        "org.telegram.plus"
    )

    execute {
        needSendTypingFingerprint.method.returnEarly()
    }
}
