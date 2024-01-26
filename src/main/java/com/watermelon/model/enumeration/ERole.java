package com.watermelon.model.enumeration;

import java.util.Arrays;
import java.util.List;

public enum ERole {
    USER(Arrays.asList(EPermission.USER_READ, EPermission.USER_UPDATE)),
    ADMIN(Arrays.asList(
            EPermission.USER_READ,
            EPermission.USER_UPDATE,
            EPermission.ADMIN_USER_MANAGE,
            EPermission.ADMIN_PRODUCT_MANAGE,
            EPermission.ADMIN_ORDER_MANAGE,
            EPermission.ADMIN_SYSTEM_REPORT)),
    ANONYMOUS(Arrays.asList(EPermission.ANONYMOUS_READ));

    private final List<EPermission> permissions;

    ERole(List<EPermission> permissions) {
        this.permissions = permissions;
    }

    public List<EPermission> getPermissions() {
        return permissions;
    }
}
