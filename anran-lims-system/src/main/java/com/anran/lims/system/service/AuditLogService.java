package com.anran.lims.system.service;

import com.anran.lims.common.result.PageResponse;
import com.anran.lims.system.dto.AuditLogQuery;
import com.anran.lims.system.dto.AuditRecordCommand;
import com.anran.lims.system.vo.AuditLogVO;

public interface AuditLogService {

    Long record(AuditRecordCommand command);

    PageResponse<AuditLogVO> page(AuditLogQuery query);
}
