package com.springsecurity.constant;

import lombok.experimental.FieldNameConstants;

@FieldNameConstants(onlyExplicitlyIncluded = true)
public enum RoleName {
    @FieldNameConstants.Include USER,
    @FieldNameConstants.Include MODERATOR,
    @FieldNameConstants.Include ADMIN,
    @FieldNameConstants.Include SUPER_ADMIN
}
