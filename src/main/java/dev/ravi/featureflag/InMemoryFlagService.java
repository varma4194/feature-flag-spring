package dev.ravi.featureflag;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryFlagService implements FeatureFlagService {
    private final ConcurrentHashMap<String, Boolean> flags;

    public InMemoryFlagService(Map<String, Boolean> initialFlags) {
        this.flags = new ConcurrentHashMap<>(initialFlags != null ? initialFlags : new HashMap<>());
    }

    @Override
    public boolean isEnabled(String flagName) {
        return flags.getOrDefault(flagName, false);
    }

    @Override
    public void enable(String flagName) {
        flags.put(flagName, true);
    }

    @Override
    public void disable(String flagName) {
        flags.put(flagName, false);
    }

    @Override
    public Map<String, Boolean> getAllFlags() {
        return Collections.unmodifiableMap(new HashMap<>(flags));
    }
}
