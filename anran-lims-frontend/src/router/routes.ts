import type { RouteRecordRaw } from 'vue-router'
import DashboardView from '../views/dashboard/DashboardView.vue'
import SystemView from '../views/system/SystemView.vue'
import SampleView from '../views/sample/SampleView.vue'
import TaskView from '../views/task/TaskView.vue'
import ReportView from '../views/report/ReportView.vue'
import InstrumentView from '../views/instrument/InstrumentView.vue'

export const routes: RouteRecordRaw[] = [
  { path: '/', name: 'dashboard', component: DashboardView, meta: { title: 'Dashboard' } },
  { path: '/system', name: 'system', component: SystemView, meta: { title: 'System' } },
  { path: '/sample', name: 'sample', component: SampleView, meta: { title: 'Sample' } },
  { path: '/task', name: 'task', component: TaskView, meta: { title: 'Task' } },
  { path: '/report', name: 'report', component: ReportView, meta: { title: 'Report' } },
  { path: '/instrument', name: 'instrument', component: InstrumentView, meta: { title: 'Instrument' } },
]
