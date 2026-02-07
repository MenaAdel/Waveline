package com.example.waveline.di

import android.content.Context
import com.example.waveline.data.remote.NotificationApi
import com.example.waveline.util.AlarmScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import nl.adaptivity.xmlutil.XmlDeclMode
import nl.adaptivity.xmlutil.serialization.XML
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideXmlConfiguration(): XML = XML {
        xmlDeclMode = XmlDeclMode.None
        repairNamespaces = true
        /*autoTagConfig {
            unknownChildHandler = { _, _, _, _, _ -> } // Ignore unknown tags
        }*/
    }

    @Provides
    @Singleton
    fun provideRetrofit(xml: XML): NotificationApi {
        val contentType = "application/xml".toMediaType()
        return Retrofit.Builder()
            .baseUrl("https://lynxapp.com/")
            .addConverterFactory(xml.asConverterFactory(contentType))
            .client(
                OkHttpClient
                    .Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        this.level = HttpLoggingInterceptor.Level.BODY
                    })
                    .build()
            )
            .build()
            .create(NotificationApi::class.java)
    }

    @Provides
    fun provideScheduler(@ApplicationContext context: Context) = AlarmScheduler(context)
}