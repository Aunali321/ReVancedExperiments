package li.auna.patches.telegram.pro

import app.revanced.patcher.fingerprint

internal val isPremiumUserFingerprint = fingerprint {
    returns("Z")
    custom { methodDef, classDef ->
        classDef.type.endsWith("Lorg/telegram/messenger/MessagesController;") &&
                methodDef.name == "isPremiumUser"
    }
}

internal val isPremiumFingerprint = fingerprint {
    returns("Z")
    custom { methodDef, classDef ->
        classDef.type.endsWith("Lorg/telegram/messenger/UserConfig;") &&
                methodDef.name == "isPremium"
    }
}

internal val isPremiumForStoryFingerprint = fingerprint {
    returns("Z")
    custom { methodDef, classDef ->
        classDef.type.endsWith("Lorg/telegram/ui/Stories/StoriesController;") &&
                methodDef.name == "isPremium"
    }
}