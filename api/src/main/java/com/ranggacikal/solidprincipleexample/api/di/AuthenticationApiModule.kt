package com.ranggacikal.solidprincipleexample.api.di

import com.ranggacikal.solidprincipleexample.api.data.api.AuthenticationApi
import com.ranggacikal.solidprincipleexample.api.util.ApiConstants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AuthenticationApiModule {
    @Provides
    @Singleton
    fun provideApiAuthentication(): AuthenticationApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthenticationApi::class.java)
    }
}