package dev.nhonnq.app.di.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.nhonnq.data.db.user.UserDao
import dev.nhonnq.data.db.user.UserDatabase
import dev.nhonnq.data.db.user.UserRemoteKeyDao
import dev.nhonnq.data.util.DiskExecutor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideUserDatabase(
        @ApplicationContext context: Context,
        diskExecutor: DiskExecutor
    ): UserDatabase {
        return Room
            .databaseBuilder(context, UserDatabase::class.java, "users.db")
            .setQueryExecutor(diskExecutor)
            .setTransactionExecutor(diskExecutor)
            .build()
    }

    @Provides
    fun provideUserDao(userDatabase: UserDatabase): UserDao {
        return userDatabase.userDao()
    }

    @Provides
    fun provideUserRemoteKeyDao(userDatabase: UserDatabase): UserRemoteKeyDao {
        return userDatabase.userRemoteKeyDao()
    }
}