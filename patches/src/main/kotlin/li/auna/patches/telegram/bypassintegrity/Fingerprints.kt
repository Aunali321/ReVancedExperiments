package li.auna.patches.telegram.bypassintegrity

import app.revanced.patcher.*
import app.revanced.patcher.patch.BytecodePatchContext
import com.android.tools.smali.dexlib2.AccessFlags

internal val BytecodePatchContext.bypassIntegrityMethod by gettingFirstMethodDeclaratively(
    "basicIntegrity", "ctsProfileMatch",
) {
    accessFlags(AccessFlags.PRIVATE, AccessFlags.SYNTHETIC)
    returnType("V")
}

internal val BytecodePatchContext.spoofSignatureMethod by gettingFirstMethodDeclaratively {
    name("getCertificateSHA256Fingerprint")
    definingClass("AndroidUtilities;")
}
