package com.example.lab10_2.data

import com.example.lab10_2.network.BookService

interface BooksRepository {
    suspend fun getBooks(query: String): List<BookInfo>
}

class NetworkBooksRepository(
    private val bookService: BookService
) : BooksRepository {
    override suspend fun getBooks(query: String): List<BookInfo> {
        val searchResponse = bookService.searchBooks(query)
        val items = searchResponse.items ?: emptyList()
        val books = items.map { item ->
            BookInfo(
                id = item.id,
                volumeInfo = item.volumeInfo
            )
        }
        return books
    }
}