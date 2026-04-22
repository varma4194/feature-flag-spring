package dev.ravi.featureflag.example;

import dev.ravi.featureflag.FeatureFlag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/demo")
public class DemoController {

    private final UserService userService;

    public DemoController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/{id}/details")
    public ResponseEntity<Optional<?>> getUserDetails(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserDetails(id));
    }

    @GetMapping("/users/active/count")
    public ResponseEntity<Integer> getActiveUsersCount() {
        return ResponseEntity.ok(userService.countActiveUsers());
    }

    @GetMapping("/users/batch")
    public ResponseEntity<List<String>> batchFetch() {
        return ResponseEntity.ok(userService.batchFetchUsers(List.of(1L, 2L, 3L)));
    }

    @GetMapping("/health")
    @FeatureFlag("health-check-enabled")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
}
