package li.auna.patches.instagram.misc.quality

import app.revanced.patcher.extensions.InstructionExtensions.addInstruction
import app.revanced.patcher.extensions.InstructionExtensions.getInstruction
import app.revanced.patcher.extensions.InstructionExtensions.instructions
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

    execute {
        val maxPostSize = "2048" // Maximum post size.
        val maxBitRate = "10000000" // Maximum bit rate possible (found in code).

        // Improve quality of images.
        // Instagram tend to reduce/compress the image resolution to user's device height and width.
        // This section of code removes that restriction and sets the resolution to 2048x2048 (max possible).
        displayMetricsFingerprint.let { it ->
            it.method.apply {
                val displayMetInstructions = instructions.filter { it.opcode == Opcode.IGET }

                // There are 3 iget instances.
                // 1.dpi 2.width 3.height.
                // We don't need to change dpi, we just need to change height and width.
                displayMetInstructions.drop(1).forEach { instruction ->
                    val index = instruction.location.index
                    val register = getInstruction<TwoRegisterInstruction>(index).registerA

                    // Set height and width to 2048.
                    addInstruction(index + 1, "const v$register, $maxPostSize")
                }
            }
        }

        // Yet another method where the image resolution is compressed.
        mediaSizeFingerprint.let { it ->
            it.classDef.apply {
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
        }

        // Improve quality of stories.
        // This section of code sets the bitrate of the stories to the maximum possible.
        storyMediaBitrateFingerprint.let { it ->
            it.method.apply {
                val ifLezIndex = instructions.first { it.opcode == Opcode.IF_LEZ }.location.index

                val bitRateRegister = getInstruction<OneRegisterInstruction>(ifLezIndex).registerA

                // Set the bitrate to maximum possible.
                addInstruction(ifLezIndex + 1, "const v$bitRateRegister, $maxBitRate")
            }
        }

        // Improve quality of reels.
        // In general Instagram tend to set the minimum bitrate between maximum possible and compressed video's bitrate.
        // This section of code sets the bitrate of the reels to the maximum possible.
        videoEncoderConfigFingerprint.let { it ->
            it.classDef.apply {
                // Get the constructor.
                val videoEncoderConfigConstructor = methods.first()

                val lastMoveResIndex = videoEncoderConfigConstructor.instructions
                    .last { it.opcode == Opcode.MOVE_RESULT }.location.index

                // Finding the register were the bitrate is stored.
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
}