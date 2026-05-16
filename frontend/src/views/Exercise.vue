<template>
  <div class="exercise">
    <!-- 顶部运动统计 -->
    <el-row :gutter="16" style="margin-bottom: 16px;">
      <el-col :span="6">
        <div class="stat-card stat-card--blue">
          <div class="stat-icon-wrap"><el-icon><Stopwatch /></el-icon></div>
          <div class="stat-data">
            <div class="stat-num">{{ dayStats.totalCalories || 0 }}<span class="stat-unit">kcal</span></div>
            <div class="stat-desc">今日消耗热量</div>
          </div>
          <div class="stat-bg-icon"><el-icon><Stopwatch /></el-icon></div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-card--green">
          <div class="stat-icon-wrap"><el-icon><Timer /></el-icon></div>
          <div class="stat-data">
            <div class="stat-num">{{ dayStats.totalDuration || 0 }}<span class="stat-unit">min</span></div>
            <div class="stat-desc">今日运动时长</div>
          </div>
          <div class="stat-bg-icon"><el-icon><Timer /></el-icon></div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-card--orange">
          <div class="stat-icon-wrap"><el-icon><TrendCharts /></el-icon></div>
          <div class="stat-data">
            <div class="stat-num">{{ exerciseRecords.length }}</div>
            <div class="stat-desc">今日运动项目</div>
          </div>
          <div class="stat-bg-icon"><el-icon><TrendCharts /></el-icon></div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card stat-card--purple">
          <div class="stat-icon-wrap"><el-icon><Flag /></el-icon></div>
          <div class="stat-data">
            <div class="stat-num">{{ avgIntensity }}<span class="stat-unit">kcal/h</span></div>
            <div class="stat-desc">平均消耗强度</div>
          </div>
          <div class="stat-bg-icon"><el-icon><Flag /></el-icon></div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="16">
      <!-- 运动库 -->
      <el-col :span="10">
        <el-card class="exercise-library-card">
          <template #header>
            <div class="card-header">
              <span class="card-title">运动库</span>
              <el-button type="primary" size="small" @click="showAddExerciseDialog">
                <el-icon><Plus /></el-icon> 添加运动
              </el-button>
            </div>
          </template>

          <el-input
            v-model="exerciseKeyword"
            placeholder="搜索运动"
            @input="loadExerciseList"
            style="margin-bottom: 14px;"
            clearable
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>

          <div class="exercise-grid">
            <div
              v-for="exercise in exerciseList"
              :key="exercise.id"
              class="exercise-item"
              @click="selectExercise(exercise)"
            >
              <div class="exercise-item-header">
                <span class="exercise-name">{{ exercise.name }}</span>
                <el-tag size="small" :type="getCategoryColor(exercise.category)">{{ exercise.category }}</el-tag>
              </div>
              <div class="exercise-meta">
                <span class="exercise-cal">
                  <el-icon><Promotion /></el-icon>
                  {{ exercise.caloriesPerMinute }} kcal/min
                </span>
              </div>
              <div v-if="exercise.description" class="exercise-desc">{{ exercise.description }}</div>
            </div>
          </div>

          <el-pagination
            v-model:current-page="exercisePage"
            :page-size="10"
            :total="exerciseTotal"
            layout="prev, pager, next"
            @current-change="loadExerciseList"
            style="margin-top: 14px; text-align: center;"
            small
          />
        </el-card>
      </el-col>

      <!-- 今日运动记录 -->
      <el-col :span="14">
        <el-card>
          <template #header>
            <div class="card-header">
              <span class="card-title">今日运动记录</span>
              <div class="header-actions">
                <el-date-picker
                  v-model="recordDate"
                  type="date"
                  placeholder="选择日期"
                  @change="loadExerciseRecords"
                  size="small"
                  style="width: 160px;"
                />
                <el-button type="primary" size="small" @click="showAddRecordDialog">
                  <el-icon><Plus /></el-icon> 添加记录
                </el-button>
              </div>
            </div>
          </template>

          <!-- 热量消耗进度 -->
          <div class="calorie-goal-card" v-if="dayStats.totalCalories">
            <div class="calorie-goal-info">
              <span>今日消耗进度</span>
              <span class="calorie-goal-num">{{ dayStats.totalCalories }} / 500 kcal</span>
            </div>
            <el-progress
              :percentage="Math.min((dayStats.totalCalories / 500) * 100, 100)"
              :stroke-width="8"
              color="#16c784"
              :show-text="false"
            />
          </div>

          <el-table :data="exerciseRecords" max-height="380" :stripe="true" class="clean-table">
            <el-table-column prop="exerciseName" label="运动项目" width="90" align="center" />
            <el-table-column prop="category" label="分类" width="80" align="center">
              <template #default="{ row }">
                <el-tag size="small" :type="getCategoryColor(row.category)">{{ row.category || '-' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="强度(kcal/min)" width="110" align="center">
              <template #default="{ row }">
                {{ row.caloriesPerMinute || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="duration" label="时长(min)" width="90" align="center">
              <template #default="{ row }">
                <span class="data-highlight">{{ row.duration }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="calories" label="消耗(kcal)" width="100" align="center">
              <template #default="{ row }">
                <span class="data-highlight">{{ row.calories }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="recordDate" label="日期" width="95" align="center">
              <template #default="{ row }">
                {{ row.recordDate ? row.recordDate.slice(5) : '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="note" label="备注" show-overflow-tooltip />
            <el-table-column label="操作" width="65" align="center" fixed="right">
              <template #default="{ row }">
                <el-button type="danger" size="small" plain @click="handleDeleteRecord(row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!-- 添加运动弹窗 -->
    <el-dialog v-model="exerciseDialogVisible" title="添加运动类型" width="480px" :close-on-click-modal="false">
      <el-form :model="exerciseForm" :rules="exerciseRules" ref="exerciseFormRef" label-width="120px">
        <el-form-item label="运动名称" prop="name">
          <el-input v-model="exerciseForm.name" placeholder="如：跑步" />
        </el-form-item>
        <el-form-item label="分类" prop="category">
          <el-select v-model="exerciseForm.category" style="width: 100%;">
            <el-option label="有氧运动" value="有氧运动" />
            <el-option label="力量训练" value="力量训练" />
            <el-option label="柔韧训练" value="柔韧训练" />
            <el-option label="球类运动" value="球类运动" />
            <el-option label="户外运动" value="户外运动" />
          </el-select>
        </el-form-item>
        <el-form-item label="消耗(kcal/分钟)" prop="caloriesPerMinute">
          <el-input-number v-model="exerciseForm.caloriesPerMinute" :precision="1" :min="0.1" :step="0.5" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="exerciseForm.description" type="textarea" :rows="2" placeholder="运动描述（可选）" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="exerciseDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAddExercise">确定添加</el-button>
      </template>
    </el-dialog>

    <!-- 添加运动记录弹窗 -->
    <el-dialog v-model="recordDialogVisible" title="添加运动记录" width="480px" :close-on-click-modal="false">
      <el-form :model="recordForm" :rules="recordRules" ref="recordFormRef" label-width="100px">
        <el-form-item label="运动类型" prop="exerciseId">
          <el-select v-model="recordForm.exerciseId" placeholder="选择运动" style="width: 100%;" filterable>
            <el-option
              v-for="exercise in exerciseList"
              :key="exercise.id"
              :label="`${exercise.name} (${exercise.caloriesPerMinute} kcal/min)`"
              :value="exercise.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="日期" prop="recordDate">
          <el-date-picker
            v-model="recordForm.recordDate"
            type="date"
            placeholder="选择日期"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="时长(分钟)" prop="duration">
          <el-input-number v-model="recordForm.duration" :min="1" :step="5" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="recordForm.note" type="textarea" :rows="2" placeholder="可选备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="recordDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAddRecord">确定添加</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Stopwatch, Timer, TrendCharts, Flag, Plus, Search, Promotion } from '@element-plus/icons-vue'
import request from '@/utils/request'
import dayjs from 'dayjs'

const exerciseList = ref([])
const exerciseTotal = ref(0)
const exercisePage = ref(1)
const exerciseKeyword = ref('')
const exerciseDialogVisible = ref(false)
const recordDialogVisible = ref(false)
const exerciseRecords = ref([])
const recordDate = ref(dayjs().format('YYYY-MM-DD'))
const dayStats = ref({})
const exerciseFormRef = ref(null)
const recordFormRef = ref(null)

const avgIntensity = computed(() => {
  const dur = dayStats.value.totalDuration
  const cal = dayStats.value.totalCalories
  if (!dur || !cal) return 0
  return Math.round((cal / dur) * 60)
})

const categoryColorMap = {
  有氧运动: 'success',
  力量训练: 'primary',
  柔韧训练: 'warning',
  球类运动: 'danger',
  户外运动: ''
}

const getCategoryColor = (cat) => categoryColorMap[cat] || 'info'

const exerciseForm = reactive({
  name: '',
  category: '',
  caloriesPerMinute: null,
  description: ''
})

const recordForm = reactive({
  exerciseId: null,
  recordDate: '',
  duration: 30,
  note: ''
})

const exerciseRules = {
  name: [{ required: true, message: '请输入运动名称', trigger: 'blur' }],
  category: [{ required: true, message: '请选择分类', trigger: 'change' }],
  caloriesPerMinute: [{ required: true, message: '请输入消耗', trigger: 'blur' }]
}

const recordRules = {
  exerciseId: [{ required: true, message: '请选择运动', trigger: 'change' }],
  recordDate: [{ required: true, message: '请选择日期', trigger: 'change' }],
  duration: [{ required: true, message: '请输入时长', trigger: 'blur' }]
}

const loadExerciseList = async () => {
  try {
    const res = await request.get('/exercise/list', {
      params: { keyword: exerciseKeyword.value, page: exercisePage.value, size: 10 }
    })
    exerciseList.value = res.data?.records || []
    exerciseTotal.value = res.data?.total || 0
  } catch (error) {
    console.error(error)
  }
}

const loadExerciseRecords = async () => {
  try {
    const date = dayjs(recordDate.value).format('YYYY-MM-DD')
    const [recordsRes, statsRes] = await Promise.all([
      request.get(`/exercise/record?recordDate=${date}`),
      request.get(`/exercise/statistics?startDate=${date}&endDate=${date}`)
    ])
    exerciseRecords.value = recordsRes.data || []
    dayStats.value = statsRes.data || {}
  } catch (error) {
    console.error(error)
  }
}

const showAddExerciseDialog = () => {
  Object.assign(exerciseForm, { name: '', category: '', caloriesPerMinute: null, description: '' })
  exerciseDialogVisible.value = true
}

const handleAddExercise = async () => {
  if (!exerciseFormRef.value) return
  await exerciseFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await request.post('/exercise', exerciseForm)
        ElMessage.success('添加成功')
        exerciseDialogVisible.value = false
        loadExerciseList()
      } catch (error) {
        console.error(error)
      }
    }
  })
}

const selectExercise = (exercise) => {
  recordForm.exerciseId = exercise.id
  recordForm.recordDate = dayjs().format('YYYY-MM-DD')
  recordForm.duration = 30
  recordForm.note = ''
  recordDialogVisible.value = true
}

const showAddRecordDialog = () => {
  Object.assign(recordForm, { exerciseId: null, recordDate: dayjs().format('YYYY-MM-DD'), duration: 30, note: '' })
  recordDialogVisible.value = true
}

const handleAddRecord = async () => {
  if (!recordFormRef.value) return
  await recordFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const data = { ...recordForm, recordDate: dayjs(recordForm.recordDate).format('YYYY-MM-DD') }
        await request.post('/exercise/record', data)
        ElMessage.success('添加成功')
        recordDialogVisible.value = false
        loadExerciseRecords()
      } catch (error) {
        console.error(error)
      }
    }
  })
}

