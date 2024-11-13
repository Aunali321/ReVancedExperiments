package li.auna.patches.telegram.ads

import app.revanced.patcher.fingerprint

internal val hideSponsoredMessagesFingerprint = fingerprint {
    returns("V")
    custom { methodDef, classDef ->
        methodDef.name == "addSponsoredMessages" && classDef.type.endsWith("Lorg/telegram/ui/ChatActivity;")
    }
}