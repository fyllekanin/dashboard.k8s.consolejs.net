package net.consolejs.k8s.dashboard.restservice.service.impl.user;

import com.google.gson.Gson;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import net.consolejs.k8s.dashboard.entityview.repository.user.UserDocument;
import net.consolejs.k8s.dashboard.entityview.restservice.request.LoginBody;
import net.consolejs.k8s.dashboard.entityview.restservice.response.AuthorizationResponse;
import net.consolejs.k8s.dashboard.repository.RepositoryFactory;
import net.consolejs.k8s.dashboard.repository.repositories.user.UserRepository;
import net.consolejs.k8s.dashboard.restservice.service.api.user.RestAuthenticationService;
import net.consolejs.k8s.dashboard.utilities.HashUtility;
import net.consolejs.k8s.dashboard.utilities.JwtUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Calendar;
import java.util.Date;

public class RestAuthenticationServiceImpl implements RestAuthenticationService {
    private static final Gson GSON = new Gson();
    private final String myJwtSecret;

    @Inject
    private RepositoryFactory myRepositoryFactory;

    public RestAuthenticationServiceImpl(@ConfigProperty(name = "JWT_SECRET") String jwtSecret) {
        myJwtSecret = jwtSecret;
    }

    @Override
    public Response login(String body) {
        Response response;
        try {
            LoginBody loginBody = GSON.fromJson(body, LoginBody.class);
            response = Response.status(Response.Status.OK)
                    .entity(GSON.toJson(getAuthorizationResponse(loginBody)))
                    .build();
        } catch (Exception e) {
            // TODO: Implement common handleException
            response = Response.status(Response.Status.BAD_REQUEST)
                    .entity(e)
                    .build();
        }
        return response;
    }

    @Override
    public Response getRefresh(String refreshToken) {
        return null;
    }

    private AuthorizationResponse getAuthorizationResponse(LoginBody loginBody) {
        UserRepository repository = myRepositoryFactory.of(UserRepository.class);
        UserDocument user = repository.getByUsername(loginBody.getUsername());
        if (user == null || !HashUtility.isPasswordEqualHash(loginBody.getPassword(), user.getPassword())) {
            throw new NotFoundException("Username or password incorrect");
        }

        return AuthorizationResponse.newBuilder()
                .withUsername(user.getUsername())
                .withAccessToken(JwtUtils.getToken(user.getObjectId(), getDateWithDayForward(1), myJwtSecret))
                .withRefreshToken(JwtUtils.getToken(user.getObjectId(), getDateWithDayForward(5), myJwtSecret))
                .build();
    }

    private Date getDateWithDayForward(int day) {
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        calendar.add(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }
}
