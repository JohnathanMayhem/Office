package com.example.officemanagerapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.officemanagerapp.database.CacheDatabase
import com.example.officemanagerapp.network.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAuthApi(
        remoteDataSource: RemoteDataSource,
        @ApplicationContext context: Context
    ): AuthApi {
        return remoteDataSource.buildApi(AuthApi::class.java, context)
    }

    @Singleton
    @Provides
    fun provideUserApi(
        remoteDataSource: RemoteDataSource,
        @ApplicationContext context: Context
    ): UserApi {
        return remoteDataSource.buildApi(UserApi::class.java, context)
    }

    @Singleton
    @Provides
    fun provideOrderApi(
        remoteDataSource: RemoteDataSource,
        @ApplicationContext context: Context
    ): OrderApi {
        return remoteDataSource.buildApi(OrderApi::class.java, context)
    }

    @Singleton
    @Provides
    fun provideHomeApi(
        remoteDataSource: RemoteDataSource,
        @ApplicationContext context: Context
    ): HomeApi {
        return remoteDataSource.buildApi(HomeApi::class.java, context)
    }

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application,
        callback: CacheDatabase.Callback
    ) = Room.databaseBuilder(app, CacheDatabase::class.java, "cache_database")
        .fallbackToDestructiveMigration()
        .addCallback(callback)
        .build()

    @Provides
    fun provideCacheDao(db: CacheDatabase) = db.cacheDao()

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope