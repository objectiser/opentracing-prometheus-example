# Setup Example on OpenShift

The maven build can be used to automatically build docker images for the two services and deploy them into the
local docker register used by minishift.

To use this approach, it is necessary to setup some environment variables before starting the build:

```
eval $(minishift docker-env)

mvn clean install docker:build
```

Run the following command two deploy the services, and prometheus service monitors for those services:

```
oc create -f services-kubernetes.yml
```

The create the route to provide public access to the REST endpoint:

```
oc create route edge ordermgr --service=ordermgr
```

Final step is to obtain the URL for the new route from the OpenShift console, under the `ordermgr` application/deployment, of the form`https://ordermgr-myproject.192.168.42.161.nip.io/`, and use it to setup an environment variable `ORDERMGR`. NOTE: remove the final '/' when doing to the export, for example


```
export ORDERMGR=https://ordermgr-myproject.192.168.42.161.nip.io
```

