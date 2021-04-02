package com.androiddevs.shoppinglisttestingyt.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.androiddevs.shoppinglisttestingyt.MainCoroutineRule
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValue
import com.androiddevs.shoppinglisttestingyt.other.Constants
import com.androiddevs.shoppinglisttestingyt.other.Status
import com.androiddevs.shoppinglisttestingyt.repositories.FakeShoppingRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

@ExperimentalCoroutinesApi
class ShoppingViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ShoppingViewModel

    @Before
    fun setup() {
        viewModel = ShoppingViewModel(FakeShoppingRepository())
    }

    @Test
    fun `insert shopping item with EMPTY FIELD, returns ERROR`() {
        viewModel.insertShoppingItem("name", "", "3.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
//        Assert.assertEquals(Status.ERROR, value.getContentIfNotHandled()?.status)
    }

    @Test
    fun `insert shopping item with TOO LONG NAME, returns ERROR`() {
        val tooLongInput = buildString {
            for (i in 1..Constants.MAX_NAME_LENGTH + 1) {
                append(1)
            }
        }

        viewModel.insertShoppingItem(tooLongInput, "1", "3.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
//        Assert.assertEquals(Status.ERROR, value.getContentIfNotHandled()?.status)
    }

    @Test
    fun `insert shopping item with TOO LONG PRICE, returns ERROR`() {
        val tooLongInput = buildString {
            for (i in 1..Constants.MAX_PRICE_LENGTH + 1) {
                append(1)
            }
        }

        viewModel.insertShoppingItem("name", "5", tooLongInput)
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
//        Assert.assertEquals(Status.ERROR, value.getContentIfNotHandled()?.status)
    }

    @Test
    fun `insert shopping item with INVALID AMOUNT, returns ERROR`() {
        val invalidAmount = "9999999999999999" // beyond int limit, should fail

        viewModel.insertShoppingItem("name", invalidAmount, "3.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
//        Assert.assertEquals(Status.ERROR, value.getContentIfNotHandled()?.status)
    }

    @Test
    fun `insert shopping item with VALID INPUT, returns SUCCESS`() {
        viewModel.insertShoppingItem("name", "5", "3.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
//        Assert.assertEquals(Status.SUCCESS, value.getContentIfNotHandled()?.status)

        // assert image set back to empty string after insertion
        assertThat(viewModel.curImageUrl.getOrAwaitValue()).isEqualTo("")
    }

    @Test
    fun `test set image url`() {
        viewModel.setCurImageUrl("bananas")
        assertThat(viewModel.curImageUrl.getOrAwaitValue()).isEqualTo("bananas")

        viewModel.setCurImageUrl("apples")
        assertThat(viewModel.curImageUrl.getOrAwaitValue()).isEqualTo("apples")

        viewModel.setCurImageUrl("123456")
        assertThat(viewModel.curImageUrl.getOrAwaitValue()).isEqualTo("123456")

        viewModel.setCurImageUrl("@!)")
        assertThat(viewModel.curImageUrl.getOrAwaitValue()).isEqualTo("@!)")

        viewModel.setCurImageUrl("@!)")
        assertThat(viewModel.curImageUrl.getOrAwaitValue()).isNotEqualTo("bananas")
    }
}