<template>
  <div class="dashboard">

    <!-- ── 顶部欢迎区 ── -->
    <div class="welcome-banner">
      <div class="welcome-text">
        <h2>减脂每一天，健康每一天</h2>
        <p>今天是 {{ today }}，继续保持！加油减脂！</p>
      </div>
      <div class="banner-stats">
        <div class="banner-stat">
          <span class="banner-stat-num" :style="calorieBalance >= 0 ? 'color:#f97316' : 'color:#16c784'">
            {{ Math.abs(calorieBalance) || 0 }}
          </span>
          <span class="banner-stat-label">{{ calorieBalance >= 0 ? '今日热量盈余' : '今日热量缺口' }} kcal</span>
        </div>
      </div>
    </div>

    <!-- ── 四大核心指标 ── -->
    <el-row :gutter="16" class="stat-row">
      <el-col :span="6">
        <div class="stat-card stat-card--green">
          <div class="stat-card-inner">
            <div class="stat-icon-wrap">
              <el-icon><DataLine /></el-icon>
            </div>
            <div class="stat-data">
              <div class="stat-num">{{ bodyStats.currentWeight || '--' }}<span class="stat-unit">kg</span></div>
              <div class="stat-desc">当前体重</div>
              <div class="stat-trend" v-if="bodyStats.weightChange != null">
                <el-icon v-if="bodyStats.weightChange < 0"><CaretBottom /></el-icon>
                <el-icon v-else><CaretTop /></el-icon>
                {{ Math.abs(bodyStats.weightChange).toFixed(1) }} kg 较上次
              </div>
            </div>
          </div>
          <div class="stat-card-bg-icon"><el-icon><DataLine /></el-icon></div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-card--orange">
          <div class="stat-card-inner">
            <div class="stat-icon-wrap">
              <el-icon><Bowl /></el-icon>
            </div>
            <div class="stat-data">
              <div class="stat-num">{{ dietStats.totalCalories || 0 }}<span class="stat-unit">kcal</span></div>
              <div class="stat-desc">今日摄入</div>
              <div class="stat-trend">
                蛋白质 {{ dietStats.totalProtein || 0 }}g
              </div>
            </div>
          </div>
          <div class="stat-card-bg-icon"><el-icon><Bowl /></el-icon></div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-card--blue">
          <div class="stat-card-inner">
            <div class="stat-icon-wrap">
              <el-icon><Stopwatch /></el-icon>
            </div>
            <div class="stat-data">
              <div class="stat-num">{{ exerciseStats.totalCalories || 0 }}<span class="stat-unit">kcal</span></div>
              <div class="stat-desc">今日消耗</div>
              <div class="stat-trend">
                运动 {{ exerciseStats.totalDuration || 0 }} 分钟
              </div>
            </div>
          </div>
          <div class="stat-card-bg-icon"><el-icon><Stopwatch /></el-icon></div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-card--purple">
          <div class="stat-card-inner">
            <div class="stat-icon-wrap">
              <el-icon><Flag /></el-icon>
            </div>
            <div class="stat-data">
              <div class="stat-num">{{ activeGoals }}</div>
              <div class="stat-desc">进行中目标</div>
              <div class="stat-trend">BMI {{ bodyStats.currentBmi || '--' }}</div>
            </div>
          </div>
          <div class="stat-card-bg-icon"><el-icon><Flag /></el-icon></div>
        </div>
      </el-col>
    </el-row>

    <!-- ── 今日热量环形 + 体重趋势 ── -->
    <el-row :gutter="16" style="margin-top: 16px;">
      <el-col :span="8">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span class="card-title">今日热量概览</span>
            </div>
          </template>
          <div class="calorie-overview">
            <div class="calorie-ring-wrap">
              <div ref="todayCalorieChart" style="height: 160px;"></div>
            </div>
            <div class="calorie-summary">
              <div class="calorie-summary-item">
                <div class="cs-label">摄入目标</div>
                <div class="cs-value cs-goal">{{ dailyCalorieTarget }} kcal</div>
              </div>
              <div class="calorie-summary-item">
                <div class="cs-label">实际摄入</div>
                <div class="cs-value" :class="intakeStatusClass">{{ dietStats.totalCalories || 0 }} kcal</div>
              </div>
              <div class="calorie-summary-item">
                <div class="cs-label">运动消耗</div>
                <div class="cs-value cs-burn">{{ exerciseStats.totalCalories || 0 }} kcal</div>
              </div>
              <div class="calorie-summary-divider"></div>
              <div class="calorie-summary-item">
                <div class="cs-label">{{ calorieBalance >= 0 ? '热量盈余' : '热量缺口' }}</div>
                <div class="cs-value" :class="calorieBalance >= 0 ? 'cs-surplus' : 'cs-deficit'">
                  {{ Math.abs(calorieBalance) || 0 }} kcal
                </div>
              </div>
              <div class="calorie-progress-wrap">
                <div class="calorie-progress-label">
                  <span>摄入进度</span>
                  <span>{{ intakePercentage }}%</span>
                </div>
                <el-progress 
                  :percentage="Math.min(intakePercentage, 100)" 
                  :stroke-width="8"
                  :color="intakeProgressColor"
                  :show-text="false"
                />
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="16">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span class="card-title">30天体重趋势</span>
              <el-tag size="small" type="success">近30天</el-tag>
            </div>
          </template>
          <div ref="weightChart" style="height: 240px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- ── 营养分布 + 近7天热量 ── -->
    <el-row :gutter="16" style="margin-top: 16px;">
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <span class="card-title">今日营养分布</span>
          </template>
          <div ref="nutritionChart" style="height: 220px;"></div>
          <el-row :gutter="10" class="nutrition-stats">
            <el-col :span="8">
              <div class="ns-item" style="--c:#16c784">
                <span class="ns-val">{{ dietStats.totalProtein || 0 }}g</span>
                <span class="ns-label">蛋白质</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="ns-item" style="--c:#f97316">
                <span class="ns-val">{{ dietStats.totalCarbohydrate || 0 }}g</span>
                <span class="ns-label">碳水化合物</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="ns-item" style="--c:#a855f7">
                <span class="ns-val">{{ dietStats.totalFat || 0 }}g</span>
                <span class="ns-label">脂肪</span>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <span class="card-title">近7天热量平衡</span>
          </template>
          <div ref="calorieChart" style="height: 240px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- ── 今日记录表格 ── -->
    <el-row :gutter="16" style="margin-top: 16px;">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span class="card-title">今日饮食记录</span>
              <router-link to="/diet" class="view-more">查看全部 →</router-link>
            </div>
          </template>
          <el-empty v-if="!todayDietRecords.length" description="今日暂无饮食记录" :image-size="60" />
          <el-table v-else :data="todayDietRecords" max-height="260" class="clean-table">
            <el-table-column label="餐次" width="70" align="center">
              <template #default="{ row }">
                <el-tag size="small" :type="mealTagType(row.mealType)">{{ getMealTypeText(row.mealType) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="calories" label="热量(kcal)" width="100" align="center" />
            <el-table-column prop="protein" label="蛋白质(g)" width="90" align="center" />
            <el-table-column prop="note" label="备注" show-overflow-tooltip />
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span class="card-title">今日运动记录</span>
              <router-link to="/exercise" class="view-more">查看全部 →</router-link>
            </div>
          </template>
          <el-empty v-if="!todayExerciseRecords.length" description="今日暂无运动记录" :image-size="60" />
          <el-table v-else :data="todayExerciseRecords" max-height="260" class="clean-table">
            <el-table-column prop="exerciseName" label="运动" width="100" align="center" />
            <el-table-column prop="duration" label="时长(min)" width="90" align="center" />
            <el-table-column prop="calories" label="消耗(kcal)" width="100" align="center" />
            <el-table-column prop="note" label="备注" show-overflow-tooltip />
          </el-table>
        </el-card>
      </el-col>
    </el-row>

  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import { useStore } from 'vuex'
import * as echarts from 'echarts'
import request from '@/utils/request'
import dayjs from 'dayjs'

const store = useStore()
const username = computed(() => store.state.username || '用户')
const today = dayjs().format('YYYY年MM月DD日 dddd')

const weightChart = ref(null)
const calorieChart = ref(null)
const todayCalorieChart = ref(null)
const nutritionChart = ref(null)

const bodyStats = ref({})
const dietStats = ref({})
const exerciseStats = ref({})
const activeGoals = ref(0)
const todayDietRecords = ref([])
const todayExerciseRecords = ref([])

const calorieBalance = computed(() => {
  const intake = dietStats.value.totalCalories || 0
  const burn = exerciseStats.value.totalCalories || 0
  const balance = intake - burn
  if (!isFinite(balance)) return 0
  return Math.round(balance)
})

const dailyCalorieTarget = ref(2000)

const intakePercentage = computed(() => {
  if (!dailyCalorieTarget.value || dailyCalorieTarget.value <= 0) return 0
  const pct = ((dietStats.value.totalCalories || 0) / dailyCalorieTarget.value) * 100
  if (!isFinite(pct)) return 0
  return Math.round(pct)
})

const intakeStatusClass = computed(() => {
  const pct = intakePercentage.value
  if (pct > 120) return 'cs-over'
  if (pct > 100) return 'cs-warning'
  return 'cs-normal'
})

const intakeProgressColor = computed(() => {
  const pct = intakePercentage.value
  if (pct > 120) return '#ef4444'
  if (pct > 100) return '#f97316'
  return '#16c784'
})

const mealTypeMap = { breakfast: '早餐', lunch: '午餐', dinner: '晚餐', snack: '加餐' }
const getMealTypeText = (t) => mealTypeMap[t] || t
const mealTagType = (t) => ({ breakfast: 'warning', lunch: '', dinner: 'danger', snack: 'success' }[t] || 'info')

// Common echart style
const axisStyle = {
  axisLine: { lineStyle: { color: '#e2e8f0' } },
  axisLabel: { color: '#94a3b8', fontSize: 11 },
  splitLine: { lineStyle: { color: '#f1f5f9', type: 'dashed' } }
}

const tooltipStyle = {
  trigger: 'axis',
  backgroundColor: 'rgba(15, 23, 42, 0.9)',
  borderColor: 'transparent',
  borderRadius: 10,
  textStyle: { color: '#f8fafc', fontSize: 12 },
  padding: [10, 14]
}

const loadData = async () => {
  try {
    const today = dayjs().format('YYYY-MM-DD')

    const [bodyRes, dietRes, exerciseRes, goalRes, dietRecordsRes, exerciseRecordsRes, userRes] = await Promise.all([
      request.get('/body/statistics'),
      request.get(`/diet/statistics?startDate=${today}&endDate=${today}`),
      request.get(`/exercise/statistics?startDate=${today}&endDate=${today}`),
      request.get('/goal?status=0'),
      request.get(`/diet/record?recordDate=${today}`),
      request.get(`/exercise/record?recordDate=${today}`),
      request.get('/auth/info')
    ])

    bodyStats.value = bodyRes.data || {}
    dietStats.value = dietRes.data || {}
    exerciseStats.value = exerciseRes.data || {}
    activeGoals.value = goalRes.data?.length || 0
    todayDietRecords.value = dietRecordsRes.data || []
    todayExerciseRecords.value = exerciseRecordsRes.data || []

    const user = userRes.data || {}
    if (user.weight && user.height && user.age && user.activityLevel) {
      const weight = Number(user.weight)
      const height = Number(user.height)
      const age = Number(user.age)
      const gender = Number(user.gender)
      const activityLevel = Number(user.activityLevel)
      
      if (weight > 0 && height > 0 && age > 0) {
        const bmr = 10 * weight + 6.25 * height - 5 * age
        const adjustedBmr = gender === 1 ? bmr + 5 : bmr - 161
        const activityFactors = { 1: 1.2, 2: 1.375, 3: 1.55, 4: 1.725, 5: 1.9 }
        const factor = activityFactors[activityLevel] || 1.2
        dailyCalorieTarget.value = Math.round(adjustedBmr * factor)
      }
    }

    await nextTick()
    initCharts()
  } catch (error) {
    console.error(error)
  }
}

const initCharts = async () => {
  try {
    const startDate = dayjs().subtract(30, 'day').format('YYYY-MM-DD')
    const endDate = dayjs().format('YYYY-MM-DD')

    const bodyDataRes = await request.get(`/body?startDate=${startDate}&endDate=${endDate}`)
    const bodyData = (bodyDataRes.data || []).slice().reverse()
    const weightData = bodyData.map(item => [dayjs(item.recordDate).format('MM/DD'), item.weight])

    // ── 体重趋势 ──
    const wc = echarts.init(weightChart.value)
    wc.setOption({
      tooltip: { ...tooltipStyle, formatter: (p) => `${p[0].axisValue}<br/>体重：<b>${p[0].value[1]} kg</b>` },
      grid: { top: 20, right: 20, bottom: 30, left: 50, containLabel: true },
      xAxis: { type: 'category', ...axisStyle, boundaryGap: false },
      yAxis: {
        type: 'value', name: 'kg', nameTextStyle: { color: '#94a3b8', fontSize: 11 },
        ...axisStyle, scale: true
      },
      series: [{
        type: 'line',
        data: weightData,
        smooth: 0.4,
        symbol: 'circle', symbolSize: 5,
        itemStyle: { color: '#16c784', borderWidth: 2, borderColor: '#fff' },
        lineStyle: { width: 3, color: '#16c784' },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(22,199,132,0.25)' },
            { offset: 1, color: 'rgba(22,199,132,0)' }
          ])
        }
      }]
    })

    // ── 今日热量环形图 ──
    const intake = dietStats.value.totalCalories || 0
    const burn = exerciseStats.value.totalCalories || 0
    const tc = echarts.init(todayCalorieChart.value)
    tc.setOption({
      tooltip: { trigger: 'item', backgroundColor: 'rgba(15,23,42,0.9)', borderColor: 'transparent', textStyle: { color: '#f8fafc' } },
      series: [{
        type: 'pie',
        radius: ['58%', '82%'],
        center: ['50%', '50%'],
        avoidLabelOverlap: false,
        itemStyle: { borderRadius: 6, borderColor: '#fff', borderWidth: 2 },
        label: { show: false },
        emphasis: { label: { show: false } },
        data: [
          { value: intake, name: '摄入热量', itemStyle: { color: intake > dailyCalorieTarget.value ? '#fb923c' : '#f97316' } },
          { value: burn, name: '运动消耗', itemStyle: { color: '#16c784' } }
        ]
      }]
    })

    // ── 今日营养 ──
    const protein = dietStats.value.totalProtein || 0
    const carbs = dietStats.value.totalCarbohydrate || 0
    const fat = dietStats.value.totalFat || 0
    const nc = echarts.init(nutritionChart.value)
    nc.setOption({
      tooltip: { trigger: 'item', backgroundColor: 'rgba(15,23,42,0.9)', borderColor: 'transparent', textStyle: { color: '#f8fafc' }, formatter: '{b}: {c}g ({d}%)' },
      legend: { show: false },
      series: [{
        type: 'pie',
        radius: ['40%', '68%'],
        center: ['50%', '50%'],
        itemStyle: { borderRadius: 6, borderColor: '#fff', borderWidth: 2 },
        label: {
          formatter: '{b}\n{c}g',
          fontSize: 11,
          color: '#475569'
        },
        data: [
          { value: protein, name: '蛋白质', itemStyle: { color: '#16c784' } },
          { value: carbs, name: '碳水', itemStyle: { color: '#f97316' } },
          { value: fat, name: '脂肪', itemStyle: { color: '#a855f7' } }
        ]
      }]
    })

    // ── 近7天热量平衡 ──
    const last7Days = []
    const intakeArr = []
    const burnArr = []
    const balanceArr = []

    for (let i = 6; i >= 0; i--) {
      const d = dayjs().subtract(i, 'day')
      const ds = d.format('YYYY-MM-DD')
      last7Days.push(d.format('MM/DD'))

      const [dr, er] = await Promise.all([
        request.get(`/diet/statistics?startDate=${ds}&endDate=${ds}`).catch(() => ({ data: {} })),
        request.get(`/exercise/statistics?startDate=${ds}&endDate=${ds}`).catch(() => ({ data: {} }))
      ])
      const din = dr.data?.totalCalories || 0
      const dex = er.data?.totalCalories || 0
      intakeArr.push(din)
      burnArr.push(dex)
      balanceArr.push(din - dex)
    }

    const cc = echarts.init(calorieChart.value)
    cc.setOption({
      tooltip: {
        ...tooltipStyle,
        trigger: 'axis',
        formatter: (params) => {
          let s = `<b>${params[0].axisValue}</b><br/>`
          params.forEach(p => { s += `${p.marker}${p.seriesName}：${p.value} kcal<br/>` })
          return s
        }
      },
      legend: {
        top: 0,
        right: 0,
        textStyle: { color: '#64748b', fontSize: 11 },
        itemWidth: 14, itemHeight: 8
      },
      grid: { top: 36, right: 20, bottom: 30, left: 50, containLabel: true },
      xAxis: { type: 'category', data: last7Days, ...axisStyle },
      yAxis: { type: 'value', name: 'kcal', nameTextStyle: { color: '#94a3b8', fontSize: 11 }, ...axisStyle },
      series: [
        {
          name: '摄入热量',
          type: 'bar',
          data: intakeArr,
          barMaxWidth: 24,
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#fb923c' }, { offset: 1, color: '#f97316' }
            ]),
            borderRadius: [4, 4, 0, 0]
          }
        },
        {
          name: '运动消耗',
          type: 'bar',
          data: burnArr,
          barMaxWidth: 24,
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#34d399' }, { offset: 1, color: '#16c784' }
            ]),
            borderRadius: [4, 4, 0, 0]
          }
        },
        {
          name: '热量缺口',
          type: 'line',
          data: balanceArr,
          smooth: 0.4,
          symbol: 'circle', symbolSize: 5,
          itemStyle: { color: '#3b82f6' },
          lineStyle: { width: 2, color: '#3b82f6', type: 'dashed' }
        }
      ]
    })
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.dashboard {
  width: 100%;
}

