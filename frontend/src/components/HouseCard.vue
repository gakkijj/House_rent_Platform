<script setup>
import { computed } from 'vue'
import { housingApi } from '../api/housing'

const props = defineProps({ house: { type: Object, required: true } })
const emit = defineEmits(['favorite'])
const favorite = computed(() => housingApi.isFavorite(props.house.id))

async function toggleFavorite() {
  await housingApi.toggleFavorite(props.house.id)
  emit('favorite')
}
</script>

<template>
  <article class="house-card">
    <RouterLink :to="`/houses/${house.id}`" class="house-cover" :style="{ background: house.cover }">
      <span class="cover-building">⌂</span>
      <span class="type-badge">{{ house.rentType === 'whole' ? '整租' : '合租' }}</span>
      <span v-if="house.status !== 'available'" class="status-badge">已租</span>
    </RouterLink>
    <div class="house-card-body">
      <div class="card-title-line">
        <RouterLink :to="`/houses/${house.id}`" class="house-title">{{ house.title }}</RouterLink>
        <button class="icon-button" :class="{ active: favorite }" :aria-label="favorite ? '取消收藏' : '收藏'" @click="toggleFavorite">♥</button>
      </div>
      <p class="muted location">⌖ {{ house.city }} · {{ house.district }} · {{ house.address }}</p>
      <div class="house-facts">{{ house.area }}㎡ · {{ house.bedroomNum }}室{{ house.livingRoomNum }}厅 · {{ house.direction }}</div>
      <div class="tag-row"><span v-for="tag in house.tags.slice(0, 3)" :key="tag" class="tag">{{ tag }}</span></div>
      <div class="price"><strong>¥{{ house.monthRent.toLocaleString() }}</strong><span>/ 月</span></div>
    </div>
  </article>
</template>
