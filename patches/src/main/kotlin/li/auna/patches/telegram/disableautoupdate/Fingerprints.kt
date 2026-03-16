package li.auna.patches.telegram.disableautoupdate

import app.revanced.patcher.*
import app.revanced.patcher.patch.BytecodePatchContext

internal val BytecodePatchContext.checkAppUpdateMethod by gettingFirstMethodDeclaratively {
    name("checkAppUpdate")
    definingClass("Lorg/telegram/ui/LaunchActivity;")
    returnType("V")
}

internal val BytecodePatchContext.setNewAppVersionAvailableMethod by gettingFirstMethodDeclaratively {
    name("setNewAppVersionAvailable")
    definingClass("Lorg/telegram/messenger/SharedConfig;")
    returnType("Z")
}

internal val BytecodePatchContext.blockViewUpdateMethod by gettingFirstMethodDeclaratively {
    name("show")
    definingClass("Lorg/telegram/ui/Components/BlockingUpdateView;")
    returnType("V")
}
