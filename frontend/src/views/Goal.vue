<template>
  <div class="goal">
    <!-- 顶部统计 -->
    <el-row :gutter="16" style="margin-bottom: 16px;">
      <el-col :span="6" v-for="item in goalSummary" :key="item.label">
        <div class="summary-card" :style="{ borderTopColor: item.color }">
          <div class="summary-num" :style="{ color: item.color }">{{ item.value }}</div>
          <div class="summary-label">{{ item.label }}</div>
        </div>
      </el-col>
    </el-row>

    <el-card>
      <template #header>
        <div class="card-header">
          <span class="card-title">目标管理</span>
          <el-button type="primary" @click="showAddDialog">
            <el-icon><Plus /></el-icon> 添加目标
          </el-button>
        </div>
      </template>

      <el-tabs v-model="activeTab" @tab-change="loadGoals" class="goal-tabs">
        <el-tab-pane label="进行中" name="0" />
        <el-tab-pane label="已完成" name="1" />
        <el-tab-pane label="已放弃" name="2" />
        <el-tab-pane label="全部" name="all" />
      </el-tabs>

      <div v-if="goalList.length === 0" class="empty-state">
        <div class="empty-icon-wrap">
          <el-icon><Flag /></el-icon>
        </div>
        <p class="empty-title">暂无目标</p>
        <p class="empty-sub">点击"添加目标"开始设定您的减脂计划</p>
        <el-button type="primary" @click="showAddDialog" style="margin-top: 16px;">立即设定目标</el-button>
      </div>

      <el-row :gutter="16" style="margin-top: 16px;" v-else>
        <el-col :span="8" v-for="goal in goalList" :key="goal.id" style="margin-bottom: 16px;">
          <div class="goal-card" :class="'goal-card--' + getStatusKey(goal.status)">
            <!-- 卡片顶部 -->
            <div class="goal-card-top">
              <div class="goal-tags">
                <el-tag :type="getGoalTypeColor(goal.goalType)" size="small">{{ getGoalTypeText(goal.goalType) }}</el-tag>
                <el-tag :type="getStatusColor(goal.status)" size="small">{{ getStatusText(goal.status) }}</el-tag>
              </div>
              <div class="goal-target-display">
                <span class="goal-target-num">{{ goal.targetValue }}</span>
                <span class="goal-target-unit">{{ getGoalUnit(goal.goalType) }}</span>
              </div>
              <div class="goal-target-hint">目标值</div>
            </div>

            <!-- 卡片主体 -->
            <div class="goal-card-body">
              <div class="goal-meta-grid">
                <div class="goal-meta-item">
                  <span class="meta-label">起始值</span>
                  <span class="meta-value">{{ goal.startValue || '--' }} {{ getGoalUnit(goal.goalType) }}</span>
                </div>
                <div class="goal-meta-item">
                  <span class="meta-label">开始日期</span>
                  <span class="meta-value">{{ goal.startDate }}</span>
                </div>
                <div class="goal-meta-item">
                  <span class="meta-label">截止日期</span>
                  <span class="meta-value">{{ goal.endDate }}</span>
                </div>
                <div class="goal-meta-item" v-if="goal.note">
                  <span class="meta-label">备注</span>
                  <span class="meta-value note-text">{{ goal.note }}</span>
                </div>
              </div>
            </div>

            <!-- 卡片底部 -->
            <div class="goal-card-footer">
              <el-button type="primary" link size="small" @click="viewProgress(goal.id)">
                <el-icon><DataAnalysis /></el-icon> 查看进度
              </el-button>
              <div class="goal-actions">
                <el-button v-if="goal.status === 0" type="success" size="small" @click="updateStatus(goal.id, 1)">完成</el-button>
                <el-button v-if="goal.status === 0" type="warning" size="small" plain @click="updateStatus(goal.id, 2)">放弃</el-button>
                <el-button type="danger" size="small" plain @click="handleDelete(goal.id)">删除</el-button>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 添加目标弹窗 -->
    <el-dialog v-model="dialogVisible" title="添加减脂目标" width="500px" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="目标类型" prop="goalType">
          <el-radio-group v-model="form.goalType">
            <el-radio-button label="weight">减重目标</el-radio-button>
            <el-radio-button label="body_fat">减脂目标</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="目标值" prop="targetValue">
          <el-input-number
            v-model="form.targetValue"
            :precision="1"
            :min="0"
            style="width: 100%;"
            :placeholder="form.goalType === 'weight' ? '目标体重(kg)' : '目标体脂率(%)'"
          />
        </el-form-item>
        <el-form-item label="起始值">
          <el-input-number
            v-model="form.startValue"
            :precision="1"
            :min="0"
            style="width: 100%;"
            :placeholder="form.goalType === 'weight' ? '当前体重(kg)' : '当前体脂率(%)'"
          />
        </el-form-item>
        <el-row :gutter="12">
          <el-col :span="12">
            <el-form-item label="开始日期" prop="startDate">
              <el-date-picker v-model="form.startDate" type="date" placeholder="选择日期" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束日期" prop="endDate">
              <el-date-picker v-model="form.endDate" type="date" placeholder="选择日期" style="width: 100%;" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注">
          <el-input v-model="form.note" type="textarea" :rows="2" placeholder="可选备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定添加</el-button>
      </template>
    </el-dialog>

    <!-- 进度弹窗 -->
    <el-dialog v-model="progressDialogVisible" title="目标进度详情" width="560px">
      <div v-if="progressData.goal" class="progress-dialog">
        <div class="progress-header">
          <el-tag :type="getGoalTypeColor(progressData.goal.goalType)" size="large">
            {{ getGoalTypeText(progressData.goal.goalType) }}
          </el-tag>
          <el-tag :type="getStatusColor(progressData.goal.status)" size="large">
            {{ getStatusText(progressData.goal.status) }}
          </el-tag>
        </div>

        <el-row :gutter="12" class="progress-stats">
          <el-col :span="8">
            <div class="progress-stat-card">
              <div class="progress-stat-val">{{ progressData.goal.startValue || '--' }}</div>
              <div class="progress-stat-label">起始值 {{ getGoalUnit(progressData.goal.goalType) }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="progress-stat-card progress-stat-card--highlight">
              <div class="progress-stat-val">{{ progressData.currentValue || '--' }}</div>
              <div class="progress-stat-label">当前值 {{ getGoalUnit(progressData.goal.goalType) }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="progress-stat-card">
              <div class="progress-stat-val">{{ progressData.goal.targetValue }}</div>
              <div class="progress-stat-label">目标值 {{ getGoalUnit(progressData.goal.goalType) }}</div>
            </div>
          </el-col>
        </el-row>

        <div class="progress-section">
          <div class="progress-section-header">
            <span>时间进度</span>
            <span class="progress-section-sub">{{ progressData.passedDays }} / {{ progressData.totalDays }} 天</span>
          </div>
          <el-progress :percentage="progressData.progressPercent || 0" :stroke-width="10" color="#16c784" />
        </div>

        <div class="progress-section" v-if="progressData.valueProgress !== undefined">
          <div class="progress-section-header">
            <span>目标完成度</span>
            <span class="progress-section-sub">已变化 {{ progressData.currentChange || 0 }} {{ getGoalUnit(progressData.goal.goalType) }}</span>
          </div>
          <el-progress
            :percentage="Math.min(progressData.valueProgress || 0, 100)"
            :stroke-width="10"
            :color="(progressData.valueProgress || 0) >= 100 ? '#16c784' : '#3b82f6'"
          />
        </div>

        <el-descriptions :column="2" border size="small" style="margin-top: 20px;">
          <el-descriptions-item label="开始日期">{{ progressData.goal.startDate }}</el-descriptions-item>
          <el-descriptions-item label="结束日期">{{ progressData.goal.endDate }}</el-descriptions-item>
          <el-descriptions-item label="总天数">{{ progressData.totalDays }} 天</el-descriptions-item>
          <el-descriptions-item label="已过天数">{{ progressData.passedDays }} 天</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Flag, Plus, DataAnalysis } from '@element-plus/icons-vue'
import request from '@/utils/request'
import dayjs from 'dayjs'

const goalList = ref([])
const allGoals = ref([])
const dialogVisible = ref(false)
const progressDialogVisible = ref(false)
const activeTab = ref('0')
const formRef = ref(null)
const progressData = ref({})

const goalSummary = computed(() => [
  { label: '进行中', value: allGoals.value.filter(g => g.status === 0).length, color: '#16c784' },
  { label: '已完成', value: allGoals.value.filter(g => g.status === 1).length, color: '#3b82f6' },
  { label: '已放弃', value: allGoals.value.filter(g => g.status === 2).length, color: '#94a3b8' },
  { label: '全部目标', value: allGoals.value.length, color: '#a855f7' }
])

const form = reactive({
  goalType: 'weight',
  targetValue: null,
  startValue: null,
  startDate: '',
  endDate: '',
  note: ''
})

const rules = {
  goalType: [{ required: true, message: '请选择目标类型', trigger: 'change' }],
  targetValue: [{ required: true, message: '请输入目标值', trigger: 'blur' }],
  startDate: [{ required: true, message: '请选择开始日期', trigger: 'change' }],
  endDate: [{ required: true, message: '请选择结束日期', trigger: 'change' }]
}

const goalTypeMap = { weight: '减重', body_fat: '减脂' }
const goalUnitMap = { weight: 'kg', body_fat: '%' }
const statusMap = { 0: '进行中', 1: '已完成', 2: '已放弃' }
const statusKeyMap = { 0: 'active', 1: 'done', 2: 'abandoned' }

const getGoalTypeText = (type) => goalTypeMap[type] || type
const getGoalUnit = (type) => goalUnitMap[type] || ''
const getStatusText = (status) => statusMap[status] || '未知'
const getStatusKey = (status) => statusKeyMap[status] || 'active'
const getGoalTypeColor = (type) => ({ weight: 'primary', body_fat: 'success' }[type] || 'info')
const getStatusColor = (status) => ({ 0: 'primary', 1: 'success', 2: 'info' }[status] || 'info')

const loadGoals = async () => {
  try {
    const [allRes, currentRes] = await Promise.all([
      request.get('/goal', { params: {} }),
      activeTab.value === 'all' 
        ? request.get('/goal', { params: {} })
        : request.get('/goal', { params: { status: activeTab.value } })
    ])
    
    allGoals.value = (allRes.data || []).map(item => ({
      ...item,
      startDate: dayjs(item.startDate).format('YYYY-MM-DD'),
      endDate: dayjs(item.endDate).format('YYYY-MM-DD')
    }))
    
    goalList.value = (currentRes.data || []).map(item => ({
      ...item,
      startDate: dayjs(item.startDate).format('YYYY-MM-DD'),
      endDate: dayjs(item.endDate).format('YYYY-MM-DD')
    }))
  } catch (error) {
    console.error(error)
  }
}

const showAddDialog = () => {
  Object.assign(form, {
    goalType: 'weight', targetValue: null, startValue: null,
    startDate: dayjs().format('YYYY-MM-DD'), endDate: '', note: ''
  })
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const data = {
          ...form,
          startDate: dayjs(form.startDate).format('YYYY-MM-DD'),
          endDate: dayjs(form.endDate).format('YYYY-MM-DD')
        }
        await request.post('/goal', data)
        ElMessage.success('添加成功')
        dialogVisible.value = false
        loadGoals()
      } catch (error) {
        console.error(error)
      }
    }
  })
}

