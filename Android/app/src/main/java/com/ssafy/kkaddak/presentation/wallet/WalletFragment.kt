package com.ssafy.kkaddak.presentation.wallet

import com.ssafy.kkaddak.R
import com.ssafy.kkaddak.common.util.generateWallet
import com.ssafy.kkaddak.common.util.insertUserWallet
import com.ssafy.kkaddak.databinding.FragmentWalletBinding
import com.ssafy.kkaddak.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WalletFragment : BaseFragment<FragmentWalletBinding>(R.layout.fragment_wallet) {
    override fun initView() {
        binding.clCharge.setOnClickListener {
            //generateWallet()
            insertUserWallet()
        }
    }
}