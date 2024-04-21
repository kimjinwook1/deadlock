package com.example.deadlock.original;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeatureFlags {

    private final FeatureFlagRepository featureFlagRepository;

    public boolean isEnable(final String featureName){
        final FeatureFlag feature = featureFlagRepository.findByFeature(featureName);
        return feature.isEnable();
    }
}
