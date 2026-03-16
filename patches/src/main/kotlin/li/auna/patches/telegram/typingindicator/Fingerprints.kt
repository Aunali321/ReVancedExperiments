package li.auna.patches.telegram.typingindicator

import app.revanced.patcher.*
import app.revanced.patcher.patch.BytecodePatchContext

internal val BytecodePatchContext.needSendTypingMethod by gettingFirstMethodDeclaratively {
    name("needSendTyping")
    returnType("V")
}
