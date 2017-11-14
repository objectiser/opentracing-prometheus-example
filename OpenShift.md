# Setup on OpenShift

This example will use [minishift](https://docs.openshift.org/latest/minishift/getting-started/index.html).
First step is to start this environment:

```
minishift start
```

When fully started, then launch the dashboard using the link displayed following `The server is accessible via web console at:
`. Log in using the credentials `developer/developer` and navigate to the `New Project` overview page.

NOTE: After using `oc create -f ...` to deploy something to OpenShift, use the console to check that it
is fully running before moving onto the next step.

## Prometheus
Add configuration to locate services to be monitored based on annotations: prometheus.io/scrape: "true".

```
oc login -u system:admin
oc adm policy add-cluster-role-to-user cluster-admin system:serviceaccount:myproject:default
oc create -f prometheus-kubernetes.yml
```

The actual service monitors will be configured with the deployed services.

The next step is to create a route to make the Prometheus UI URL accessible:

```
oc create route edge prometheus --service=prometheus
```

Go to the OpenShift console Overview, expand the entry for `prometheus` and you will see a link of the form https://prometheus-myproject.192.168.42.161.nip.io/ which can be used to open the Prometheus console.

## OpenTracing

Install an appropriate OpenTracing compliant tracing system.

### Jaeger

Install the Jaeger OpenTracing tracing system:

```
oc process -f https://raw.githubusercontent.com/jaegertracing/jaeger-openshift/master/all-in-one/jaeger-all-in-one-template.yml | oc create -f -
```

Once the pods are all started, then open the dashboard using the link located in the OpenShift console under the `jaeger` deployment and `jaeger-query` service. It will be of the form `https://jaeger-query-myproject.192.168.42.161.nip.io/`.


## Shutting down

When you have finished running the example, simply run:

```
minishift delete
```

