package li.auna.patches.telegram.bypassintegrity

import app.revanced.patcher.fingerprint
import com.android.tools.smali.dexlib2.AccessFlags

// lambda$fillNextCodeParams$29
internal val bypassIntegrityFingerprint = fingerprint {
    accessFlags(AccessFlags.PRIVATE, AccessFlags.SYNTHETIC)
    returns("V")
    strings("basicIntegrity", "ctsProfileMatch")
}