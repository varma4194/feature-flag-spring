package dev.ravi.featureflag.example;

import dev.ravi.featureflag.FeatureFlag;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @FeatureFlag("user-details-v2")
    public Optional<UserDetails> getUserDetails(Long userId) {
        return Optional.of(new UserDetails(userId, "User " + userId, "user" + userId + "@example.com"));
    }

    @FeatureFlag(value = "admin-export", throwIfDisabled = true)
    public String exportUsersAsJson(List<Long> userIds) {
        return "{\"users\": [" + userIds.size() + "]}";
    }

    @FeatureFlag("batch-operations")
    public List<String> batchFetchUsers(List<Long> ids) {
        return ids.stream().map(id -> "User-" + id).toList();
    }

    @FeatureFlag("count-users")
    public int countActiveUsers() {
        return 42;
    }

    static class UserDetails {
        Long id;
        String name;
        String email;

        UserDetails(Long id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }
    }
}
