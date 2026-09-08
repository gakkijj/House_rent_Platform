import { reactive } from 'vue'

const wait = (value, ms = 180) => new Promise((resolve) => setTimeout(() => resolve(structuredClone(value)), ms))
const today = new Date().toISOString().slice(0, 10)

const homes = [
  { id: 101, title: '西湖边 · 两室一厅整租', city: '杭州', district: '西湖区', address: '文三路 188 号', rentType: 'whole', monthRent: 5200, area: 76, bedroomNum: 2, toiletNum: 1, kitchenNum: 1, livingRoomNum: 1, floor: 8, maxFloor: 18, direction: '南', buildYear: 2018, hasElevator: true, hasAirConditioner: true, status: 'available', lat: 30.278, lng: 120.125, tags: ['近地铁', '采光好', '可做饭'], description: '步行 6 分钟到地铁站，楼下生活配套齐全。房东直租，随时可以看房。', landlord: { name: '李女士', phone: '138****2219' }, cover: 'linear-gradient(135deg, #94b7b0, #345a67)' },
  { id: 102, title: '滨江 CBD · 主卧带阳台', city: '杭州', district: '滨江区', address: '江南大道 429 号', rentType: 'share', monthRent: 2600, area: 22, bedroomNum: 1, toiletNum: 1, kitchenNum: 1, livingRoomNum: 1, floor: 12, maxFloor: 30, direction: '东南', buildYear: 2020, hasElevator: true, hasAirConditioner: true, status: 'available', lat: 30.208, lng: 120.215, tags: ['主卧', '独立阳台', '女生优先'], description: '三室合租，另外两位租客作息规律。公共区域定期保洁。', landlord: { name: '周先生', phone: '139****7088' }, cover: 'linear-gradient(135deg, #deaa67, #925d4c)' },
  { id: 103, title: '未来科技城 · 一居室', city: '杭州', district: '余杭区', address: '文一西路 998 号', rentType: 'whole', monthRent: 3300, area: 48, bedroomNum: 1, toiletNum: 1, kitchenNum: 1, livingRoomNum: 1, floor: 5, maxFloor: 11, direction: '南', buildYear: 2016, hasElevator: true, hasAirConditioner: true, status: 'rented', lat: 30.281, lng: 119.993, tags: ['精装', '近园区', '拎包入住'], description: '适合在未来科技城工作的租客，安静社区，物业管理完善。', landlord: { name: '王先生', phone: '137****4602' }, cover: 'linear-gradient(135deg, #889cc5, #46527f)' },
  { id: 104, title: '朝阳公园 · 朝南次卧', city: '北京', district: '朝阳区', address: '甜水园北里 16 号', rentType: 'share', monthRent: 3100, area: 16, bedroomNum: 1, toiletNum: 1, kitchenNum: 1, livingRoomNum: 1, floor: 6, maxFloor: 7, direction: '南', buildYear: 2008, hasElevator: false, hasAirConditioner: true, status: 'available', lat: 39.929, lng: 116.485, tags: ['近公园', '无中介费', '随时入住'], description: '两室一厅合租，公共空间整洁，交通便利。', landlord: { name: '陈女士', phone: '136****3310' }, cover: 'linear-gradient(135deg, #9ab9a0, #557564)' },
  { id: 105, title: '科技园 · 通勤友好一居', city: '深圳', district: '南山区', address: '科苑路 15 号', rentType: 'whole', monthRent: 4700, area: 42, bedroomNum: 1, toiletNum: 1, kitchenNum: 1, livingRoomNum: 1, floor: 19, maxFloor: 28, direction: '西', buildYear: 2019, hasElevator: true, hasAirConditioner: true, status: 'available', lat: 22.541, lng: 113.948, tags: ['地铁口', '家电齐全', '高层视野'], description: '距科技园步行约十分钟，独立一居，适合一人或情侣居住。', landlord: { name: '刘先生', phone: '135****8924' }, cover: 'linear-gradient(135deg, #a98eaa, #66546a)' },
  { id: 106, title: '陆家嘴 · 安静两居室', city: '上海', district: '浦东新区', address: '浦东南路 1101 号', rentType: 'whole', monthRent: 7800, area: 68, bedroomNum: 2, toiletNum: 1, kitchenNum: 1, livingRoomNum: 1, floor: 9, maxFloor: 20, direction: '南北', buildYear: 2014, hasElevator: true, hasAirConditioner: true, status: 'available', lat: 31.235, lng: 121.515, tags: ['双地铁', '南北通透', '近商圈'], description: '小区环境安静，步行可到地铁和商业综合体。', landlord: { name: '孙女士', phone: '158****0196' }, cover: 'linear-gradient(135deg, #7caebb, #3e6374)' }
]

