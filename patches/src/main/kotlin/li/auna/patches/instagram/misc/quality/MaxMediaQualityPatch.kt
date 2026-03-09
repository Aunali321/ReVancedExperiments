package li.auna.patches.instagram.misc.quality

import app.revanced.patcher.*
import app.revanced.patcher.extensions.addInstruction
import app.revanced.patcher.extensions.getInstruction
import app.revanced.patcher.extensions.instructions
import app.revanced.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.TwoRegisterInstruction

@Suppress("unused")
val maxMediaQualityPatch = bytecodePatch(
    name = "Max Media Quality",
    description = "Enable max media quality.",
) {
    compatibleWith(
        "com.instagram.android"
    )

    apply {
        val maxPostSize = "2048" // Maximum post size.
        val maxBitRate = "10000000" // Maximum bit rate possible (found in code).

        // Improve quality of images.
        displayMetricsMethod.apply {
            val displayMetInstructions = instructions.filter { it.opcode == Opcode.IGET }

            displayMetInstructions.drop(1).forEach { instruction ->
                val index = instruction.location.index
                val register = getInstruction<TwoRegisterInstruction>(index).registerA

                // Set height and width to 2048.
                addInstruction(index + 1, "const v$register, $maxPostSize")
            }
        }

        // Yet another method where the image resolution is compressed.
        val mediaSizeClassDef = firstClassDef(mediaSizeMethod.definingClass)
        mediaSizeClassDef.apply {
            val mediaSetMethod =
                methods.first { it.returnType == "Lcom/instagram/model/mediasize/ExtendedImageUrl;" }

            val mediaSetInstructions =
                mediaSetMethod.instructions.filter { it.opcode == Opcode.INVOKE_VIRTUAL }

            mediaSetInstructions.forEach { instruction ->
                val index = instruction.location.index + 1
                val register = mediaSetMethod.getInstruction<OneRegisterInstruction>(index).registerA

                // Set height and width to 2048.
                mediaSetMethod.addInstruction(index + 1, "const v$register, $maxPostSize")
            }
        }

        // Improve quality of stories.
        storyMediaBitrateMethod.apply {
            val ifLezIndex = instructions.first { it.opcode == Opcode.IF_LEZ }.location.index

            val bitRateRegister = getInstruction<OneRegisterInstruction>(ifLezIndex).registerA

            // Set the bitrate to maximum possible.
            addInstruction(ifLezIndex + 1, "const v$bitRateRegister, $maxBitRate")
        }

        // Improve quality of reels.
        val videoEncoderClassDef = firstClassDef(videoEncoderConfigMethod.definingClass)
        videoEncoderClassDef.apply {
            // Get the constructor.
            val videoEncoderConfigConstructor = methods.first()

            val lastMoveResIndex = videoEncoderConfigConstructor.instructions
                .last { it.opcode == Opcode.MOVE_RESULT }.location.index

            val bitRateRegister =
                videoEncoderConfigConstructor.getInstruction<OneRegisterInstruction>(lastMoveResIndex).registerA

            // Set bitrate to maximum possible.
            videoEncoderConfigConstructor.addInstruction(
                lastMoveResIndex + 1,
                "const v$bitRateRegister, $maxBitRate",
            )
        }
    }
}
