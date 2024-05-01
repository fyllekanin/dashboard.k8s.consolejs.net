# Setup service account for the application
The application will need a service account to interact
and control the cluster.

## The service account
Create a new service account which we will use as the authorization to
the cluster.

**Note:** If you update the namespace, keep that in mind for the other resources

```yaml
apiVersion: v1
kind: ServiceAccount
metadata:
  name: dashboard-admin-sa
  namespace: consolejs-dashboard
```

## Cluster role binding
Kubernetes comes with the **cluster-admin** role out of the box, so we only need to create a binding from that role to our newly created service account.

```yaml
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  name: dashboard-admin-sa-cluster-binding
subjects:
- kind: ServiceAccount
  name: dashboard-admin-sa
  namespace: consolejs-dashboard
roleRef:
  kind: ClusterRole
  name: cluster-admin
  apiGroup: rbac.authorization.k8s.io
```

## Token secret
To get the token for the service account we need to create a secret which
will be populated with the token

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: dashboard-admin-secret
  namespace: consolejs-dashboard
  annotations:
    kubernetes.io/service-account.name: dashboard-admin-sa
type: kubernetes.io/service-account-token
```

## Extract the token
We now need to extract the token from the secret and save it, it will be used
as environment variable to the application. So it can be stored in example github actions secrets.

```bash
kubectl --namespace=consolejs-dashboard get secret/dashboard-admin-secret -o=jsonpath='{.data.token}' | base64 --decode
```

## Delete the token secret
As we have extraced the token we no longer need the secret and it can be deleted. But it can also be kept as long as you are careful of permissions to other users don't get access to it.

```bash
kubectl delete secret dashboard-admin-secret -n consolejs-dashboard
```