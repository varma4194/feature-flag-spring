# Feature Flag Spring Boot Starter

A lightweight, zero-dependency feature flag library for Spring Boot. Toggle features at runtime using annotations, configuration files, or a REST API.

## Features

- **Simple annotation-based** toggles with `@FeatureFlag`
- **Multiple backends**: in-memory (default) or Redis
- **Smart return handling**: returns sensible defaults based on method signature
- **Runtime control**: enable/disable flags via REST API
- **Production-ready**: minimal overhead, thread-safe

## Installation

Add to your `pom.xml`:

```xml
<dependency>
    <groupId>dev.ravi</groupId>
    <artifactId>feature-flag-spring</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Quick Start

### Basic Usage

Annotate any Spring bean method:

```java
@RestController
public class UserController {

    @GetMapping("/users/{id}")
    @FeatureFlag("new-user-api")
    public User getUser(@PathVariable Long id) {
        return userService.findById(id);
    }
}
```

### Configuration

Add to `application.yml`:

```yaml
feature-flags:
  backend: memory
  flags:
    new-user-api: true
    beta-feature: false
    dark-mode: true
  api:
    enabled: true
```

When a flag is disabled:
- By default, returns a sensible value based on return type:
  - `void` → `null`
  - `Optional<T>` → `Optional.empty()`
  - `List<T>` → `List.of()`
  - `String` → `""`
  - Primitives → `0`, `false`, `0.0`, etc.
  - Objects → `null`

## Advanced Usage

### Throw Exception When Disabled

```java
@GetMapping("/premium/data")
@FeatureFlag(value = "premium-feature", throwIfDisabled = true)
public PremiumData getPremiumData() {
    return premiumService.getData();
}
```

When the flag is off, raises `FeatureDisabledException` instead of returning a default.

### Custom Fallback

```java
@FeatureFlag(value = "new-algorithm", fallback = "fallback-algorithm")
public Result calculate(Data data) {
    return expensiveNewAlgorithm(data);
}
```

The `fallback` parameter is reserved for future use in scenarios where you'd delegate to an alternative method.

## Redis Backend

For distributed systems, use Redis:

```yaml
feature-flags:
  backend: redis
  flags:
    feature-a: true
```

Ensure you have Redis configured in Spring:

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
```

The library automatically detects `StringRedisTemplate` on the classpath and switches to Redis storage.

## REST API

If enabled in config, manage flags at runtime:

```yaml
feature-flags:
  api:
    enabled: true
```

Available endpoints:

```bash
# List all flags
GET /feature-flags

# Enable a flag
PUT /feature-flags/{name}/enable

# Disable a flag
PUT /feature-flags/{name}/disable
```

Example:

```bash
curl http://localhost:8080/feature-flags
curl -X PUT http://localhost:8080/feature-flags/new-user-api/enable
```

## Return Type Handling

The aspect intelligently handles return types:

| Return Type | Disabled Value |
|---|---|
| `void` | `null` |
| `Optional<T>` | `Optional.empty()` |
| `List<T>` / `Collection<T>` | `List.of()` (empty) |
| `String` | `""` |
| `int` / `Integer` | `0` |
| `long` / `Long` | `0L` |
| `boolean` / `Boolean` | `false` |
| `double` / `Double` | `0.0` |
| `float` / `Float` | `0.0f` |
| Custom objects | `null` |

## Error Handling

When `throwIfDisabled = true`:

```java
try {
    featureService.doSomething();
} catch (FeatureDisabledException e) {
    log.warn("Feature {} is disabled", e.getFlagName());
}
```

## Implementation Notes

- **Thread-safe**: Uses `ConcurrentHashMap` for in-memory storage
- **Zero configuration**: Works out of the box with sensible defaults
- **Aspect-based**: Integrates seamlessly with Spring AOP
- **Fast**: Minimal performance overhead
- **Testable**: Easy to mock or override for tests

## Example Project Structure

```
src/main/java/com/example/
├── MyApplication.java
├── controller/
│   └── UserController.java
├── service/
│   └── UserService.java
└── config/
    └── FeatureConfig.java

application.yml
```

## License

MIT
