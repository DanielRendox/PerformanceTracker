package com.rendox.performancetracker.di

import android.app.Application
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.rendox.performancetracker.Database
import com.rendox.performancetracker.data.RoutineDataSource
import com.rendox.performancetracker.data.RoutineDataSourceImpl
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
    fun provideSqlDriver(app: Application): SqlDriver =
        AndroidSqliteDriver(
            schema = Database.Schema,
            context = app,
            name = "routine.db"
        )

    @Provides
    @Singleton
    fun provideRoutineDataSource(driver: SqlDriver): RoutineDataSource =
        RoutineDataSourceImpl(Database(driver))
}