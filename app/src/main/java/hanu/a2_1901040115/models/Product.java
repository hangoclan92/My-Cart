package hanu.a2_1901040115.models;

public class Product {
    private int id;
    private String thumbnail;
    private String name;
    private double price;
    private int numItems;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getNumItems() {
        return numItems;
    }

    public void setNumItems(int numItems) {
        this.numItems = numItems;
    }

    public Product(int id, String thumbnail, String name, double price) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.name = name;
        this.price = price;
    }

    public Product(int id, String thumbnail, String name, double price, int numItems) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.name = name;
        this.price = price;
        this.numItems = numItems;
    }
}
