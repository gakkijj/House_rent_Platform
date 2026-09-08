<script setup>
import { RouterView, useRoute, useRouter } from 'vue-router'
import { session } from '../stores/session'
import { housingApi } from '../api/housing'
const route = useRoute()
const router = useRouter()
const menus = [
  { to: '/admin', label: '数据概览', icon: '▦', exact: true },
  { to: '/admin/houses', label: '房源管理', icon: '⌂' },
  { to: '/admin/orders', label: '预约管理', icon: '◷' },
  { to: '/admin/users', label: '用户管理', icon: '♙' },
  { to: '/admin/news', label: '资讯管理', icon: '▤' },
  { to: '/admin/feedback', label: '反馈管理', icon: '✦' }
]
async function logout() { try { await housingApi.logout() } finally { session.logout(); router.push('/') } }
</script>

<template>
  <div class="admin-shell">
    <aside class="admin-sidebar">
      <RouterLink to="/" class="brand admin-brand"><span>⌂</span> 租住 <small>管理台</small></RouterLink>
      <nav class="side-nav">
        <RouterLink v-for="item in menus" :key="item.to" :to="item.to" :class="{ active: item.exact ? route.path === item.to : route.path.startsWith(item.to) }"><span>{{ item.icon }}</span>{{ item.label }}</RouterLink>
      </nav>
      <div class="admin-profile"><div class="avatar">管</div><div><b>{{ session.state.user?.name || '平台管理员' }}</b><small>系统管理员</small></div></div>
    </aside>
    <section class="admin-main">
      <header class="admin-topbar"><span>房源租赁管理</span><div><RouterLink to="/" class="text-link">查看站点</RouterLink><button class="text-link" @click="logout">退出登录</button></div></header>
      <div class="admin-content"><RouterView /></div>
    </section>
  </div>
</template>
