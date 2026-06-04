package com.anran.lims.common.result;

public record ApiResponse<T>(boolean success, String code, String message, T data) {

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, "OK", "OK", data);
    }

    public static <T> ApiResponse<T> fail(String message) {
        return fail("ERROR", message);
    }

    public static <T> ApiResponse<T> fail(String code, String message) {
        return new ApiResponse<>(false, code, message, null);
    }
}
