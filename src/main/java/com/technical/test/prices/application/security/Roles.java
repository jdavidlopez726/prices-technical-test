package com.technical.test.prices.application.security;

/**
 * Roles a user can have. They decide who may run each use case, so they are defined in the application layer, where
 * every adapter can reference them. The names must match the ones stored in the {@code roles} table.
 * <p>
 * They are {@code String} constants, and not an enum, so they can be used in annotation attributes.
 */
public final class Roles {

    public static final String USER = "USER";
    public static final String ADMIN = "ADMIN";

    private Roles() {
    }
}
