package org.geektimes.projects.user.domain;

import java.math.BigDecimal;

/**
 * @author xubin
 * @date 2021/3/17 15:45
 */
public class Book {

    private String name;

    private BigDecimal price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
