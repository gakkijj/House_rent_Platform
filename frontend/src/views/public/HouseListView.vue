<script setup>
import { onMounted, reactive, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { housingApi } from '../../api/housing'
import HouseCard from '../../components/HouseCard.vue'
const route = useRoute()
const homes = ref([])
const loading = ref(true)
const filters = reactive({ keyword: '', city: '', rentType: '', minRent: '', maxRent: '' })
async function load() { loading.value = true; homes.value = await housingApi.listHouses(filters); loading.value = false }
function reset() { Object.assign(filters, { keyword: '', city: '', rentType: '', minRent: '', maxRent: '' }); load() }
onMounted(() => { Object.assign(filters, route.query); load() })
watch(filters, load, { deep: true })
</script>

<template>
  <div class="page-width list-page">
    <div class="page-intro"><p class="eyebrow">LISTINGS</p><h1>找到适合你的房子</h1><p>当前可结合区域、租住方式和预算筛选。</p></div>
    <div class="listing-layout">
      <aside class="filter-panel"><div class="filter-title"><b>筛选条件</b><button class="text-link" @click="reset">重置</button></div><label>关键词<input v-model="filters.keyword" placeholder="小区、区域或特点" /></label><label>城市<select v-model="filters.city"><option value="">全部城市</option><option>杭州</option><option>北京</option><option>上海</option><option>深圳</option></select></label><label>租住方式<select v-model="filters.rentType"><option value="">整租 / 合租</option><option value="whole">整租</option><option value="share">合租</option></select></label><label>月租预算<div class="input-row"><input v-model="filters.minRent" inputmode="numeric" placeholder="最低" /><span>—</span><input v-model="filters.maxRent" inputmode="numeric" placeholder="最高" /></div></label></aside>
      <section><div class="result-top"><b>{{ loading ? '正在加载…' : `共找到 ${homes.length} 套房源` }}</b><span>按最新发布排序</span></div><div v-if="!loading && homes.length" class="house-grid"><HouseCard v-for="house in homes" :key="house.id" :house="house" @favorite="load" /></div><div v-else-if="!loading" class="empty-state"><span>⌂</span><h3>暂时没有匹配的房源</h3><p>试试放宽预算或更换筛选条件。</p><button class="button" @click="reset">清空筛选</button></div></section>
    </div>
  </div>
</template>
