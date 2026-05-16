<template>
  <el-container class="layout-container">
    <el-aside width="220px" class="aside">
      <div class="logo">
        <div class="logo-icon">
          <el-icon><TrendCharts /></el-icon>
        </div>
        <div class="logo-text">
          <span class="logo-title">健康减脂</span>
          <span class="logo-sub">Health System</span>
        </div>
      </div>

      <nav class="nav-menu">
        <router-link
          v-for="item in menuItems"
          :key="item.path"
          :to="item.path"
          class="nav-item"
          :class="{ 'nav-item--active': activeMenu === item.path }"
        >
          <div class="nav-icon">
            <el-icon><component :is="item.icon" /></el-icon>
          </div>
          <span class="nav-label">{{ item.label }}</span>
          <div v-if="activeMenu === item.path" class="nav-active-bar"></div>
        </router-link>
      </nav>

      <div class="aside-footer">
        <div class="user-card">
          <el-avatar :size="36" icon="UserFilled" class="user-avatar" />
          <div class="user-card-info">
            <span class="user-card-name">{{ username }}</span>
            <span class="user-card-role">健康管理者</span>
          </div>
        </div>
      </div>
    </el-aside>

    <el-container>
      <el-header class="header">
        <div class="header-left">
          <div class="breadcrumb">
            <span class="page-title">{{ pageTitle }}</span>
          </div>
        </div>
        <div class="header-right">
          <div class="header-date">{{ currentDate }}</div>
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <el-avatar :size="32" icon="UserFilled" class="header-avatar" />
              <span class="username">{{ username }}</span>
              <el-icon class="caret"><CaretBottom /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon> 个人中心
                </el-dropdown-item>
                <el-dropdown-item command="logout" divided>
                  <el-icon><SwitchButton /></el-icon> 退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { ElMessage, ElMessageBox } from 'element-plus'
import dayjs from 'dayjs'

const route = useRoute()
const router = useRouter()
const store = useStore()

const activeMenu = computed(() => route.path)
const pageTitle = computed(() => route.meta.title || '首页')
const username = computed(() => store.state.nickname || store.state.username || '用户')
const currentDate = computed(() => dayjs().format('YYYY年MM月DD日'))

const menuItems = [
  { path: '/dashboard', label: '数据总览', icon: 'HomeFilled' },
  { path: '/body', label: '身体数据', icon: 'DataLine' },
  { path: '/diet', label: '饮食管理', icon: 'Bowl' },
  { path: '/exercise', label: '运动管理', icon: 'Stopwatch' },
  { path: '/goal', label: '目标管理', icon: 'Flag' },
  { path: '/profile', label: '个人中心', icon: 'User' },
]

const handleCommand = (command) => {
  if (command === 'profile') {
    router.push('/profile')
  } else if (command === 'logout') {
    ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      store.dispatch('logout')
      ElMessage.success('退出成功')
      router.push('/login')
    }).catch(() => {})
  }
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
  background: var(--color-page-bg);
}

/* ── Aside ── */
.aside {
  background: linear-gradient(180deg, #0d1f14 0%, #0f2d1c 40%, #0a2218 100%);
  display: flex;
  flex-direction: column;
  border-right: none;
  box-shadow: 4px 0 20px rgba(0, 0, 0, 0.15);
  position: relative;
  overflow: hidden;
}

/* subtle background pattern */
.aside::before {
  content: '';
  position: absolute;
  top: -60px;
  right: -60px;
  width: 200px;
  height: 200px;
  background: radial-gradient(circle, rgba(22, 199, 132, 0.12) 0%, transparent 70%);
  pointer-events: none;
}

.aside::after {
  content: '';
  position: absolute;
  bottom: 80px;
  left: -40px;
  width: 160px;
  height: 160px;
  background: radial-gradient(circle, rgba(22, 199, 132, 0.08) 0%, transparent 70%);
  pointer-events: none;
}

/* Logo */
.logo {
  height: 72px;
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 0 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.06);
  flex-shrink: 0;
}

.logo-icon {
  width: 38px;
  height: 38px;
  background: linear-gradient(135deg, #16c784, #059669);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  color: white;
  box-shadow: 0 4px 12px rgba(22, 199, 132, 0.4);
  flex-shrink: 0;
}

.logo-text {
  display: flex;
  flex-direction: column;
}

.logo-title {
  font-size: 16px;
  font-weight: 700;
  color: #ffffff;
  letter-spacing: 0.5px;
  line-height: 1.2;
}

.logo-sub {
  font-size: 10px;
  color: rgba(255, 255, 255, 0.35);
  letter-spacing: 1px;
  text-transform: uppercase;
  margin-top: 2px;
}

/* Nav */
.nav-menu {
  flex: 1;
  padding: 16px 12px;
  display: flex;
  flex-direction: column;
  gap: 4px;
  overflow-y: auto;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 11px 14px;
  border-radius: 10px;
  text-decoration: none;
  color: rgba(255, 255, 255, 0.55);
  font-size: 14px;
  font-weight: 500;
  transition: all 0.2s ease;
  position: relative;
  cursor: pointer;
}

.nav-item:hover {
  background: rgba(255, 255, 255, 0.07);
  color: rgba(255, 255, 255, 0.9);
}

.nav-item--active {
  background: rgba(22, 199, 132, 0.15);
  color: #16c784;
}

.nav-item--active:hover {
  background: rgba(22, 199, 132, 0.2);
  color: #16c784;
}

.nav-icon {
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
  font-size: 16px;
  flex-shrink: 0;
  background: rgba(255, 255, 255, 0.05);
  transition: all 0.2s ease;
}

.nav-item--active .nav-icon {
  background: rgba(22, 199, 132, 0.2);
  color: #16c784;
}

.nav-label {
  flex: 1;
  letter-spacing: 0.3px;
}

.nav-active-bar {
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 3px;
  height: 20px;
  background: #16c784;
  border-radius: 2px 0 0 2px;
}

/* Footer */
.aside-footer {
  padding: 12px;
  border-top: 1px solid rgba(255, 255, 255, 0.06);
  flex-shrink: 0;
}

.user-card {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 12px;
  border-radius: 10px;
  background: rgba(255, 255, 255, 0.05);
}

.user-avatar {
  background: linear-gradient(135deg, #16c784, #059669) !important;
  flex-shrink: 0;
}

.user-card-info {
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.user-card-name {
  font-size: 13px;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.85);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.user-card-role {
  font-size: 11px;
  color: rgba(255, 255, 255, 0.35);
  margin-top: 1px;
}

/* ── Header ── */
.header {
  background: rgba(255, 255, 255, 0.92);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  box-shadow: 0 1px 0 rgba(0, 0, 0, 0.06);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  height: 60px !important;
  border-bottom: 1px solid var(--color-border);
  position: sticky;
  top: 0;
  z-index: 100;
}

.page-title {
  font-size: 18px;
  font-weight: 700;
  color: var(--color-text-primary);
  letter-spacing: 0.3px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.header-date {
  font-size: 13px;
  color: var(--color-text-muted);
  font-weight: 400;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 6px 10px;
  border-radius: 8px;
  transition: background 0.2s;
}

.user-info:hover {
  background: var(--color-bg-light);
}

.header-avatar {
  background: linear-gradient(135deg, #16c784, #059669) !important;
}

.username {
  font-size: 14px;
  font-weight: 500;
  color: var(--color-text-primary);
}

.caret {
  font-size: 12px;
  color: var(--color-text-muted);
}

/* ── Main ── */
.main {
  background: var(--color-page-bg);
  padding: 24px;
  overflow-y: auto;
}
</style>
