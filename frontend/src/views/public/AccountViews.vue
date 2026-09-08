<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { housingApi } from '../../api/housing'
import HouseCard from '../../components/HouseCard.vue'
import { session } from '../../stores/session'

const props = defineProps({ page: { type: String, default: 'login' } })
const router = useRouter()
const favorites = ref([])
const message = ref('')
const auth = reactive({ userName: '', password: '', displayName: '', phone: '', email: '' })
const isLogin = computed(() => props.page === 'login')
onMounted(async () => { if (props.page === 'favorites') favorites.value = await housingApi.listFavorites() })
async function submitAuth() {
  message.value = ''
  try {
    const user = isLogin.value ? await housingApi.login(auth) : await housingApi.register(auth)
    session.login(user)
    router.push(user.role === 'admin' ? '/admin' : '/')
  } catch (error) { message.value = error.message }
}
function demoAdmin() { session.login('admin'); router.push('/admin') }
async function submitFeedback(event) { const content = new FormData(event.target).get('content'); try { await housingApi.submitFeedback(content); message.value = '已收到你的反馈，感谢帮助我们改进。'; event.target.reset() } catch (error) { message.value = error.message } }
</script>

<template>
  <div v-if="page === 'favorites'" class="page-width simple-page"><div class="section-heading"><div><p class="eyebrow">FAVORITES</p><h1>我的收藏</h1></div><RouterLink to="/houses" class="text-link">继续找房 →</RouterLink></div><div v-if="favorites.length" class="house-grid"><HouseCard v-for="house in favorites" :key="house.id" :house="house" @favorite="favorites = favorites.filter((item) => housingApi.isFavorite(item.id))" /></div><div v-else class="empty-state"><span>♡</span><h3>还没有收藏房源</h3><p>看到合适的房子，点击心形按钮收藏起来。</p><RouterLink to="/houses" class="button">去找房</RouterLink></div></div>
  <div v-else-if="page === 'feedback'" class="page-width simple-page narrow"><p class="eyebrow">FEEDBACK</p><h1>告诉我们你的想法</h1><p class="page-subtitle">你提出的每一条建议，都会帮助租客更轻松地找到房子。</p><form class="form-card" @submit.prevent="submitFeedback"><label>反馈内容<textarea name="content" rows="7" placeholder="例如：我希望可以按通勤距离筛选房源…"></textarea></label><button class="button primary">提交反馈</button><p v-if="message" class="form-notice">{{ message }}</p></form></div>
  <div v-else class="auth-page"><div class="auth-card"><RouterLink to="/" class="brand">⌂ 租住</RouterLink><p class="eyebrow">{{ isLogin ? 'WELCOME BACK' : 'CREATE ACCOUNT' }}</p><h1>{{ isLogin ? '欢迎回来' : '创建你的账户' }}</h1><p class="muted">{{ isLogin ? '登录后可以收藏房源和管理租赁订单。' : '注册后会自动以租客身份登录。' }}</p><form @submit.prevent="submitAuth"><label>用户名<input v-model="auth.userName" autocomplete="username" placeholder="请输入用户名" /></label><label v-if="!isLogin">显示名称<input v-model="auth.displayName" placeholder="例如：小王" /></label><label v-if="!isLogin">手机号码<input v-model="auth.phone" type="tel" placeholder="选填" /></label><label v-if="!isLogin">邮箱<input v-model="auth.email" type="email" placeholder="选填" /></label><label>密码<input v-model="auth.password" autocomplete="current-password" type="password" placeholder="请输入密码" /></label><button class="button primary full">{{ isLogin ? '登录' : '注册并登录' }}</button></form><button v-if="housingApi.mode === 'mock' && isLogin" class="demo-admin" @click="demoAdmin">以管理员身份体验管理台 →</button><p v-if="message" class="form-notice">{{ message }}</p><p class="muted auth-switch">{{ isLogin ? '还没有账号？' : '已有账号？' }} <RouterLink :to="isLogin ? '/register' : '/login'">{{ isLogin ? '去注册' : '去登录' }}</RouterLink></p></div></div>
</template>
