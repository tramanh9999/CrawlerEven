package entities;

import java.io.Serializable;

public class Category implements Serializable {


    public Category(int id, String categoryName, String categoryUrl) {
        this.id = id;
        this.categoryName = categoryName;
        this.categoryUrl = categoryUrl;
    }

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryUrl() {
        return categoryUrl;
    }

    public void setCategoryUrl(String categoryUrl) {
        this.categoryUrl = categoryUrl;
    }

    private String categoryName;

    private String categoryUrl;

    public Category() {
    }
}
