package li.auna.patches.instagram.interaction.bio

import app.revanced.patcher.fingerprint

internal val selectableBioFingerprint = fingerprint {
    returns("V")
   strings("is_bio_visible")
}