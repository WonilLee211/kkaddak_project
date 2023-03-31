package com.ssafy.kkaddak.common.util

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ssafy.kkaddak.ApplicationClass
import com.ssafy.kkaddak.common.util.NFT_sol_MusicNFT.*
import com.ssafy.kkaddak.domain.entity.profile.NFTItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.RemoteFunctionCall
import org.web3j.protocol.http.HttpService
import org.web3j.tx.ReadonlyTransactionManager
import org.web3j.tx.gas.StaticGasProvider
import java.math.BigInteger

private const val INFURA_URL = "https://rpc.ssafy-blockchain.com"
private const val NFT_CONTRACT_ADDRESS = "0x0C0391FF59A532a51cf8E10F3C0632401EA0b4B8"
private const val TAG = "NFT info"

class NFTFunction {
    // Ethereum 네트워크에 연결
    private val web3j = Web3j.build(HttpService(INFURA_URL))

    // 가스 제공자 설정
    private val gasPrice = BigInteger.valueOf(0) // 20 Gwei
    private val gasLimit = BigInteger.valueOf(4_300_000) // 가스 한도
    private val contractGasProvider = StaticGasProvider(gasPrice, gasLimit)

    // 트랜잭션 매니저 조회용
    private val transactionManager = ReadonlyTransactionManager(web3j, NFT_CONTRACT_ADDRESS)

    fun getNFTCount() {
        Thread {
            val katToken = load(
                NFT_CONTRACT_ADDRESS,
                web3j,
                transactionManager,
                contractGasProvider
            )

            val remoteFunctionCall = katToken.balanceOf(
                String(
                    ApplicationClass.keyStore.decryptData(
                        WalletFunction().decode(ApplicationClass.preferences.walletAddress.toString())
                    )
                )
            )

            try {
                val count = remoteFunctionCall.send().toString()
                Log.d(TAG, "getNFTList: $count")

            } catch (e: Exception) {
                System.err.println("Error while fetching the balance: ${e.message}")
            }
        }.start()
    }

    fun getTokensOfOwner(): MutableLiveData<List<NFTItem>> {
        val result = MutableLiveData<List<NFTItem>>()

        CoroutineScope(Dispatchers.IO).launch {

            val katToken = load(
                NFT_CONTRACT_ADDRESS,
                web3j,
                transactionManager,
                contractGasProvider
            )

            try {
                val remoteFunctionCall = katToken.getTokensOfOwner(
                    String(
                        ApplicationClass.keyStore.decryptData(
                            WalletFunction().decode(ApplicationClass.preferences.walletAddress.toString())
                        )
                    )
                ) as RemoteFunctionCall<List<*>>

                val nftList = remoteFunctionCall.send() as List<MusicNFTMetaData>
                val list = mutableListOf<NFTItem>()

                for (i in nftList) {
                    Log.d(TAG, "getTokensOfOwner: ${i}")
                    list.add(
                        NFTItem(
                            i.nftImageUrl,
                            i.tokenId
                        )
                    )
                }
                result.postValue(list)
            } catch (e: Exception) {
                System.err.println("Error while get RecentTransactionList: ${e.message}")
            }
        }
        return result
    }

    fun getMetaData(nftId: String) {

    }

    fun mintMusicNFT(){

    }
}