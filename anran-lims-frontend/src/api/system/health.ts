import { request } from '../http'

export interface HealthStatus {
  status: string
  service: string
  time: string
}

export function getHealthStatus() {
  return request<HealthStatus>('/health')
}
