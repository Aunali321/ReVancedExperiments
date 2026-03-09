package li.auna.patches.telegram.disableautoupdate

import app.revanced.patcher.*
import app.revanced.patcher.patch.BytecodePatchContext

internal val BytecodePatchContext.checkAppUpdateMethod by gettingFirstMethodDeclaratively {
    name("checkAppUpdate")
    definingClass("LaunchActivity;")
    returnType("V")
}

internal val BytecodePatchContext.setNewAppVersionAvailableMethod by gettingFirstMethodDeclaratively {
    name("setNewAppVersionAvailable")
    definingClass("SharedConfig;")
    returnType("Z")
}

internal val BytecodePatchContext.blockViewUpdateMethod by gettingFirstMethodDeclaratively {
    name("show")
    definingClass("BlockingUpdateView;")
    returnType("V")
}
