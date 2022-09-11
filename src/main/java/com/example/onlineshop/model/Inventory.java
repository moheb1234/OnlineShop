package com.example.onlineshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "INVENTORY", schema = "shop")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Min(value = 0, message = "inventory can not be mines")
    private int number;

    @OneToOne
    @JsonIgnore
    private Product product;

    @UpdateTimestamp
    private Date lastModifiedAt;

    public Inventory() {
        number = 1;
    }
}
