package com.example.deadlock.original.component;

import com.example.deadlock.original.domain.Doo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DooComponent {
    public List<Doo> list(Long id) {
        return List.of(new Doo());
    }
}
