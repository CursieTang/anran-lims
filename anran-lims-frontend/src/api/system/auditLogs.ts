import { request } from '../http'
import type { PageResponse } from '../../types/api'

export interface AuditLog {
  id: number
  moduleCode: string
  actionCode: string
  targetType?: string
  targetId?: string
  operatorId?: number
  operatorName?: string
  clientIp?: string
  requestUri?: string
  requestMethod?: string
  resultCode: string
  detail?: string
  createTime: string
}

export interface AuditLogQuery {
  pageNo?: number
  pageSize?: number
  moduleCode?: string
  actionCode?: string
  operatorName?: string
  resultCode?: string
}

export function fetchAuditLogs(query: AuditLogQuery = {}) {
  const params = new URLSearchParams()
  Object.entries(query).forEach(([key, value]) => {
    if (value !== undefined && value !== '') {
      params.set(key, String(value))
    }
  })

  const queryString = params.toString()
  return request<PageResponse<AuditLog>>(`/system/audit-logs${queryString ? `?${queryString}` : ''}`)
}
