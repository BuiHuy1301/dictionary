package com.example.mydictionary

class SuggestionsProvider {
    fun getSuggestions(list: ArrayList<String>, keyword: String): ArrayList<String>{
        val suggestionList = ArrayList<String>()
        var i = 0
        var count = 0
        val fileSize = list.size
        val firstCharacter = keyword.substring(0, 1).lowercase()
        val keywordLength = keyword.length
        while (count < 20 && i < fileSize) {
            if (list[i].length >= keywordLength && list[i].substring(0, keywordLength).lowercase() == keyword) {
                suggestionList.add(list[i].substring(0, list[i].indexOf("\t")))
                count++
            } else if (count > 0 && firstCharacter != list[i].substring(0, 1).lowercase()) {
                count = 20
            }
            i++
        }
        return suggestionList
    }

    fun findTranslation(
        detailList: ArrayList<String>,
        unspellingList: ArrayList<String>,
        keyword: String
    ): String {
        val fullKeySearch = "@$keyword /"
        val unspellKeySearch = "@$keyword"

        detailList.forEachIndexed { i: Int, value: String ->
            if (value.contains(fullKeySearch)) {
                return getTranslation(detailList, i)
            }
        }
        unspellingList.forEachIndexed { i, string ->
            if (string == unspellKeySearch) {
                return getTranslation(unspellingList, i)
            }
        }
        return fullKeySearch
    }

    private fun getTranslation(detailList: ArrayList<String>, startIndex: Int): String {
        val length = detailList.size
        val result = StringBuilder(detailList[startIndex])
        for (i in startIndex + 1 until length) {
            if (detailList[i].indexOf("@") == 0) {
                break
            }
            result.append("\n")
            result.append(detailList[i])
        }
        return result.toString().substring(1, result.length)
    }
}