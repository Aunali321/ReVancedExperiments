package li.auna.patches.shared

import app.revanced.patcher.*
import app.revanced.patcher.patch.BytecodePatchContext

internal val BytecodePatchContext.castContextFetchMethod by gettingFirstMethodDeclaratively("Error fetching CastContext.")

internal val BytecodePatchContext.primeMethod by gettingFirstMethodDeclaratively(
    "com.google.android.GoogleCamera", "com.android.vending",
)