const state = reactive({
  favorites: new Set(JSON.parse(localStorage.getItem('houserent-favorites') || '[]')),
  appointments: JSON.parse(localStorage.getItem('houserent-appointments') || '[]'),
  houses: homes,
  feedbacks: [
    { id: 1, user: '张同学', content: '希望增加地铁线路筛选。', status: 'pending', createdAt: '2026-09-03' },
    { id: 2, user: '王女士', content: '预约看房流程很清晰。', status: 'resolved', createdAt: '2026-09-01' }
  ],
  news: [
    { id: 1, title: '签订租房合同前，建议确认这 5 件事', summary: '从身份、房屋权属、费用和交接四个维度减少租房风险。', body: '请核验出租人身份与房屋权属；逐项确认租金、押金、水电和物业费用；记录屋内家具家电状况；明确租期和违约责任。', publishedAt: '2026-09-02' },
    { id: 2, title: '租房旺季找房节奏指南', summary: '建立筛选条件，集中看房，避免临时决策。', body: '先明确预算、通勤上限和不可妥协的条件，再把候选房源分批预约。看房时拍照记录，签约前保留沟通凭证。', publishedAt: '2026-08-28' }
  ]
})

const persist = () => {
  localStorage.setItem('houserent-favorites', JSON.stringify([...state.favorites]))
  localStorage.setItem('houserent-appointments', JSON.stringify(state.appointments))
}

function applyFilters(items, filters = {}) {
  const keyword = (filters.keyword || '').trim().toLowerCase()
  return items.filter((house) => {
    const matchedKeyword = !keyword || [house.title, house.city, house.district, house.address, ...house.tags].join(' ').toLowerCase().includes(keyword)
    const matchedCity = !filters.city || house.city === filters.city
    const matchedType = !filters.rentType || house.rentType === filters.rentType
    const matchedMin = !filters.minRent || house.monthRent >= Number(filters.minRent)
    const matchedMax = !filters.maxRent || house.monthRent <= Number(filters.maxRent)
    const matchedStatus = !filters.status || house.status === filters.status
    return matchedKeyword && matchedCity && matchedType && matchedMin && matchedMax && matchedStatus
  })
}

const mockHousingApi = {
  mode: 'mock',
  async listHouses(filters) { return wait(applyFilters(state.houses, filters)) },
  async getHouse(id) { return wait(state.houses.find((item) => item.id === Number(id)) || null) },
  async listFavorites() { return wait(state.houses.filter((house) => state.favorites.has(house.id))) },
  async toggleFavorite(id) {
    const houseId = Number(id)
    state.favorites.has(houseId) ? state.favorites.delete(houseId) : state.favorites.add(houseId)
    persist()
    return wait({ active: state.favorites.has(houseId) }, 80)
  },
  isFavorite(id) { return state.favorites.has(Number(id)) },
  async createAppointment(payload) {
    if (!payload.name?.trim() || !payload.phone?.trim() || !payload.visitDate) throw new Error('请完整填写预约信息')
    state.appointments.unshift({ id: Date.now(), ...payload, status: 'pending', createdAt: today })
    persist()
    return wait({ success: true })
  },
  async listAppointments() { return wait(state.appointments) },
  async submitFeedback(content) {
    if (!content?.trim()) throw new Error('请输入反馈内容')
    state.feedbacks.unshift({ id: Date.now(), user: '演示用户', content, status: 'pending', createdAt: today })
    return wait({ success: true })
  },
  async listFeedbacks() { return wait(state.feedbacks) },
  async updateFeedbackStatus(id, status) { state.feedbacks.find((item) => item.id === id).status = status; return wait({ success: true }) },
  async listNews() { return wait(state.news) },
  async getNews(id) { return wait(state.news.find((item) => item.id === Number(id)) || null) },
  async saveHouse(payload) {
    if (!payload.title?.trim() || !payload.city || !payload.monthRent) throw new Error('请填写标题、城市和月租金')
    if (payload.id) Object.assign(state.houses.find((item) => item.id === payload.id), payload)
    else state.houses.unshift({ ...payload, id: Date.now(), status: 'pending', cover: 'linear-gradient(135deg, #a1b7d1, #52687f)', tags: payload.tags || [] })
    return wait({ success: true })
  },
  async updateHouseStatus(id, status) { state.houses.find((item) => item.id === Number(id)).status = status; return wait({ success: true }) },
  async deleteHouse(id) { state.houses.splice(state.houses.findIndex((item) => item.id === Number(id)), 1); return wait({ success: true }) },
  async login({ userName }) { return wait({ id: 1, name: userName || '演示租客', role: 'customer', avatar: '租' }) },
  async register({ userName }) { return wait({ id: 1, name: userName || '演示租客', role: 'customer', avatar: '租' }) },
  async logout() { return wait({ success: true }) },
  async me() { throw new Error('未登录') },
  async listManagedHouses() { return wait(state.houses) }
}

