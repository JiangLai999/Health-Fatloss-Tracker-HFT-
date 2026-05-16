<template>
  <div class="body-data">

    <!-- 顶部指标卡 -->
    <el-row :gutter="16" style="margin-bottom: 16px;">
      <el-col :span="6">
        <div class="metric-card metric-card--green">
          <div class="metric-icon-wrap">
            <el-icon><Odometer /></el-icon>
          </div>
          <div class="metric-info">
            <div class="metric-label">当前体重</div>
            <div class="metric-value">
              {{ currentMetrics.weight || '--' }}
              <span class="metric-unit">kg</span>
            </div>
            <div class="metric-sub" v-if="weightChange !== null">
              <el-icon v-if="weightChange < 0" style="color:#16c784"><CaretBottom /></el-icon>
              <el-icon v-else style="color:#f97316"><CaretTop /></el-icon>
              {{ Math.abs(weightChange).toFixed(1) }} kg
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="metric-card metric-card--blue">
          <div class="metric-icon-wrap">
            <el-icon><DataAnalysis /></el-icon>
          </div>
          <div class="metric-info">
            <div class="metric-label">BMI 指数</div>
            <div class="metric-value">{{ currentMetrics.bmi || '--' }}</div>
            <div class="metric-sub">
              <el-tag size="small" :type="bmiTagType">{{ bmiLabel }}</el-tag>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="metric-card metric-card--orange">
          <div class="metric-icon-wrap">
            <el-icon><TrendCharts /></el-icon>
          </div>
          <div class="metric-info">
            <div class="metric-label">体脂率</div>
            <div class="metric-value">
              {{ currentMetrics.bodyFatRate || '--' }}
              <span class="metric-unit">%</span>
            </div>
            <div class="metric-sub">目标 &lt; 20%</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="metric-card metric-card--purple">
          <div class="metric-icon-wrap">
            <el-icon><Histogram /></el-icon>
          </div>
          <div class="metric-info">
            <div class="metric-label">肌肉量</div>
            <div class="metric-value">
              {{ currentMetrics.muscleMass || '--' }}
              <span class="metric-unit">kg</span>
            </div>
            <div class="metric-sub">水分 {{ currentMetrics.waterContent || '--' }}%</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 体重趋势图 -->
    <el-card style="margin-bottom: 16px;">
      <template #header>
        <div class="card-header">
          <span class="card-title">体重趋势</span>
          <div class="header-actions">
            <el-radio-group v-model="chartDays" size="small" @change="loadData">
              <el-radio-button :label="7">7天</el-radio-button>
              <el-radio-button :label="30">30天</el-radio-button>
              <el-radio-button :label="90">90天</el-radio-button>
            </el-radio-group>
          </div>
        </div>
      </template>
      <div ref="weightTrendChart" style="height: 260px;"></div>
    </el-card>

    <!-- 数据记录表 -->
    <el-card>
      <template #header>
        <div class="card-header">
          <span class="card-title">身体数据记录</span>
          <div class="header-actions">
            <el-date-picker
              v-model="dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              size="small"
              style="width: 260px;"
              @change="loadData"
            />
            <el-button type="primary" size="small" @click="showAddDialog">
              <el-icon><Plus /></el-icon> 添加记录
            </el-button>
          </div>
        </div>
      </template>

      <el-table :data="bodyDataList" style="width: 100%;" :stripe="true" class="clean-table">
        <el-table-column prop="recordDate" label="日期" width="120" />
        <el-table-column prop="weight" label="体重(kg)" width="100">
          <template #default="{ row }">
            <span class="data-highlight">{{ row.weight }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="bmi" label="BMI" width="90">
          <template #default="{ row }">
            <el-tag size="small" :type="getBmiTagType(row.bmi)">{{ row.bmi }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="bodyFatRate" label="体脂率(%)" width="110" />
        <el-table-column prop="muscleMass" label="肌肉量(kg)" width="110" />
        <el-table-column prop="waterContent" label="水分(%)" width="100" />
        <el-table-column prop="note" label="备注" show-overflow-tooltip />
        <el-table-column label="操作" width="80" fixed="right">
          <template #default="{ row }">
            <el-button type="danger" size="small" plain @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加记录弹窗 -->
    <el-dialog v-model="dialogVisible" title="添加身体数据" width="480px" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px" class="dialog-form">
        <el-form-item label="记录日期" prop="recordDate">
          <el-date-picker
            v-model="form.recordDate"
            type="date"
            placeholder="选择日期"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="体重(kg)" prop="weight">
          <el-input-number v-model="form.weight" :precision="1" :min="20" :max="300" :step="0.1" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="体脂率(%)">
          <el-input-number v-model="form.bodyFatRate" :precision="1" :min="1" :max="60" :step="0.1" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="肌肉量(kg)">
          <el-input-number v-model="form.muscleMass" :precision="1" :min="0" :step="0.1" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="水分(%)">
          <el-input-number v-model="form.waterContent" :precision="1" :min="0" :max="100" :step="0.1" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.note" type="textarea" :rows="2" placeholder="可选备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定添加</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Odometer, DataAnalysis, TrendCharts, Histogram, Plus } from '@element-plus/icons-vue'
import request from '@/utils/request'
import dayjs from 'dayjs'
import * as echarts from 'echarts'

const bodyDataList = ref([])
const dialogVisible = ref(false)
const formRef = ref(null)
const dateRange = ref([])
const weightTrendChart = ref(null)
const chartDays = ref(30)
let chartInstance = null

const currentMetrics = computed(() => bodyDataList.value[0] || {})

const weightChange = computed(() => {
  if (bodyDataList.value.length < 2) return null
  return bodyDataList.value[0].weight - bodyDataList.value[1].weight
})

const bmiLabel = computed(() => {
  const bmi = currentMetrics.value.bmi
  if (!bmi) return '--'
  if (bmi < 18.5) return '偏瘦'
  if (bmi < 24) return '正常'
  if (bmi < 28) return '偏重'
  return '肥胖'
})

const bmiTagType = computed(() => {
  const bmi = currentMetrics.value.bmi
  if (!bmi) return 'info'
  if (bmi < 18.5) return 'warning'
  if (bmi < 24) return 'success'
  if (bmi < 28) return 'warning'
  return 'danger'
})

const getBmiTagType = (bmi) => {
  if (!bmi) return 'info'
  if (bmi < 18.5) return 'warning'
  if (bmi < 24) return 'success'
  if (bmi < 28) return 'warning'
  return 'danger'
}

const form = reactive({
  recordDate: '',
  weight: null,
  bodyFatRate: null,
  muscleMass: null,
  waterContent: null,
  note: ''
})

const rules = {
  recordDate: [{ required: true, message: '请选择日期', trigger: 'change' }],
  weight: [{ required: true, message: '请输入体重', trigger: 'blur' }]
}

const loadData = async () => {
  try {
    const params = {}
    if (dateRange.value?.length === 2) {
      params.startDate = dayjs(dateRange.value[0]).format('YYYY-MM-DD')
      params.endDate = dayjs(dateRange.value[1]).format('YYYY-MM-DD')
    } else {
      params.startDate = dayjs().subtract(chartDays.value, 'day').format('YYYY-MM-DD')
      params.endDate = dayjs().format('YYYY-MM-DD')
    }
    const res = await request.get('/body', { params })
    bodyDataList.value = (res.data || []).map(item => ({
      ...item,
      recordDate: dayjs(item.recordDate).format('YYYY-MM-DD')
    }))
    await nextTick()
    initChart()
  } catch (error) {
    console.error(error)
  }
}

const initChart = () => {
  if (!weightTrendChart.value) return
  const chartData = bodyDataList.value.slice().reverse()
  const dates = chartData.map(item => item.recordDate)
  const weights = chartData.map(item => item.weight)
  const fatRates = chartData.map(item => item.bodyFatRate)

  if (!chartInstance) {
    chartInstance = echarts.init(weightTrendChart.value)
  }

  chartInstance.setOption({
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(15,23,42,0.9)',
      borderColor: 'transparent',
      borderRadius: 10,
      textStyle: { color: '#f8fafc', fontSize: 12 },
      padding: [10, 14]
    },
    legend: {
      top: 0, right: 0,
      textStyle: { color: '#64748b', fontSize: 11 },
      itemWidth: 14, itemHeight: 8
    },
    grid: { top: 36, right: 20, bottom: 30, left: 50, containLabel: true },
    xAxis: {
      type: 'category',
      data: dates,
      axisLine: { lineStyle: { color: '#e2e8f0' } },
      axisLabel: { color: '#94a3b8', fontSize: 11 }
    },
    yAxis: [
      {
        type: 'value', name: '体重(kg)',
        nameTextStyle: { color: '#94a3b8', fontSize: 11 },
        axisLine: { show: false },
        splitLine: { lineStyle: { color: '#f1f5f9', type: 'dashed' } },
        axisLabel: { color: '#94a3b8', fontSize: 11 },
        scale: true
      },
      {
        type: 'value', name: '体脂率(%)',
        nameTextStyle: { color: '#94a3b8', fontSize: 11 },
        axisLine: { show: false },
        splitLine: { show: false },
        axisLabel: { color: '#94a3b8', fontSize: 11 },
        scale: true
      }
    ],
    series: [
      {
        name: '体重',
        type: 'line',
        yAxisIndex: 0,
        data: weights,
        smooth: 0.4,
        symbol: 'circle', symbolSize: 5,
        itemStyle: { color: '#16c784', borderWidth: 2, borderColor: '#fff' },
        lineStyle: { width: 3, color: '#16c784' },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(22,199,132,0.2)' },
            { offset: 1, color: 'rgba(22,199,132,0)' }
          ])
        }
      },
      {
        name: '体脂率',
        type: 'line',
        yAxisIndex: 1,
        data: fatRates,
        smooth: 0.4,
        symbol: 'circle', symbolSize: 4,
        itemStyle: { color: '#f97316' },
        lineStyle: { width: 2, color: '#f97316', type: 'dashed' }
      }
    ]
  })
}

