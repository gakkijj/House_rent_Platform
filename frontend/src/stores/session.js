import { computed, reactive } from 'vue'

const saved = JSON.parse(localStorage.getItem('houserent-session') || 'null')
const state = reactive({ user: saved || null })

export const session = {
  state,
  loggedIn: computed(() => Boolean(state.user)),
  login(userOrRole = 'customer') {
    state.user = typeof userOrRole === 'object'
      ? { ...userOrRole, avatar: userOrRole.avatar || (userOrRole.role === 'admin' ? '管' : '租') }
      : userOrRole === 'admin'
      ? { name: '平台管理员', role: 'admin', avatar: '管' }
      : { name: '演示租客', role: 'customer', avatar: '租' }
    localStorage.setItem('houserent-session', JSON.stringify(state.user))
  },
  logout() { state.user = null; localStorage.removeItem('houserent-session') }
}
