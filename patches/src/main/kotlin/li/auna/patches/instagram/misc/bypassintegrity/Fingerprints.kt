package li.auna.patches.instagram.misc.bypassintegrity

import app.revanced.patcher.fingerprint
import li.auna.util.getReference
import li.auna.util.indexOfFirstInstruction
import com.android.tools.smali.dexlib2.iface.reference.MethodReference

internal val isValidSignatureClassFingerprint = fingerprint {
    strings("The provider for uri '", "' is not trusted: ")
}

internal val isValidSignatureMethodFingerprint = fingerprint {
    parameters("L", "Z")
    returns("Z")
    custom { method, _ ->
        method.indexOfFirstInstruction {
            getReference<MethodReference>()?.name == "keySet"
        } >= 0
    }
}