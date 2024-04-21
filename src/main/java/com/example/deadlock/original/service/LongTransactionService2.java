package com.example.deadlock.original.service;

import com.example.deadlock.original.component.CompleteComponent;
import com.example.deadlock.original.component.UpdateComponent;
import com.example.deadlock.original.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LongTransactionService2 {

    private final ChildRepository childRepository;
    private final RootRepository rootRepository;
    private final UpdateComponent updateComponent;
    private final CompleteComponent completeComponent;

    @Transactional
    public void execute(BaseEntity base) {
        if (base.isAlreadyCompleted()) return;
        final String code = base.getCode();
        final Child child = getChild(code);
        base.setData(child.getData());
        completeComponent.execute(base, child);
        base.complete();
        updateRoot(base.getRootId());
    }

    private Child getChild(String code) {
        return childRepository.findByCode(code).orElseThrow(() -> new RuntimeException("not found"));
    }

    private void updateRoot(Long rootId) {
        final RootEntity rootEntity = rootRepository.findById(rootId).orElseThrow();
        rootEntity.complete();
        updateComponent.execute(rootEntity);

    }

}
