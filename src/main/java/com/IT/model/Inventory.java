package com.IT.model;

import javax.persistence.*;

@Entity
@Table(name = "INVENTORY")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", nullable = false, updatable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUBCATEGORY_ID")
    private SubCategory subCategory;

    @Column(name = "QUANTITY", nullable = false)
    private int quantity;

    public Inventory(){

    }

    public Inventory(String name, SubCategory subCategory, int quantity) {
        this.name = name;
        this.subCategory = subCategory;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
