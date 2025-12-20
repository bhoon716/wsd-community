package wsd.community.domain.user.entity;

public enum UserRole {

    OWNER,
    ADMIN,
    USER
    ;

    public String getKey() {
        return "ROLE_" + name();
    }
}
