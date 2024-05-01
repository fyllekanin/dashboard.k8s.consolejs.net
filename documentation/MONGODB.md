# Install mongo databsae
This documentation will go though installing and setting up
a mongo database on the cluster in **consolejs-dashboard** namespace.

## Install longhorn
For persistance you can either follow this documentation and install
longhorn for storage or you can install any alternative and update the mongo-db manifests.

```bash
kubectl apply -f https://raw.githubusercontent.com/longhorn/longhorn/v1.6.1/deploy/longhorn.yaml
```

## Setup secret with username & password
Replace **{username}** & **{password}** in below configuration
```yaml
apiVersion: v1
stringData:
  username: {username}
  password: {password}
  connection: mongodb://{username}@{password}:dashboard-mongodb/admin
kind: Secret
metadata:
  creationTimestamp: null
  name: dashboard-mongodb-credentials
  namespace: consolejs-dashboard
```

## Setup persistent volumne
```yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: dashboard-mongodb-data
  namespace: consolejs-dashboard
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 2Gi
  storageClassName: "longhorn"
```

## Setup service
```yaml
apiVersion: v1
kind: Service
metadata:
  labels:
    app.kubernetes.io/name: dashboard-mongodb
  name: dashboard-mongodb
  namespace: consolejs-dashboard
spec:
  ports:
    - port: 27017
      targetPort: 27017
  selector:
    app.kubernetes.io/name: dashboard-mongodb
```

## Setup the deployment
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  labels:
    app.kubernetes.io/name: dashboard-mongodb
  name: dashboard-mongodb
  namespace: consolejs-dashboard
spec:
  replicas: 1
  selector:
    matchLabels:
      app.kubernetes.io/name: dashboard-mongodb
  strategy: { }
  template:
    metadata:
      labels:
        app.kubernetes.io/name: dashboard-mongodb
    spec:
      containers:
        - image: mongo
          name: dashboard-mongodb
          args: [ "--dbpath","/data/db" ]
          livenessProbe: null
          readinessProbe: null
          env:
            - name: MONGO_INITDB_ROOT_USERNAME
              valueFrom:
                secretKeyRef:
                  name: dashboard-mongodb-credentials
                  key: username
            - name: MONGO_INITDB_ROOT_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: dashboard-mongodb-credentials
                  key: password
          volumeMounts:
            - name: "dashboard-mongodb-data-dir"
              mountPath: "/data/db"
      volumes:
        - name: "dashboard-mongodb-data-dir"
          persistentVolumeClaim:
            claimName: "dashboard-mongodb-data"
```