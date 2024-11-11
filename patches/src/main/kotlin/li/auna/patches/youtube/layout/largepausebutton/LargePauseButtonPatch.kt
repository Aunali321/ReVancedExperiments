package li.auna.patches.youtube.layout.largepausebutton

import app.revanced.patcher.patch.resourcePatch

@Suppress("unused")
val largePauseButtonPatch = resourcePatch(
    name = "Large pause button",
    description = "Adds a large pause button to the player",
) {
    compatibleWith(
        "com.google.android.youtube",
        "app.revanced.android.youtube"
    )

    execute {
        document("res/layout/youtube_controls_button_group_layout.xml").use { document ->
            val btnTouchArea =  document.getElementsByTagName("FrameLayout").item(0)
            btnTouchArea.attributes.getNamedItem("android:layout_width").nodeValue = "180dp"
            btnTouchArea.attributes.getNamedItem("android:layout_height").nodeValue = "180dp"

            // modify the width and height of player_control_play_pause_replay_button element
            val btn =  document.getElementsByTagName("com.google.android.libraries.youtube.common.ui.TouchImageView").item(0)
            btn.attributes.getNamedItem("android:layout_width").nodeValue = "112dp"
            btn.attributes.getNamedItem("android:layout_height").nodeValue = "112dp"
            // modify the padding of player_control_play_pause_replay_button element. default is 8dp
            btn.attributes.getNamedItem("android:padding").nodeValue = "16dp"
        }

    }
}