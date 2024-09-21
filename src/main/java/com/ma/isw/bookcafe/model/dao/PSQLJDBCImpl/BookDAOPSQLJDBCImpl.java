package com.ma.isw.bookcafe.model.dao.PSQLJDBCImpl;

import com.ma.isw.bookcafe.model.dao.BookDAO;
import com.ma.isw.bookcafe.model.dao.exception.NoBookFoundException;
import com.ma.isw.bookcafe.model.dao.exception.NoEventFoundException;
import com.ma.isw.bookcafe.model.mo.Book;
import com.ma.isw.bookcafe.model.mo.Event;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class BookDAOPSQLJDBCImpl implements BookDAO {

    Connection conn;

    public BookDAOPSQLJDBCImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void addBook(String ISBN, String title, String author, String genre, int publicationYear, int yearEdition, String description, int numPages, double bookPrice, double ebookPrice, int monthlyClicks, String urlBookcoverImage) {

    }

    @Override
    public void updateBook(Book book) {

    }

    @Override
    public void deleteBook(String ISBN) {

    }

    @Override
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM book";
            ps= conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Book book = readBook(rs);

                books.add(book);
            }

        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
        return books;
    }

    @Override
    public Book getBookByISBN(String ISBN) throws NoBookFoundException{
        Book book;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM book WHERE isbn=?";

            ps= conn.prepareStatement(sql);
            ps.setString(1, ISBN);

            rs = ps.executeQuery();

            if(rs.next()) {
                book = readBook(rs);
            }
            else throw new NoBookFoundException("No_Event_in_DB");
        }
        catch(Exception e){
            if(e.getMessage().contains("No_Thread_in_DB")){
                throw new NoBookFoundException("Errore: evento non trovato");
            }
            throw new RuntimeException(e);
        }
        return book;
    }

    public static Book readBook(ResultSet rs){
        Book book = new Book();
        try {
            String ISBN = rs.getString("ISBN");
            String title = rs.getString("title");
            String author = rs.getString("author");
            String genre = rs.getString("genre");
            int publicationYear = rs.getInt("publication_year");
            int yearEdition = rs.getInt("year_edition");
            String description = rs.getString("description");
            int numPages = rs.getInt("num_pages");
            double bookPrice = rs.getDouble("book_price");
            double ebookPrice = rs.getDouble("ebook_price");
            int monthlyClicks = rs.getInt("monthly_clicks");
            String urlBookcoverImage = rs.getString("url_bookcover_image");
            boolean deleted = rs.getBoolean("deleted");

            book.setISBN(ISBN);
            book.setTitle(title);
            book.setAuthor(author);
            book.setGenre(genre);
            book.setPublicationYear(publicationYear);
            book.setYearEdition(yearEdition);
            book.setDescription(description);
            book.setNumPages(numPages);
            book.setBookPrice(bookPrice);
            book.setEbookPrice(ebookPrice);
            book.setMonthlyClicks(monthlyClicks);
            book.setUrlBookcoverImage(urlBookcoverImage);
            book.setDeleted(deleted);
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return book;
    }
}