package com.ssafy.kkaddak.data.remote.datasource.market

import com.ssafy.kkaddak.data.remote.service.MarketApiService
import javax.inject.Inject

class MarketRemoteDataSourceImpl @Inject constructor(
    private val marketApiService: MarketApiService
) : MarketRemoteDataSource {

    override suspend fun getAllNfts(lastId: Int, limit: Int, onlySelling: Boolean): List<NftItemResponse> =
        marketApiService.getAllNfts(lastId, limit, onlySelling).data!!

    override suspend fun getBookmarks(lastId: Int, limit: Int, onlySelling: Boolean): List<NftItemResponse> =
        marketApiService.getBookmarks(lastId, limit, onlySelling).data!!

    override suspend fun uploadNft(creatorName: String, nftId: String, nftImagePath: String, price: Double, songTitle: String): UploadNftItemResponse =
        marketApiService.uploadNft(UploadNftItemRequest(creatorName, nftId, nftImagePath, price, songTitle)).data!!

    override suspend fun getDetailNft(marketId: Int): NftDetailItemResponse =
        marketApiService.getDetailNft(marketId).data!!

    override suspend fun requestMarketBookmark(marketId: Int): Boolean =
        marketApiService.requestMarketBookmark(marketId).data!!

    override suspend fun cancelMarketBookmark(marketId: Int): Boolean =
        marketApiService.cancelMarketBookmark(marketId).data!!

    override suspend fun closeMarket(marketId: Int): Boolean =
        marketApiService.closeMarket(marketId).data!!
}