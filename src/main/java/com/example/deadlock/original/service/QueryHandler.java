package com.example.deadlock.original.service;

import com.example.deadlock.original.component.DooComponent;
import com.example.deadlock.original.component.FooComponent;
import com.example.deadlock.original.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryHandler {

    private final ChildRepository childRepository;
    private final FooComponent fooComponent;
    private final DooComponent dooComponent;
    private final JoRepository joRepository;
    private final RootRepository rootRepository;

    List<DooJo> getDooJos(final List<Doo> dooAlllist) {
        List<DooJo> dooJoList = new ArrayList<>();
        for (Doo doo : dooAlllist) {
            final Long id = doo.getId();
            final Jo jo = joRepository.findById(id).orElseThrow(() -> new RuntimeException("not found"));
            dooJoList.add(new DooJo(jo, doo));
        }
        return dooJoList;
    }

    List<Doo> getDoos(final List<Foo> fooList) {
        List<Doo> dooAlllist = new ArrayList<>();
        for (Foo foo : fooList) {
            final List<Doo> dooList = dooComponent.list(foo.getId());
            dooAlllist.addAll(dooList);
        }
        return dooAlllist;
    }

    List<Foo> getList(final BaseEntity base) {
        return fooComponent.list(base.getFoo());
    }

    RootEntity getRootEntity(final BaseEntity base) {
        return rootRepository.findById(base.getRootId()).orElseThrow();
    }

    Child getChild(String code) {
        return childRepository.findByCode(code).orElseThrow(() -> new RuntimeException("not found"));
    }
}
