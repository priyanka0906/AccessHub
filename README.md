# AccessHub

AccessHub acts as a centralized gateway for authentication and authorization, ensuring consistent security policies across microservices.

## Features
- JWT token issuance and validation
- Role-based and fine-grained access control
- Reactive database integration with PostgreSQL (R2DBC)

## Tech Stack
- Spring Boot WebFlux
- Spring Security
- R2DBC + PostgreSQL
- JJWT (JSON Web Token library)
- Lombok

## Usage
Microservices authenticate via AccessHub to obtain JWT tokens, which are then used to access protected endpoints across the ecosystem.
