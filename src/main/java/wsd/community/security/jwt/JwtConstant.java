package wsd.community.security.jwt;

public class JwtConstant {

    public static final String REDIS_RT_PREFIX = "RT:";
    public static final String REDIS_BL_PREFIX = "BL:";
    public static final String CLAIM_EMAIL = "email";
    public static final String CLAIM_ROLE = "role";
    public static final String CLAIM_TYPE = "type";
    public static final String ACCESS_TOKEN_TYPE = "access";
    public static final String REFRESH_TOKEN_TYPE = "refresh";
    public static final String LOGOUT_VALUE = "logout";
    public static final String TOKEN_PREFIX = "Bearer ";

    private JwtConstant() {
    }
}
