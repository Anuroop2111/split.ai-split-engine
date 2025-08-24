package commonMain.kotlin.io.split.engine

import com.sun.org.apache.bcel.internal.util.Args.require

/** Minor units (e.g., paise for INR). 12345 = ₹123.45 when scale=2 */
data class Money(val minor: Long, val scale: Int = 2) { init { require(scale >= 0) } }
