package com.anran.lims.system.controller;

import com.anran.lims.common.result.ApiResponse;
import com.anran.lims.common.result.PageResponse;
import com.anran.lims.system.dto.AuditLogQuery;
import com.anran.lims.system.service.AuditLogService;
import com.anran.lims.system.vo.AuditLogVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system/audit-logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    public ApiResponse<PageResponse<AuditLogVO>> page(@Valid AuditLogQuery query) {
        return ApiResponse.ok(auditLogService.page(query));
    }
}
