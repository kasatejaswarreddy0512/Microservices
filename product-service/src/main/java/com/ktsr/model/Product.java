package com.ktsr.model;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Product {

    @Id
    private String id;
    @NotNull(message = "Product name must not be null...!")
    private String name;
    @NotNull(message = "Product description must not be null...!")
    private String description;
    @NotNull(message = "Product price must not be null...!")
    private double price;
}
