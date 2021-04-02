package com.androiddevs.shoppinglisttestingyt.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValue
import com.androiddevs.shoppinglisttestingyt.launchFragmentInHiltContainer
import com.androiddevs.shoppinglisttestingyt.ui.ShoppingFragment
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Rule
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest  // unit tests
//@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ShoppingDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

//    private lateinit var database: ShoppingItemDatabase
    @Inject
    @Named("test_db")
    lateinit var database: ShoppingItemDatabase
    private lateinit var dao: ShoppingDao

    @Before
    fun setup() {
        // 'in memory' is held in RAM for test, not in persistent storage
//        database = Room.inMemoryDatabaseBuilder(
//            ApplicationProvider.getApplicationContext(),
//            ShoppingItemDatabase::class.java
//        ).allowMainThreadQueries().build()

        hiltRule.inject()
        dao = database.shoppingDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    // the suspend function must be executed in a coroutine
    fun insertShoppingItemTest() = runBlockingTest {
        val shoppingItem = ShoppingItem("name", 1, 1f, "url", id = 1)
        dao.insertShoppingItem(shoppingItem)

        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

        // truth
        assertThat(allShoppingItems).contains(shoppingItem)
        // junit
//        assertTrue(allShoppingItems.contains(shoppingItem))
    }

    @Test
    fun deleteShoppingItem() = runBlockingTest {
        val shoppingItem = ShoppingItem("name", 1, 1f, "url", id = 1)
        dao.insertShoppingItem(shoppingItem)
        dao.deleteShoppingItem(shoppingItem)

        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

        // truth
        assertThat(allShoppingItems).doesNotContain(shoppingItem)
        // junit
//        assertFalse(allShoppingItems.contains(shoppingItem))
    }

    @Test
    fun observeTotalPriceSum() = runBlockingTest {
        val shoppingItem1 = ShoppingItem("name1", 2, 10f, "url", id = 1)
        val shoppingItem2 = ShoppingItem("name2", 4, 5.5f, "url", id = 2)
        val shoppingItem3 = ShoppingItem("name3", 0, 100f, "url", id = 3)

        dao.insertShoppingItem(shoppingItem1)
        dao.insertShoppingItem(shoppingItem2)
        dao.insertShoppingItem(shoppingItem3)

        val totalPriceSum = dao.observeTotalPrice().getOrAwaitValue()

        // truth
        assertThat(totalPriceSum).isEqualTo(2*10f + 4*5.5f)
        // junit
//        assertEquals(totalPriceSum, 2*10f + 4*5.5f)
    }
}