# Installation

All documentation is assuming we will use the **consolejs-dashboard** namespace.
A new/existing namespace can be used instead, just make sure to update the
namespace in all resources named.

## Resources
This project expects to be a front to the user management on the cluster
and therefor expects to have cluster admin permissions

### Create namespace
```bash
kubectl create namespace consolejs-dashboard
```

### Setup The role, account and token
Follow below documentation to setup a service account which will
have the role **cluster-admin**. This is required for the application
to have the correct permissions.

Alternative create a new role with the roles you want the application to have.

This might limited what you will be able to do within the dashboard.

[documentation](SERVICE_ACCOUNT.md)

### Optional: Install mongo database on the cluster
If you don't already have a database setup either on the cluster or somewhere else, then follow below to install one along side on the cluster for which the application will store settings/user information etc.

[documentation](MONGODB.md)

-----

### 