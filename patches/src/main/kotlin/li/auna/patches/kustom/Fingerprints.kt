package li.auna.patches.kustom

import app.revanced.patcher.fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

internal val hasPurchasedFingerprint = fingerprint {
    accessFlags(AccessFlags.PUBLIC, AccessFlags.FINAL)
    returns("Z")
    custom { methodDef, classDef ->
        methodDef.name == "isLicensed" && classDef.type.endsWith("Lorg/kustom/billing/LicenseState;")
    }
}

internal val isPurchaseValidFingerprint = fingerprint {
    accessFlags(AccessFlags.PUBLIC, AccessFlags.FINAL)
    returns("Z")
    custom { methodDef, classDef ->
        methodDef.name == "isValid" && classDef.type.endsWith("Lorg/kustom/billing/LicenseState;")
    }
}