package dev.ravi.featureflag;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@AutoConfiguration
@EnableConfigurationProperties(FeatureFlagProperties.class)
public class FeatureFlagAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    public FeatureFlagService featureFlagService(
            FeatureFlagProperties properties) {

        if ("redis".equalsIgnoreCase(properties.getBackend())) {
            try {
                Class.forName("org.springframework.data.redis.core.StringRedisTemplate");
                return null;
            } catch (ClassNotFoundException e) {
                return new InMemoryFlagService(properties.getFlags());
            }
        }

        return new InMemoryFlagService(properties.getFlags());
    }

    @Bean
    @ConditionalOnClass(StringRedisTemplate.class)
    @ConditionalOnMissingBean
    public FeatureFlagService redisFeatureFlagService(
            FeatureFlagProperties properties,
            StringRedisTemplate redisTemplate) {

        if ("redis".equalsIgnoreCase(properties.getBackend())) {
            return new RedisFlagService(redisTemplate);
        }

        return null;
    }

    @Bean
    @ConditionalOnMissingBean
    public FeatureFlagAspect featureFlagAspect(FeatureFlagService featureFlagService) {
        return new FeatureFlagAspect(featureFlagService);
    }

    @Bean
    @ConditionalOnMissingBean
    public FeatureFlagController featureFlagController(FeatureFlagService featureFlagService) {
        return new FeatureFlagController(featureFlagService);
    }
}
