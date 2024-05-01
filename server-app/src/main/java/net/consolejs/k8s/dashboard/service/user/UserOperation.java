package net.consolejs.k8s.dashboard.service.user;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class UserOperation {
    private final String myUsername;
    private final String myNamespace;
    private final boolean myIsForCluster;
    private final List<String> myApiGroups;
    private final List<String> myResources;
    private final List<String> myVerbs;

    public UserOperation(Builder builder) {
        myUsername = builder.myUsername;
        myNamespace = builder.myNamespace;
        myIsForCluster = builder.myIsForCluster;
        myApiGroups = builder.myApiGroups == null ? new ArrayList<>() : ImmutableList.copyOf(builder.myApiGroups);
        myResources = builder.myResources == null ? new ArrayList<>() : ImmutableList.copyOf(builder.myResources);
        myVerbs = builder.myVerbs == null ? new ArrayList<>() : ImmutableList.copyOf(builder.myVerbs);
    }

    public String getUsername() {
        return myUsername;
    }

    public String getNamespace() {
        return myNamespace;
    }

    public boolean isForCluster() {
        return myIsForCluster;
    }

    public List<String> getApiGroups() {
        return ImmutableList.copyOf(myApiGroups);
    }

    public List<String> getResources() {
        return ImmutableList.copyOf(myResources);
    }

    public List<String> getVerbs() {
        return ImmutableList.copyOf(myVerbs);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String myUsername;
        private String myNamespace;
        private boolean myIsForCluster;
        private List<String> myApiGroups;
        private List<String> myResources;
        private List<String> myVerbs;

        private Builder() {
            // Empty
        }

        public Builder withUsername(String username) {
            myUsername = username;
            return this;
        }

        public Builder withNamespace(String namespace) {
            myNamespace = namespace;
            return this;
        }

        public Builder isForCluster(boolean isForCluster) {
            myIsForCluster = isForCluster;
            return this;
        }

        public Builder withApiGroups(List<String> apiGroups) {
            myApiGroups = apiGroups;
            return this;
        }

        public Builder withResources(List<String> resources) {
            myResources = resources;
            return this;
        }

        public Builder withVerbs(List<String> verbs) {
            myVerbs = verbs;
            return this;
        }

        public UserOperation build() {
            Preconditions.checkArgument((myNamespace != null && !myIsForCluster || myNamespace == null && myIsForCluster),
                                        "Either namespace or isForCluster needs to be set");
            Preconditions.checkNotNull(myUsername, "Username can not be null");
            return new UserOperation(this);
        }
    }
}
