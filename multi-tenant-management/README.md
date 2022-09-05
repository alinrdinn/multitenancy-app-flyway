# Multi Tenant Management

## Overview  

This app provides a simple rest interface for dynamically adding tenants. This app use FlywayDB for database migrations.

## Running the Multi Tenant Management Service

Run via maven
```
mvn spring-boot:run
```

## Testing the Multi Tenant Management Service

please configure you email for email verification. You can change the configs in src/main/resources/application.yml.
Example for inserting tenant:

```
curl -X POST -d '{"email":"nurdin@email.com", "password":"1234"}' localhost:8088/tenants
```

## Configuration

Change default port value and other settings in src/main/resources/application.yml.
