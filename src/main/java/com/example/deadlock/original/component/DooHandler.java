package com.example.deadlock.original.component;

import com.example.deadlock.original.domain.Doo;
import org.springframework.stereotype.Component;

@Component
public class DooHandler {
    public void save(Doo doo) {
        System.out.println("handle");
    }
}
