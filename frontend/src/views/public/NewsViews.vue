<script setup>
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { housingApi } from '../../api/housing'
const props = defineProps({ detail: Boolean })
const route = useRoute()
const news = ref([])
const item = ref(null)
onMounted(async () => { if (props.detail) item.value = await housingApi.getNews(route.params.id); else news.value = await housingApi.listNews() })
</script>

<template>
  <div v-if="detail" class="page-width article-page"><RouterLink to="/news" class="back-link">← 返回租房指南</RouterLink><article v-if="item"><p class="eyebrow">RENTING GUIDE · {{ item.publishedAt }}</p><h1>{{ item.title }}</h1><p class="article-lead">{{ item.summary }}</p><div class="article-body">{{ item.body }}</div></article></div>
  <div v-else class="page-width simple-page narrow"><p class="eyebrow">RENTING GUIDE</p><h1>租房指南</h1><p class="page-subtitle">在签约之前，多一点确定。</p><div class="news-list"><RouterLink v-for="newsItem in news" :key="newsItem.id" :to="`/news/${newsItem.id}`" class="news-card"><p>{{ newsItem.publishedAt }}</p><h2>{{ newsItem.title }}</h2><span>{{ newsItem.summary }}</span><b>阅读全文 →</b></RouterLink></div></div>
</template>
