package wsd.community.security.config;

public class SecurityConstant {

    public static final String CLAIM_USER_ID = "userId";
    public static final String CLAIM_EMAIL = "email";
    public static final String CLAIM_ROLE = "role";
    public static final String CLAIM_TYPE = "type";
    public static final String ACCESS_TOKEN_TYPE = "access";
    public static final String REFRESH_TOKEN_TYPE = "refresh";
    public static final String LOGOUT_VALUE = "logout";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String AUTHORIZATION = "Authorization";
    public static final int COOKIE_MAX_AGE = 7 * 24 * 60 * 60; // 7 days

    private SecurityConstant() {
    }
}
