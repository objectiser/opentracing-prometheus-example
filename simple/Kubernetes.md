# Setup Example on Kubernetes

The maven build can be used to automatically build docker images for the two services and deploy them into the
[local docker register used by minikube](https://kubernetes.io/docs/getting-started-guides/minikube/#reusing-the-docker-daemon).

To use this approach, it is necessary to setup some environment variables before starting the build:

```
eval $(minikube docker-env)

mvn clean install docker:build
```

NOTE: To push to a remote registry you can use:
```
mvn clean install docker:build docker:push -docker.registry=docker.io/your_username
```

Run the following command to deploy the services, and prometheus service monitors for those services:

```
kubectl create -f services-kubernetes.yml
```

Final step is to obtain the URL for the _ordermgr_ REST endpoint:

```
export ORDERMGR=$(minikube service ordermgr --url)
```