async function request(path, options = {}) {
  const response = await fetch(path, {
    credentials: 'same-origin',
    headers: { 'Content-Type': 'application/json', ...(options.headers || {}) },
    ...options
  })
  let body
  try { body = await response.json() } catch (_) { throw new Error('服务返回了非 JSON 响应') }
  if (!response.ok || body.code !== 1) throw new Error(body.msg || '请求失败')
  return body.result
}

const remoteFavorites = reactive(new Set())
const queryString = (filters = {}) => {
  const query = new URLSearchParams()
  Object.entries(filters).forEach(([key, value]) => { if (value !== '' && value !== undefined && value !== null) query.set(key, value) })
  const text = query.toString()
  return text ? `?${text}` : ''
}

const remoteHousingApi = {
  mode: 'remote',
  async listHouses(filters) { return (await request(`/api/v1/houses${queryString(filters)}`)).items },
  async listManagedHouses() { return request('/api/v1/admin/houses') },
  async getHouse(id) { return request(`/api/v1/houses/${id}`) },
  async listFavorites() {
    const homes = await request('/api/v1/houses/favorites')
    remoteFavorites.clear(); homes.forEach((home) => remoteFavorites.add(home.id))
    return homes
  },
  async toggleFavorite(id) {
    const active = remoteFavorites.has(Number(id))
    const result = await request(`/api/v1/houses/${id}/favorite`, { method: active ? 'DELETE' : 'POST' })
    result ? remoteFavorites.add(Number(id)) : remoteFavorites.delete(Number(id))
    return { active: result }
  },
  isFavorite(id) { return remoteFavorites.has(Number(id)) },
  async createAppointment(payload) {
    return request('/api/v1/orders', { method: 'POST', body: JSON.stringify({ houseId: payload.houseId, endDate: payload.endDate || payload.visitDate }) })
  },
  async listAppointments() { return request('/api/v1/admin/orders') },
  async listMyOrders() { return request('/api/v1/orders/mine') },
  async getOrder(id) { return request(`/api/v1/orders/${id}`) },
  async signAgreement(id) { return request(`/api/v1/orders/${id}/agreement`, { method: 'POST' }) },
  async payOrder(id) { return request(`/api/v1/orders/${id}/pay`, { method: 'POST' }) },
  async submitFeedback(content) { return request('/api/v1/feedbacks', { method: 'POST', body: JSON.stringify({ content }) }) },
  async listFeedbacks() { return request('/api/v1/admin/feedbacks') },
  async updateFeedbackStatus(id, status) { if (status === 'resolved') return request(`/api/v1/admin/feedbacks/${id}/resolve`, { method: 'POST' }) },
  async listNews() {
    const items = await request('/api/v1/news')
    return items.map((item) => ({ ...item, publishedAt: item.createTime?.slice(0, 10), body: item.content }))
  },
  async getNews(id) {
    const item = await request(`/api/v1/news/${id}`)
    return { ...item, publishedAt: item.createTime?.slice(0, 10), body: item.content }
  },
  async saveHouse(house) {
    const payload = { ...house, content: house.description, hasElevator: Boolean(house.hasElevator), hasAirConditioner: Boolean(house.hasAirConditioner) }
    return request(house.id ? `/api/v1/admin/houses/${house.id}` : '/api/v1/admin/houses', { method: house.id ? 'PATCH' : 'POST', body: JSON.stringify(payload) })
  },
  async updateHouseStatus(id, status) { return request(`/api/v1/admin/houses/${id}/status`, { method: 'POST', body: JSON.stringify({ status }) }) },
  async deleteHouse(id) { return request(`/api/v1/admin/houses/${id}`, { method: 'DELETE' }) },
  async login(payload) { return request('/api/v1/auth/login', { method: 'POST', body: JSON.stringify(payload) }) },
  async register(payload) { return request('/api/v1/auth/register', { method: 'POST', body: JSON.stringify(payload) }) },
  async logout() { return request('/api/v1/auth/logout', { method: 'POST' }) },
  async me() { return request('/api/v1/auth/me') },
  async listUsers() { return request('/api/v1/admin/users') },
  async listAdminNews() {
    const items = await request('/api/v1/admin/news')
    return items.map((item) => ({ ...item, publishedAt: item.createTime?.slice(0, 10), body: item.content }))
  }
}

export const housingApi = import.meta.env.VITE_API_MODE === 'mock' ? mockHousingApi : remoteHousingApi
