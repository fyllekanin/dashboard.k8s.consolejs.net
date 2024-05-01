package net.consolejs.k8s.dashboard.entityview.restservice.response;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class AuthorizationResponse {
    @SerializedName("username")
    private final String myUsername;
    @SerializedName("accessToken")
    private final String myAccessToken;
    @SerializedName("refreshToken")
    private final String myRefreshToken;

    private AuthorizationResponse(Builder builder) {
        myUsername = builder.myUsername;
        myAccessToken = builder.myAccessToken;
        myRefreshToken = builder.myRefreshToken;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getUsername() {
        return myUsername;
    }

    public String getAccessToken() {
        return myAccessToken;
    }

    public String getRefreshToken() {
        return myRefreshToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AuthorizationResponse that = (AuthorizationResponse) o;
        return Objects.equals(myUsername, that.myUsername) &&
                Objects.equals(myAccessToken, that.myAccessToken) &&
                Objects.equals(myRefreshToken, that.myRefreshToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(myUsername, myAccessToken, myRefreshToken);
    }

    public static class Builder {
        private String myUsername;
        private String myAccessToken;
        private String myRefreshToken;

        private Builder() {

        }

        public Builder withUsername(String username) {
            myUsername = username;
            return this;
        }

        public Builder withAccessToken(String accessToken) {
            myAccessToken = accessToken;
            return this;
        }

        public Builder withRefreshToken(String refreshToken) {
            myRefreshToken = refreshToken;
            return this;
        }

        public AuthorizationResponse build() {
            return new AuthorizationResponse(this);
        }
    }
}