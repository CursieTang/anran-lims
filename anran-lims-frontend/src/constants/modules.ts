export const limsModules = ['system', 'sample', 'task', 'report', 'instrument'] as const

export type LimsModule = (typeof limsModules)[number]
