package cvicenia.gettersSettersTest.api;

import java.util.Date;

public class Product {
    private double price;
    private String name;
    private long quantity;
    private Date datum;

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public long getQuantity() {
        return quantity;
    }

    public Date getDatum() {
        return datum;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public void setDatum(Date d) {
        this.datum = d;
    }
}
