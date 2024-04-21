package com.example.deadlock.original.component;

import com.example.deadlock.original.domain.Doo;
import com.example.deadlock.original.domain.Jo;
import com.example.deadlock.original.domain.JoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DooExecutor {
    private final JoRepository joRepository;

    public void doIt(List<Doo> dooList) {
        for (Doo doo : dooList) {
            final Long id = doo.getId();
            final Jo jo = joRepository.findById(id).orElseThrow(() -> new RuntimeException("not found"));
            jo.handle(doo.getJoData());

            doo.cool();
        }
    }
}
