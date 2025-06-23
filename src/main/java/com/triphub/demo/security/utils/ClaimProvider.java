package com.triphub.demo.security.utils;


import com.triphub.demo.api.Admin.model.Admin;

import java.util.HashMap;
import java.util.Map;

public class ClaimProvider {

    private ClaimProvider() {
        throw new IllegalStateException("Utility class");
    }

    public static Map<String, Object> generateClaims(Admin admin) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", admin.getId());
        claims.put("email", admin.getEmail());
        return claims;
    }
}
