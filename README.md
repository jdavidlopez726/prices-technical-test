# Prices Service

REST API that returns the price that applies to a product of a brand at a given date and time.

A product can have several price lists whose date ranges overlap. When more than one is in force at the requested date, **the one with the highest priority wins**.

```http
GET /prices?date=2020-06-14T16:00:00&productId=35455&brandId=1
Authorization: Bearer <token>
```
```json
{
  "brandId": 1,
  "productId": 35455,
  "priceList": 2,
  "startDate": "2020-06-14T15:00:00",
  "endDate": "2020-06-14T18:30:00",
  "price": { "amount": 25.45, "currency": "EUR" }
}
```

## Contents

- [Tech stack](#tech-stack)
- [Getting started](#getting-started)
- [Authentication](#authentication)
- [Architecture](#architecture)
- [Database](#database)
- [Testing](#testing)
- [Development workflow](#development-workflow)

## Tech stack

| Area | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.1 (Spring MVC, Spring Data JPA) |
| Security | Spring Security 7.1, OAuth2 resource server, JWT signed with RS256 |
| Persistence | H2 (in memory), Hibernate 7.4, Liquibase 5 |
| Mapping | MapStruct 1.6, Lombok |
| API docs | springdoc-openapi (Swagger UI) |
| Testing | JUnit 5, Mockito, AssertJ, Spring Security Test, ArchUnit 1.5 |

## Getting started

**Requirements:** JDK 21. Maven is not needed: the project includes the Maven wrapper.

```bash
./mvnw spring-boot:run
```

On Windows use `mvnw.cmd` instead of `./mvnw`. The API starts at `http://localhost:8080`, with the database created and loaded on startup.

Build and run all the tests:

```bash
./mvnw clean verify
```

Swagger UI: http://localhost:8080/swagger-ui/index.html

## Authentication

Every endpoint except the login requires a JWT access token, sent as `Authorization: Bearer <token>`.

### Demo users

| Username | Password | Role | `GET /prices` |
|---|---|---|---|
| `admin` | `admin` | `ADMIN` | ✅ Allowed |
| `user` | `user` | `USER` | ✅ Allowed |
| `guest` | `guest` | `GUEST` | ❌ 403 Forbidden |

Roles are hierarchical, **`ADMIN > USER > GUEST`**: each role can do everything the roles below it can. `GET /prices` requires `USER`, so `admin` and `user` can query prices and `guest` cannot.

These credentials are for local demo purposes only. Passwords are stored as BCrypt hashes.

### Getting and using a token

1. Log in to get a token, valid for 1 hour:

   ```bash
   curl -X POST http://localhost:8080/auth/login \
        -H "Content-Type: application/json" \
        -d '{"username": "user", "password": "user"}'
   ```
   ```json
   { "accessToken": "eyJraWQiOi...", "tokenType": "Bearer", "expiresIn": 3600 }
   ```

2. Send the token on every request:

   ```bash
   curl "http://localhost:8080/prices?date=2020-06-14T16:00:00&productId=35455&brandId=1" \
        -H "Authorization: Bearer <accessToken>"
   ```

Both steps in one go, from a Bash shell:

```bash
TOKEN=$(curl -s -X POST http://localhost:8080/auth/login -H "Content-Type: application/json" \
        -d '{"username": "user", "password": "user"}' | sed -E 's/.*"accessToken":"([^"]+)".*/\1/')
curl "http://localhost:8080/prices?date=2020-06-14T16:00:00&productId=35455&brandId=1" \
     -H "Authorization: Bearer $TOKEN"
```

### From Swagger UI

1. Open [Swagger UI](http://localhost:8080/swagger-ui/index.html). The documentation is public; only calling the endpoints requires a token.
2. In **Authentication → POST /auth/login**, click **Try it out** and **Execute**. The request body is already filled in with the `user` credentials.
3. Copy the `accessToken` from the response.
4. Click **Authorize** (top right), paste the token and confirm.
5. Call **Prices → GET /prices**. Swagger sends the token automatically.

## Architecture

The project follows the **onion architecture**: the business core is in the centre, and every dependency points inwards.

```mermaid
flowchart LR
    infrastructure["Infrastructure (adapters)<br/>rest · security · persistence · config"]
    application["Application<br/>use cases · roles"]
    domain["Domain<br/>model · business rules · ports"]
    infrastructure --> application --> domain
    infrastructure --> domain
```

`domain` and `application` form the core: plain Java, with no framework.

| Package | Ring | Contents |
|---|---|---|
| `domain.model` | Domain model | `Price`, value objects `Money`, `BrandId`, `ProductId` |
| `domain.service` | Domain services | `ApplicablePriceSelector`: the business rule |
| `domain.repository` | Domain services | `PriceRepositoryPort`: what the domain needs from storage |
| `application` | Application services | `PriceServiceImpl` (the use case) and the `Roles` constants |
| `infrastructure.rest` | Adapter | Controller, DTOs, error handling |
| `infrastructure.persistence` | Adapter | JPA entities, repositories, `PriceRepositoryAdapter`, `UserDetailsServiceAdapter` |
| `infrastructure.security` | Adapter | Security filter chain, login, JWT issuing and validation |
| `infrastructure.config` | Adapter | Bean wiring for the core, JPA auditing, OpenAPI |

The rules are enforced by [`ArchitectureTest`](src/test/java/com/technical/test/prices/ArchitectureTest.java) with ArchUnit, so the build fails if they are broken:

- Each ring may only depend on the rings inside it.
- Adapters may not depend on each other.
- `domain` and `application` may not depend on Spring or Jakarta.

## Database

H2 in memory. The schema and the demo data are created by Liquibase on every startup, so the data resets on each restart.

```
db/changelog/
├── db.changelog-master.xml
├── v1.0/  prices, brands and products tables, and the sample prices
└── v2.0/  users and roles tables, and the demo users
```

Sample prices, for brand `1` (ZARA) and product `35455`:

| Price list | From | To | Priority | Price |
|---|---|---|---|---|
| 1 | 2020-06-14 00:00 | 2020-12-31 23:59 | 0 | 35.50 EUR |
| 2 | 2020-06-14 15:00 | 2020-06-14 18:30 | 1 | 25.45 EUR |
| 3 | 2020-06-15 00:00 | 2020-06-15 11:00 | 1 | 30.50 EUR |
| 4 | 2020-06-15 16:00 | 2020-12-31 23:59 | 1 | 38.95 EUR |

The H2 web console is disabled.

## Testing

```bash
./mvnw clean verify
```

| Type | What it covers | Classes |
|---|---|---|
| Domain unit tests | Price date range, priority selection, `Money` validation | `PriceTest`, `ApplicablePriceSelectorTest`, `MoneyTest` |
| Application unit tests | Use case orchestration, with the repository mocked | `PriceServiceImplTest` |
| Security unit tests | Login flow, JWT configuration validation | `LoginServiceTest`, `JwtPropertiesTest` |
| Integration tests | Full application, from HTTP to the database, with security enabled | `PriceControllerIntegrationTest`, `AuthControllerIntegrationTest` |
| Architecture tests | Onion dependency rules | `ArchitectureTest` |

The integration tests cover the five scenarios of the exercise, for product `35455` and brand `1`:

| # | Request date | Expected price list | Expected price |
|---|---|---|---|
| 1 | 2020-06-14 10:00 | 1 | 35.50 EUR |
| 2 | 2020-06-14 16:00 | 2 | 25.45 EUR |
| 3 | 2020-06-14 21:00 | 1 | 35.50 EUR |
| 4 | 2020-06-15 10:00 | 3 | 30.50 EUR |
| 5 | 2020-06-16 21:00 | 4 | 38.95 EUR |

They also cover the security flows end to end: logging in with each demo user, calling the API with the real token, and tampered, malformed or missing tokens.

## Development workflow

- **Branches:** Git Flow. `main` holds released versions, tagged as `vX.Y.Z`. `develop` collects finished work, and changes are made in `feature/*` branches merged through pull requests.
- **Commits:** [Conventional Commits](https://www.conventionalcommits.org/) (`feat:`, `fix:`, `refactor:`, `docs:`...). A `!` marks a breaking change.
- **Versioning:** [Semantic Versioning](https://semver.org/). The next release is **2.0.0**, a major version, because it breaks the 1.x API: the price is now returned as a nested `price` object, and every request requires an access token.
