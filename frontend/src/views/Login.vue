<template>
  <div class="login-container">
    <div class="login-bg">
      <div class="bg-gradient"></div>
      <div class="bg-pattern"></div>
      <div class="floating-shapes">
        <div class="shape shape-1"></div>
        <div class="shape shape-2"></div>
        <div class="shape shape-3"></div>
      </div>
    </div>

    <div class="login-content">
      <div class="brand-section">
        <div class="brand-content">
          <div class="brand-logo">
            <div class="logo-circle">
              <el-icon><TrendCharts /></el-icon>
            </div>
          </div>
          <h1 class="brand-title">健康减脂系统</h1>
          <p class="brand-subtitle">科学管理 · 健康生活 · 轻松达标</p>

          <div class="feature-grid">
            <div class="feature-card">
              <div class="feature-icon">
                <el-icon><DataLine /></el-icon>
              </div>
              <div class="feature-text">
                <h3>体重追踪</h3>
                <p>精准记录每日变化</p>
              </div>
            </div>
            <div class="feature-card">
              <div class="feature-icon">
                <el-icon><Bowl /></el-icon>
              </div>
              <div class="feature-text">
                <h3>饮食管理</h3>
                <p>智能营养分析</p>
              </div>
            </div>
            <div class="feature-card">
              <div class="feature-icon">
                <el-icon><Stopwatch /></el-icon>
              </div>
              <div class="feature-text">
                <h3>运动记录</h3>
                <p>科学消耗计算</p>
              </div>
            </div>
            <div class="feature-card">
              <div class="feature-icon">
                <el-icon><Flag /></el-icon>
              </div>
              <div class="feature-text">
                <h3>目标达成</h3>
                <p>可视化进度跟踪</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="login-section">
        <div class="login-box">
          <div class="login-header">
            <h2>欢迎回来</h2>
            <p>登录您的健康管理账户</p>
          </div>

          <div class="login-type-tabs">
            <div 
              :class="['tab-item', { active: loginType === 'username' }]"
              @click="loginType = 'username'"
            >
              <el-icon><User /></el-icon>
              <span>用户名登录</span>
            </div>
            <div 
              :class="['tab-item', { active: loginType === 'phone' }]"
              @click="loginType = 'phone'"
            >
              <el-icon><Iphone /></el-icon>
              <span>手机号登录</span>
            </div>
          </div>

          <el-form :model="loginForm" :rules="currentRules" ref="loginFormRef" class="login-form">
            <el-form-item prop="username">
              <el-input
                v-model="loginForm.username"
                :placeholder="loginType === 'phone' ? '请输入手机号' : '请输入用户名'"
                :prefix-icon="loginType === 'phone' ? 'Iphone' : 'User'"
                size="large"
                clearable
              />
            </el-form-item>
            <el-form-item prop="password">
              <el-input
                v-model="loginForm.password"
                type="password"
                placeholder="请输入密码"
                prefix-icon="Lock"
                size="large"
                show-password
                @keyup.enter="handleLogin"
              />
            </el-form-item>
            <div class="form-extra">
              <router-link to="/forgot-password" class="forgot-link">忘记密码？</router-link>
            </div>
            <el-form-item>
              <el-button
                type="primary"
                size="large"
                :loading="loading"
                @click="handleLogin"
                class="login-btn"
              >
                {{ loading ? '登录中...' : '立即登录' }}
              </el-button>
            </el-form-item>
          </el-form>

          <div class="login-footer">
            <span>还没有账号？</span>
            <router-link to="/register" class="register-link">立即注册</router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
import { ElMessage } from 'element-plus'
import { DataLine, Bowl, Stopwatch, Flag, TrendCharts, User, Iphone } from '@element-plus/icons-vue'

const router = useRouter()
const store = useStore()
const loginFormRef = ref(null)
const loading = ref(false)
const loginType = ref('username')

const loginForm = reactive({
  username: '',
  password: ''
})

const usernameRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const phoneRules = {
  username: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const currentRules = computed(() => loginType.value === 'phone' ? phoneRules : usernameRules)

const handleLogin = async () => {
  if (!loginFormRef.value) return

  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        await store.dispatch('login', loginForm)
        ElMessage.success('登录成功')
        router.push('/dashboard')
      } catch (error) {
        console.error(error)
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.login-container {
  width: 100%;
  height: 100vh;
  position: relative;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-bg {
  position: absolute;
  inset: 0;
  z-index: 0;
}

.bg-gradient {
  position: absolute;
  inset: 0;
  background: linear-gradient(135deg, #0a2218 0%, #0d3d2a 25%, #0f5239 50%, #0d3d2a 75%, #0a2218 100%);
}

.bg-pattern {
  position: absolute;
  inset: 0;
  background-image:
    radial-gradient(circle at 20% 30%, rgba(22, 199, 132, 0.08) 0%, transparent 50%),
    radial-gradient(circle at 80% 70%, rgba(5, 150, 105, 0.06) 0%, transparent 50%),
    radial-gradient(circle at 50% 50%, rgba(22, 199, 132, 0.04) 0%, transparent 70%);
  opacity: 0.6;
}

.floating-shapes {
  position: absolute;
  inset: 0;
  overflow: hidden;
}

.shape {
  position: absolute;
  border-radius: 50%;
  filter: blur(60px);
  opacity: 0.15;
  animation: float 20s ease-in-out infinite;
}

.shape-1 {
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, #16c784, transparent);
  top: -100px;
  left: -100px;
  animation-delay: 0s;
}

.shape-2 {
  width: 300px;
  height: 300px;
  background: radial-gradient(circle, #059669, transparent);
  bottom: -80px;
  right: 10%;
  animation-delay: 7s;
}

.shape-3 {
  width: 250px;
  height: 250px;
  background: radial-gradient(circle, #16c784, transparent);
  top: 40%;
  right: -80px;
  animation-delay: 14s;
}

@keyframes float {
  0%, 100% { transform: translate(0, 0) scale(1); }
  33% { transform: translate(30px, -30px) scale(1.1); }
  66% { transform: translate(-20px, 20px) scale(0.9); }
}

.login-content {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 1200px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 60px;
  padding: 40px;
}

.brand-section {
  flex: 1;
  max-width: 520px;
}

.brand-content {
  color: white;
}

.brand-logo {
  margin-bottom: 24px;
}

.logo-circle {
  width: 72px;
  height: 72px;
  background: linear-gradient(135deg, #16c784, #059669);
  border-radius: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 36px;
  color: white;
  box-shadow: 0 8px 32px rgba(22, 199, 132, 0.4);
}

.brand-title {
  font-size: 48px;
  font-weight: 800;
  margin-bottom: 12px;
  letter-spacing: -0.5px;
  background: linear-gradient(135deg, #ffffff 0%, rgba(255, 255, 255, 0.8) 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.brand-subtitle {
  font-size: 16px;
  color: rgba(255, 255, 255, 0.7);
  margin-bottom: 48px;
  letter-spacing: 1px;
}

.feature-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.feature-card {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 18px;
  background: rgba(255, 255, 255, 0.08);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 14px;
  transition: all 0.3s ease;
}

.feature-card:hover {
  background: rgba(255, 255, 255, 0.12);
  border-color: rgba(22, 199, 132, 0.3);
  transform: translateY(-2px);
}

.feature-icon {
  width: 44px;
  height: 44px;
  background: rgba(22, 199, 132, 0.2);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  color: #16c784;
  flex-shrink: 0;
}

.feature-text h3 {
  font-size: 14px;
  font-weight: 600;
  color: white;
  margin-bottom: 2px;
}

.feature-text p {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.5);
}

.login-section {
  flex: 0 0 420px;
}

.login-box {
  background: rgba(255, 255, 255, 0.98);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3), 0 0 1px rgba(255, 255, 255, 0.5) inset;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.login-header {
  text-align: center;
  margin-bottom: 24px;
}

.login-header h2 {
  font-size: 28px;
  font-weight: 700;
  color: var(--color-text-primary);
  margin-bottom: 8px;
}

.login-header p {
  font-size: 14px;
  color: var(--color-text-secondary);
}

.login-type-tabs {
  display: flex;
  gap: 12px;
  margin-bottom: 24px;
  padding: 4px;
  background: #f5f7fa;
  border-radius: 12px;
}

.tab-item {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 12px;
  font-size: 14px;
  font-weight: 500;
  color: #606266;
  background: transparent;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.tab-item:hover {
  color: #16c784;
}

.tab-item.active {
  background: white;
  color: #16c784;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.login-form {
  margin-bottom: 20px;
}

.login-form :deep(.el-form-item) {
  margin-bottom: 20px;
}

.login-form :deep(.el-input__wrapper) {
  padding: 12px 16px;
  box-shadow: 0 0 0 1px var(--color-border) inset;
  background: #fafbfc;
}

.login-form :deep(.el-input__wrapper:hover) {
  background: white;
}

.form-extra {
  display: flex;
  justify-content: flex-end;
  margin: -12px 0 16px;
}

.forgot-link {
  font-size: 13px;
  color: var(--color-text-secondary);
  text-decoration: none;
  transition: color 0.2s;
}

.forgot-link:hover {
  color: var(--color-primary);
}

.login-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 0.5px;
  border-radius: 10px;
  background: linear-gradient(135deg, #16c784 0%, #059669 100%);
  border: none;
  box-shadow: 0 4px 16px rgba(22, 199, 132, 0.3);
  transition: all 0.3s ease;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(22, 199, 132, 0.4);
}

.login-btn:active {
  transform: translateY(0);
}

.login-footer {
  text-align: center;
  font-size: 14px;
  color: var(--color-text-secondary);
}

.register-link {
  color: var(--color-primary);
  text-decoration: none;
  font-weight: 600;
  margin-left: 4px;
  transition: color 0.2s;
}

.register-link:hover {
  color: var(--color-primary-dark);
  text-decoration: underline;
}

@media (max-width: 1024px) {
  .login-content {
    flex-direction: column;
    gap: 40px;
  }

  .brand-section {
    max-width: 100%;
    text-align: center;
  }

  .brand-title {
    font-size: 36px;
  }

  .feature-grid {
    max-width: 500px;
    margin: 0 auto;
  }

  .login-section {
    flex: none;
    width: 100%;
    max-width: 420px;
  }
}
</style>
