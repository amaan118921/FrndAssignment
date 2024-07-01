package com.example.frndassignment.di

import com.example.frndassignment.Constants
import com.example.frndassignment.data.Api
import com.example.frndassignment.data.repository.TaskRepositoryImpl
import com.example.frndassignment.domain.repository.TaskRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Modules {


    @Provides
    @Singleton
    fun provideTaskRepository(api: Api): TaskRepository {
        return TaskRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    @Provides
    @Singleton
    fun provideApi(moshi: Moshi): Api {
        return Retrofit.Builder().addConverterFactory(
            MoshiConverterFactory.create(moshi)
        ).baseUrl(Constants.BASE)
            .build().create(Api::class.java)
    }
}