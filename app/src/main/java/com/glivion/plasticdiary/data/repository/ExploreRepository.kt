package com.glivion.plasticdiary.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.glivion.plasticdiary.data.database.dao.explore.MapsDao
import com.glivion.plasticdiary.data.database.dao.explore.ResourcesDao
import com.glivion.plasticdiary.data.database.dao.explore.TipsDao
import com.glivion.plasticdiary.data.remote.service.ExploreService
import com.glivion.plasticdiary.model.BaseAuthResponse
import com.glivion.plasticdiary.model.explore.BaseExploreResponse
import com.glivion.plasticdiary.model.explore.Map
import com.glivion.plasticdiary.model.explore.Resource
import com.glivion.plasticdiary.model.explore.Tip
import com.glivion.plasticdiary.preference.AppPreference
import com.glivion.plasticdiary.util.Utils
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import javax.inject.Inject

class ExploreRepository @Inject constructor(
    private val tipsDao: TipsDao,
    private val resourcesDao: ResourcesDao,
    private val mapsDao: MapsDao,
    private val service: ExploreService,
    private val utils: Utils,
    private val appPreference: AppPreference
) {

    fun insertTips(tips: List<Tip>):Completable = tipsDao.insert(tips)
    fun insertResources(resources: List<Resource>):Completable = resourcesDao.insert(resources)
    fun insertMapItems(maps: List<Map>):Completable = mapsDao.insert(maps)

    fun getAllTrips(): Observable<List<Tip>> = tipsDao.getAllTips().toObservable()
    fun getAllResources(): Observable<List<Resource>> = resourcesDao.getAllResources().toObservable()
    fun getAllMapItems(): Observable<List<Map>> = mapsDao.getAllMapItems().toObservable()

    fun getExplorePageItems(): Single<Response<BaseExploreResponse>> = service.getExplorePageItems()

    fun submitTip(tip:String): Single<Response<BaseAuthResponse>> = service.submitTip(tip, appPreference.getUser()?.token!!)

    @RequiresApi(Build.VERSION_CODES.M)
    fun isConnectedToInternet(): Boolean = utils.isConnectedToInternet()
}