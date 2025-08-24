package io.split.engine

import kotlin.jvm.JvmStatic

object SplitEngineJvm {
    @JvmStatic
    fun computeShares(input: SplitInput): List<Share> =
        SplitEngine.computeShares(input)
}
