package com.example.deadlock.original.service;

import com.example.deadlock.original.component.DooComponent;
import com.example.deadlock.original.component.DooHandler;
import com.example.deadlock.original.component.FooComponent;
import com.example.deadlock.original.domain.*;
import com.example.deadlock.original.external.ExternalApi;
import com.example.deadlock.original.external.ExternalApi2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LongTransactionService {

    private final ChildRepository childRepository;
    private final RootRepository rootRepository;

    private final ExternalApi externalApi;
    private final FooComponent fooComponent;

    private final DooComponent dooComponent;
    private final DooHandler dooHandler;
    private final JoRepository joRepository;

    private final ExternalApi2 externalApi2;

    @Transactional
    public void execute(BaseEntity base) {
        if (base.isAlreadyCompleted()) return;
        final String code = base.getCode();
        final Child child = getChild(code);

        // query
        List<Doo> dooAlllist = new ArrayList<>();

        final RootEntity rootEntity = rootRepository.findById(base.getRootId()).orElseThrow();

        final List<Foo> fooList = fooComponent.list(base.getFoo());
        for (Foo foo : fooList) {
            final List<Doo> dooList = dooComponent.list(foo.getId());
            dooAlllist.addAll(dooList);
        }

        List<DooJo> dooJoList = new ArrayList<>();
        for (Doo doo : dooAlllist) {
            final Long id = doo.getId();
            final Jo jo = joRepository.findById(id).orElseThrow(() -> new RuntimeException("not found"));
            dooJoList.add(new DooJo(jo, doo));
        }

        // command
        base.setData(child.getData());
        for (DooJo dooJo : dooJoList) {
            final Jo jo = dooJo.jo;
            final Doo doo = dooJo.doo;
            jo.handle(doo.getJoData());
        }
        for (Doo doo : dooAlllist) {
            doo.cool();
        }
        base.complete();
        rootEntity.complete();
        for (Foo foo : fooList) {
            foo.complete();
        }

        // save
        for (Doo doo : dooAlllist) {
            dooHandler.save(doo);
        }

        rootRepository.save(rootEntity);
        externalApi.call(base, child);
        externalApi2.call(rootEntity.getId());
    }

    private Child getChild(String code) {
        return childRepository.findByCode(code).orElseThrow(() -> new RuntimeException("not found"));
    }

    private class DooJo {
        private final Jo jo;
        private final Doo doo;

        public DooJo(final Jo jo, final Doo doo) {
            this.jo = jo;
            this.doo = doo;
        }
    }

}
