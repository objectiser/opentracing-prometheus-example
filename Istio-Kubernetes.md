# Setup on Istio running on Kubernetes

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

## Istio

Follow the instructions for installing [Istio](https://istio.io/docs/setup/kubernetes/quick-start.html) in minikube.

NOTE: Use of Istio 0.7.1 or higher is required to avoid prometheus scrapes being recorded as trace instances in Jaeger.

### Prometheus

Add configuration to locate services to be monitored based on annotations: prometheus.io/scrape: "true".

Install Prometheus using the 'add-on' template provided in the Istio distribution:

```
kubectl apply -f install/kubernetes/addons/prometheus.yaml
```
NOTE: From version 0.8.0, the 'add-ons' will no longer be provided directly, and instead will be
installed via helm.

#### Avoid trace instances being created for Prometheus metric scrapes

By default, Prometheus scrape requests to retrieve metrics from annotated services will result in Istio recording a trace instance. To avoid this, before deploying Prometheus, you should first
deploy the following NGINX proxy which will add an extra header to the scrape request to
disable tracing.

```
kubectl create -f prometheus-nginx.yml -n istio-system
``` 

To ensure that Prometheus requests are proxied through this NGINX gateway, we need to update
the `prometheus.yaml` to include the following `proxy_url` property to two of the scrape jobs:

```
    # scrape config for service endpoints.
    - job_name: 'kubernetes-service-endpoints'
      proxy_url: 'http://prometheus-nginx:9191'

    ...

    # Example scrape config for pods
    - job_name: 'kubernetes-pods'
      proxy_url: 'http://prometheus-nginx:9191'

    ...
```

## OpenTracing

Install an appropriate OpenTracing compliant tracing system.

### Jaeger

Install the Jaeger OpenTracing tracing system:

```
kubectl apply -n istio-system -f https://raw.githubusercontent.com/jaegertracing/jaeger-kubernetes/master/all-in-one/jaeger-all-in-one-template.yml

```

Once the pods are all started, then open the dashboard using the link returned from:

```
minikube service jaeger-query --url -n istio-system
```

## Shutting down

When you have finished running the example, simply run:

```
minikube stop
minikube delete
```

