<template>
  <div class="register-container">
    <div class="brand-section">
      <div class="brand-content">
        <h1 class="brand-title">健康减脂系统</h1>
        <p class="brand-subtitle">科学管理 · 健康生活 · 轻松达标</p>
        <div class="feature-list">
          <div class="feature-item">
            <el-icon><DataLine /></el-icon>
            <span>体重追踪</span>
          </div>
          <div class="feature-item">
            <el-icon><Bowl /></el-icon>
            <span>饮食管理</span>
          </div>
          <div class="feature-item">
            <el-icon><Stopwatch /></el-icon>
            <span>运动记录</span>
          </div>
          <div class="feature-item">
            <el-icon><Flag /></el-icon>
            <span>目标达成</span>
          </div>
        </div>
      </div>
    </div>
    <div class="register-section">
      <div class="register-box">
        <div class="register-header">
          <h2>创建账户</h2>
          <p>开启您的健康管理之旅</p>
        </div>

        <el-form :model="registerForm" :rules="rules" ref="registerFormRef" class="register-form">
          <el-row :gutter="12">
            <el-col :span="12">
              <el-form-item prop="username">
                <el-input
                  v-model="registerForm.username"
                  placeholder="用户名"
                  prefix-icon="User"
                  size="large"
                  clearable
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item prop="nickname">
                <el-input
                  v-model="registerForm.nickname"
                  placeholder="昵称"
                  prefix-icon="UserFilled"
                  size="large"
                  clearable
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item prop="password">
            <el-input
              v-model="registerForm.password"
              type="password"
              placeholder="密码（6-20位）"
              prefix-icon="Lock"
              size="large"
              show-password
              @input="checkPasswordStrength"
            />
            <div v-if="registerForm.password" class="password-strength">
              <div class="strength-bar">
                <div
                  class="strength-fill"
                  :class="'strength-' + passwordStrength.level"
                  :style="{ width: passwordStrength.percent + '%' }"
                ></div>
              </div>
              <span class="strength-text" :class="'strength-' + passwordStrength.level">
                {{ passwordStrength.text }}
              </span>
            </div>
          </el-form-item>

          <el-form-item prop="confirmPassword">
            <el-input
              v-model="registerForm.confirmPassword"
              type="password"
              placeholder="确认密码"
              prefix-icon="Lock"
              size="large"
              show-password
            />
          </el-form-item>

          <el-form-item prop="email">
            <el-input
              v-model="registerForm.email"
              placeholder="邮箱（选填，用于找回密码）"
              prefix-icon="Message"
              size="large"
              clearable
            />
          </el-form-item>

          <el-form-item prop="phone">
            <el-input
              v-model="registerForm.phone"
              placeholder="手机号（选填）"
              prefix-icon="Phone"
              size="large"
              clearable
            />
          </el-form-item>

          <el-form-item prop="agreement">
            <el-checkbox v-model="registerForm.agreement">
              我已阅读并同意
              <a href="#" class="agreement-link" @click.prevent>《用户协议》</a>
              和
              <a href="#" class="agreement-link" @click.prevent>《隐私政策》</a>
            </el-checkbox>
          </el-form-item>

          <el-form-item>
            <el-button
              type="primary"
              size="large"
              :loading="loading"
              @click="handleRegister"
              class="register-btn"
            >
              {{ loading ? '注册中...' : '立即注册' }}
            </el-button>
          </el-form-item>
        </el-form>

        <div class="login-link">
          已有账号？<router-link to="/login">立即登录</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { DataLine, Bowl, Stopwatch, Flag } from '@element-plus/icons-vue'
import request from '@/utils/request'

const router = useRouter()
const registerFormRef = ref(null)
const loading = ref(false)

const registerForm = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  nickname: '',
  email: '',
  phone: '',
  agreement: false
})

const passwordStrength = reactive({
  level: 'weak',
  percent: 0,
  text: '弱'
})

const validatePass = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== registerForm.password) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const validateAgreement = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请阅读并同意用户协议和隐私政策'))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度为3-20个字符', trigger: 'blur' }
  ],
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度为6-20个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validatePass, trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  agreement: [
    { validator: validateAgreement, trigger: 'change' }
  ]
}

