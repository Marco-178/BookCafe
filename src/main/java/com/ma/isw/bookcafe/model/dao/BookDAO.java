package com.ma.isw.bookcafe.model.dao;

import com.ma.isw.bookcafe.model.dao.exception.NoBookFoundException;
import com.ma.isw.bookcafe.model.mo.Book;
import java.util.List;

public interface BookDAO {

    public void addBook(String ISBN,
                        String title,
                        String author,
                        String genre,
                        int publicationYear,
                        int yearEdition,
                        String description,
                        int numPages,
                        double bookPrice,
                        double ebookPrice,
                        int monthlyClicks,
                        String urlBookcoverImage);

    public void updateBook(Book book);

    public void deleteBook(String ISBN);

    public Book getBookByISBN(String ISBN) throws NoBookFoundException;

    public List<Book> getAllBooks();
}

