package com.d121211017.stroyappsubmission.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.d121211017.stroyappsubmission.DummyData
import com.d121211017.stroyappsubmission.MainDispatcherRule
import com.d121211017.stroyappsubmission.data.remote.entity.ListStoryItem
import com.d121211017.stroyappsubmission.data.repository.StoryAppRepository
import com.d121211017.stroyappsubmission.getOrAwaitValue
import com.d121211017.stroyappsubmission.ui.adapter.RecyclerViewAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryListViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    lateinit var storyAppRepository: StoryAppRepository


    @Test
    fun `Get Story Empty Should Return No Data`(){

    }

    @Test
    fun `Get Story Should Not Null and Return Data`() = runTest{
        val dummyData = DummyData.getDummyData()
//        val data: PagingData<ListStoryItem> = PagingData.from(dummyQuote)
        val data: PagingData<ListStoryItem> = StoryPagingSource.snapshot(dummyData)
        val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStory.value = data
        Mockito.`when`(storyAppRepository.myStoriesForPaging()).thenReturn(expectedStory)

        val storyListViewModel = StoryListViewModel(storyAppRepository = storyAppRepository)
        val actualStory: PagingData<ListStoryItem> = storyListViewModel.storiesForPaging.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = RecyclerViewAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyData.size, differ.snapshot().size)
        Assert.assertEquals(dummyData[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Quote Empty Should Return No Data`() = runTest {
        val data: PagingData<ListStoryItem> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStory.value = data
        Mockito.`when`(storyAppRepository.myStoriesForPaging()).thenReturn(expectedStory)

        val storyListViewModel = StoryListViewModel(storyAppRepository = storyAppRepository)
        val actualStory: PagingData<ListStoryItem> = storyListViewModel.storiesForPaging.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = RecyclerViewAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        Assert.assertEquals(0, differ.snapshot().size)
    }
}



class StoryPagingSource : PagingSource<Int, LiveData<List<ListStoryItem>>>() {
    companion object {
        fun snapshot(items: List<ListStoryItem>): PagingData<ListStoryItem> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStoryItem>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStoryItem>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}