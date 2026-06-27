package com.anran.lims.system.service.impl;

import com.anran.lims.common.result.PageResponse;
import com.anran.lims.common.exception.BusinessException;
import com.anran.lims.common.exception.ErrorCode;
import com.anran.lims.system.converter.AuditLogConverter;
import com.anran.lims.system.dto.AuditLogQuery;
import com.anran.lims.system.dto.AuditRecordCommand;
import com.anran.lims.system.entity.AuditLog;
import com.anran.lims.system.mapper.AuditLogMapper;
import com.anran.lims.system.service.AuditLogService;
import com.anran.lims.system.vo.AuditLogVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogMapper auditLogMapper;

    public AuditLogServiceImpl(AuditLogMapper auditLogMapper) {
        this.auditLogMapper = auditLogMapper;
    }

    @Override
    @Transactional
    public Long record(AuditRecordCommand command) {
        if (!StringUtils.hasText(command.moduleCode()) || !StringUtils.hasText(command.actionCode())) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Audit moduleCode and actionCode are required");
        }
        AuditLog auditLog = AuditLogConverter.fromCommand(command);
        auditLogMapper.insert(auditLog);
        return auditLog.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<AuditLogVO> page(AuditLogQuery query) {
        LambdaQueryWrapper<AuditLog> wrapper = new LambdaQueryWrapper<AuditLog>()
                .eq(StringUtils.hasText(query.getModuleCode()), AuditLog::getModuleCode, query.getModuleCode())
                .eq(StringUtils.hasText(query.getActionCode()), AuditLog::getActionCode, query.getActionCode())
                .eq(StringUtils.hasText(query.getResultCode()), AuditLog::getResultCode, query.getResultCode())
                .like(StringUtils.hasText(query.getOperatorName()), AuditLog::getOperatorName, query.getOperatorName())
                .orderByDesc(AuditLog::getCreateTime)
                .orderByDesc(AuditLog::getId);

        Page<AuditLog> page = auditLogMapper.selectPage(Page.of(query.getPageNo(), query.getPageSize()), wrapper);
        List<AuditLogVO> records = page.getRecords().stream()
                .map(AuditLogConverter::toVO)
                .toList();

        return PageResponse.of(page.getCurrent(), page.getSize(), page.getTotal(), records);
    }
}
