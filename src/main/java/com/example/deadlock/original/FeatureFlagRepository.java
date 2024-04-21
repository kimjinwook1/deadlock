package com.example.deadlock.original;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FeatureFlagRepository extends JpaRepository<FeatureFlag, Long> {
    FeatureFlag findByFeature(String feature);
}
