package com.anran.lims.system.converter;

import com.anran.lims.system.dto.AuditRecordCommand;
import com.anran.lims.system.entity.AuditLog;
import com.anran.lims.system.vo.AuditLogVO;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

public final class AuditLogConverter {

    private AuditLogConverter() {
    }

    public static AuditLog fromCommand(AuditRecordCommand command) {
        AuditLog auditLog = new AuditLog();
        auditLog.setModuleCode(command.moduleCode());
        auditLog.setActionCode(command.actionCode());
        auditLog.setTargetType(command.targetType());
        auditLog.setTargetId(command.targetId());
        auditLog.setOperatorId(command.operatorId());
        auditLog.setOperatorName(command.operatorName());
        auditLog.setClientIp(command.clientIp());
        auditLog.setRequestUri(command.requestUri());
        auditLog.setRequestMethod(command.requestMethod());
        auditLog.setResultCode(StringUtils.hasText(command.resultCode()) ? command.resultCode() : "SUCCESS");
        auditLog.setDetail(command.detail());
        auditLog.setCreateTime(LocalDateTime.now());
        return auditLog;
    }

    public static AuditLogVO toVO(AuditLog auditLog) {
        return new AuditLogVO(
                auditLog.getId(),
                auditLog.getModuleCode(),
                auditLog.getActionCode(),
                auditLog.getTargetType(),
                auditLog.getTargetId(),
                auditLog.getOperatorId(),
                auditLog.getOperatorName(),
                auditLog.getClientIp(),
                auditLog.getRequestUri(),
                auditLog.getRequestMethod(),
                auditLog.getResultCode(),
                auditLog.getDetail(),
                auditLog.getCreateTime()
        );
    }
}
