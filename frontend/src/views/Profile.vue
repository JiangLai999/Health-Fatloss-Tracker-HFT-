<template>
  <div class="profile">
    <el-row :gutter="16">
      <!-- 左侧：个人头像 + 基本信息表单 -->
      <el-col :span="16">
        <!-- 个人信息卡 -->
        <el-card style="margin-bottom: 16px;">
          <template #header>
            <span class="card-title">个人信息</span>
          </template>

          <div class="profile-avatar-row">
            <div class="avatar-wrap">
              <el-avatar :size="80" icon="UserFilled" class="main-avatar" />
              <div class="avatar-badge">{{ genderText }}</div>
            </div>
            <div class="profile-intro">
              <div class="profile-name">{{ displayName }}</div>
              <div class="profile-meta">
                <span v-if="form.nickname">昵称：{{ form.nickname }}</span>
                <span v-if="!form.nickname && username">用户名：{{ username }}</span>
                <span v-if="form.age">· {{ form.age }}岁</span>
                <span v-if="form.height">· {{ form.height }}cm</span>
              </div>
              <div class="profile-tags">
                <el-tag size="small" type="success">{{ activityLevelText }}</el-tag>
                <el-tag size="small" v-if="form.email">已绑定邮箱</el-tag>
              </div>
            </div>
          </div>
        </el-card>

        <!-- 编辑表单 -->
        <el-card>
          <template #header>
            <span class="card-title">编辑信息</span>
          </template>

          <el-form :model="form" :rules="rules" ref="formRef" label-width="90px" class="profile-form">
            <el-row :gutter="16">
              <el-col :span="12">
                <el-form-item label="用户名">
                  <el-input v-model="username" disabled />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="昵称" prop="nickname">
                  <el-input v-model="form.nickname" placeholder="请输入昵称" clearable />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="16">
              <el-col :span="12">
                <el-form-item label="邮箱" prop="email">
                  <el-input v-model="form.email" placeholder="请输入邮箱" clearable />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="手机号" prop="phone">
                  <el-input v-model="form.phone" placeholder="请输入手机号" clearable />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="16">
              <el-col :span="12">
                <el-form-item label="性别">
                  <el-radio-group v-model="form.gender">
                    <el-radio-button :label="1">男</el-radio-button>
                    <el-radio-button :label="2">女</el-radio-button>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="年龄">
                  <el-input-number v-model="form.age" :min="1" :max="150" style="width: 100%;" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="16">
              <el-col :span="12">
                <el-form-item label="身高(cm)">
                  <el-input-number v-model="form.height" :precision="1" :min="50" :max="300" style="width: 100%;" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="体重(kg)">
                  <el-input-number v-model="form.weight" :precision="1" :min="20" :max="500" style="width: 100%;" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="活动水平">
              <el-select v-model="form.activityLevel" style="width: 100%;">
                <el-option label="久坐（很少或没有运动）" :value="1" />
                <el-option label="轻度活动（每周运动1-3天）" :value="2" />
                <el-option label="中度活动（每周运动3-5天）" :value="3" />
                <el-option label="重度活动（每周运动6-7天）" :value="4" />
                <el-option label="极度活动（体力劳动/每天两练）" :value="5" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSubmit" class="save-btn">
                <el-icon><Check /></el-icon> 保存信息
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>

      <!-- 右侧：健康数据面板 -->
      <el-col :span="8">
        <!-- 身体数据统计 -->
        <el-card style="margin-bottom: 16px;">
          <template #header>
            <span class="card-title">当前身体数据</span>
          </template>

          <div class="health-stats">
            <div class="health-stat-item">
              <div class="health-stat-icon" style="background: #ecfdf5; color: #059669;">
                <el-icon><DataLine /></el-icon>
              </div>
              <div class="health-stat-info">
                <div class="health-stat-label">体重</div>
                <div class="health-stat-val">{{ bodyStats.currentWeight || '--' }} <span>kg</span></div>
              </div>
            </div>
            <div class="health-stat-item">
              <div class="health-stat-icon" style="background: #eff6ff; color: #2563eb;">
                <el-icon><DataAnalysis /></el-icon>
              </div>
              <div class="health-stat-info">
                <div class="health-stat-label">BMI</div>
                <div class="health-stat-val">
                  {{ bodyStats.currentBmi || '--' }}
                  <el-tag v-if="bodyStats.currentBmi" size="small" :type="bmiTagType">{{ bmiLabel }}</el-tag>
                </div>
              </div>
            </div>
            <div class="health-stat-item">
              <div class="health-stat-icon" style="background: #fff7ed; color: #ea580c;">
                <el-icon><TrendCharts /></el-icon>
              </div>
              <div class="health-stat-info">
                <div class="health-stat-label">体脂率</div>
                <div class="health-stat-val">{{ bodyStats.currentBodyFatRate || '--' }} <span>%</span></div>
              </div>
            </div>
            <div class="health-stat-item">
              <div class="health-stat-icon" style="background: #faf5ff; color: #9333ea;">
                <el-icon><Histogram /></el-icon>
              </div>
              <div class="health-stat-info">
                <div class="health-stat-label">肌肉量</div>
                <div class="health-stat-val">{{ bodyStats.currentMuscleMass || '--' }} <span>kg</span></div>
              </div>
            </div>
          </div>
        </el-card>

        <!-- 每日热量估算 -->
        <el-card>
          <template #header>
            <span class="card-title">每日热量估算</span>
          </template>
          <div class="calorie-estimate">
            <div class="calorie-ring">
              <div class="calorie-ring-inner">
                <div class="calorie-ring-num">{{ bmr }}</div>
                <div class="calorie-ring-sub">基础代谢</div>
              </div>
            </div>
            <div class="calorie-info">
              <div class="calorie-info-item">
                <span class="calorie-info-label">维持体重</span>
                <span class="calorie-info-val">{{ tdee }} kcal</span>
              </div>
              <div class="calorie-info-item">
                <span class="calorie-info-label">减脂建议摄入</span>
                <span class="calorie-info-val" style="color: #16c784;">{{ tdeeReduced }} kcal</span>
              </div>
              <div class="calorie-info-tip">
                减少摄入 {{ tdee - tdeeReduced }} kcal/天，约每周减重 0.5kg
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useStore } from 'vuex'
import { ElMessage } from 'element-plus'
import { DataLine, DataAnalysis, TrendCharts, Histogram, Check } from '@element-plus/icons-vue'
import request from '@/utils/request'

