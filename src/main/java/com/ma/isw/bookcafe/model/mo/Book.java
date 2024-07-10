package com.ma.isw.bookcafe.model.mo;

public class Book {
    private String ISBN;
    private String title;
    private String author;
    private String genre;
    private int publicationYear;
    private int yearEdition;
    private String description;
    private int numPages;
    private double bookPrice;
    private double ebookPrice;
    private int monthlyClicks;
    private boolean deleted;

    // Getters and Setters
    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public int getYearEdition() {
        return yearEdition;
    }

    public void setYearEdition(int yearEdition) {
        this.yearEdition = yearEdition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumPages() {
        return numPages;
    }

    public void setNumPages(int numPages) {
        this.numPages = numPages;
    }

    public double getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(double bookPrice) {
        this.bookPrice = bookPrice;
    }

    public double getEbookPrice() {
        return ebookPrice;
    }

    public void setEbookPrice(double ebookPrice) {
        this.ebookPrice = ebookPrice;
    }

    public int getMonthlyClicks() {
        return monthlyClicks;
    }

    public void setMonthlyClicks(int monthlyClicks) {
        this.monthlyClicks = monthlyClicks;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}

