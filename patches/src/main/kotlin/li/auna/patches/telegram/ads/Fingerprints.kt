package li.auna.patches.telegram.ads

import app.revanced.patcher.*
import app.revanced.patcher.patch.BytecodePatchContext

internal val BytecodePatchContext.hideSponsoredMessagesMethod by gettingFirstMethodDeclaratively {
    name("addSponsoredMessages")
    definingClass("ChatActivity;")
    returnType("V")
}
