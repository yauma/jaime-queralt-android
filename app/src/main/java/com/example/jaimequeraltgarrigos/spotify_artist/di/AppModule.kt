package com.example.jaimequeraltgarrigos.spotify_artist.di

import android.app.Application
import com.example.jaimequeraltgarrigos.spotify_artist.MyApplication
import com.example.jaimequeraltgarrigos.spotify_artist.data.database.ArtistDataBase
import com.example.jaimequeraltgarrigos.spotify_artist.data.database.getDatabase
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.MainNetwork
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.getNetworkService
import com.example.jaimequeraltgarrigos.spotify_artist.repository.ArtistRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun databaseProvider(app: Application): ArtistDataBase = getDatabase(app)

    @Provides
    @Singleton
    fun serviceProvider(): MainNetwork = getNetworkService()


    @Provides
    @Singleton
    fun artistRepositoryProvider(
        @IoDispatcher dispatcher: CoroutineDispatcher,
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
        dataBase: ArtistDataBase,
        network: MainNetwork
    ) =
        ArtistRepositoryImpl(dispatcher, defaultDispatcher, dataBase, network)
}