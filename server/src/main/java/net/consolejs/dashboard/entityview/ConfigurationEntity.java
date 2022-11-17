package net.consolejs.dashboard.entityview;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationEntity {
    private final List<String> myNamespaces;

    private ConfigurationEntity(Builder builder) {
        myNamespaces = builder.myNamespaces;
    }

    public List<String> getNamespaces() {
        return new ArrayList<>(myNamespaces);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private List<String> myNamespaces;

        Builder() {
            // Empty
        }

        public Builder withNamespaces(List<String> namespaces) {
            myNamespaces = new ArrayList<>(namespaces);
            return this;
        }

        public ConfigurationEntity build() {
            return new ConfigurationEntity(this);
        }
    }
}