/* ── Welcome Banner ── */
.welcome-banner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 24px;
  background: linear-gradient(135deg, #0d3d2a 0%, #0f5239 60%, #0d3d2a 100%);
  border-radius: 16px;
  margin-bottom: 16px;
  box-shadow: 0 4px 20px rgba(22, 199, 132, 0.2);
  position: relative;
  overflow: hidden;
}

.welcome-banner::before {
  content: '';
  position: absolute;
  right: -20px;
  top: -30px;
  width: 180px;
  height: 180px;
  background: radial-gradient(circle, rgba(22, 199, 132, 0.15) 0%, transparent 70%);
}

.welcome-text h2 {
  font-size: 22px;
  font-weight: 700;
  color: #ffffff;
  margin-bottom: 4px;
}

.welcome-text p {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.6);
}

.banner-stats {
  text-align: right;
}

.banner-stat-num {
  font-size: 36px;
  font-weight: 800;
  display: block;
  line-height: 1;
}

.banner-stat-label {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.55);
  margin-top: 4px;
  display: block;
}

/* ── Stat Cards ── */
.stat-row {
  margin-bottom: 0;
}

.stat-card {
  border-radius: 14px;
  padding: 20px;
  position: relative;
  overflow: hidden;
  box-shadow: var(--shadow-md);
  transition: all 0.25s ease;
  cursor: default;
}

