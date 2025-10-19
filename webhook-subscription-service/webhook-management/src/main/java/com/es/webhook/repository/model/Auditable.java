package com.es.webhook.repository.model;

import java.time.Instant;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public abstract class Auditable {

  @CreatedBy
  @Field("created_by")
  private String createdBy;

  @CreatedDate
  @Field("created_at")
  private Instant createdAt;

  @LastModifiedBy
  @Field("updated_by")
  private String updatedBy;

  @LastModifiedDate
  @Field("updated_at")
  private Instant updatedAt;
}
