# REST-API-payment-service
In this mock service app there are 3 services, Landing, Middleman and Operator. Communication between these services are done with JWT's, there are two keys that handle communication, one used between Landing - Middleman and the other Middleman - Operator.
Landing server uses nodejs and the others use tomcat, also a PostgreSQL database is used to store user data for queries. 

User enters a phone number to the landing page to subscribe to a service,
tokens are created and forwarded in this order and a correct response is returned in landing page:

Landing --> Middleman --> Operator --> Middleman --> Landing
