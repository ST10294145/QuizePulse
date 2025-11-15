package com.saihilg.quizepulse.utils

import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.*

class TranslationManager(private val lifecycleOwner: LifecycleOwner) {

    private var translator: Translator? = null

    fun prepare(
        sourceLang: String,
        targetLang: String,
        onReady: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(
                TranslateLanguage.fromLanguageTag(sourceLang) ?: TranslateLanguage.ENGLISH
            )
            .setTargetLanguage(
                TranslateLanguage.fromLanguageTag(targetLang) ?: TranslateLanguage.ENGLISH
            )
            .build()

        translator?.close()
        translator = Translation.getClient(options)

        lifecycleOwner.lifecycle.addObserver(translator!!)

        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        translator!!.downloadModelIfNeeded(conditions)
            .addOnSuccessListener { onReady() }
            .addOnFailureListener { e -> onError(e) }
    }

    fun translate(text: String, onSuccess: (String) -> Unit, onFail: () -> Unit) {
        translator?.translate(text)
            ?.addOnSuccessListener { onSuccess(it) }
            ?.addOnFailureListener { onFail() }
    }
}
