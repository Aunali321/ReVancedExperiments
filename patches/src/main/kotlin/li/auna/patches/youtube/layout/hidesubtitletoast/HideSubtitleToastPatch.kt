package li.auna.patches.youtube.layout.hidesubtitletoast

import app.revanced.patcher.extensions.InstructionExtensions.instructions
import app.revanced.patcher.extensions.InstructionExtensions.removeInstruction
import app.revanced.patcher.patch.bytecodePatch
import li.auna.util.returnEarly

@Suppress("unused")
val hideSubtitleToastPatch = bytecodePatch(
    name = "Hide subtitle toast",
    description = "Hides the subtitle toast when toggling subtitles on/off and when changing subtitles",
) {
    compatibleWith(
        "com.google.android.youtube",
        "app.revanced.android.youtube"
    )

    execute {
        hideSubtitleToastFingerprint.method.returnEarly()

        hideSubtitleToastFingerprint2.method.apply {
            val lastIndex = instructions.lastIndex - 2
            removeInstruction(lastIndex)
        }
    }
}