const store = useStore()
const formRef = ref(null)
const bodyStats = ref({})

const username = computed(() => store.state.username)
const displayName = computed(() => store.state.nickname || store.state.username || '用户')

const form = reactive({
  nickname: '',
  email: '',
  phone: '',
  gender: 1,
  age: null,
  height: null,
  weight: null,
  activityLevel: 1
})

// 计算属性
const genderText = computed(() => form.gender === 1 ? '男' : '女')

const activityLevelMap = {
  1: '久坐人群',
  2: '轻度活动',
  3: '中度活动',
  4: '重度活动',
  5: '极度活动'
}
const activityLevelText = computed(() => activityLevelMap[form.activityLevel] || '久坐人群')

const bmiLabel = computed(() => {
  const bmi = bodyStats.value.currentBmi
  if (!bmi) return ''
  if (bmi < 18.5) return '偏瘦'
  if (bmi < 24) return '正常'
  if (bmi < 28) return '偏重'
  return '肥胖'
})

const bmiTagType = computed(() => {
  const bmi = bodyStats.value.currentBmi
  if (!bmi) return 'info'
  if (bmi < 18.5) return 'warning'
  if (bmi < 24) return 'success'
  if (bmi < 28) return 'warning'
  return 'danger'
})

// 基础代谢率（Mifflin-St Jeor 公式）
const bmr = computed(() => {
  const { weight, height, age, gender } = form
  if (!weight || !height || !age) return '--'
  const base = 10 * weight + 6.25 * height - 5 * age
  return Math.round(gender === 1 ? base + 5 : base - 161)
})

// 总能量消耗
const activityFactors = { 1: 1.2, 2: 1.375, 3: 1.55, 4: 1.725, 5: 1.9 }
const tdee = computed(() => {
  if (bmr.value === '--') return '--'
  return Math.round(bmr.value * (activityFactors[form.activityLevel] || 1.2))
})

const tdeeReduced = computed(() => {
  if (tdee.value === '--') return '--'
  return Math.round(tdee.value - 500)
})

const rules = {
  email: [{ type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }],
  phone: [{ pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }]
}

