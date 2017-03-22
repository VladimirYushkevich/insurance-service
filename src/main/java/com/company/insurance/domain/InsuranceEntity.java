package com.company.insurance.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Base class for persistence entities.
 */

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
public abstract class InsuranceEntity implements Serializable {

    private static final long serialVersionUID = -7714889639444246722L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @LastModifiedDate
    @Version
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
}
