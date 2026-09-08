import { createRouter, createWebHistory } from 'vue-router'
import PublicLayout from '../layouts/PublicLayout.vue'
import AdminLayout from '../layouts/AdminLayout.vue'
import HomeView from '../views/public/HomeView.vue'
import HouseListView from '../views/public/HouseListView.vue'
import HouseDetailView from '../views/public/HouseDetailView.vue'
import AccountViews from '../views/public/AccountViews.vue'
import NewsViews from '../views/public/NewsViews.vue'
import AdminViews from '../views/admin/AdminViews.vue'
import { session } from '../stores/session'

const publicPage = (component, props) => ({ component: PublicLayout, children: [{ path: '', component, props }] })
const adminPage = (page) => ({ component: AdminLayout, meta: { requiresAdmin: true }, children: [{ path: '', component: AdminViews, props: { page } }] })

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', ...publicPage(HomeView) },
    { path: '/houses', ...publicPage(HouseListView) },
    { path: '/houses/:id', ...publicPage(HouseDetailView) },
    { path: '/favorites', ...publicPage(AccountViews, { page: 'favorites' }) },
    { path: '/feedback', ...publicPage(AccountViews, { page: 'feedback' }) },
    { path: '/login', ...publicPage(AccountViews, { page: 'login' }) },
    { path: '/register', ...publicPage(AccountViews, { page: 'register' }) },
    { path: '/news', ...publicPage(NewsViews) },
    { path: '/news/:id', ...publicPage(NewsViews, { detail: true }) },
    { path: '/admin', ...adminPage('dashboard') },
    { path: '/admin/houses', ...adminPage('houses') },
    { path: '/admin/houses/form', ...adminPage('form') },
    { path: '/admin/orders', ...adminPage('orders') },
    { path: '/admin/users', ...adminPage('users') },
    { path: '/admin/news', ...adminPage('news') },
    { path: '/admin/feedback', ...adminPage('feedback') },
    { path: '/:pathMatch(.*)*', redirect: '/' }
  ]
})

router.beforeEach((to) => {
  if (to.meta.requiresAdmin && session.state.user?.role !== 'admin') return '/login'
})

export default router
