package com.example.masterproject.model.modules

import android.content.Context
import com.example.masterproject.model.config.theme.SharedPreferencesThemeDataSource
import com.example.masterproject.model.config.theme.ThemeDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

//@Module
//@InstallIn(SingletonComponent::class)
//object ThemeDataSourceModule {
//
//    @Provides
//    fun provideThemeDataSource(@ApplicationContext context: Context): ThemeDataSource{
//        return SharedPreferencesThemeDataSource(context)
//    }
//}