package com.ali.multitenant.management.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tenant {

    @Id
    @Column(name = "tenant_id")
    private String tenantId;

    // @Column(name = "organization_name")
    // private String organizationName;

    @Column(name = "schema")
    private String schema;

}