const handleDeleteRecord = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这条记录吗？', '提示', { type: 'warning' })
    await request.delete(`/exercise/record/${id}`)
    ElMessage.success('删除成功')
    loadExerciseRecords()
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

onMounted(() => {
  loadExerciseList()
  loadExerciseRecords()
})
</script>

<style scoped>
.exercise { width: 100%; }

/* 统计卡片 */
.stat-card {
  border-radius: 14px;
  padding: 18px;
  position: relative;
  overflow: hidden;
  box-shadow: var(--shadow-md);
  transition: all 0.25s ease;
  display: flex;
  align-items: center;
  gap: 12px;
}

.stat-card:hover {
  transform: translateY(-3px);
  box-shadow: var(--shadow-lg);
}

.stat-card--blue   { background: linear-gradient(135deg, #eff6ff, #bfdbfe); border: 1px solid #93c5fd; }
.stat-card--green  { background: linear-gradient(135deg, #ecfdf5, #d1fae5); border: 1px solid #a7f3d0; }
.stat-card--orange { background: linear-gradient(135deg, #fff7ed, #fed7aa); border: 1px solid #fdba74; }
.stat-card--purple { background: linear-gradient(135deg, #faf5ff, #e9d5ff); border: 1px solid #c4b5fd; }

.stat-icon-wrap {
  width: 46px; height: 46px;
  border-radius: 12px;
  display: flex; align-items: center; justify-content: center;
  font-size: 20px; flex-shrink: 0;
  position: relative; z-index: 1;
}

.stat-card--blue .stat-icon-wrap   { background: rgba(59,130,246,0.2);   color: #2563eb; }
.stat-card--green .stat-icon-wrap  { background: rgba(22,199,132,0.2);   color: #059669; }
.stat-card--orange .stat-icon-wrap { background: rgba(249,115,22,0.2);   color: #ea580c; }
.stat-card--purple .stat-icon-wrap { background: rgba(168,85,247,0.2);   color: #9333ea; }

.stat-data { position: relative; z-index: 1; }

.stat-num {
  font-size: 22px;
  font-weight: 800;
  color: var(--color-text-primary);
  line-height: 1.1;
}

.stat-unit {
  font-size: 11px;
  font-weight: 400;
  color: var(--color-text-secondary);
  margin-left: 2px;
}

.stat-desc {
  font-size: 11px;
  color: var(--color-text-secondary);
  margin-top: 2px;
}

.stat-bg-icon {
  position: absolute;
  right: -6px; bottom: -8px;
  font-size: 72px;
  opacity: 0.05;
}

/* 运动库 */
.exercise-library-card :deep(.el-card__body) {
  padding: 16px 20px;
}

.exercise-grid {
  max-height: 440px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.exercise-item {
  padding: 12px 14px;
  background: #f8fafc;
  border: 1px solid #e2e8f0;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.exercise-item:hover {
  background: #ecfdf5;
  border-color: #a7f3d0;
  transform: translateX(3px);
}

.exercise-item-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 6px;
}

.exercise-name {
  font-size: 14px;
  font-weight: 600;
  color: var(--color-text-primary);
}

.exercise-meta {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  color: var(--color-text-secondary);
  margin-bottom: 4px;
}

.exercise-cal {
  display: flex;
  align-items: center;
  gap: 3px;
  font-weight: 500;
  color: #f97316;
}

.exercise-desc {
  font-size: 11px;
  color: var(--color-text-muted);
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 热量消耗进度 */
.calorie-goal-card {
  background: #f8fafc;
  border-radius: 10px;
  padding: 14px 16px;
  margin-bottom: 14px;
}

.calorie-goal-info {
  display: flex;
  justify-content: space-between;
  font-size: 13px;
  color: var(--color-text-secondary);
  margin-bottom: 8px;
}

.calorie-goal-num {
  font-weight: 600;
  color: var(--color-primary-dark);
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
</style>
