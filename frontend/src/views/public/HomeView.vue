<script setup>
import { onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { housingApi } from '../../api/housing'
import HouseCard from '../../components/HouseCard.vue'

const router = useRouter()
const homes = ref([])
const search = reactive({ keyword: '', city: '', rentType: '' })
onMounted(async () => { homes.value = await housingApi.listHouses({}) })
function submit() { router.push({ path: '/houses', query: Object.fromEntries(Object.entries(search).filter(([, value]) => value)) }) }
</script>

<template>
  <section class="hero">
    <div class="hero-content">
      <p class="eyebrow">FIND A PLACE TO CALL HOME</p>
      <h1>住进合适的生活里</h1>
      <p class="hero-copy">用清晰的条件，找到更确定的房源。</p>
      <form class="search-panel" @submit.prevent="submit">
        <label><span>想住哪里</span><input v-model="search.keyword" placeholder="小区、商圈或地铁站" /></label>
        <label><span>城市</span><select v-model="search.city"><option value="">不限城市</option><option>杭州</option><option>北京</option><option>上海</option><option>深圳</option></select></label>
        <label><span>租住方式</span><select v-model="search.rentType"><option value="">整租 / 合租</option><option value="whole">整租</option><option value="share">合租</option></select></label>
        <button class="button primary">开始找房 →</button>
      </form>
    </div>
    <div class="hero-shape shape-one"></div><div class="hero-shape shape-two"></div>
  </section>
  <section class="section page-width">
    <div class="section-heading"><div><p class="eyebrow">RECENT LISTINGS</p><h2>最新上架</h2></div><RouterLink to="/houses" class="text-link">查看全部 →</RouterLink></div>
    <div class="house-grid"><HouseCard v-for="house in homes.filter((item) => item.status === 'available').slice(0, 3)" :key="house.id" :house="house" @favorite="homes = homes.slice()" /></div>
  </section>
  <section class="value-section"><div class="page-width value-grid"><div><p class="eyebrow">RENT WITH CONFIDENCE</p><h2>从看房到入住，<br />每一步都看得见。</h2></div><div class="value-items"><div><b>01</b><span><strong>明确的房源信息</strong><small>面积、配套、状态和联系人一次看清。</small></span></div><div><b>02</b><span><strong>轻量的预约流程</strong><small>提交预约意向，保留每一次沟通记录。</small></span></div><div><b>03</b><span><strong>可追溯的平台管理</strong><small>房源审核、反馈处理、预约状态统一管理。</small></span></div></div></div></section>
</template>
