package com.springboot.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="p")
public class Phone {
    private int id;
    private String name;
    private BigDecimal price;
    private Date dateInProduced;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public Date getDateInProduced() {
        return dateInProduced;
    }

    public void setDateInProduced(Date dateInProduced) {
        this.dateInProduced = dateInProduced;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        Phone phone = (Phone)o;
        return id == phone.id &&
            Objects.equals(name, phone.name) &&
            Objects.equals(price, phone.price) &&
            Objects.equals(dateInProduced, phone.dateInProduced);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, price, dateInProduced);
    }

    @Override
    public String toString() {
        return "Phone{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", price=" + price +
            ", dateInProduced=" + dateInProduced +
            '}';
    }
}
