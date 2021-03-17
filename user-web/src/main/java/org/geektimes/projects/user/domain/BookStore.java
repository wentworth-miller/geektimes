package org.geektimes.projects.user.domain;

import java.util.List;

/**
 * @author xubin
 * @date 2021/3/17 15:45
 */
public class BookStore {

    private String storeName;

    private List<Book> books;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "BookStore{" + "storeName='" + storeName + '\'' + ", books=" + books + '}';
    }
}
