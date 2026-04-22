package dev.ravi.featureflag;

import java.util.Map;

public interface FeatureFlagService {
    boolean isEnabled(String flagName);
    void enable(String flagName);
    void disable(String flagName);
    Map<String, Boolean> getAllFlags();
}
