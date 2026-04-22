package dev.ravi.featureflag;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryFlagServiceTest {
    private InMemoryFlagService service;

    @BeforeEach
    void setup() {
        var initialFlags = new HashMap<String, Boolean>();
        initialFlags.put("enabled-flag", true);
        initialFlags.put("disabled-flag", false);
        service = new InMemoryFlagService(initialFlags);
    }

    @Test
    void testIsEnabledReturnsTrueWhenEnabled() {
        assertTrue(service.isEnabled("enabled-flag"));
    }

    @Test
    void testIsEnabledReturnsFalseWhenDisabled() {
        assertFalse(service.isEnabled("disabled-flag"));
    }

    @Test
    void testIsEnabledReturnsFalseForUnknownFlag() {
        assertFalse(service.isEnabled("unknown-flag"));
    }

    @Test
    void testEnableFlagSetsToTrue() {
        service.enable("disabled-flag");
        assertTrue(service.isEnabled("disabled-flag"));
    }

    @Test
    void testDisableFlagSetsToFalse() {
        service.disable("enabled-flag");
        assertFalse(service.isEnabled("enabled-flag"));
    }

    @Test
    void testGetAllFlags() {
        Map<String, Boolean> flags = service.getAllFlags();
        assertEquals(2, flags.size());
        assertTrue(flags.get("enabled-flag"));
        assertFalse(flags.get("disabled-flag"));
    }

    @Test
    void testGetAllFlagsReturnsImmutableCopy() {
        var flags = service.getAllFlags();
        assertThrows(UnsupportedOperationException.class, () -> flags.put("new", true));
    }
}
