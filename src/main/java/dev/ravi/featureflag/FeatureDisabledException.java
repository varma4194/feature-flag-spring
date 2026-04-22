package dev.ravi.featureflag;

public class FeatureDisabledException extends RuntimeException {
    private final String flagName;

    public FeatureDisabledException(String flagName) {
        super("Feature flag '" + flagName + "' is disabled");
        this.flagName = flagName;
    }

    public String getFlagName() {
        return flagName;
    }
}
