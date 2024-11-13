package li.auna.patches.telegram.bypassintegrity

import app.revanced.patcher.fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

internal val bypassIntegrityFingerprint = fingerprint {
    accessFlags(AccessFlags.PRIVATE, AccessFlags.SYNTHETIC)
    returns("V")
    strings("basicIntegrity", "ctsProfileMatch")
}

internal val spoofSignatureFingerprint = fingerprint {
    custom { methodDef, classDef ->
        methodDef.name == "getCertificateSHA256Fingerprint" && classDef.type.endsWith("Lorg/telegram/messenger/AndroidUtilities;")
    }
}