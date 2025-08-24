package io.split.engine

/** Minor units (e.g., paise for INR). 12345 = ₹123.45 when scale=2 */
data class Money(val minor: Long, val scale: Int = 2) {
    init {
        require(scale >= 0) { "scale must be >= 0" }
    }
}
