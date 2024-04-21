package com.example.deadlock.original;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class FeatureFlag {

    @Id
    private Long id;
    private String feature;
    private boolean enable;

    public boolean isEnable() {
        return enable;
    }
}
