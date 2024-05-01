package net.consolejs.k8s.dashboard;

import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.consolejs.k8s.dashboard.service.user.UserOperation;
import net.consolejs.k8s.dashboard.service.user.UserService;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class AppLifecycle {
    private static final String ADMIN_USERNAME = "admin";
    private static final Logger LOGGER = Logger.getLogger(AppLifecycle.class.getName());

    @Inject
    private UserService myUserService;

    @Startup
    void onStarted() {
        LOGGER.info("Application started!");

        if (!myUserService.isUserPresent(ADMIN_USERNAME)) {
            initializeAdminUser();
            myUserService.createUser(UserOperation.newBuilder()
                                             .withApiGroups(List.of(""))
                                             .withResources(List.of(""))
                                             .withVerbs(List.of(""))
                                             .withUsername(ADMIN_USERNAME)
                                             .isForCluster(true)
                                             .build(), ADMIN_USERNAME);
        }
        System.out.println("gg");
    }

    private void initializeAdminUser() {

    }
}
