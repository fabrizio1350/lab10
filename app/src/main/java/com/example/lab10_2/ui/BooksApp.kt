package com.example.lab10_2.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lab10_2.ui.screens.HomeScreen

@Composable
fun BooksApp() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val booksViewModel: BooksViewModel = viewModel(factory = BooksViewModel.Factory)
        HomeScreen(
            booksUiState = booksViewModel.booksUiState,
            retryAction = { booksViewModel.getBooks("android development") }
        )
    }
}