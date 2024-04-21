package com.example.deadlock.original.component;

import com.example.deadlock.original.domain.Doo;
import com.example.deadlock.original.domain.Foo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BarComponent {
    private final DooComponent dooComponent;
    private final DooExecutor dooExecutor;
    private final DooHandler dooHandler;

    public void execute(Foo foo) {
        final List<Doo> dooList = dooComponent.list(foo.getId());
        dooExecutor.doIt(dooList);
        dooList.forEach(doo -> dooHandler.save(doo));
    }
}
