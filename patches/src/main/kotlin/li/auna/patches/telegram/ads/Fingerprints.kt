package li.auna.patches.telegram.ads

import app.revanced.patcher.fingerprint

internal val hideSponsoredMessagesFingerprint = fingerprint {
    returns("V")
    custom { methodDef, classDef ->
        // org.telegram.ui.ChatActivity -> Lorg/telegram/ui/ChatActivity;
        classDef.type.endsWith("Lorg/telegram/ui/ChatActivity;") && methodDef.name == "addSponsoredMessages"
    }
}