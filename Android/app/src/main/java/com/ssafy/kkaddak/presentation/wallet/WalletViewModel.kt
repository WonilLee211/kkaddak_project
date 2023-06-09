package com.ssafy.kkaddak.presentation.wallet

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.kkaddak.ApplicationClass
import com.ssafy.kkaddak.data.remote.Resource
import com.ssafy.kkaddak.domain.usecase.user.ChargeCoinUseCase
import com.ssafy.kkaddak.domain.usecase.user.RegisterWalletAccountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val registerWalletAccountUseCase: RegisterWalletAccountUseCase,
    private val chargeCoinUseCase: ChargeCoinUseCase
) : ViewModel() {

    fun registerWalletAccount(walletAccount: String) = viewModelScope.launch {
        when (val value = registerWalletAccountUseCase(walletAccount)) {
            is Resource.Success<Boolean> -> {}
            is Resource.Error -> {
                ApplicationClass.preferences.walletAddress = ""
                ApplicationClass.preferences.privateKey = ""
                Log.e("registerWalletAccount", "registerWalletAccount: ${value.errorMessage}")
            }
        }
    }

    fun chargeCoin(receiptId: String) = viewModelScope.launch {
        when (val value = chargeCoinUseCase(receiptId)) {
            is Resource.Success<Boolean> -> {}
            is Resource.Error -> {
                Log.e("chargeCoin", "chargeCoin: ${value.errorMessage}")
            }
        }
    }
}