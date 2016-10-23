Webhooks Vanhackathon
=====================
	
# Company
	HootSuite

# Project description
	Write a webhook calling service that will reliably POST data to destination URLs in the order POST message requests are received.

# Swagger Documentation
	http://localhost/api

# Technologies
* java
* camel
* jms
* activemq
* redis
* restlet

# Running
  	mvn camel:run

# Scaling
* REST - Multiples instances of REST routes sending POST requests to JMS endpoint's.
* JMS - Network of brokers and replicated levelDB storage.
* WORKER - Multiples instances connected to one or more JMS servers. 
* REDIS - Cluster of Redis nodes.
   
# Solutions
	Messages not sent within 24 hours can be deleted.
	=> Solved using JMSExpiration
	Message ordering to a destination should be preserved, even when there are pending message retries for that destination
	=> ActiveMQ queue preserve the order, failed messages will me managed inside a transaction context. ( http://activemq.apache.org/message-groups.html ) 
	Messages that failed to send should retried 3 or more times before they are deleted.
	=> Retries managed inside the JMS transaction context ( also the retry interval ).
	How can I scale out this service across multiple servers while preserving per-destination ordering ?
	=> Using activeMQ Message Group
	Is your API using the standard RESTful conventions for the 4 operations ?
	=> yes

# Considerations
	How well does your service support concurrency for multiple destinations while preserving per-destination ordering?
	=> ActiveMQ has native support for this scenario using Message Groups.
    How secure is this? 
    => Requests are susceptible to man in the middle attack ( sniffer and injection ) if traffic is not encrypted.
    Should you require HTTPS urls?
    => Definitely.
    Should the content be signed with something like an HMAC?
    => HMAC are secure standards widely used on others scenarios like JWT and can help improve security signing the requests. So yes, it would be good to implement HMAC signature.
    Should any url be allowed (e.g. one that has or resolves to a private IP address?)
    =>	No, private urls can be used to attack/explore the network where this service is hosted.


The service should support the following remote requests via REST

    register a new destination (URL) returning its id
    list registered destinations [{id, URL},...]
    delete a destination by id
    POST a message to this destination (id, msg-body, content-type): this causes the server to POST the given msg-body to the URL associated with that id.

Behaviour:

    If the destination URL is not responding (e.g. the servier is down) or returns a non-200 response, your service should resend the message at a later time
    





