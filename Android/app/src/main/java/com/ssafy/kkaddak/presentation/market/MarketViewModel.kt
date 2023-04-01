package com.ssafy.kkaddak.presentation.market

import android.util.Log
import androidx.lifecycle.*
import com.ssafy.kkaddak.data.remote.Resource
import com.ssafy.kkaddak.domain.entity.market.HistoryItem
import com.ssafy.kkaddak.domain.entity.market.NftItem
import com.ssafy.kkaddak.domain.entity.profile.ProfileItem
import com.ssafy.kkaddak.domain.usecase.market.CancelMarketBookmarkUseCase
import com.ssafy.kkaddak.domain.usecase.market.GetAllNftsUseCase
import com.ssafy.kkaddak.domain.usecase.market.GetBookmarksUseCase
import com.ssafy.kkaddak.domain.usecase.market.RequestMarketBookmarkUseCase
import com.ssafy.kkaddak.domain.usecase.profile.GetProfileInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val getAllNftsUseCase: GetAllNftsUseCase,
    private val getBookmarksUseCase: GetBookmarksUseCase,
    private val requestMarketBookmarkUseCase: RequestMarketBookmarkUseCase,
    private val cancelMarketBookmarkUseCase: CancelMarketBookmarkUseCase,
    private val getProfileInfoUseCase: GetProfileInfoUseCase
) : ViewModel() {

    private val _nftListData: MutableLiveData<List<NftItem>?> = MutableLiveData()
    val nftListData: LiveData<List<NftItem>?> = _nftListData

    private val _nftTempData: MutableLiveData<List<NftItem>?> = MutableLiveData()
    val nftTempData: LiveData<List<NftItem>?> = _nftTempData

    private val _nftData: MutableLiveData<NftItem> = MutableLiveData()
    val nftData: LiveData<NftItem> = _nftData

    private val _nftHistoryData: MutableLiveData<List<HistoryItem>?> = MutableLiveData()
    val nftHistoryData: LiveData<List<HistoryItem>?> = _nftHistoryData

    var creatorImg: String = ""

    // 기존 리스트의 마지막 아이디보다 새로 불러온 리스트의 첫 아이디가 큰 경우는 중복으로 판단
    private fun dup(list1: List<NftItem>, list2: List<NftItem>): Boolean {
        if (list1.isNotEmpty() && list2.isNotEmpty()) {
            if (list1[list1.size - 1].marketId <= list2[list2.size - 1].marketId) {
                return true
            }
        }
        return false
    }

    fun tempHistory() = viewModelScope.launch {
        val datas = mutableListOf<HistoryItem>()
        _nftHistoryData.value = datas
    }

    fun getAllNfts(lastId: Int, limit: Int, onlySelling: Boolean) = viewModelScope.launch {
        when (val value = getAllNftsUseCase(lastId, limit, onlySelling)) {
            is Resource.Success<List<NftItem>> -> {
                _nftTempData.value = value.data
            }
            is Resource.Error -> {
                Log.e("getAllNfts", "getAllNfts: ${value.errorMessage}")
            }
        }
    }

    fun getBookmarks(lastId: Int, limit: Int, onlySelling: Boolean) = viewModelScope.launch {
        when (val value = getBookmarksUseCase(lastId, limit, onlySelling)) {
            is Resource.Success<List<NftItem>> -> {
                _nftTempData.value = value.data
            }
            is Resource.Error -> {
                Log.e("getBookmarks", "getBookmarks: ${value.errorMessage}")
            }
        }
    }

    fun getData(item: NftItem) = viewModelScope.launch {
        _nftData.value = item
    }

    fun clearData() {
        _nftListData.value = listOf()
        _nftTempData.value = listOf()
    }

    fun getSum() {
        val list1: List<NftItem>? = _nftListData.value
        val list2: List<NftItem>? = _nftTempData.value

        val joinedList: MutableList<NftItem> = ArrayList()

        // 기존 리스트와 새로 불러온 리스트 합치기
        list1?.let { joinedList.addAll(it) }
        list2?.let { joinedList.addAll(it) }

        // 중복 부분 제거
        if (list1 != null && list2 != null) {
            if (dup(list1, list2)) {
                if(joinedList.size >= 20) {
                    for (i in 0..19) {
                        joinedList.removeAt(joinedList.size - 1)
                    }
                }
            }
        }

        _nftListData.value = joinedList
    }

    fun getLastId(): Int? {
        return _nftListData.value?.size?.let { _nftListData.value?.get(it - 1) }?.marketId
    }

    fun getTempSize(): Int? {
        return _nftTempData.value?.size
    }

    fun getSize(): Int? {
        return _nftListData.value?.size
    }

    fun getCreatorImg(nickname: String) = viewModelScope.launch {
        when (val value = getProfileInfoUseCase(nickname)) {
            is Resource.Success<ProfileItem> -> {
                creatorImg = value.data.profilepath.toString()
            }
            is Resource.Error -> {
                Log.e("getCreatorImg", "getCreatorImg: ${value.errorMessage}")
            }
        }
    }

    suspend fun requestBookmark(marketId: Int) = viewModelScope.async {
        when (val value = requestMarketBookmarkUseCase(marketId)) {
            is Resource.Success<Boolean> ->
                return@async value.data.toString()
            is Resource.Error -> {
                Log.e("requestMarektBookmark", "requestMarektBookmark: ${value.errorMessage}")
                return@async "error"
            }
        }
    }.await()

    suspend fun cancelBookmark(marketId: Int) = viewModelScope.async {
        when (val value = cancelMarketBookmarkUseCase(marketId)) {
            is Resource.Success<Boolean> ->
                return@async value.data.toString()
            is Resource.Error -> {
                Log.e("cancelMarektBookmark", "cancelMarektBookmark: ${value.errorMessage}")
                return@async "error"
            }
        }
    }.await()
}