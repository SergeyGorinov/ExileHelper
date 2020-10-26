package com.poetradeapp.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.poetradeapp.R
import com.poetradeapp.activities.MainActivity
import com.poetradeapp.dl.DatabaseRepository
import com.poetradeapp.http.RequestService
import com.poetradeapp.models.HashData
import com.poetradeapp.models.database.*
import com.poetradeapp.models.enums.HashType
import kotlinx.android.synthetic.main.fragment_loader.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import retrofit2.await
import java.io.File
import kotlin.system.exitProcess

@ExperimentalCoroutinesApi
class LoaderFragment : Fragment() {

    private val repository: DatabaseRepository by inject()
    private val retrofit: RequestService by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val transitionInflanter = TransitionInflater.from(context)
        enterTransition = transitionInflanter.inflateTransition(R.transition.fragment_fade)
        exitTransition = transitionInflanter.inflateTransition(R.transition.fragment_fade)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_loader, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loaderProgressBar.progress = 0

        val handler = CoroutineExceptionHandler { _, _ ->
            requireActivity().runOnUiThread {
                AlertDialog.Builder(requireContext()).setTitle("Error")
                    .setMessage("Error during get remote data")
                    .setOnCancelListener { exitProcess(-1) }.show()
            }
        }

        GlobalScope.launch(handler) {
            checkForUpdate()
        }.invokeOnCompletion {
            GlobalScope.launch(Dispatchers.Main) {
                (context as MainActivity).closeLoader()
            }
        }
    }

    private suspend fun checkForUpdate() {
        val hashTableFile = File(requireContext().dataDir, "hashTable")

        val leaguesDataResponse =
            retrofit.getLeagueData("api/trade/data/leagues").await()
        val itemsDataResponse =
            retrofit.getItemsData("api/trade/data/items").await()
        val statsDataResponse =
            retrofit.getStatsData("api/trade/data/stats").await()
        val staticDataResponse =
            retrofit.getStaticData("api/trade/data/static").await()

        val hashData = if (!hashTableFile.exists()) {
            hashTableFile.createNewFile()
            mutableListOf()
        } else {
            hashTableFile.readLines().map {
                val data = it.split('=')
                val type = when (data[0]) {
                    HashType.LeaguesHash.text -> HashType.LeaguesHash
                    HashType.ItemsHash.text -> HashType.ItemsHash
                    HashType.StatsHash.text -> HashType.StatsHash
                    HashType.StaticItemsHash.text -> HashType.StaticItemsHash
                    else -> HashType.LeaguesHash
                }
                HashData(type, data[1].toInt(), false)
            }.toMutableList()
        }

        HashType.values().forEach { type ->
            val hashCode = when (type) {
                HashType.LeaguesHash -> leaguesDataResponse.hashCode()
                HashType.ItemsHash -> itemsDataResponse.hashCode()
                HashType.StatsHash -> statsDataResponse.hashCode()
                HashType.StaticItemsHash -> staticDataResponse.hashCode()
            }
            hashData.singleOrNull { s -> s.type == type }?.let {
                if (it.hash != hashCode) {
                    it.hash = hashCode
                    it.isUpdated = true
                }
            } ?: run {
                hashData.add(
                    HashData(
                        type,
                        hashCode,
                        true
                    )
                )
            }
        }

        if (hashData.any { a -> a.isUpdated }) {
            hashData.filter { f -> f.isUpdated }.forEach {
                when (it.type) {
                    HashType.LeaguesHash -> {
                        leaguesDataResponse.result.map { m ->
                            LeagueModel(
                                id = m.id,
                                label = m.text
                            )
                        }.forEach { league ->
                            repository.dao().insert(league)
                        }
                    }
                    HashType.ItemsHash -> {
                        itemsDataResponse.result.map { m ->
                            ItemGroupWithItems(
                                ItemGroupModel(label = m.label),
                                m.entries.map { item ->
                                    ItemModel(
                                        type = item.type,
                                        text = item.text,
                                        name = item.name,
                                        disc = item.disc,
                                        unique = item.flags?.unique,
                                        prophecy = item.flags?.prophecy
                                    )
                                }
                            )
                        }.forEach { group ->
                            repository.dao().insert(group)
                        }
                    }
                    HashType.StatsHash -> {
                        statsDataResponse.result.map { m ->
                            StatGroupWithItems(
                                StatGroupModel(label = m.label),
                                m.entries.map { item ->
                                    StatModel(
                                        id = item.id,
                                        text = item.text,
                                        type = item.type,
                                        options = item.option?.options?.map { option ->
                                            Option(
                                                option.id,
                                                option.text
                                            )
                                        }
                                    )
                                }
                            )
                        }.forEach { group ->
                            repository.dao().insert(group)
                        }
                    }
                    HashType.StaticItemsHash -> {
                        staticDataResponse.result.map { m ->
                            StaticGroupWithItems(
                                StaticGroupModel(
                                    id = m.id,
                                    label = m.label
                                ),
                                m.entries.map { item ->
                                    StaticItemModel(
                                        id = item.id,
                                        label = item.text,
                                        image = item.image
                                    )
                                }
                            )
                        }.forEach { group ->
                            repository.dao().insert(group)
                        }
                    }
                }
            }
            hashTableFile.writeText(hashData.joinToString("\n") {
                "${it.type.text}=${it.hash}"
            })
        }
    }
}