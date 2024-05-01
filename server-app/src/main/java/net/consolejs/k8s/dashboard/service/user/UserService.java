package net.consolejs.k8s.dashboard.service.user;

import io.fabric8.kubernetes.api.model.ServiceAccountBuilder;
import io.fabric8.kubernetes.api.model.rbac.ClusterRoleBindingBuilder;
import io.fabric8.kubernetes.api.model.rbac.ClusterRoleBuilder;
import io.fabric8.kubernetes.api.model.rbac.PolicyRule;
import io.fabric8.kubernetes.api.model.rbac.PolicyRuleBuilder;
import io.fabric8.kubernetes.api.model.rbac.RoleBindingBuilder;
import io.fabric8.kubernetes.api.model.rbac.RoleBuilder;
import io.fabric8.kubernetes.api.model.rbac.RoleRefBuilder;
import io.fabric8.kubernetes.api.model.rbac.Subject;
import io.fabric8.kubernetes.api.model.rbac.SubjectBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.consolejs.k8s.dashboard.entityview.repository.user.UserDocument;
import net.consolejs.k8s.dashboard.repository.RepositoryFactory;
import net.consolejs.k8s.dashboard.repository.repositories.user.UserRepository;
import net.consolejs.k8s.dashboard.utilities.HashUtility;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.management.openmbean.KeyAlreadyExistsException;

@ApplicationScoped
public class UserService {
    private final String myNamespace;

    @Inject
    private RepositoryFactory myRepositoryFactory;

    public UserService(@ConfigProperty(name = "NAMESPACE") String namespace) {
        myNamespace = namespace;
    }

    public boolean isUserPresent(String username) {
        return myRepositoryFactory.of(UserRepository.class)
                .isUserPresent(username);
    }

    public void createUser(UserOperation operation, String password) {
        if (myRepositoryFactory.of(UserRepository.class)
                .isUserPresent(operation.getUsername())) {
            throw new KeyAlreadyExistsException(String.format("User %s already exists", operation.getUsername()));
        }

        try (DefaultKubernetesClient client = new DefaultKubernetesClient()) {
            String roleName = createRole(client, operation);
            String serviceAccountName = createServiceAccount(client, operation);
            createRoleBinding(client, operation, roleName, serviceAccountName);
            myRepositoryFactory.of(UserRepository.class)
                    .create(UserDocument.newBuilder()
                                    .withUsername(operation.getUsername())
                                    .withPassword(HashUtility.getHashedPassword(password))
                                    .build());
        }
    }

    private void createRoleBinding(DefaultKubernetesClient client, UserOperation operation, String roleName,
                                   String serviceAccountName) {
        String namespace = operation.isForCluster() ? myNamespace : operation.getNamespace();
        Subject serviceAccountSubject = new SubjectBuilder()
                .withKind("ServiceAccount")
                .withName(serviceAccountName)
                .withNamespace(namespace)
                .build();
        if (operation.isForCluster()) {
            client.rbac()
                    .clusterRoleBindings()
                    .createOrReplace(new ClusterRoleBindingBuilder()
                                             .withNewMetadata()
                                             .withNamespace(myNamespace)
                                             .withName(myNamespace + "-cluster-binding-" + operation.getUsername())
                                             .and()
                                             .withSubjects(serviceAccountSubject)
                                             .withRoleRef(new RoleRefBuilder()
                                                                  .withKind("ClusterRole")
                                                                  .withName(roleName)
                                                                  .withApiGroup("rbac.authorization.k8s.io")
                                                                  .build())
                                             .build());
        } else {
            client.rbac()
                    .roleBindings()
                    .createOrReplace(new RoleBindingBuilder()
                                             .withNewMetadata()
                                             .withNamespace(myNamespace)
                                             .withName(myNamespace + "-binding-" + operation.getUsername())
                                             .and()
                                             .withSubjects(serviceAccountSubject)
                                             .withRoleRef(new RoleRefBuilder()
                                                                  .withKind("Role")
                                                                  .withName(roleName)
                                                                  .withApiGroup("rbac.authorization.k8s.io")
                                                                  .build())
                                             .build());
        }
    }

    private String createServiceAccount(DefaultKubernetesClient client, UserOperation operation) {
        String namespace = operation.isForCluster() ? myNamespace : operation.getNamespace();
        String name = myNamespace + "-service-account-" + operation.getUsername();
        client.serviceAccounts()
                .createOrReplace(new ServiceAccountBuilder()
                                         .withNewMetadata()
                                         .withNamespace(namespace)
                                         .withName(name)
                                         .and()
                                         .build());
        return name;
    }

    private String createRole(DefaultKubernetesClient client, UserOperation operation) {
        String name = null;
        if (operation.isForCluster()) {
            name = myNamespace + "-cluster-" + operation.getUsername();
            client.rbac()
                    .clusterRoles()
                    .createOrReplace(new ClusterRoleBuilder()
                                             .withNewMetadata()
                                             .withNamespace(myNamespace)
                                             .withName(name)
                                             .and()
                                             .withRules(getPolicyRule(operation))
                                             .build());
            return name;
        } else {
            name = myNamespace + "-role-" + operation.getUsername();
            client.rbac()
                    .roles()
                    .inNamespace(operation.getNamespace())
                    .createOrReplace(new RoleBuilder()
                                             .withNewMetadata()
                                             .withNamespace(operation.getNamespace())
                                             .withName(name)
                                             .and()
                                             .withRules(getPolicyRule(operation))
                                             .build());
        }
        return name;
    }

    private PolicyRule getPolicyRule(UserOperation operation) {
        return new PolicyRuleBuilder()
                .withApiGroups(operation.getApiGroups())
                .withResources(operation.getResources())
                .withVerbs(operation.getVerbs())
                .build();
    }
}
