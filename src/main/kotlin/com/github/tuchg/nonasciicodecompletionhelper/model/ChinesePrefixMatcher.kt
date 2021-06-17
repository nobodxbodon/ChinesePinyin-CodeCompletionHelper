package com.github.tuchg.nonasciicodecompletionhelper.model

import com.github.tuchg.nonasciicodecompletionhelper.utils.countContainsSomeChar
import com.github.tuchg.nonasciicodecompletionhelper.utils.五笔
import com.intellij.codeInsight.completion.PlainPrefixMatcher
import pansong291.simplepinyin.Pinyin

/**
 * @author: tuchg
 * @date: 2021/2/4 13:58
 * @description: 转换中文使之与IDE沟通的关键类
 */
class ChinesePrefixMatcher(prefix: String) : PlainPrefixMatcher(prefix) {

    override fun prefixMatches(name: String): Boolean {

        return if (Pinyin.hasChinese(name)) {
            for (s in 五笔(name)) {
                if (countContainsSomeChar(s, prefix) >= prefix.length) {
                    return true
                }
            }
            return false
        } else super.prefixMatches(name)
    }


    override fun cloneWithPrefix(prefix: String) = if (prefix == this.prefix) this else ChinesePrefixMatcher(prefix)
}
