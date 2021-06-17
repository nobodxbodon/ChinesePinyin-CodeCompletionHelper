package com.github.tuchg.nonasciicodecompletionhelper.utils

import com.intellij.util.containers.ContainerUtil
import pansong291.simplepinyin.Pinyin

class PinyinEx {
    companion object {
        /**
         * 字符计数缓存
         */
        @JvmStatic
        val COUNT_CHAR_CACHE: MutableMap<String, Int> = ContainerUtil.createSoftValueMap()

        /**
         * 笛卡尔积缓存
         */
        @JvmStatic
        val CARTESIAN_CACHE: MutableMap<String, Array<String>> = ContainerUtil.createSoftValueMap()

        // 摘自此库：https://github.com/program-in-chinese/npm-wubi-code-data/blob/master/%E4%BA%94%E7%AC%94%E7%A0%81%E8%A1%A8%E6%95%B0%E6%8D%AE.js
        @JvmStatic
        val 五笔98=mapOf(
                '问' to "ukd", '候' to "whnd", '打' to "rsh", '印' to "qgbh", '输' to "lwgj", '出' to "bmk"
        )
    }
}


/**
 * 获取多音字组合 (笛卡尔积)
 *
 * @param str      输入字符串
 * @param caseType 大小写类型
 * @return 多音字拼接的结果数组
 * @Author tuchg
 */
fun toPinyin(str: String, caseType: Int): Array<String> {
    val key = "$str-$caseType"
    PinyinEx.CARTESIAN_CACHE[key]?.let {
        return it
    }
    val result = str
            .map { element -> Pinyin.toPinyin(element, caseType) }
            .reduce { acc, strings ->
                acc.flatMap { a ->
                    strings.map { b -> a + b }
                }.toTypedArray()
            }
    PinyinEx.CARTESIAN_CACHE[key] = result
    return result
}

fun 五笔(文本: String): Array<String> {
    val 五笔码 = 文本
            .map { 字 -> PinyinEx.五笔98.getOrDefault(字, 字) }
            .joinToString(separator = "")
    println("求五笔码: ${文本} -> ${五笔码}")
    return arrayOf(五笔码)
}


/**
 * 计算字符串内包含需要的字符的数量
 * @param origin 文本串
 * @param needed 模式串
 */
fun countContainsSomeChar(origin: String, needed: String): Int {
    val key = "$origin-$needed"
    PinyinEx.COUNT_CHAR_CACHE[key]?.let {
        return it
    }
    val counted = mutableSetOf<Int>()
    var i = 0
    var j = 0
    while (i < needed.length) {
        val indexOf = origin.indexOf(needed[i], j++)
        if (indexOf != -1) {
            if (counted.add(indexOf)) {
                i++
            }
        } else {
            i++
        }
    }
    val size = counted.size
    println("匹配字符数:${key} -> ${size}")
    PinyinEx.COUNT_CHAR_CACHE[key] = size
    return size
}

