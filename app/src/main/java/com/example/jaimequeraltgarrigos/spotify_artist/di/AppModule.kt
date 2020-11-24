package com.example.jaimequeraltgarrigos.spotify_artist.di

import android.app.Application
import com.example.jaimequeraltgarrigos.spotify_artist.data.database.ArtistDataBase
import com.example.jaimequeraltgarrigos.spotify_artist.data.database.getDatabase
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.MainNetwork
import com.example.jaimequeraltgarrigos.spotify_artist.data.network.getNetworkService
import com.example.jaimequeraltgarrigos.spotify_artist.repository.ArtistRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
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
        dataBase: ArtistDataBase,
        network: MainNetwork
    ) =
        ArtistRepositoryImpl(dataBase.artistDao, network)
}