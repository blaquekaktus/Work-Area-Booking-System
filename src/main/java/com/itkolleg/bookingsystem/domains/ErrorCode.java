package com.itkolleg.bookingsystem.domains;

public enum ErrorCode {
    METHOD_ARGUMENT_NOT_VALID("1001"),
    RESOURCE_NOT_FOUND("1002"),
    DESK_NOT_AVAILABLE("1003"),
    DATA_ACCESS_ERROR("1004"),
    GENERAL_ERROR("9999");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
