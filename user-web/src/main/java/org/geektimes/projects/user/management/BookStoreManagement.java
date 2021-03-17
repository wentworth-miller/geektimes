package org.geektimes.projects.user.management;

import java.util.List;

import org.geektimes.projects.user.domain.Book;
import org.geektimes.projects.user.domain.BookStore;

/**
 * @author xubin
 * @date 2021/3/17 16:14
 */
public class BookStoreManagement implements BookStoreManagementMXBean {

    private BookStore bookStore;

    public BookStoreManagement(BookStore bookStore) {
        this.bookStore = bookStore;
    }

    @Override
    public String getStoreName() {
        return bookStore.getStoreName();
    }

    @Override
    public List<Book> getBooks() {
        return bookStore.getBooks();
    }

    @Override
    public void setStoreName(String storeName) {
        bookStore.setStoreName(storeName);
    }

    @Override
    public String toString() {
        return bookStore.toString();
    }

    @Override
    public String firstBookName() {
        return bookStore.getStoreName();
    }
}
