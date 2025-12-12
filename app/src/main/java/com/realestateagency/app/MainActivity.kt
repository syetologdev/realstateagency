package com.realestateagency.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.realestateagency.app.ui.theme.RealEstateAgencyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RealEstateAgencyTheme {
                MainApp()
            }
        }
    }
}
