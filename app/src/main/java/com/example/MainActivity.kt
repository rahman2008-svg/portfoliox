package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.data.AppDatabase
import com.example.data.PortfolioRepository
import com.example.ui.PortfolioViewModel
import com.example.ui.PortfolioViewModelFactory
import com.example.ui.screens.PortfolioAppNavHost
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 1. Initialize Database, DAO, and Repository
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = PortfolioRepository(database.portfolioDao())
        
        // 2. Instantiate ViewModel using Custom Factory
        val factory = PortfolioViewModelFactory(application, repository)
        val viewModel = ViewModelProvider(this, factory)[PortfolioViewModel::class.java]
        
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme(
                darkTheme = viewModel.isDarkMode,
                dynamicColor = false // disable dynamic system colors to preserve chosen brand style colors
            ) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PortfolioAppNavHost(viewModel = viewModel)
                }
            }
        }
    }
}

@androidx.compose.runtime.Composable
fun Greeting(name: String, modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier) {
    androidx.compose.material3.Text(text = "Hello $name!", modifier = modifier)
}

