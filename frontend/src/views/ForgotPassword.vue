<template>
  <div class="forgot-container">
    <div class="forgot-bg">
      <div class="bg-gradient"></div>
      <div class="floating-shapes">
        <div class="shape shape-1"></div>
        <div class="shape shape-2"></div>
        <div class="shape shape-3"></div>
      </div>
    </div>

    <div class="forgot-content">
      <div class="forgot-box">
        <div class="forgot-header">
          <router-link to="/login" class="back-link">
            <el-icon><ArrowLeft /></el-icon>
            返回登录
          </router-link>
          <h2>找回密码</h2>
          <p>通过手机号重置您的登录密码</p>
        </div>

        <div class="steps">
          <el-steps :active="currentStep" align-center class="steps">
            <el-step title="验证手机" />
            <el-step title="设置密码" />
            <el-step title="完成" />
          </el-steps>
        </div>

        <div v-if="currentStep === 0" class="step-content">
          <el-form :model="phoneForm" :rules="phoneRules" ref="phoneFormRef" class="forgot-form">
            <el-form-item prop="phone">
              <el-input
                v-model="phoneForm.phone"
                placeholder="请输入手机号"
                prefix-icon="Iphone"
                size="large"
                clearable
              />
            </el-form-item>
            <el-form-item prop="verifyCode">
              <div class="code-input-group">
                <el-input
                  v-model="phoneForm.verifyCode"
                  placeholder="请输入验证码"
                  prefix-icon="Key"
                  size="large"
                  maxlength="6"
                />
                <el-button
                  type="primary"
                  size="large"
                  :disabled="countdown > 0"
                  :loading="sendingCode"
                  @click="sendVerifyCode"
                >
                  {{ countdown > 0 ? `${countdown}s` : '获取验证码' }}
                </el-button>
              </div>
            </el-form-item>
            <el-form-item>
              <el-button
                type="primary"
                size="large"
                :loading="verifying"
                @click="verifyPhone"
                class="submit-btn"
              >
                下一步
              </el-button>
            </el-form-item>
          </el-form>
        </div>

        <div v-if="currentStep === 1" class="step-content">
          <el-form :model="passwordForm" :rules="passwordRules" ref="passwordFormRef" class="forgot-form">
            <el-form-item prop="newPassword">
              <el-input
                v-model="passwordForm.newPassword"
                type="password"
                placeholder="请输入新密码"
                prefix-icon="Lock"
                size="large"
                show-password
              />
            </el-form-item>
            <el-form-item prop="confirmPassword">
              <el-input
                v-model="passwordForm.confirmPassword"
                type="password"
                placeholder="请确认新密码"
                prefix-icon="Lock"
                size="large"
                show-password
              />
            </el-form-item>
            <el-form-item>
              <el-button
                type="primary"
                size="large"
                :loading="resetting"
                @click="resetPassword"
                class="submit-btn"
              >
                确认重置
              </el-button>
            </el-form-item>
          </el-form>
        </div>

        <div v-if="currentStep === 2" class="step-content">
          <div class="success-content">
            <el-result
              icon="success"
              title="密码重置成功"
              sub-title="您的新密码已设置成功，请使用新密码登录"
            />
            <el-button type="primary" size="large" @click="goToLogin" class="submit-btn">
              立即登录
            </el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowLeft } from '@element-plus/icons-vue'
import request from '@/utils/request'

const router = useRouter()
const phoneFormRef = ref(null)
const passwordFormRef = ref(null)
const currentStep = ref(0)
const countdown = ref(0)
const sendingCode = ref(false)
const verifying = ref(false)
const resetting = ref(false)

const phoneForm = reactive({
  phone: '',
  verifyCode: ''
})

