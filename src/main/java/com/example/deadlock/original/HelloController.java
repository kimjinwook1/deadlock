package com.example.deadlock.original;

import com.example.deadlock.original.domain.BaseEntity;
import com.example.deadlock.original.service.LongTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class HelloController {
    private final LongTransactionService longTransactionService;
    private final LongTransactionService longTransactionService2;
    private final FeatureFlags featureFlags;

    public void hello() {

        final boolean enable = featureFlags.isEnable("feature-name");
        if (enable) {
            longTransactionService.execute(new BaseEntity());
        } else {
            longTransactionService2.execute(new BaseEntity());
        }
    }
}
