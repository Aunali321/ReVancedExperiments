package li.auna.patches.instagram.interaction.bio

import app.revanced.patcher.*
import app.revanced.patcher.patch.BytecodePatchContext

internal val BytecodePatchContext.selectableBioMethod by gettingFirstMethodDeclaratively("is_bio_visible") {
    returnType("V")
}
