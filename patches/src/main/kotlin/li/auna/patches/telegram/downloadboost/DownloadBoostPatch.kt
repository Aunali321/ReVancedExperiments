package li.auna.patches.telegram.downloadboost

import app.revanced.patcher.extensions.InstructionExtensions.addInstructions
import app.revanced.patcher.patch.bytecodePatch
import app.revanced.patcher.util.proxy.mutableTypes.MutableMethod.Companion.toMutable
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.builder.MutableMethodImplementation
import com.android.tools.smali.dexlib2.immutable.ImmutableMethod

@Suppress("unused")
val downloadBoostPatch = bytecodePatch(
    name = "Download Speed Boost",
    description = "Boosts download speed",
) {
    compatibleWith(
        "org.telegram.messenger",
        "org.telegram.messenger.web"
        "uz.unnarsx.cherrygram"
    )

    execute {
        val className = updateParamsFingerprint.originalClassDef.type
        val originalMethod = updateParamsFingerprint.method
        val returnType = originalMethod.returnType

        updateParamsFingerprint.classDef.methods.removeIf { it.name == originalMethod.name }

        updateParamsFingerprint.classDef.methods.add(
            ImmutableMethod(
                className,
                originalMethod.name,
                emptyList(),
                returnType,
                AccessFlags.PRIVATE.value,
                null,
                null,
                MutableMethodImplementation(5)
            ).toMutable().apply {
                addInstructions(
                    """
                        .line 266
                        iget v0, p0, Lorg/telegram/messenger/FileLoadOperation;->preloadPrefixSize:I
                    
                        if-gtz v0, :cond_e
                    
                        iget v0, p0, Lorg/telegram/messenger/FileLoadOperation;->currentAccount:I
                    
                        invoke-static {v0}, Lorg/telegram/messenger/MessagesController;->getInstance(I)Lorg/telegram/messenger/MessagesController;
                    
                        move-result-object v0
                    
                        iget-boolean v0, v0, Lorg/telegram/messenger/MessagesController;->getfileExperimentalParams:Z
                    
                        if-eqz v0, :cond_1d
                    
                        :cond_e
                        iget-boolean v0, p0, Lorg/telegram/messenger/FileLoadOperation;->forceSmallChunk:Z
                    
                        if-nez v0, :cond_1d
                    
                        const/high16 v0, 0x80000
                    
                        .line 267
                        iput v0, p0, Lorg/telegram/messenger/FileLoadOperation;->downloadChunkSizeBig:I
                    
                        const/16 v0, 0x8
                    
                        .line 268
                        iput v0, p0, Lorg/telegram/messenger/FileLoadOperation;->maxDownloadRequests:I
                    
                        .line 269
                        iput v0, p0, Lorg/telegram/messenger/FileLoadOperation;->maxDownloadRequestsBig:I
                    
                        goto :goto_26
                    
                        :cond_1d
                        const/high16 v0, 0x80000
                    
                        .line 271
                        iput v0, p0, Lorg/telegram/messenger/FileLoadOperation;->downloadChunkSizeBig:I
                    
                        const/16 v0, 0x8
                    
                        .line 272
                        iput v0, p0, Lorg/telegram/messenger/FileLoadOperation;->maxDownloadRequests:I
                    
                        .line 273
                        iput v0, p0, Lorg/telegram/messenger/FileLoadOperation;->maxDownloadRequestsBig:I
                        
                        goto :goto_26
                    
                        :goto_26
                        const-wide/32 v0, 0x7d000000
                    
                        .line 275
                        iget v2, p0, Lorg/telegram/messenger/FileLoadOperation;->downloadChunkSizeBig:I
                    
                        int-to-long v2, v2
                    
                        div-long/2addr v0, v2
                    
                        long-to-int v1, v0
                    
                        iput v1, p0, Lorg/telegram/messenger/FileLoadOperation;->maxCdnParts:I
                    
                        return-void
                    """
                )
            }
        )
    }
}
