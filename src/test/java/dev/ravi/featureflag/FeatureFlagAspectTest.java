package dev.ravi.featureflag;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FeatureFlagAspectTest {
    private TestService testService;
    private InMemoryFlagService flagService;

    @BeforeEach
    void setup() {
        flagService = new InMemoryFlagService(null);
        var aspect = new FeatureFlagAspect(flagService);

        var factory = new AspectJProxyFactory(new TestService());
        factory.addAspect(aspect);
        testService = factory.getProxy();
    }

    @Test
    void testEnabledFlag() {
        flagService.enable("test-flag");
        assertEquals("result", testService.methodWithFlag());
    }

    @Test
    void testDisabledFlagReturnsNull() {
        flagService.disable("test-flag");
        assertNull(testService.methodWithFlag());
    }

    @Test
    void testDisabledFlagThrowsException() {
        flagService.disable("test-throw-flag");
        assertThrows(FeatureDisabledException.class, () -> testService.methodWithThrow());
    }

    @Test
    void testDisabledListReturnsEmpty() {
        flagService.disable("list-flag");
        var result = testService.listMethod();
        assertTrue(result.isEmpty());
    }

    @Test
    void testDisabledOptionalReturnsEmpty() {
        flagService.disable("optional-flag");
        var result = testService.optionalMethod();
        assertTrue(result.isEmpty());
    }

    @Test
    void testDisabledIntReturnsZero() {
        flagService.disable("int-flag");
        assertEquals(0, testService.intMethod());
    }

    public static class TestService {
        @FeatureFlag("test-flag")
        public String methodWithFlag() {
            return "result";
        }

        @FeatureFlag(value = "test-throw-flag", throwIfDisabled = true)
        public String methodWithThrow() {
            return "result";
        }

        @FeatureFlag("list-flag")
        public List<String> listMethod() {
            return List.of("item1", "item2");
        }

        @FeatureFlag("optional-flag")
        public Optional<String> optionalMethod() {
            return Optional.of("value");
        }

        @FeatureFlag("int-flag")
        public int intMethod() {
            return 42;
        }
    }
}