.stat-card:hover {
  transform: translateY(-3px);
  box-shadow: var(--shadow-lg);
}

.stat-card--green {
  background: linear-gradient(135deg, #ecfdf5 0%, #d1fae5 100%);
  border: 1px solid #a7f3d0;
}
.stat-card--orange {
  background: linear-gradient(135deg, #fff7ed 0%, #fed7aa 100%);
  border: 1px solid #fdba74;
}
.stat-card--blue {
  background: linear-gradient(135deg, #eff6ff 0%, #bfdbfe 100%);
  border: 1px solid #93c5fd;
}
.stat-card--purple {
  background: linear-gradient(135deg, #faf5ff 0%, #e9d5ff 100%);
  border: 1px solid #c4b5fd;
}

.stat-card-inner {
  display: flex;
  align-items: center;
  gap: 14px;
  position: relative;
  z-index: 1;
}

.stat-icon-wrap {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22px;
  flex-shrink: 0;
}

.stat-card--green .stat-icon-wrap { background: rgba(22, 199, 132, 0.2); color: #059669; }
.stat-card--orange .stat-icon-wrap { background: rgba(249, 115, 22, 0.2); color: #ea580c; }
.stat-card--blue .stat-icon-wrap { background: rgba(59, 130, 246, 0.2); color: #2563eb; }
.stat-card--purple .stat-icon-wrap { background: rgba(168, 85, 247, 0.2); color: #9333ea; }

.stat-data { flex: 1; }

.stat-num {
  font-size: 26px;
  font-weight: 800;
  color: var(--color-text-primary);
  line-height: 1.1;
}

.stat-unit {
  font-size: 12px;
  font-weight: 400;
  color: var(--color-text-secondary);
  margin-left: 2px;
}

.stat-desc {
  font-size: 12px;
  color: var(--color-text-secondary);
  margin-top: 3px;
}

.stat-trend {
  font-size: 11px;
  color: var(--color-text-muted);
  margin-top: 4px;
  display: flex;
  align-items: center;
  gap: 2px;
}

.stat-card-bg-icon {
  position: absolute;
  right: -8px;
  bottom: -8px;
  font-size: 80px;
  opacity: 0.06;
  z-index: 0;
}

/* ── Chart Cards ── */
.chart-card :deep(.el-card__body) {
  padding: 16px 20px;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.card-title {
  font-size: 15px;
  font-weight: 600;
  color: var(--color-text-primary);
}

.calorie-legend {
  display: flex;
  justify-content: center;
  gap: 24px;
  margin-top: 8px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  color: var(--color-text-secondary);
}

.legend-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  flex-shrink: 0;
}

.calorie-overview {
  display: flex;
  flex-direction: column;
  gap: 4px;
  height: 240px;
}

.calorie-ring-wrap {
  margin: 0 -10px;
  flex: 1;
  min-height: 140px;
}

.calorie-summary {
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding-top: 8px;
}

.calorie-summary-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.cs-label {
  font-size: 12px;
  color: var(--color-text-secondary);
}

.cs-value {
  font-size: 15px;
  font-weight: 600;
}

.cs-goal { color: #64748b; }
.cs-burn { color: #16c784; }
.cs-surplus { color: #f97316; }
.cs-deficit { color: #16c784; }
.cs-normal { color: #16c784; }
.cs-warning { color: #f97316; }
.cs-over { color: #ef4444; }

.calorie-summary-divider {
  height: 1px;
  background: #e2e8f0;
  margin: 4px 0;
}

.calorie-progress-wrap {
  margin-top: 6px;
}

.calorie-progress-label {
  display: flex;
  justify-content: space-between;
  font-size: 11px;
  color: var(--color-text-muted);
  margin-bottom: 6px;
}

/* ── Nutrition Stats ── */
.nutrition-stats {
  margin-top: 12px;
}

.ns-item {
  text-align: center;
  padding: 10px 6px;
  border-radius: 10px;
  background: #f8fafc;
  border-top: 3px solid var(--c);
}

.ns-val {
  display: block;
  font-size: 18px;
  font-weight: 700;
  color: var(--c);
  line-height: 1.2;
}

.ns-label {
  display: block;
  font-size: 11px;
  color: var(--color-text-muted);
  margin-top: 3px;
}

/* ── Tables ── */
.clean-table :deep(.el-table__header-wrapper th) {
  background-color: #f8fafc;
  color: var(--color-text-secondary);
  font-weight: 500;
  font-size: 12px;
  border-bottom: 1px solid var(--color-border);
}

.clean-table :deep(.el-table__row:hover td) {
  background-color: #ecfdf5 !important;
}

.view-more {
  font-size: 12px;
  color: var(--color-primary);
  text-decoration: none;
  font-weight: 500;
  transition: color 0.2s;
}

.view-more:hover {
  color: var(--color-primary-dark);
}
</style>
