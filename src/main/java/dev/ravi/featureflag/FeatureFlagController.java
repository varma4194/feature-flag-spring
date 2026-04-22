package dev.ravi.featureflag;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/feature-flags")
@ConditionalOnProperty(prefix = "feature-flags.api", name = "enabled", havingValue = "true")
public class FeatureFlagController {
    private final FeatureFlagService featureFlagService;

    public FeatureFlagController(FeatureFlagService featureFlagService) {
        this.featureFlagService = featureFlagService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Boolean>> listFlags() {
        return ResponseEntity.ok(featureFlagService.getAllFlags());
    }

    @PutMapping("/{name}/enable")
    public ResponseEntity<Void> enableFlag(@PathVariable String name) {
        featureFlagService.enable(name);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{name}/disable")
    public ResponseEntity<Void> disableFlag(@PathVariable String name) {
        featureFlagService.disable(name);
        return ResponseEntity.ok().build();
    }
}
