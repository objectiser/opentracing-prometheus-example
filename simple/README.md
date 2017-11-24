# Simple Two Service Example

The example consists of two services, _ordermgr_ and _accountmgr_. The first service presents two REST endpoints
called _buy_ and _sell_. These REST methods will call a _hello_ REST endpoint on the second service.

To deploy the example on the appropriate cloud environment:

* [Kubernetes instructions](Kubernetes.md)

* [OpenShift instructions](OpenShift.md)

* [Istio/Kubernetes instructions](Istio-Kubernetes.md)

Once the services have been successfully deployed and started it is time to try out the services. Using
the _ordermgr_ endpoint address (provided as part of the instructions for installing the example in
the cloud environment), perform some test calls to the service:

```
curl $ORDERMGR/buy
curl $ORDERMGR/sell
```

Then go to the OpenTracing dashboard to examine the traces that were generated from the service invocations. These
should show calls to _ordermgr_ and subsequently from _ordermgr_ to _accountmgr_.

Note that _accountmgr_ will randomly flag an error, although the following call will explicitly create an
error due to using an invalid URL within _ordermgr_ when calling _accountmgr_:

```
curl $ORDERMGR/fail
```

Rather than manually invoke the endpoints above, the following script will loop randomly performing
these calls:

```
./genorders.sh
```

Next go to the Prometheus dashboard to examine the metrics reported from those service invocations.

Some example queries:

* `sum(increase(span_count[1m])) without (pod,instance,job,namespace,pod_template_hash,transaction,error)`

This query will return span count distribution ignoring the fields listed in the brackets listed after
the `without` keyword. This means that the individual metrics that would otherwise be individually displayed
for those fields will now be grouped/aggregated into a single metric.


* `avg(rate(span_sum[1m])) without (pod,instance,job,namespace,pod_template_hash,transaction,error)`

This query shows the average duration over time.


* `sum(increase(span_count{error="true",span_kind="server"}[1m])) without (pod,instance,job,namespace,pod_template_hash,transaction,error,operation,span_kind) / sum(increase(span_count{span_kind="server"}[1m])) without (pod,instance,job,namespace,pod_template_hash,transaction,error,operation,span_kind)`

This query shows the ratio between successful and erronous server spans (i.e. the spans representing the
invocations received by a service). It can be an indication of when a service is generating too many
errors.


* `sum(increase(span_count{transaction="sell",service="accountmgr"}[1m])) without (pod,instance,job,namespace,pod_template_hash,transaction,error)`

This query is a variation of the first, with the additional constraints that the transaction is `sell` and service is `accountmgr`. The 'transaction' field is propagated from _ordermgr_ to _accountmgr_ using the OpenTracing baggage concept - so it enables the business context in which _accountmgr_'s operation was invoked to be understood, and used to isolate the metrics for that scenario.

 
