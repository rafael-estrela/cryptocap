package br.eti.rafaelcouto.cryptocap.ext

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class NumberExtensionsTest {

    @Test
    fun doubleAsMonetaryTensTest() {
        val amount = 12.3
        val expected = "US$ 12.30"
        val actual = amount.asMonetary()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun doubleAsMonetaryHundredsTest() {
        val amount = 125.84
        val expected = "US$ 125.84"
        val actual = amount.asMonetary()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun doubleAsMonetaryThousandsTest() {
        val amount = 4125.77
        val expected = "US$ 4,125.77"
        val actual = amount.asMonetary()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun doubleAsMonetaryTenThousandsTest() {
        val amount = 42125.0
        val expected = "US$ 42,125.00"
        val actual = amount.asMonetary()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun doubleAsMonetaryHundredThousandsTest() {
        val amount = 421256.99
        val expected = "US$ 421,256.99"
        val actual = amount.asMonetary()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun doubleAsMonetaryMillionsTest() {
        val amount = 1000000.13
        val expected = "US$ 1,000,000.13"
        val actual = amount.asMonetary()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun doubleAsMonetaryZeroTest() {
        val amount = 0.0
        val expected = "US$ 0.00"
        val actual = amount.asMonetary()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun intAsMonetaryTensTest() {
        val amount = 12
        val expected = "US$ 12.00"
        val actual = amount.asMonetary()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun intAsMonetaryHundredsTest() {
        val amount = 125
        val expected = "US$ 125.00"
        val actual = amount.asMonetary()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun intAsMonetaryThousandsTest() {
        val amount = 4125
        val expected = "US$ 4,125.00"
        val actual = amount.asMonetary()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun intAsMonetaryTenThousandsTest() {
        val amount = 42125
        val expected = "US$ 42,125.00"
        val actual = amount.asMonetary()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun intAsMonetaryHundredThousandsTest() {
        val amount = 421256
        val expected = "US$ 421,256.00"
        val actual = amount.asMonetary()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun intAsMonetaryMillionsTest() {
        val amount = 1000000
        val expected = "US$ 1,000,000.00"
        val actual = amount.asMonetary()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun intAsMonetaryZeroTest() {
        val amount = 0
        val expected = "US$ 0.00"
        val actual = amount.asMonetary()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun doubleAsPercentageFractionTest() {
        val amount = 0.4756888888
        val expected = "0.47568889%"
        val actual = amount.asPercentage()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun doubleAsPercentageZeroTest() {
        val amount = 0.0
        val expected = "0%"
        val actual = amount.asPercentage()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun doubleAsPercentageSingleTest() {
        val amount = 4.3
        val expected = "4.3%"
        val actual = amount.asPercentage()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun doubleAsPercentageTensTest() {
        val amount = 43.8
        val expected = "43.8%"
        val actual = amount.asPercentage()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun doubleAsPercentageHundredsTest() {
        val amount = 249.7
        val expected = "249.7%"
        val actual = amount.asPercentage()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun doubleAsPercentageThousandsTest() {
        val amount = 2581.2
        val expected = "2,581.2%"
        val actual = amount.asPercentage()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun intAsPercentageZeroTest() {
        val amount = 0
        val expected = "0%"
        val actual = amount.asPercentage()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun intAsPercentageHundredsTest() {
        val amount = 100
        val expected = "100%"
        val actual = amount.asPercentage()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun intAsPercentageThousandsTest() {
        val amount = 2600
        val expected = "2,600%"
        val actual = amount.asPercentage()

        assertThat(actual).isEqualTo(expected)
    }
}
