package com.engineerskasa.plasticdiary.di

import com.engineerskasa.plasticdiary.BuildConfig
import com.engineerskasa.plasticdiary.data.remote.interceptor.AuthInterceptor
import com.engineerskasa.plasticdiary.data.remote.service.AuthService
import com.engineerskasa.plasticdiary.preference.AppPreference
import com.engineerskasa.plasticdiary.util.READ_TIMEOUT
import com.engineerskasa.plasticdiary.util.REQUEST_TIMEOUT
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
}