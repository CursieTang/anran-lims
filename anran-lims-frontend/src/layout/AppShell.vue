<template>
  <main class="app-shell">
    <aside class="sidebar">
      <h1>Anran LIMS</h1>
      <nav>
        <RouterLink v-for="item in navigation" :key="item.path" :to="item.path">
          {{ item.title }}
        </RouterLink>
      </nav>
    </aside>
    <section class="workspace">
      <RouterView />
    </section>
  </main>
</template>

<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'

const navigation = [
  { path: '/', title: 'Dashboard' },
  { path: '/system', title: 'System' },
  { path: '/sample', title: 'Sample' },
  { path: '/task', title: 'Task' },
  { path: '/report', title: 'Report' },
  { path: '/instrument', title: 'Instrument' },
]
</script>
