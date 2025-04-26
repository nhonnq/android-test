package dev.nhonnq.app.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.nhonnq.data.usercase.GetUserLocal
import dev.nhonnq.data.api.UserApi
import dev.nhonnq.data.db.user.UserDao
import dev.nhonnq.data.db.user.UserRemoteKeyDao
import dev.nhonnq.data.repository.user.UserDataSource
import dev.nhonnq.data.repository.user.UserLocalDataSource
import dev.nhonnq.data.repository.user.UserRemoteDataSource
import dev.nhonnq.data.repository.user.UserRemoteMediator
import dev.nhonnq.data.repository.user.UserRepositoryImpl
import dev.nhonnq.domain.repository.UserRepository
import dev.nhonnq.domain.usecase.FetchUserDetails
import dev.nhonnq.domain.usecase.FetchUserList
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        userRemote: UserDataSource.Remote,
        userLocal: UserDataSource.Local,
        userRemoteMediator: UserRemoteMediator,
    ): UserRepository {
        return UserRepositoryImpl(userRemote, userLocal, userRemoteMediator)
    }

    @Provides
    @Singleton
    fun provideUserLocalDataSource(
        userDao: UserDao,
        remoteKeyDao: UserRemoteKeyDao,
    ): UserDataSource.Local {
        return UserLocalDataSource(userDao, remoteKeyDao)
    }

    @Provides
    @Singleton
    fun provideUserRemoteDataSource(userApi: UserApi): UserDataSource.Remote {
        return UserRemoteDataSource(userApi)
    }

    @Provides
    @Singleton
    fun provideUserMediator(
        userLocalDataSource: UserDataSource.Local,
        userRemoteDataSource: UserDataSource.Remote
    ): UserRemoteMediator {
        return UserRemoteMediator(userLocalDataSource, userRemoteDataSource)
    }

    @Provides
    fun provideFetchUserListUseCase(userRepository: UserRepository): FetchUserList {
        return FetchUserList(userRepository)
    }

    @Provides
    fun provideFetchUserDetailsUseCase(userRepository: UserRepository): FetchUserDetails {
        return FetchUserDetails(userRepository)
    }

    @Provides
    fun provideGetUserLocalUseCase(userLocalDataSource: UserDataSource.Local): GetUserLocal {
        return GetUserLocal(userLocalDataSource)
    }
}