import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/views/Login.vue'
import Register from '@/views/Register.vue'
import ForgotPassword from '@/views/ForgotPassword.vue'
import Layout from '@/views/Layout.vue'
import Dashboard from '@/views/Dashboard.vue'
import BodyData from '@/views/BodyData.vue'
import Diet from '@/views/Diet.vue'
import Exercise from '@/views/Exercise.vue'
import Goal from '@/views/Goal.vue'
import Profile from '@/views/Profile.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { requiresAuth: false }
  },
  {
    path: '/register',
    name: 'Register',
    component: Register,
    meta: { requiresAuth: false }
  },
  {
    path: '/forgot-password',
    name: 'ForgotPassword',
    component: ForgotPassword,
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    meta: { requiresAuth: true },
    children: [
      { path: 'dashboard', name: 'Dashboard', component: Dashboard, meta: { title: '数据总览' } },
      { path: 'body', name: 'BodyData', component: BodyData, meta: { title: '身体数据' } },
      { path: 'diet', name: 'Diet', component: Diet, meta: { title: '饮食管理' } },
      { path: 'exercise', name: 'Exercise', component: Exercise, meta: { title: '运动管理' } },
      { path: 'goal', name: 'Goal', component: Goal, meta: { title: '目标管理' } },
      { path: 'profile', name: 'Profile', component: Profile, meta: { title: '个人中心' } }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth !== false && !token) {
    next('/login')
  } else if ((to.path === '/login' || to.path === '/register' || to.path === '/forgot-password') && token) {
    next('/dashboard')
  } else {
    next()
  }
})

export default router
