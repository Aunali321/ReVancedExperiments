package li.auna.patches.youtube.layout.hidesubtitletoast

import app.revanced.patcher.*
import app.revanced.patcher.patch.BytecodePatchContext
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode

internal val BytecodePatchContext.hideSubtitleToastMethod by gettingFirstMethodDeclaratively {
    accessFlags(AccessFlags.PUBLIC, AccessFlags.FINAL)
    returnType("V")
    parameterTypes("Lcom/google/android/libraries/youtube/player/subtitles/model/SubtitleTrack;")
    opcodes(
        Opcode.APUT_OBJECT,
        Opcode.CHECK_CAST,
        Opcode.CONST,
        Opcode.INVOKE_VIRTUAL,
        Opcode.MOVE_RESULT_OBJECT,
        Opcode.GOTO,
    )
}

internal val BytecodePatchContext.hideSubtitleToastMethod2 by gettingFirstMethodDeclaratively {
    accessFlags(AccessFlags.PUBLIC, AccessFlags.FINAL)
    returnType("V")
    parameterTypes("Lcom/google/android/libraries/youtube/player/subtitles/model/SubtitleTrack;")
    opcodes(
        Opcode.APUT_OBJECT,
        Opcode.CHECK_CAST,
        Opcode.CONST,
        Opcode.INVOKE_VIRTUAL,
        Opcode.MOVE_RESULT_OBJECT,
        Opcode.GOTO,
    )
}
