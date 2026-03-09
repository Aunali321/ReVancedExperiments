package li.auna.patches.kustom

import app.revanced.patcher.extensions.instructions
import app.revanced.patcher.extensions.replaceInstruction
import app.revanced.patcher.patch.bytecodePatch

@Suppress("unused")
val hideSubtitleToastPatch = bytecodePatch(
    name = "Unlock Pro",
    description = "Unlock Pro features",
) {
    compatibleWith(
        "org.kustom.wallpaper"("3.73b314511"),
        "org.kustom.widget"("3.73b314511"),
        "org.kustom.lockscreen"("3.73b314511"),
    )

    apply {
        hasPurchasedMethod.apply {
            val index = instructions.lastIndex - 1
            // Set hasPremium = true.
            replaceInstruction(index, "const/4 v0, 0x1")
        }

        isPurchaseValidMethod.apply {
            val index = instructions.lastIndex - 3
            replaceInstruction(index, "const/4 v0, 0x1")
        }
    }
}
