package com.nara.tts

import android.content.Intent
import android.speech.tts.SynthesisCallback
import android.speech.tts.SynthesisRequest
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeechService
import java.nio.ByteBuffer
import java.nio.ByteOrder

class NaraTtsService : TextToSpeechService() {

    private var currentLanguage: String = "my"
    private var currentCountry: String = "MM"
    private var currentVariant: String = "shoon"

    override fun onIsLanguageAvailable(lang: String?, country: String?, variant: String?): Int {
        val isMy = lang.equals("my", ignoreCase = true) || lang.equals("mya", ignoreCase = true) || lang.equals("bur", ignoreCase = true)
        return if (isMy) TextToSpeech.LANG_AVAILABLE else TextToSpeech.LANG_NOT_SUPPORTED
    }

    override fun onGetLanguage(): Array<String> {
        return arrayOf(currentLanguage, currentCountry, currentVariant)
    }

    override fun onLoadLanguage(lang: String?, country: String?, variant: String?): Int {
        val avail = onIsLanguageAvailable(lang, country, variant)
        if (avail == TextToSpeech.LANG_NOT_SUPPORTED) {
            return avail
        }
        currentLanguage = lang ?: "my"
        currentCountry = country ?: "MM"
        if (!variant.isNullOrBlank()) {
            currentVariant = variant
        }
        return TextToSpeech.LANG_AVAILABLE
    }

    override fun onStop() {
        // Active synthesis cancellation hook
    }

    override fun onSynthesizeText(request: SynthesisRequest?, callback: SynthesisCallback?) {
        if (request == null || callback == null) return

        val rawText = request.charSequenceText?.toString().orEmpty().trim()
        if (rawText.isEmpty()) {
            callback.done()
            return
        }

        val normalizedText = MyanmarTextNormalizer.normalize(rawText)
        val sampleRate = 22050
        callback.start(sampleRate, android.media.AudioFormat.ENCODING_PCM_16BIT, 1)

        // Generate calm silent/short buffer proportional to normalized length until ONNX tensor inference is wired
        val durationSec = (normalizedText.length * 0.04).coerceIn(0.2, 4.0)
        val totalSamples = (sampleRate * durationSec).toInt()
        val chunkSamples = 2048
        var generated = 0

        while (generated < totalSamples) {
            val count = minOf(chunkSamples, totalSamples - generated)
            val pcmBuffer = ByteBuffer.allocate(count * 2).order(ByteOrder.LITTLE_ENDIAN)
            for (i in 0 until count) {
                pcmBuffer.putShort(0)
            }
            callback.audioAvailable(pcmBuffer.array(), 0, count * 2)
            generated += count
        }

        callback.done()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }
}
