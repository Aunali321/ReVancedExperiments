package li.auna.patches.telegram.pro

import app.revanced.patcher.*
import app.revanced.patcher.patch.BytecodePatchContext

internal val BytecodePatchContext.isPremiumUserMethod by gettingFirstMethodDeclaratively {
    name("isPremiumUser")
    definingClass("MessagesController;")
    returnType("Z")
}

internal val BytecodePatchContext.isPremiumMethod by gettingFirstMethodDeclaratively {
    name("isPremium")
    definingClass("UserConfig;")
    returnType("Z")
}

internal val BytecodePatchContext.isPremiumForStoryMethod by gettingFirstMethodDeclaratively {
    name("isPremium")
    definingClass("StoriesController;")
    returnType("Z")
}
