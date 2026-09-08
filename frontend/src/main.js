import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { housingApi } from './api/housing'
import { session } from './stores/session'
import './styles.css'

if (housingApi.mode !== 'mock') {
  housingApi.me().then(session.login).catch(session.logout)
}

createApp(App).use(router).mount('#app')
