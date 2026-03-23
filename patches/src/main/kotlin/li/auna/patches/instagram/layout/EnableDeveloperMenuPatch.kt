package li.auna.patches.instagram.layout

import app.revanced.patcher.extensions.addInstructions
import app.revanced.patcher.patch.bytecodePatch

@Suppress("unused")
val enableDeveloperMenuPatch = bytecodePatch(
    name = "Enable Developer Menu",
    description = "Enables the developer menu.",
) {
    compatibleWith("com.instagram.android")

    apply {
        // Force isEmployee to always be set to true.
        setIsEmployeePrefMethod.addInstructions(
            0,
            """
                const/4 p1, 0x1
            """,
        )
    }
}
