package com.anran.lims.system.vo;

import java.time.LocalDateTime;

public record AuditLogVO(
        Long id,
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
        String detail,
        LocalDateTime createTime
) {
}