const checkPasswordStrength = () => {
  const pwd = registerForm.password
  if (!pwd) {
    passwordStrength.level = 'weak'
    passwordStrength.percent = 0
    passwordStrength.text = '弱'
    return
  }

  let strength = 0
  if (pwd.length >= 6) strength += 25
  if (pwd.length >= 10) strength += 25
  if (/[a-z]/.test(pwd) && /[A-Z]/.test(pwd)) strength += 25
  if (/\d/.test(pwd)) strength += 15
  if (/[!@#$%^&*(),.?":{}|<>]/.test(pwd)) strength += 10

  passwordStrength.percent = Math.min(strength, 100)

  if (strength < 40) {
    passwordStrength.level = 'weak'
    passwordStrength.text = '弱'
  } else if (strength < 70) {
    passwordStrength.level = 'medium'
    passwordStrength.text = '中'
  } else {
    passwordStrength.level = 'strong'
    passwordStrength.text = '强'
  }
}

const handleRegister = async () => {
  if (!registerFormRef.value) return

  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const { confirmPassword, agreement, ...data } = registerForm
        await request.post('/auth/register', data)
        ElMessage.success('注册成功，请登录')
        router.push('/login')
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
.register-container {
  width: 100%;
  height: 100vh;
  background: linear-gradient(135deg, #0a2218 0%, #0d3d2a 25%, #0f5239 50%, #0d3d2a 75%, #0a2218 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 60px;
  padding: 40px;
  position: relative;
  overflow: hidden;
}

.register-container::before {
  content: '';
  position: absolute;
  top: -100px;
  left: -100px;
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, rgba(22, 199, 132, 0.1) 0%, transparent 70%);
  filter: blur(40px);
  pointer-events: none;
}

.register-container::after {
  content: '';
  position: absolute;
  bottom: -80px;
  right: 5%;
  width: 300px;
  height: 300px;
  background: radial-gradient(circle, rgba(5, 150, 105, 0.08) 0%, transparent 70%);
  filter: blur(40px);
  pointer-events: none;
}

.brand-section {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 300px;
  position: relative;
  z-index: 1;
}

.brand-content {
  color: white;
  text-align: center;
}

.brand-title {
  font-size: 42px;
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
  margin-bottom: 40px;
  color: rgba(255, 255, 255, 0.65);
  letter-spacing: 1px;
}

.feature-list {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
  margin-top: 40px;
}

.feature-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 18px 14px;
  background: rgba(255, 255, 255, 0.08);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 14px;
  transition: all 0.3s ease;
}

.feature-item:hover {
  background: rgba(255, 255, 255, 0.13);
  border-color: rgba(22, 199, 132, 0.3);
  transform: translateY(-3px);
}

.feature-item .el-icon {
  font-size: 24px;
  color: #16c784;
  padding: 10px;
  background: rgba(22, 199, 132, 0.15);
  border-radius: 10px;
}

.feature-item span {
  font-size: 13px;
  font-weight: 500;
  color: rgba(255, 255, 255, 0.8);
}

.register-section {
  flex: 0 0 460px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  z-index: 1;
}

.register-box {
  width: 100%;
  padding: 40px;
  background: rgba(255, 255, 255, 0.98);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3), 0 0 1px rgba(255, 255, 255, 0.5) inset;
  border: 1px solid rgba(255, 255, 255, 0.2);
  max-height: 90vh;
  overflow-y: auto;
}

.register-header {
  text-align: center;
  margin-bottom: 28px;
}

.register-header h2 {
  font-size: 26px;
  font-weight: 700;
  color: #0f172a;
  margin-bottom: 6px;
}

.register-header p {
  font-size: 13px;
  color: #64748b;
}

.register-form :deep(.el-form-item) {
  margin-bottom: 18px;
}

.register-form :deep(.el-input__wrapper) {
  padding: 11px 14px;
  box-shadow: 0 0 0 1px var(--color-border) inset;
  background: #fafbfc;
}

.register-form :deep(.el-input__wrapper:hover) {
  background: white;
}

.password-strength {
  margin-top: 6px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.strength-bar {
  flex: 1;
  height: 4px;
  background: #e2e8f0;
  border-radius: 2px;
  overflow: hidden;
}

.strength-fill {
  height: 100%;
  transition: all 0.3s ease;
  border-radius: 2px;
}

.strength-fill.strength-weak {
  background: #ef4444;
}

.strength-fill.strength-medium {
  background: #f59e0b;
}

.strength-fill.strength-strong {
  background: #16c784;
}

.strength-text {
  font-size: 12px;
  font-weight: 500;
  min-width: 20px;
}

.strength-text.strength-weak { color: #ef4444; }
.strength-text.strength-medium { color: #f59e0b; }
.strength-text.strength-strong { color: #16c784; }

.agreement-link {
  color: var(--color-primary);
  text-decoration: none;
  font-weight: 500;
}

.agreement-link:hover {
  text-decoration: underline;
}

.register-btn {
  width: 100%;
  height: 48px;
  font-size: 15px;
  font-weight: 600;
  background: linear-gradient(135deg, #16c784 0%, #059669 100%);
  border: none;
  border-radius: 10px;
  box-shadow: 0 4px 16px rgba(22, 199, 132, 0.3);
  transition: all 0.3s ease;
  margin-top: 4px;
}

.register-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(22, 199, 132, 0.4);
}

.login-link {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
  color: #64748b;
}

.login-link a {
  color: #16c784;
  text-decoration: none;
  font-weight: 600;
  margin-left: 4px;
  transition: color 0.2s ease;
}

.login-link a:hover {
  color: #059669;
  text-decoration: underline;
}

@media (max-width: 1024px) {
  .register-container {
    flex-direction: column;
    gap: 30px;
  }

  .brand-section {
    flex: none;
    min-width: auto;
  }

  .register-section {
    flex: none;
    width: 100%;
    max-width: 460px;
  }
}
</style>
