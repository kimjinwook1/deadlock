package com.example.deadlock.original.service;

import com.example.deadlock.original.component.DooHandler;
import com.example.deadlock.original.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SaveHandler {

    private final RootRepository rootRepository;
    private final JoRepository joRepository;
    private final FooRepository fooRepository;
    private final BaseEntityRepository baseEntityRepository;

    private final DooHandler dooHandler;


    @Transactional
    void extracted(final BaseEntity base, final RootEntity rootEntity, final List<Doo> dooAlllist, final List<Foo> fooList, final List<DooJo> dooJoList) {
        rootRepository.save(rootEntity);
        for (Doo doo : dooAlllist) {
            dooHandler.save(doo);
        }
        for (Foo foo : fooList) {
            fooRepository.save(foo);
        }
        baseEntityRepository.save(base);
        for (DooJo dooJo : dooJoList) {
            final Jo jo = dooJo.getJo();
            joRepository.save(jo);
        }
    }
}
