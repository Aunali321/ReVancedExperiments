package li.auna.patches.instagram.adblock

import app.revanced.patcher.*
import app.revanced.patcher.patch.BytecodePatchContext
import com.android.tools.smali.dexlib2.AccessFlags

internal val BytecodePatchContext.adInjectorMethod by gettingFirstMethodDeclaratively(
    "SponsoredContentController.insertItem",
    "SponsoredContentController::Delivery",
) {
    accessFlags(AccessFlags.PRIVATE)
    returnType("Z")
    parameterTypes("L", "L")
}
