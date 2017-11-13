# Setup on Kubernetes

This example will use [minikube](https://kubernetes.io/docs/getting-started-guides/minikube/).
First step is to start this environment:

```
minikube start
```

When fully started, then launch the dashboard:

```
minikube dashboard
```

NOTE: You may get an error _Could not find finalized endpoint being pointed to by kubernetes-dashboard: Error validating service: Error getting service kubernetes-dashboard: services "kubernetes-dashboard" not found_. If so,
just wait a while and try again.

NOTE: After using `kubectl create -f ...` to deploy something to Kubernetes, use the console to check that it
is fully running before moving onto the next step.

## Prometheus

To obtain metrics from the deployed services, we will use the
[coreos prometheus operator](https://coreos.com/operators/prometheus/docs/latest/user-guides/getting-started.html)
project. To install, use the following command:

```
kubectl create -f https://raw.githubusercontent.com/coreos/prometheus-operator/v0.11.0/bundle.yaml
```

Add configuration to locate service monitors based on label "scrape: true":

```
kubectl create -f prometheus-kubernetes.yml
```

The actual service monitors will be configured with the deployed services.

TODO: Currently there is one service monitor per service - using the label associated with the app. However
it may be good to try having a single service monitor with a more generic label, which the services can
be associated with.

Open the Prometheus dashboard using the link returned from:

```
minikube service prometheus --url
```

## OpenTracing

Install an appropriate OpenTracing compliant tracing system.

### Jaeger

Install the Jaeger OpenTracing tracing system:

```
kubectl create -f https://raw.githubusercontent.com/jaegertracing/jaeger-kubernetes/master/all-in-one/jaeger-all-in-one-template.yml
```

Once the pods are all started, then open the dashboard using the link returned from:

```
minikube service jaeger-query --url
```

## Shutting down

When you have finished running the example, simply run:

```
minikube stop
minikube delete
```

