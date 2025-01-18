package com.example.masterproject.ui.screens.setting

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.masterproject.model.database.DatabaseConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel(){

//    fun resetDatabase(){
//        context.deleteDatabase(DatabaseConfig.DATABASE_NAME)
//    }
}