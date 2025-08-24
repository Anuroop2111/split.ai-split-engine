package io.split.engine

enum class SplitMode { EQUAL, EXACT, PERCENTAGE, RATIO }

data class SplitInput(
    val total: Money,
    val participants: List<String>,
    val mode: SplitMode,
    val payload: SplitPayload
)

sealed interface SplitPayload
data class EqualPayload(val users: List<String>) : SplitPayload
data class ExactPayload(val items: List<UserAmount>) : SplitPayload
data class PercentPayload(val items: List<UserBps>) : SplitPayload
data class RatioPayload(val items: List<UserRatio>) : SplitPayload

data class UserAmount(val userId: String, val amount: Money)
data class UserBps(val userId: String, val bps: Int) // 10000 = 100.00%
data class UserRatio(val userId: String, val ratio: Int)

data class Share(val userId: String, val owe: Money)