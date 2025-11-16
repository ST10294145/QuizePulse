package com.saihilg.quizepulse

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

object LocaleHelper {

    fun setLocale(context: Context, language: String): ContextWrapper {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
        } else {
            config.locale = locale
        }
        config.setLayoutDirection(locale)

        val newContext = context.createConfigurationContext(config)
        return ContextWrapper(newContext)
    }
}
