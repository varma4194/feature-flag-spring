package dev.ravi.featureflag.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"dev.ravi.featureflag", "dev.ravi.featureflag.example"})
public class FeatureFlagDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeatureFlagDemoApplication.class, args);
    }
}