const showAddDialog = () => {
  form.recordDate = dayjs().format('YYYY-MM-DD')
  form.weight = null
  form.bodyFatRate = null
  form.muscleMass = null
  form.waterContent = null
  form.note = ''
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const data = { ...form, recordDate: dayjs(form.recordDate).format('YYYY-MM-DD') }
        await request.post('/body', data)
        ElMessage.success('添加成功')
        dialogVisible.value = false
        loadData()
      } catch (error) {
        console.error(error)
      }
    }
  })
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这条记录吗？', '提示', { type: 'warning' })
    await request.delete(`/body/${id}`)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

onMounted(() => { loadData() })
</script>

<style scoped>
.body-data { width: 100%; }

.metric-card {
  border-radius: 14px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 14px;
  box-shadow: var(--shadow-md);
  transition: all 0.25s ease;
}

.metric-card:hover {
  transform: translateY(-3px);
  box-shadow: var(--shadow-lg);
}

.metric-card--green { background: linear-gradient(135deg, #ecfdf5, #d1fae5); border: 1px solid #a7f3d0; }
.metric-card--blue  { background: linear-gradient(135deg, #eff6ff, #bfdbfe); border: 1px solid #93c5fd; }
.metric-card--orange{ background: linear-gradient(135deg, #fff7ed, #fed7aa); border: 1px solid #fdba74; }
.metric-card--purple{ background: linear-gradient(135deg, #faf5ff, #e9d5ff); border: 1px solid #c4b5fd; }

.metric-icon-wrap {
  width: 48px; height: 48px;
  border-radius: 12px;
  display: flex; align-items: center; justify-content: center;
  font-size: 22px; flex-shrink: 0;
}

.metric-card--green .metric-icon-wrap  { background: rgba(22,199,132,0.2); color: #059669; }
.metric-card--blue .metric-icon-wrap   { background: rgba(59,130,246,0.2);  color: #2563eb; }
.metric-card--orange .metric-icon-wrap { background: rgba(249,115,22,0.2);  color: #ea580c; }
.metric-card--purple .metric-icon-wrap { background: rgba(168,85,247,0.2);  color: #9333ea; }

.metric-info { flex: 1; }

.metric-label {
  font-size: 12px;
  color: var(--color-text-secondary);
  margin-bottom: 4px;
}

.metric-value {
  font-size: 24px;
  font-weight: 800;
  color: var(--color-text-primary);
  line-height: 1.1;
}

.metric-unit {
  font-size: 12px;
  font-weight: 400;
  color: var(--color-text-secondary);
  margin-left: 2px;
}

.metric-sub {
  font-size: 11px;
  color: var(--color-text-muted);
  margin-top: 4px;
  display: flex;
  align-items: center;
  gap: 2px;
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

.header-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.data-highlight {
  font-weight: 600;
  color: var(--color-primary-dark);
}

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

.dialog-form :deep(.el-form-item) {
  margin-bottom: 18px;
}
</style>
