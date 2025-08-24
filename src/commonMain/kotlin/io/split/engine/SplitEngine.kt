package io.split.engine

object SplitEngine {
    fun computeShares(input: SplitInput): List<Share> = when (input.mode) {
        SplitMode.EQUAL -> equal(input)
        SplitMode.EXACT -> exact(input)
        SplitMode.PERCENTAGE -> percent(input)
        SplitMode.RATIO -> ratio(input)
    }

    private fun equal(input: SplitInput): List<Share> {
        val users = (input.payload as EqualPayload).users
        require(users.isNotEmpty())
        val n = users.size
        val base = input.total.minor / n
        var rem = (input.total.minor % n).toInt()
        return users.map { u ->
            val extra = if (rem > 0) 1 else 0
            if (rem > 0) rem--
            Share(u, Money(base + extra, input.total.scale))
        }
    }

    private fun exact(input: SplitInput): List<Share> {
        val items = (input.payload as ExactPayload).items
        val sum = items.sumOf { it.amount.minor }
        require(sum == input.total.minor) { "Exact split does not sum to total" }
        return items.map { Share(it.userId, it.amount) }
    }

    private fun percent(input: SplitInput): List<Share> {
        val items = (input.payload as PercentPayload).items
        require(items.sumOf { it.bps } == 10_000) { "Percentages must sum to 10000 bps" }
        val total = input.total.minor

        data class Tmp(val user: String, val base: Long, val rem: Long)

        val tmp = items.map {
            val prod = total * it.bps
            val base = prod / 10_000
            val rem = prod % 10_000
            Tmp(it.userId, base, rem)
        }

        val used = tmp.sumOf { it.base }
        var leftover = (total - used).toInt()
        val ordered = tmp.sortedByDescending { it.rem }
        val bonus = ordered.mapIndexed { idx, t -> t.user to (t.base + if (idx < leftover) 1 else 0) }.toMap()

        return items.map { Share(it.userId, Money(bonus[it.userId]!!, input.total.scale)) }
    }

    private fun ratio(input: SplitInput): List<Share> {
        val items = (input.payload as RatioPayload).items
        val total = input.total.minor
        val rSum = items.sumOf { it.ratio }
        require(rSum > 0)

        data class Tmp(val user: String, val base: Long, val rem: Long)

        val tmp = items.map {
            val prod = total * it.ratio
            val base = prod / rSum
            val rem = prod % rSum
            Tmp(it.userId, base, rem)
        }

        val used = tmp.sumOf { it.base }
        var leftover = (total - used).toInt()
        val ordered = tmp.sortedByDescending { it.rem }
        val bonus = ordered.mapIndexed { idx, t -> t.user to (t.base + if (idx < leftover) 1 else 0) }.toMap()

        return items.map { Share(it.userId, Money(bonus[it.userId]!!, input.total.scale)) }
    }
}