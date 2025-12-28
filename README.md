# fetch-secrets (Spring Boot + AWS Secrets Manager)

This is a small Spring Boot project that demonstrates loading configuration from **AWS Secrets Manager** using **Spring Cloud AWS** (`spring.config.import=aws-secretsmanager:...`) and binding it into typed config (`@ConfigurationProperties`).

## Prerequisites

- Java 17+
- Maven (or use `mvnw` / `mvnw.cmd`)
- AWS credentials available via the default AWS SDK credential chain (env vars, AWS profile, or IAM role)

## How it works

- `application.properties` imports a secret from Secrets Manager as a property source.
- `JwtProperties` binds `jwt.private-key` and `jwt.public-key`.
- `JwtService` can print the keys **exactly as loaded** when `poc.print-secrets=true` (POC-only).

## Secret layout (recommended)

Store a **JSON** secret in AWS Secrets Manager (name/ARN referenced in `spring.config.import`), for example:

```json
{
  "jwt": {
    "private-key": "-----BEGIN PRIVATE KEY-----\\n...\\n-----END PRIVATE KEY-----",
    "public-key": "-----BEGIN PUBLIC KEY-----\\n...\\n-----END PUBLIC KEY-----"
  }
}
```

Spring Cloud AWS maps JSON keys to properties, so the above becomes:

- `jwt.private-key`
- `jwt.public-key`

## Configuration

In `src/main/resources/application.properties`:

- `spring.config.import=optional:aws-secretsmanager:dev/config-keys`
  - `optional:` allows local startup even if AWS isn’t configured.
  - In real environments you typically remove `optional:` (fail fast) and point to the correct secret name/ARN.

## Run

```bash
./mvnw spring-boot:run
```

### Local run (without AWS)

Use the `local` profile to load placeholder values from `application-local.properties`:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

### POC: print keys as loaded (for manual verification)

This prints the key values **exactly as loaded** on startup. It is **disabled by default**.

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--poc.print-secrets=true"
```

## Production notes (standards)

- **Never log secrets** unless you’re doing a temporary POC verification.
- **Prefer IAM roles** over static access keys.
- **Fail fast in prod**: set `spring.config.import=aws-secretsmanager:<secret>` (without `optional:`) in your deployment config.