const passwordForm = reactive({
  newPassword: '',
  confirmPassword: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请确认新密码'))
  } else if (value !== passwordForm.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const phoneRules = {
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  verifyCode: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { len: 6, message: '验证码为6位数字', trigger: 'blur' }
  ]
}

const passwordRules = {
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度为6-20个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

let timer = null

const sendVerifyCode = async () => {
  if (!phoneForm.phone) {
    ElMessage.warning('请先输入手机号')
    return
  }

  if (!/^1[3-9]\d{9}$/.test(phoneForm.phone)) {
    ElMessage.warning('请输入正确的手机号')
    return
  }

  sendingCode.value = true
  try {
    const res = await request.post('/auth/send-code', {
      phone: phoneForm.phone,
      type: 'reset'
    })
    phoneForm.verifyCode = res.data.code
    ElMessage.success('验证码已自动填充')
    
    countdown.value = 60
    timer = setInterval(() => {
      countdown.value--
      if (countdown.value <= 0) {
        clearInterval(timer)
        timer = null
      }
    }, 1000)
  } catch (error) {
    console.error(error)
  } finally {
    sendingCode.value = false
  }
}

const verifyPhone = async () => {
  if (!phoneFormRef.value) return

  await phoneFormRef.value.validate(async (valid) => {
    if (valid) {
      verifying.value = true
      try {
        await request.get('/auth/check-phone', {
          params: { phone: phoneForm.phone }
        })
        currentStep.value = 1
      } catch (error) {
        console.error(error)
      } finally {
        verifying.value = false
      }
    }
  })
}

const resetPassword = async () => {
  if (!passwordFormRef.value) return

  await passwordFormRef.value.validate(async (valid) => {
    if (valid) {
      resetting.value = true
      try {
        await request.post('/auth/reset-password', {
          phone: phoneForm.phone,
          verifyCode: phoneForm.verifyCode,
          newPassword: passwordForm.newPassword
        })
        currentStep.value = 2
        ElMessage.success('密码重置成功')
      } catch (error) {
        console.error(error)
      } finally {
        resetting.value = false
      }
    }
  })
}

const goToLogin = () => {
  router.push('/login')
}
</script>

<style scoped>
.forgot-container {
  width: 100%;
  min-height: 100vh;
  position: relative;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}

.forgot-bg {
  position: absolute;
  inset: 0;
  z-index: 0;
}

.bg-gradient {
  position: absolute;
  inset: 0;
  background: linear-gradient(135deg, #0a2218 0%, #0d3d2a 25%, #0f5239 50%, #0d3d2a 75%, #0a2218 100%);
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
}

.shape-2 {
  width: 300px;
  height: 300px;
  background: radial-gradient(circle, #059669, transparent);
  bottom: -80px;
  right: 10%;
}

.shape-3 {
  width: 250px;
  height: 250px;
  background: radial-gradient(circle, #16c784, transparent);
  top: 40%;
  right: -80px;
}

@keyframes float {
  0%, 100% { transform: translate(0, 0) scale(1); }
  50% { transform: translate(30px, -30px) scale(1.1); }
}

.forgot-content {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 460px;
  padding: 40px 20px;
}

.forgot-box {
  background: rgba(255, 255, 255, 0.98);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.forgot-header {
  margin-bottom: 32px;
}

.back-link {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  color: #606266;
  text-decoration: none;
  font-size: 14px;
  margin-bottom: 16px;
  transition: color 0.2s;
}

.back-link:hover {
  color: #16c784;
}

.forgot-header h2 {
  font-size: 28px;
  font-weight: 700;
  color: #1a1a1a;
  margin-bottom: 8px;
}

.forgot-header p {
  font-size: 14px;
  color: #909399;
}

.steps {
  margin-bottom: 32px;
}

.step-content {
  min-height: 200px;
}

.forgot-form :deep(.el-form-item) {
  margin-bottom: 20px;
}

.forgot-form :deep(.el-input__wrapper) {
  padding: 12px 16px;
}

.code-input-group {
  display: flex;
  gap: 12px;
}

.code-input-group .el-input {
  flex: 1;
}

.code-input-group .el-button {
  width: 120px;
}

.submit-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 10px;
  background: linear-gradient(135deg, #16c784 0%, #059669 100%);
  border: none;
}

.success-content {
  text-align: center;
  padding: 20px 0;
}
</style>
