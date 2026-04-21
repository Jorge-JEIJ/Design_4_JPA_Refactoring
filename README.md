# Design4_JPA_Refactoring
Refactorization excercise done the 15/04/2026 over the Transactional project (31/3/2026)
The goal was to abstract the service used by the Controller into an interface, so the service was autoinyected depending on the configuration .yaml
To demonstrate, we create a second service that instead of accessing the local h2 database, it accesses an external server through RestTemplate / RestClient (we accessed a copy of the original project and its local h2, but point stands) and handle it so all services send the same response with same functionality. 
