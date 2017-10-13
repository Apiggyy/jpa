package com.self.learning.helloworld;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Table(name = "t_category")
@Entity
public class Category {
    private int id;
    private String categoryName;
    private Set<Item> items = new HashSet<Item>();

    @GeneratedValue
    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "category_name")
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @ManyToMany(mappedBy = "categories")
    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }
}
