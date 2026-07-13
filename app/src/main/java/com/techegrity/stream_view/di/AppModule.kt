package com.techegrity.stream_view.di

import com.techegrity.stream_view.core.common.dispatchers.DefaultDispatcherProvider
import com.techegrity.stream_view.core.common.dispatchers.DispatcherProvider
import com.techegrity.stream_view.data.repository.SeedStreamRepository
import com.techegrity.stream_view.domain.repository.StreamRepository
import com.techegrity.stream_view.domain.usecase.AddStreamUseCase
import com.techegrity.stream_view.domain.usecase.GetStreamByIdUseCase
import com.techegrity.stream_view.domain.usecase.GetStreamsUseCase
import com.techegrity.stream_view.domain.usecase.ObserveStreamsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider = DefaultDispatcherProvider()

    @Provides
    @Singleton
    fun provideStreamRepository(): StreamRepository = SeedStreamRepository()

    @Provides
    @Singleton
    fun provideGetStreamsUseCase(repository: StreamRepository): GetStreamsUseCase =
        GetStreamsUseCase(repository)

    @Provides
    @Singleton
    fun provideObserveStreamsUseCase(repository: StreamRepository): ObserveStreamsUseCase =
        ObserveStreamsUseCase(repository)

    @Provides
    @Singleton
    fun provideAddStreamUseCase(repository: StreamRepository): AddStreamUseCase =
        AddStreamUseCase(repository)

    @Provides
    @Singleton
    fun provideGetStreamByIdUseCase(repository: StreamRepository): GetStreamByIdUseCase =
        GetStreamByIdUseCase(repository)
}