const viewProgress = async (goalId) => {
  try {
    const res = await request.get(`/goal/${goalId}/progress`)
    progressData.value = res.data || {}
    progressDialogVisible.value = true
  } catch (error) {
    console.error(error)
  }
}

const updateStatus = async (goalId, status) => {
  try {
    const statusText = status === 1 ? '完成' : '放弃'
    await ElMessageBox.confirm(`确定要${statusText}这个目标吗？`, '提示', { type: 'warning' })
    await request.put(`/goal/${goalId}/status`, null, { params: { status } })
    ElMessage.success(`${statusText}成功`)
    loadGoals()
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

const handleDelete = async (goalId) => {
  try {
    await ElMessageBox.confirm('确定要删除这个目标吗？', '提示', { type: 'warning' })
    await request.delete(`/goal/${goalId}`)
    ElMessage.success('删除成功')
    loadGoals()
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

onMounted(() => { loadGoals() })
</script>

<style scoped>
.goal { width: 100%; }

/* 汇总卡片 */
.summary-card {
  background: white;
  border-radius: 12px;
  padding: 18px 20px;
  border-top: 3px solid;
  box-shadow: var(--shadow-md);
  text-align: center;
  transition: all 0.25s ease;
}

.summary-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-lg);
}

.summary-num {
  font-size: 32px;
  font-weight: 800;
  line-height: 1;
  margin-bottom: 6px;
}

.summary-label {
  font-size: 13px;
  color: var(--color-text-secondary);
}

/* 目标卡片 */
.goal-card {
  background: white;
  border-radius: 14px;
  border: 1px solid var(--color-border);
  overflow: hidden;
  box-shadow: var(--shadow-sm);
  transition: all 0.25s ease;
}

.goal-card:hover {
  box-shadow: var(--shadow-lg);
  transform: translateY(-3px);
}

.goal-card--active  { border-top: 3px solid #16c784; }
.goal-card--done    { border-top: 3px solid #3b82f6; opacity: 0.88; }
.goal-card--abandoned { border-top: 3px solid #94a3b8; opacity: 0.7; }

.goal-card-top {
  padding: 16px;
  background: #f8fafc;
  border-bottom: 1px solid var(--color-border-soft);
}

.goal-tags {
  display: flex;
  gap: 6px;
  margin-bottom: 12px;
}

.goal-target-display {
  display: flex;
  align-items: baseline;
  gap: 4px;
}

.goal-target-num {
  font-size: 30px;
  font-weight: 800;
  color: var(--color-text-primary);
}

.goal-target-unit {
  font-size: 14px;
  color: var(--color-text-secondary);
}

.goal-target-hint {
  font-size: 11px;
  color: var(--color-text-muted);
  margin-top: 2px;
}

.goal-card-body {
  padding: 14px 16px;
}

.goal-meta-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
}

.goal-meta-item {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.meta-label {
  font-size: 11px;
  color: var(--color-text-muted);
}

.meta-value {
  font-size: 13px;
  font-weight: 500;
  color: var(--color-text-secondary);
}

.note-text {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.goal-card-footer {
  padding: 10px 16px;
  border-top: 1px solid var(--color-border-soft);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.goal-actions {
  display: flex;
  gap: 6px;
}

/* 空状态 */
.empty-state {
  text-align: center;
  padding: 60px 0;
}

.empty-icon-wrap {
  width: 72px; height: 72px;
  background: #f1f5f9;
  border-radius: 50%;
  display: flex; align-items: center; justify-content: center;
  font-size: 32px; color: #cbd5e1;
  margin: 0 auto 16px;
}

.empty-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--color-text-secondary);
  margin-bottom: 6px;
}

.empty-sub {
  font-size: 13px;
  color: var(--color-text-muted);
}

/* 进度弹窗 */
.progress-dialog { padding: 4px 0; }

.progress-header {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.progress-stats { margin-bottom: 24px; }

.progress-stat-card {
  background: #f8fafc;
  border-radius: 10px;
  padding: 14px;
  text-align: center;
  border: 1px solid var(--color-border);
}

.progress-stat-card--highlight {
  background: #ecfdf5;
  border-color: #a7f3d0;
}

.progress-stat-val {
  font-size: 22px;
  font-weight: 700;
  color: var(--color-text-primary);
}

.progress-stat-label {
  font-size: 12px;
  color: var(--color-text-secondary);
  margin-top: 4px;
}

.progress-section { margin-bottom: 20px; }

.progress-section-header {
  display: flex;
  justify-content: space-between;
  font-size: 14px;
  font-weight: 500;
  color: var(--color-text-primary);
  margin-bottom: 8px;
}

.progress-section-sub {
  font-size: 12px;
  color: var(--color-text-muted);
  font-weight: 400;
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
</style>