const loadUserInfo = async () => {
  try {
    const res = await request.get('/auth/info')
    const user = res.data
    form.nickname = user.nickname || ''
    form.email = user.email || ''
    form.phone = user.phone || ''
    form.gender = user.gender || 1
    form.age = user.age
    form.height = user.height
    form.weight = user.weight
    form.activityLevel = user.activityLevel || 1
  } catch (error) {
    console.error(error)
  }
}

const loadBodyStats = async () => {
  try {
    const res = await request.get('/body/statistics')
    bodyStats.value = res.data || {}
  } catch (error) {
    console.error(error)
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await request.put('/auth/info', form)
        ElMessage.success('保存成功')
        if (form.nickname) {
          store.commit('SET_NICKNAME', form.nickname)
        }
        loadUserInfo()
      } catch (error) {
        console.error(error)
      }
    }
  })
}

onMounted(() => {
  loadUserInfo()
  loadBodyStats()
})
</script>

<style scoped>
.profile { width: 100%; }

.card-title {
  font-size: 15px;
  font-weight: 600;
  color: var(--color-text-primary);
}

/* 头像行 */
.profile-avatar-row {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 8px 0 12px;
}

.avatar-wrap {
  position: relative;
  flex-shrink: 0;
}

.main-avatar {
  background: linear-gradient(135deg, #16c784, #059669) !important;
  font-size: 36px;
}

.avatar-badge {
  position: absolute;
  bottom: -4px;
  right: -4px;
  background: #16c784;
  color: white;
  font-size: 10px;
  font-weight: 600;
  padding: 1px 6px;
  border-radius: 10px;
  border: 2px solid white;
}

.profile-intro { flex: 1; }

.profile-name {
  font-size: 20px;
  font-weight: 700;
  color: var(--color-text-primary);
  margin-bottom: 4px;
}

.profile-meta {
  font-size: 13px;
  color: var(--color-text-secondary);
  margin-bottom: 10px;
}

.profile-tags {
  display: flex;
  gap: 8px;
}

/* 表单 */
.profile-form :deep(.el-form-item) {
  margin-bottom: 18px;
}

.save-btn {
  background: linear-gradient(135deg, #16c784 0%, #059669 100%);
  border: none;
  padding: 10px 28px;
  transition: all 0.3s ease;
}

.save-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 6px 16px rgba(22, 199, 132, 0.35);
}

/* 健康数据 */
.health-stats {
  display: flex;
  flex-direction: column;
  gap: 14px;
  padding: 4px 0;
}

.health-stat-item {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 12px 14px;
  background: #f8fafc;
  border-radius: 10px;
  transition: all 0.2s;
}

.health-stat-item:hover {
  background: #ecfdf5;
}

.health-stat-icon {
  width: 40px; height: 40px;
  border-radius: 10px;
  display: flex; align-items: center; justify-content: center;
  font-size: 18px;
  flex-shrink: 0;
}

.health-stat-info { flex: 1; }

.health-stat-label {
  font-size: 11px;
  color: var(--color-text-muted);
  margin-bottom: 2px;
}

.health-stat-val {
  font-size: 18px;
  font-weight: 700;
  color: var(--color-text-primary);
  display: flex;
  align-items: center;
  gap: 6px;
}

.health-stat-val span {
  font-size: 12px;
  font-weight: 400;
  color: var(--color-text-secondary);
}

/* 热量估算 */
.calorie-estimate {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.calorie-ring {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  background: conic-gradient(#16c784 0deg, #d1fae5 360deg);
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto;
  box-shadow: 0 4px 16px rgba(22, 199, 132, 0.25);
}

.calorie-ring-inner {
  width: 88px;
  height: 88px;
  background: white;
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.calorie-ring-num {
  font-size: 20px;
  font-weight: 800;
  color: var(--color-text-primary);
  line-height: 1;
}

.calorie-ring-sub {
  font-size: 10px;
  color: var(--color-text-muted);
  margin-top: 2px;
}

.calorie-info {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.calorie-info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 14px;
  background: #f8fafc;
  border-radius: 8px;
}

.calorie-info-label {
  font-size: 13px;
  color: var(--color-text-secondary);
}

.calorie-info-val {
  font-size: 15px;
  font-weight: 700;
  color: var(--color-text-primary);
}

.calorie-info-tip {
  font-size: 11px;
  color: var(--color-text-muted);
  text-align: center;
  background: #ecfdf5;
  border-radius: 8px;
  padding: 8px;
  line-height: 1.5;
}
</style>
