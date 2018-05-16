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

Add configuration to locate services to be monitored based on annotations: prometheus.io/scrape: "true".

Prometheus can be installed using helm:

```
helm init
helm install stable/prometheus
```

Follow the displayed instructions to expose a port to the console, e.g.

```
export POD_NAME=$(kubectl get pods --namespace default -l "app=prometheus,component=server" -o jsonpath="{.items[0].metadata.name}")
kubectl --namespace default port-forward $POD_NAME 9090
```

and then open up a browser at http://localhost:9090/graph

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

