# Multi Tenant Service

## Overview  

This app provides a simple rest interface for dynamically adding employee inside a tenant. This app use FlywayDB for database migrations.

## Running the Multi Tenant Service

Run via maven
```
mvn spring-boot:run
```

## Testing the Multi Tenant Service

Make sure you have already created tenant from multi-tenant-management service. Also, please configure you email for email verification. You can change the configs in src/main/resources/application.yml.
Insert employee for different tenants:

```
curl -H "X-TENANT-ID: feo454bo32oioi23" -X POST -d '{"email":"ali@email.com", "password":"1234"}' localhost:8080/employees
```
