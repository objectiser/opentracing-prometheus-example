# Simple Two Service Example

The example consists of two services, _service1_ and _service2_. The first service presents two REST endpoints
called _buy_ and _sell_. These REST methods will call a _hello_ REST endpoint on the second service.

To deploy the example on the appropriate cloud environment:

* [Kubernetes instructions](Kubernetes.md)

Once the services have been successfully deployed and started it is time to try out the services. Using
the _service1_ endpoint address (provided as part of the instructions for installing the example in
the cloud environment), perform some test calls to the service:

```
curl $SERVICE1/buy

and

curl $SERVICE1/sell
```

Then go to the OpenTracing dashboard to examine the traces that were generated from the service invocations. These
should show calls to _service1_ and subsequently from _service1_ to _service2_.

Next go to the Prometheus dashboard to examine the metrics reported from those service invocations.

Some example queries:

* `sum(sum_over_time(span_count[5s])) without (pod,instance)`

This query will return span count summaries.

The `without (pod,instance)` clause is used to aggregate the metrics across multiple instances of the same service (i.e. if multiple replicas are used). However if interested in the specific load on the individual instances then this clause could be removed.

The example produces metrics specific to the 'business transaction' in which the invocation was performed. However if the transaction is not of interest, then it can also be added to the 'without' clause, e.g. `without (pod,instance,transaction)`.

* `sum(sum_over_time(span_count{transaction="sell",service="service2"}[5s])) without (pod,instance)`

This query is a variation of the first, with the additional constraints that the transaction is `sell` and service is `service2`. The 'transaction' field is propagated from service1 to service2 using the OpenTracing baggage concept - so it enables the business context in which service2's operation was invoked to be understood, and used to isolate the metrics for that scenario.


