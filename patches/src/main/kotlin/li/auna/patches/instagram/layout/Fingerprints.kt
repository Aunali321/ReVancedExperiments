package li.auna.patches.instagram.layout

import app.revanced.patcher.*
import app.revanced.patcher.patch.BytecodePatchContext

internal val BytecodePatchContext.setIsEmployeePrefMethod by gettingFirstMethodDeclaratively(
    "UserPreferences", "IsEmployee",
) {
    returnType("V")
    parameterTypes("Landroid/content/Context;", "Z")
}
