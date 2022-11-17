package genecar.root.security.vo;

import genecar.root.common.consts.EnumModel;

public enum UserRole implements EnumModel {
    SUPER("ROLE_SUPER"),
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String role_user;

    UserRole(String role_user) {
        this.role_user = role_user;
    }

    @Override
    public String getKey() {
        return name();
    }

    @Override
    public String getValue() {
        return role_user;
    }
}