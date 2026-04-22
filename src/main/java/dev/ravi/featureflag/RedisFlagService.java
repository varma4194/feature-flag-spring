package dev.ravi.featureflag;

import org.springframework.data.redis.core.StringRedisTemplate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RedisFlagService implements FeatureFlagService {
    private static final String KEY_PREFIX = "feature-flags:";
    private final StringRedisTemplate redisTemplate;

    public RedisFlagService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean isEnabled(String flagName) {
        String value = redisTemplate.opsForValue().get(KEY_PREFIX + flagName);
        return "1".equals(value);
    }

    @Override
    public void enable(String flagName) {
        redisTemplate.opsForValue().set(KEY_PREFIX + flagName, "1");
    }

    @Override
    public void disable(String flagName) {
        redisTemplate.opsForValue().set(KEY_PREFIX + flagName, "0");
    }

    @Override
    public Map<String, Boolean> getAllFlags() {
        var flags = new HashMap<String, Boolean>();
        Set<String> keys = redisTemplate.keys(KEY_PREFIX + "*");

        if (keys != null) {
            for (String key : keys) {
                String flagName = key.substring(KEY_PREFIX.length());
                String value = redisTemplate.opsForValue().get(key);
                flags.put(flagName, "1".equals(value));
            }
        }

        return flags;
    }
}
