package net.consolejs.k8s.dashboard.entityview.restservice.request;

import com.google.gson.annotations.SerializedName;

public class LoginBody {
    @SerializedName("username")
    private final String myUsername;
    @SerializedName("password")
    private final String myPassword;

    private LoginBody(Builder builder) {
        myUsername = builder.myUsername;
        myPassword = builder.myPassword;
    }

    public String getUsername() {
        return myUsername;
    }

    public String getPassword() {
        return myPassword;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String myUsername;
        private String myPassword;

        private Builder() {
            // Empty
        }

        public Builder withUsername(String username) {
            myUsername = username;
            return this;
        }

        public Builder withPassword(String password) {
            myPassword = password;
            return this;
        }

        public LoginBody build() {
            return new LoginBody(this);
        }
    }
}
