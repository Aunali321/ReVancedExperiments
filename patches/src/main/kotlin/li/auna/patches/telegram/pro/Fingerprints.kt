package li.auna.patches.telegram.pro

import app.revanced.patcher.fingerprint

internal val isPremiumUserFingerprint = fingerprint {
    returns("Z")
    custom { methodDef, classDef ->
        methodDef.name == "isPremiumUser" && classDef.type.endsWith("Lorg/telegram/messenger/MessagesController;")

    }
}

internal val isPremiumFingerprint = fingerprint {
    returns("Z")
    custom { methodDef, classDef ->
        methodDef.name == "isPremium" && classDef.type.endsWith("Lorg/telegram/messenger/UserConfig;")
    }
}

internal val isPremiumForStoryFingerprint = fingerprint {
    returns("Z")
    custom { methodDef, classDef ->
        methodDef.name == "isPremium" && classDef.type.endsWith("Lorg/telegram/ui/Stories/StoriesController;")

    }
}