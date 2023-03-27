package com.ssafy.kkaddak.data.remote.datasource.profile

import com.ssafy.kkaddak.data.remote.datasource.song.SongResponse
import com.ssafy.kkaddak.data.remote.service.ProfileApiService
import javax.inject.Inject

class ProfileRemoteDataSourceImpl @Inject constructor(
    private val profileApiService: ProfileApiService
) : ProfileRemoteDataSource {

    override suspend fun getProfileInfo(nickname: String): ProfileResponse =
        profileApiService.getProfileInfo(nickname).data!!

    override suspend fun getProfileSong(nickname: String): List<SongResponse> =
        profileApiService.getProfileSong(nickname).data!!
}