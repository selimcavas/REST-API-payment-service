# REST-API-payment-service
In this mock service app there are 3 services, Landing, Middleman and Operator. Comminication between these services are done with JWT's, there are two keys that handle communication, one used between Landing - Middleman and the other Middleman - Operator.

User enters a phone number to the landing page to subscribe to a service,
tokens are created and forwarded in this order and a correct response is returned in landing pages:

Landing --> Middle --> Operator
        <--        <--
