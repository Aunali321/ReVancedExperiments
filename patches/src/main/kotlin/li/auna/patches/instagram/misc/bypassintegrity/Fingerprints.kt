package li.auna.patches.instagram.misc.bypassintegrity

import app.revanced.patcher.*
import app.revanced.patcher.patch.BytecodePatchContext
import com.android.tools.smali.dexlib2.iface.ClassDef

internal val BytecodePatchContext.isValidSignatureClassMethod by gettingFirstMethodDeclaratively(
    "The provider for uri '", "' is not trusted: ",
)

context(_: BytecodePatchContext)
internal fun ClassDef.getIsValidSignatureMethod() = firstMethodDeclaratively {
    parameterTypes("L", "Z")
    returnType("Z")
}
