package com.nara.tts

import android.content.Context
import org.json.JSONObject

data class VoiceInfo(
    val id: String,
    val name: String,
    val bundled: Boolean,
    val downloadUrl: String
)

object VoiceJsonLoader {
    fun loadVoices(context: Context): List<VoiceInfo> {
        val rawJson = context.assets.open("voice.json").bufferedReader().use { it.readText() }
        val root = JSONObject(rawJson)
        val arr = root.getJSONArray("voices")
        val list = mutableListOf<VoiceInfo>()
        for (i in 0 until arr.length()) {
            val obj = arr.getJSONObject(i)
            list.add(
                VoiceInfo(
                    id = obj.getString("id"),
                    name = obj.getString("name"),
                    bundled = obj.optBoolean("bundled", false),
                    downloadUrl = obj.optString("download_url", "")
                )
            )
        }
        return list
    }
}
