package com.example.lab10_2.data

import com.example.lab10_2.network.BookService

interface BooksRepository {
    suspend fun getBooks(query: String): List<BookInfo>
}

class NetworkBooksRepository(
    private val bookService: BookService
) : BooksRepository {
    override suspend fun getBooks(query: String): List<BookInfo> {
        return try {
            val searchResponse = bookService.searchBooks(query)
            val items = searchResponse.items ?: emptyList()
            val books = mutableListOf<BookInfo>()
            for (item in items) {
                try {
                    val bookInfo = bookService.getBook(item.id)
                    books.add(bookInfo)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            books
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback mock data in case of HTTP 429 Too Many Requests
            listOf(
                BookInfo("1", VolumeInfo("The Google Story", ImageLinks("https://books.google.com/books/content?id=zyTCAlFPjgYC&printsec=frontcover&img=1&zoom=1"))),
                BookInfo("2", VolumeInfo("Clean Code", ImageLinks("https://books.google.com/books/content?id=_i6bDeoCQzsC&printsec=frontcover&img=1&zoom=1"))),
                BookInfo("3", VolumeInfo("Android Programming", ImageLinks("https://books.google.com/books/content?id=pIsfAAAAQBAJ&printsec=frontcover&img=1&zoom=1"))),
                BookInfo("4", VolumeInfo("Kotlin in Action", ImageLinks("https://books.google.com/books/content?id=S4a0AwAAQBAJ&printsec=frontcover&img=1&zoom=1"))),
                BookInfo("5", VolumeInfo("Head First Android", ImageLinks("https://books.google.com/books/content?id=L_uXAwAAQBAJ&printsec=frontcover&img=1&zoom=1"))),
                BookInfo("6", VolumeInfo("Java: The Complete Reference", ImageLinks("https://books.google.com/books/content?id=fQ83EAAAQBAJ&printsec=frontcover&img=1&zoom=1"))),
                BookInfo("7", VolumeInfo("Effective Java", ImageLinks("https://books.google.com/books/content?id=ka2VUBqHiWkC&printsec=frontcover&img=1&zoom=1"))),
                BookInfo("8", VolumeInfo("Design Patterns", ImageLinks("https://books.google.com/books/content?id=6ohuptvnEZ8C&printsec=frontcover&img=1&zoom=1")))
            )
        }
    }
}