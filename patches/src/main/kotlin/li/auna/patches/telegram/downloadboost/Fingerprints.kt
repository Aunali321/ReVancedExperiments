package li.auna.patches.telegram.downloadboost

import app.revanced.patcher.fingerprint

internal val updateParamsFingerprint = fingerprint {
    returns("V")
    custom { methodDef, classDef ->
        methodDef.name == "updateParams" && classDef.type.endsWith("Lorg/telegram/messenger/FileLoadOperation;")
    }
}