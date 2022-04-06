package com.picpay.desafio.android.di

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.picpay.desafio.android.data.repository.UserDataRepository
import com.picpay.desafio.android.data.repository.UserDataRepositoryImpl
import com.picpay.desafio.android.data.repository.datasource.UserDataSource
import com.picpay.desafio.android.data.repository.datasource.network.NetworkUserDataSourceImpl
import com.picpay.desafio.android.data.repository.datasource.network.PicPayService
import com.picpay.desafio.android.data.repository.helpers.NetworkHelper
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit


const val BASE_URL = "https://609a908e0f5a13001721b74e.mockapi.io/picpay/api/"

val remoteDataModule = module {
    factory { provideGson() }
    factory { provideLoggerInterceptor() }
    factory { provideCache(application = androidApplication()) }
    factory { NetworkHelper(gson = get()) }

    single { provideRetrofit(factory = get(), client = get()) }
    single { provideHttpClient(cache = get(), loggerInterceptor = get()) }
    single { provideService(retrofit = get()) }

    single<UserDataSource> { NetworkUserDataSourceImpl(api = get(), networkHelper = get()) }
    single<UserDataRepository> { UserDataRepositoryImpl(remoteDataSource = get()) }
}

/**
 * Retrofit instance.
 */
fun provideRetrofit(factory: Gson, client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(factory))
        .build()
}

/**
 * http client security.
 */
fun provideHttpClient(cache: Cache, loggerInterceptor: Interceptor): OkHttpClient {
    return OkHttpClient
        .Builder()
        .cache(cache)
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(1, TimeUnit.MINUTES)
        .writeTimeout(1, TimeUnit.MINUTES)
        .addInterceptor(loggerInterceptor)
        .build()
}

/**
 * Cache instance used in OkHttpClient.
 */
fun provideCache(application: Application): Cache {
    val cacheSize = 10 * 1024 * 1024
    return Cache(application.cacheDir, cacheSize.toLong())
}

/**
 * Gson instance used in Retrofit.
 */
fun provideGson(): Gson {
    return GsonBuilder().create()
}

/**
 * Interceptor for logs used in OkHttpClient.
 */
fun provideLoggerInterceptor(): Interceptor {
    return HttpLoggingInterceptor { message -> Timber.tag("OkHttp").d(message) }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
}

/**
 * Api Service instance.
 */
fun provideService(retrofit: Retrofit): PicPayService {
    return retrofit.create(PicPayService::class.java)
}