package org.geektimes.projects.user.management;

import java.util.List;

import org.geektimes.projects.user.domain.Book;

/**
 * MXBean 结尾，可以自定义对象进行嵌套，解析为：javax.management.openmbean.CompositeData
 * 
 * @author xubin
 * @date 2021/3/17 15:50
 */
public interface BookStoreManagementMXBean {

    String getStoreName();

    List<Book> getBooks();

    void setStoreName(String storeName);

    @Override
    String toString();

    String firstBookName();
}
