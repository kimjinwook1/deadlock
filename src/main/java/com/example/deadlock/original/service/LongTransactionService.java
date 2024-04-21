package com.example.deadlock.original.service;

import com.example.deadlock.original.component.*;
import com.example.deadlock.original.domain.*;
import com.example.deadlock.original.external.ExternalApi;
import com.example.deadlock.original.external.ExternalApi2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        base.setData(child.getData());

        final List<Foo> fooList = fooComponent.list(base.getFoo());
        for (Foo foo : fooList) {

            final List<Doo> dooList = dooComponent.list(foo.getId());

            for (Doo doo : dooList) {
                final Long id = doo.getId();
                final Jo jo = joRepository.findById(id).orElseThrow(() -> new RuntimeException("not found"));
                jo.handle(doo.getJoData());

                doo.cool();
            }
            dooList.forEach(doo -> dooHandler.save(doo));
            foo.complete();
        }
        externalApi.call(base, child);

        base.complete();
        final RootEntity rootEntity = rootRepository.findById(base.getRootId()).orElseThrow();
        rootEntity.complete();
        rootRepository.save(rootEntity);
        externalApi2.call(rootEntity.getId());
    }

    private Child getChild(String code) {
        return childRepository.findByCode(code).orElseThrow(() -> new RuntimeException("not found"));
    }

}
