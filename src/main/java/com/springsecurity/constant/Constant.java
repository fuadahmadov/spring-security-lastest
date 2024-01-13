package com.springsecurity.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constant {
    public static final String I18N = "i18n/messages";
    public static final String AUTH_HEADER = "Authorization";
    public static final String BEARER_AUTH_HEADER = "Bearer";
    public static final String TOKEN_TYPE = "type";
    public static final String USER_ID = "id";
    public static final String USER_EMAIL = "email";
    public static final String USER_ROLES = "roles";
}
