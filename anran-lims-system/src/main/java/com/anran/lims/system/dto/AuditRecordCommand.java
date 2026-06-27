package com.anran.lims.system.dto;

public record AuditRecordCommand(
        String moduleCode,
        String actionCode,
        String targetType,
        String targetId,
        Long operatorId,
        String operatorName,
        String clientIp,
        String requestUri,
        String requestMethod,
        String resultCode,
        String detail
) {
}
