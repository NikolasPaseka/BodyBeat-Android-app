package cz.mendelu.xpaseka.bodybeat.utils

import java.lang.StringBuilder

object HashGenerator {
    fun getHash(): String {
        val letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        var s = StringBuilder()
        for (i in 0..19) {
            s.append(letters.random())
        }
        return s.toString()
    }
}