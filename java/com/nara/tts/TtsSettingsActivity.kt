package com.nara.tts

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TtsSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val scrollView = ScrollView(this)
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 40, 40, 40)
        }

        val titleView = TextView(this).apply {
            text = "Shoon TTS Engine (Nara Mini v1)"
            textSize = 20f
            setPadding(0, 0, 0, 24)
        }
        layout.addView(titleView)

        val shoonStatusView = TextView(this).apply {
            text = "Shoon Voice: Included / Bundled (shoon)"
            textSize = 15f
            setPadding(0, 0, 0, 24)
        }
        layout.addView(shoonStatusView)

        val openTtsSettingsBtn = Button(this).apply {
            text = "Open Android TTS Settings"
            setOnClickListener {
                startActivity(Intent(Settings.ACTION_TTS_SETTINGS))
            }
        }
        layout.addView(openTtsSettingsBtn)

        val extraVoicesHeader = TextView(this).apply {
            text = "\nDownload Additional Voices (Hugging Face):"
            textSize = 16f
            setPadding(0, 24, 0, 12)
        }
        layout.addView(extraVoicesHeader)

        val extraVoices = listOf(
            "Download Thazin Voice" to "https://huggingface.co/YeeNyunn/nara-mini-v1/tree/main/onnx/thazin",
            "Download Ko Banyar Voice" to "https://huggingface.co/YeeNyunn/nara-mini-v1/tree/main/onnx/ko-banyar",
            "Download Htet Myat Voice" to "https://huggingface.co/YeeNyunn/nara-mini-v1/tree/main/onnx/htet-myat"
        )

        for ((label, url) in extraVoices) {
            val btn = Button(this).apply {
                text = label
                setOnClickListener {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }
            }
            layout.addView(btn)
        }

        scrollView.addView(layout)
        setContentView(scrollView)
    }
}
