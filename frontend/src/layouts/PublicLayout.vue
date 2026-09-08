<script setup>
import { useRouter } from 'vue-router'
import { session } from '../stores/session'

const router = useRouter()
function goAccount() { router.push(session.state.user?.role === 'admin' ? '/admin' : '/favorites') }
function logout() { session.logout(); router.push('/') }
</script>

<template>
  <div class="app-shell">
    <header class="public-header">
      <RouterLink to="/" class="brand"><span>⌂</span> 租住</RouterLink>
      <nav class="main-nav">
        <RouterLink to="/houses">找房</RouterLink>
        <RouterLink to="/news">租房指南</RouterLink>
        <RouterLink to="/feedback">意见反馈</RouterLink>
      </nav>
      <div class="header-actions">
        <RouterLink v-if="session.state.user?.role === 'tenant'" to="/favorites" class="text-link">我的收藏</RouterLink>
        <button v-if="session.state.user" class="avatar" @click="goAccount">{{ session.state.user.avatar }}</button>
        <button v-if="session.state.user" class="text-link" @click="logout">退出</button>
        <RouterLink v-else to="/login" class="button small">登录 / 注册</RouterLink>
      </div>
    </header>
    <main><slot /></main>
    <footer class="public-footer"><span class="brand footer-brand">⌂ 租住</span><span>让每一次找房，都更确定。</span><span>Vue 迁移演示版</span></footer>
  </div>
</template>
