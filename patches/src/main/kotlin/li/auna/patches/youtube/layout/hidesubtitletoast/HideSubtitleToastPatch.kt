package li.auna.patches.youtube.layout.hidesubtitletoast

import app.revanced.patcher.extensions.instructions
import app.revanced.patcher.extensions.removeInstruction
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

    apply {
        hideSubtitleToastMethod.returnEarly()

        hideSubtitleToastMethod2.apply {
            val lastIndex = instructions.lastIndex - 2
            removeInstruction(lastIndex)
        }
    }
}
