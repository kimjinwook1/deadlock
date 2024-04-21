package com.example.deadlock.original.service;

import com.example.deadlock.original.component.DooHandler;
import com.example.deadlock.original.domain.*;
import com.example.deadlock.original.external.ExternalApi;
import com.example.deadlock.original.external.ExternalApi2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LongTransactionService {

    private final RootRepository rootRepository;
    private final JoRepository joRepository;
    private final FooRepository fooRepository;
    private final BaseEntityRepository baseEntityRepository;

    private final DooHandler dooHandler;

    private final ExternalApi externalApi;
    private final ExternalApi2 externalApi2;

    private final QueryHandler queryHandler;

    public void execute(BaseEntity base) {
        if (base.isAlreadyCompleted()) return;

        // query
        final String code = base.getCode();
        final Child child = queryHandler.getChild(code);
        final RootEntity rootEntity = queryHandler.getRootEntity(base);
        final List<Foo> fooList = queryHandler.getList(base);
        final List<Doo> dooAlllist = queryHandler.getDoos(fooList);
        final List<DooJo> dooJoList = queryHandler.getDooJos(dooAlllist);

        // command
        base.setData(child.getData());
        for (DooJo dooJo : dooJoList) {
            final Jo jo = dooJo.getJo();
            final Doo doo = dooJo.getDoo();
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
        rootRepository.save(rootEntity);
        externalApi.call(base, child);
        externalApi2.call(rootEntity.getId());
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
