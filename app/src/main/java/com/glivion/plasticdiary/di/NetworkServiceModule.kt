package com.glivion.plasticdiary.di

import com.glivion.plasticdiary.BuildConfig
import com.glivion.plasticdiary.data.remote.interceptor.AuthInterceptor
import com.glivion.plasticdiary.data.remote.service.AuthService
import com.glivion.plasticdiary.data.remote.service.ExploreService
import com.glivion.plasticdiary.data.remote.service.HomeService
import com.glivion.plasticdiary.data.remote.service.SettingService
import com.glivion.plasticdiary.preference.AppPreference
import com.glivion.plasticdiary.util.READ_TIMEOUT
import com.glivion.plasticdiary.util.REQUEST_TIMEOUT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkServiceModule {
    private fun <T> buildService(
        service: Class<T>,
        tokenManager: AppPreference,
        retrofit: Retrofit,
        okHttpClient: OkHttpClient
    ): T {
        val loggingInterceptor = HttpLoggingInterceptor()

        if (BuildConfig.DEBUG)
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        else loggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS

        val client = okHttpClient.newBuilder()
            .connectTimeout(REQUEST_TIMEOUT, TimeUnit.MINUTES)
            .addInterceptor(AuthInterceptor(tokenManager))
            .writeTimeout(READ_TIMEOUT, TimeUnit.MINUTES)
            .readTimeout(READ_TIMEOUT, TimeUnit.MINUTES)
            .addInterceptor(loggingInterceptor)
//            .addInterceptor { chain ->
//                val authResponse = chain.proceed(chain.request())
//                if (authResponse.code == 401 || authResponse.code == 403) {
//                    tokenManager.deleteToken()
//                }
//                authResponse
//            }
            .build()

        val newRetrofit = retrofit.newBuilder().client(client).build()
        return newRetrofit.create(service)
    }

    @Singleton
    @Provides
    fun providesAuthService(
        tokenManager : AppPreference,
        retrofit : Retrofit,
        okHttpClient : OkHttpClient,
    ) : AuthService =
        buildService(
            AuthService::class.java,
            tokenManager,
            retrofit,
            okHttpClient
        )

    @Singleton
    @Provides
    fun providesExploreService(
        tokenManager : AppPreference,
        retrofit : Retrofit,
        okHttpClient : OkHttpClient,
    ) : ExploreService =
        buildService(
            ExploreService::class.java,
            tokenManager,
            retrofit,
            okHttpClient
        )

    @Singleton
    @Provides
    fun providesHomeService(
        tokenManager : AppPreference,
        retrofit : Retrofit,
        okHttpClient : OkHttpClient,
    ) : HomeService =
        buildService(
            HomeService::class.java,
            tokenManager,
            retrofit,
            okHttpClient
        )

    @Singleton
    @Provides
    fun providesSettingService(
        tokenManager : AppPreference,
        retrofit : Retrofit,
        okHttpClient : OkHttpClient,
    ) : SettingService =
        buildService(
            SettingService::class.java,
            tokenManager,
            retrofit,
            okHttpClient
        )
}