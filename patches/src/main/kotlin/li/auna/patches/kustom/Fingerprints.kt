package li.auna.patches.kustom

import app.revanced.patcher.fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

internal val hasPurchasedFingerprint = fingerprint {
    accessFlags(AccessFlags.PUBLIC, AccessFlags.FINAL)
    returns("Z")
    custom { methodDef, classDef ->
        classDef.type.endsWith("Lorg/kustom/billing/LicenseState;") &&
                methodDef.name == "isLicensed"
    }
}

internal val isPurchaseValidFingerprint = fingerprint {
    accessFlags(AccessFlags.PUBLIC, AccessFlags.FINAL)
    returns("Z")
    custom { methodDef, classDef ->
        classDef.type.endsWith("Lorg/kustom/billing/LicenseState;") &&
                methodDef.name == "isValid"
    }
}