package com.self.learning.helloworld;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Table(name = "t_item")
@Entity
public class Item {
    private int id;
    private String itemName;
    private Set<Category> categories = new HashSet<Category>();

    @GeneratedValue
    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "item_name")
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @JoinTable(name = "category_item_map"
            ,joinColumns = {@JoinColumn(name = "item_id",referencedColumnName = "id")}
            ,inverseJoinColumns = {@JoinColumn(name="category_id",referencedColumnName = "id")})
    @ManyToMany
    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }
}
