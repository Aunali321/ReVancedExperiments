package li.auna.patches.kustom

import app.revanced.patcher.*
import app.revanced.patcher.patch.BytecodePatchContext
import com.android.tools.smali.dexlib2.AccessFlags

internal val BytecodePatchContext.hasPurchasedMethod by gettingFirstMethodDeclaratively {
    accessFlags(AccessFlags.PUBLIC, AccessFlags.FINAL)
    returnType("Z")
    name("isLicensed")
    definingClass("LicenseState;")
}

internal val BytecodePatchContext.isPurchaseValidMethod by gettingFirstMethodDeclaratively {
    accessFlags(AccessFlags.PUBLIC, AccessFlags.FINAL)
    returnType("Z")
    name("isValid")
    definingClass("LicenseState;")
}
