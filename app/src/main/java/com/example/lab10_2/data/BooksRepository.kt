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
        val books = mutableListOf<BookInfo>()
        for (item in items) {
            try {
                val bookInfo = bookService.getBook(item.id)
                books.add(bookInfo)
            } catch (e: Exception) {
                // Ignore if one fails
            }
        }
        return books
    }
}