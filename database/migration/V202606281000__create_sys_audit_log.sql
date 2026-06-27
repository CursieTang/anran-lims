CREATE TABLE sys_audit_log (
    id BIGINT NOT NULL AUTO_INCREMENT,
    module_code VARCHAR(64) NOT NULL COMMENT 'Business module code',
    action_code VARCHAR(64) NOT NULL COMMENT 'Business action code',
    target_type VARCHAR(64) NULL COMMENT 'Target object type',
    target_id VARCHAR(128) NULL COMMENT 'Target object id',
    operator_id BIGINT NULL COMMENT 'Operator user id',
    operator_name VARCHAR(128) NULL COMMENT 'Operator display name',
    client_ip VARCHAR(64) NULL COMMENT 'Client IP address',
    request_uri VARCHAR(512) NULL COMMENT 'Request URI',
    request_method VARCHAR(16) NULL COMMENT 'HTTP method',
    result_code VARCHAR(32) NOT NULL DEFAULT 'SUCCESS' COMMENT 'Operation result code',
    detail TEXT NULL COMMENT 'Audit detail snapshot',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_sys_audit_log_module_time (module_code, create_time),
    KEY idx_sys_audit_log_operator_time (operator_id, create_time),
    KEY idx_sys_audit_log_target (target_type, target_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='System audit log';
