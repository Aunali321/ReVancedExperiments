package li.auna.patches.instagram.layout

import app.revanced.patcher.fingerprint

internal val shouldAddPrefTTLFingerprint = fingerprint {
    custom { methodDef, classDef ->
        methodDef.name == "shouldAddPrefTTL" && classDef.type.endsWith("Lcom/instagram/debug/whoptions/WhitehatOptionsFragment;")
    }
}