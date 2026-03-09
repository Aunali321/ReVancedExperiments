package li.auna.patches.instagram.misc.quality

import app.revanced.patcher.*
import app.revanced.patcher.patch.BytecodePatchContext

internal val BytecodePatchContext.displayMetricsMethod by gettingFirstMethodDeclaratively("%sdpi; %sx%s") {
    returnType("Ljava/lang/String;")
}

internal val BytecodePatchContext.mediaSizeMethod by gettingFirstMethodDeclaratively("_8.jpg", "_6.jpg")

internal val BytecodePatchContext.storyMediaBitrateMethod by gettingFirstMethodDeclaratively(
    "color-format", "bitrate", "frame-rate", "i-frame-interval", "profile", "level",
) {
    returnType("Landroid/media/MediaFormat;")
}

internal val BytecodePatchContext.videoEncoderConfigMethod by gettingFirstMethodDeclaratively("VideoEncoderConfig{width=") {
    returnType("Ljava/lang/String;")
}
