<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { fetchAuditLogs, type AuditLog } from '../../api/system/auditLogs'

const loading = ref(false)
const errorMessage = ref('')
const auditLogs = ref<AuditLog[]>([])
const total = ref(0)

async function loadAuditLogs() {
  loading.value = true
  errorMessage.value = ''
  try {
    const page = await fetchAuditLogs({ pageNo: 1, pageSize: 10 })
    auditLogs.value = page.records
    total.value = page.total
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Failed to load audit logs'
  } finally {
    loading.value = false
  }
}

onMounted(loadAuditLogs)
</script>

<template>
  <section class="page">
    <header class="page-header">
      <div>
        <p class="eyebrow">P0 Compliance</p>
        <h2>System Governance</h2>
        <p>Users, roles, permissions, audit logs, and security settings.</p>
      </div>
      <button class="ghost-button" type="button" :disabled="loading" @click="loadAuditLogs">
        {{ loading ? 'Loading...' : 'Refresh' }}
      </button>
    </header>

    <div class="metric-grid">
      <article class="metric-card">
        <span>Audit Entries</span>
        <strong>{{ total }}</strong>
        <small>Append-only operation records</small>
      </article>
      <article class="metric-card">
        <span>Priority</span>
        <strong>P0</strong>
        <small>Audit and traceability first</small>
      </article>
      <article class="metric-card">
        <span>Boundary</span>
        <strong>System</strong>
        <small>RBAC, security, audit</small>
      </article>
    </div>

    <section class="panel">
      <div class="panel-title">
        <h3>Recent Audit Logs</h3>
        <p>Read-only view backed by <code>sys_audit_log</code>.</p>
      </div>

      <p v-if="errorMessage" class="error-text">{{ errorMessage }}</p>

      <div class="table-wrap">
        <table>
          <thead>
            <tr>
              <th>Time</th>
              <th>Module</th>
              <th>Action</th>
              <th>Operator</th>
              <th>Target</th>
              <th>Result</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="!loading && auditLogs.length === 0">
              <td colspan="6">No audit logs yet. Apply the migration and record business operations first.</td>
            </tr>
            <tr v-for="log in auditLogs" :key="log.id">
              <td>{{ log.createTime }}</td>
              <td>{{ log.moduleCode }}</td>
              <td>{{ log.actionCode }}</td>
              <td>{{ log.operatorName || '-' }}</td>
              <td>{{ log.targetType || '-' }} / {{ log.targetId || '-' }}</td>
              <td>{{ log.resultCode }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>
  </section>
</template>
