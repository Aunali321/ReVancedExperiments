package li.auna.patches.instagram.misc.quality

import app.revanced.patcher.fingerprint

internal val displayMetricsFingerprint = fingerprint {
    returns("Ljava/lang/String;")
    strings("%sdpi; %sx%s")
}

internal val mediaSizeFingerprint = fingerprint {
    strings("_8.jpg", "_6.jpg")
}

internal val storyMediaBitrateFingerprint = fingerprint {
    returns("Landroid/media/MediaFormat;")
    strings("color-format", "bitrate", "frame-rate", "i-frame-interval", "profile", "level")
}

internal val videoEncoderConfigFingerprint = fingerprint {
    returns("Ljava/lang/String;")
    strings("VideoEncoderConfig{width=")
}