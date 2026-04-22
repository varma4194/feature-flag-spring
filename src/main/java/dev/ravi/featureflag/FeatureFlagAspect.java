package dev.ravi.featureflag;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Aspect
@Component
public class FeatureFlagAspect {
    private final FeatureFlagService featureFlagService;

    public FeatureFlagAspect(FeatureFlagService featureFlagService) {
        this.featureFlagService = featureFlagService;
    }

    @Around("@annotation(dev.ravi.featureflag.FeatureFlag)")
    public Object interceptFeatureFlag(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        FeatureFlag annotation = method.getAnnotation(FeatureFlag.class);

        String flagName = annotation.value();
        boolean enabled = featureFlagService.isEnabled(flagName);

        if (enabled) {
            return joinPoint.proceed();
        }

        if (annotation.throwIfDisabled()) {
            throw new FeatureDisabledException(flagName);
        }

        return getDefaultReturnValue(method);
    }

    private Object getDefaultReturnValue(Method method) {
        Class<?> returnType = method.getReturnType();

        if (returnType == void.class) {
            return null;
        }

        if (returnType == Optional.class) {
            return Optional.empty();
        }

        if (Collection.class.isAssignableFrom(returnType)) {
            if (List.class.isAssignableFrom(returnType)) {
                return List.of();
            }
            // For other collection types, return empty implementations
            if (returnType.isInterface()) {
                return returnType == Collection.class ? List.of() : null;
            }
            try {
                return returnType.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                return null;
            }
        }

        if (returnType == String.class) {
            return "";
        }

        if (returnType == int.class || returnType == Integer.class) {
            return 0;
        }

        if (returnType == long.class || returnType == Long.class) {
            return 0L;
        }

        if (returnType == boolean.class || returnType == Boolean.class) {
            return false;
        }

        if (returnType == double.class || returnType == Double.class) {
            return 0.0;
        }

        if (returnType == float.class || returnType == Float.class) {
            return 0.0f;
        }

        return null;
    }
}
