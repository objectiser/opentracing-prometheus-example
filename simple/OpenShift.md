# Setup Example on OpenShift

The maven build can be used to automatically build docker images for the two services and deploy them into the
local docker register used by minishift.

To use this approach, it is necessary to setup some environment variables before starting the build:

```
eval $(minishift docker-env)

mvn clean install docker:build
```

NOTE: To push to a remote registry you can use:
```
mvn clean install docker:build docker:push -docker.registry=docker.io/your_username
```

Run the following command to deploy the services, and prometheus service monitors for those services:

```
oc create -f services-kubernetes.yml
```

The create the route to provide public access to the REST endpoint:

```
oc create route edge ordermgr --service=ordermgr
```

Final step is to obtain the URL for the new route:


```
export ORDERMGR=https://`oc get route ordermgr -o=jsonpath="{.spec.host}"`
```
