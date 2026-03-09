package li.auna.patches.telegram.downloadboost

import app.revanced.patcher.*
import app.revanced.patcher.patch.BytecodePatchContext

internal val BytecodePatchContext.updateParamsMethod by gettingFirstMethodDeclaratively {
    name("updateParams")
    definingClass("FileLoadOperation;")
    returnType("V")
}
