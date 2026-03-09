package li.auna.patches.instagram.layout

import app.revanced.patcher.*
import app.revanced.patcher.patch.BytecodePatchContext

internal val BytecodePatchContext.shouldAddPrefTTLMethod by gettingFirstMethodDeclaratively {
    name("shouldAddPrefTTL")
    definingClass("WhitehatOptionsFragment;")
}
