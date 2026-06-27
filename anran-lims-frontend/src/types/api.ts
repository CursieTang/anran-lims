export interface ApiResponse<T> {
  success: boolean
  code: string
  message: string
  data: T
}

export interface PageResponse<T> {
  pageNo: number
  pageSize: number
  total: number
  records: T[]
}
