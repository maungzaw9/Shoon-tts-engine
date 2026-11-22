package com.nara.tts

object MyanmarTextNormalizer {
    private val digitMap = mapOf(
        '၀' to "သုည", '0' to "သုည",
        '၁' to "တစ်",   '1' to "တစ်",
        '၂' to "နှစ်",   '2' to "နှစ်",
        '၃' to "သုံး",   '3' to "သုံး",
        '၄' to "လေး",   '4' to "လေး",
        '၅' to "ငါး",   '5' to "ငါး",
        '၆' to "ခြောက်", '6' to "ခြောက်",
        '၇' to "ခုနစ်",  '7' to "ခုနစ်",
        '၈' to "ရှစ်",   '8' to "ရှစ်",
        '၉' to "ကိုး",   '9' to "ကိုး"
    )

    fun normalize(input: String): String {
        val sb = StringBuilder()
        for (ch in input) {
            val mapped = digitMap[ch]
            if (mapped != null) {
                sb.append(mapped).append(" ")
            } else {
                sb.append(ch)
            }
        }
        return sb.toString().replace(Regex("\\s+"), " ").trim()
    }
}
