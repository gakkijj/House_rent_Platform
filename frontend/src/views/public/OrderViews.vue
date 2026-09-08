<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { housingApi } from '../../api/housing'

const props = defineProps({ page: { type: String, default: 'list' } })
const route = useRoute()
const router = useRouter()
const orders = ref([])
const order = ref(null)
const message = ref('')
const statusText = { pending_agreement: '待确认合同', pending_payment: '待支付', active: '生效中', expired: '已到期', cancelled: '已取消', ending: '退租申请中', end_rejected: '退租申请被拒绝' }
const canSign = computed(() => order.value?.status === 'pending_agreement')
const canPay = computed(() => order.value?.status === 'pending_payment')
onMounted(async () => {
  try {
    if (props.page === 'list') orders.value = await housingApi.listMyOrders()
    else order.value = await housingApi.getOrder(route.params.id)
  } catch (error) { message.value = error.message }
})
async function sign() { try { order.value = await housingApi.signAgreement(order.value.id); message.value = '合同已确认，可以进行支付。' } catch (error) { message.value = error.message } }
async function pay() { try { order.value = await housingApi.payOrder(order.value.id); message.value = '支付成功，订单已生效。' } catch (error) { message.value = error.message } }
</script>

<template>
  <div v-if="page === 'list'" class="page-width simple-page"><div class="section-heading"><div><p class="eyebrow">MY ORDERS</p><h1>我的订单</h1></div><RouterLink to="/houses" class="text-link">继续找房 →</RouterLink></div><p v-if="message" class="form-notice">{{ message }}</p><div v-if="orders.length" class="order-list"><article v-for="item in orders" :key="item.id" class="order-card"><div><p class="eyebrow">订单 #{{ item.id }}</p><h2>{{ item.houseTitle }}</h2><p class="muted">租期：{{ String(item.startDate).slice(0, 10) }} 至 {{ String(item.endDate).slice(0, 10) }}</p></div><div class="order-summary"><b>¥{{ item.totalAmount?.toLocaleString() }}</b><span class="status-pill" :class="item.status === 'active' ? 'available' : 'pending'">{{ statusText[item.status] }}</span><RouterLink :to="`/orders/${item.id}/agreement`" class="button small">查看订单</RouterLink></div></article></div><div v-else class="empty-state"><span>◷</span><h3>还没有租赁订单</h3><p>选定房源后，可以在线确认合同并完成支付。</p><RouterLink to="/houses" class="button">去找房</RouterLink></div></div>
  <div v-else class="page-width order-flow"><RouterLink to="/orders" class="back-link">← 返回我的订单</RouterLink><section v-if="order" class="agreement-card"><p class="eyebrow">RENTAL AGREEMENT · ORDER #{{ order.id }}</p><h1>{{ page === 'payment' ? '确认支付信息' : '租赁合同确认' }}</h1><div class="agreement-grid"><div><span>房源</span><b>{{ order.houseTitle }}</b></div><div><span>月租金</span><b>¥{{ order.monthRent?.toLocaleString() }}</b></div><div><span>租赁天数</span><b>{{ order.dayNum }} 天</b></div><div><span>合同总额</span><b>¥{{ order.totalAmount?.toLocaleString() }}</b></div><div><span>起租日期</span><b>{{ String(order.startDate).slice(0, 10) }}</b></div><div><span>结束日期</span><b>{{ String(order.endDate).slice(0, 10) }}</b></div></div><div class="agreement-rules"><h3>确认事项</h3><p>请确认房源、租期和费用信息。线上确认仅用于演示旧系统既有的合同与模拟支付状态流转，实际租赁仍应以双方线下签约的书面合同为准。</p></div><div class="flow-actions"><RouterLink to="/orders" class="button ghost">稍后处理</RouterLink><button v-if="canSign" class="button primary" @click="sign">确认合同</button><button v-else-if="canPay" class="button primary" @click="pay">模拟支付 ¥{{ order.totalAmount?.toLocaleString() }}</button><RouterLink v-else to="/orders" class="button">{{ statusText[order.status] }}</RouterLink></div><p v-if="message" class="form-notice">{{ message }}</p></section><div v-else class="empty-state"><span>!</span><h3>订单无法加载</h3><p>{{ message || '请返回订单列表重试。' }}</p></div></div>
</template>
