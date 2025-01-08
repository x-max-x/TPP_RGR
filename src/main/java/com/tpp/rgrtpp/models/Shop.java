package com.tpp.rgrtpp.models;

import jakarta.persistence.*;

@Entity
@Table(name = "shops")
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String shopName;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    private String address;

    private Integer openingYear;

    // Гетери та сетери
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getShopName() {
        return shopName;
    }
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
    public City getCity() {
        return city;
    }
    public void setCity(City city) {
        this.city = city;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public Integer getOpeningYear() {
        return openingYear;
    }
    public void setOpeningYear(Integer openingYear) {
        this.openingYear = openingYear;
    }
}
