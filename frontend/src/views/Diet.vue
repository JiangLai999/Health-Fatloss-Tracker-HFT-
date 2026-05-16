<template>
  <div class="diet">
    <!-- 顶部营养摄入卡片 -->
    <div class="nutrition-overview">
      <div class="overview-card overview-card--main">
        <div class="overview-icon">
          <el-icon><Bowl /></el-icon>
        </div>
        <div class="overview-content">
          <div class="overview-label">今日总摄入</div>
          <div class="overview-value">{{ dayStats.totalCalories || 0 }} <span class="overview-unit">kcal</span></div>
          <div class="overview-sub">建议 1800-2200 kcal</div>
        </div>
      </div>
      <div class="overview-card overview-card--protein">
        <div class="overview-mini-icon" style="background: rgba(22,199,132,0.15); color: #16c784;">
          <el-icon><Coffee /></el-icon>
        </div>
        <div class="overview-mini-content">
          <div class="overview-mini-label">蛋白质</div>
          <div class="overview-mini-value">{{ dayStats.totalProtein || 0 }}g</div>
          <div class="overview-progress">
            <div class="progress-bar">
              <div class="progress-fill" :style="{ width: Math.min((dayStats.totalProtein || 0) / 80 * 100, 100) + '%', background: '#16c784' }"></div>
            </div>
            <span class="progress-text">目标 80g</span>
          </div>
        </div>
      </div>
      <div class="overview-card overview-card--carb">
        <div class="overview-mini-icon" style="background: rgba(249,115,22,0.15); color: #f97316;">
          <el-icon><Grape /></el-icon>
        </div>
        <div class="overview-mini-content">
          <div class="overview-mini-label">碳水化合物</div>
          <div class="overview-mini-value">{{ dayStats.totalCarbohydrate || 0 }}g</div>
          <div class="overview-progress">
            <div class="progress-bar">
              <div class="progress-fill" :style="{ width: Math.min((dayStats.totalCarbohydrate || 0) / 250 * 100, 100) + '%', background: '#f97316' }"></div>
            </div>
            <span class="progress-text">目标 250g</span>
          </div>
        </div>
      </div>
      <div class="overview-card overview-card--fat">
        <div class="overview-mini-icon" style="background: rgba(168,85,247,0.15); color: #a855f7;">
          <el-icon><Apple /></el-icon>
        </div>
        <div class="overview-mini-content">
          <div class="overview-mini-label">脂肪</div>
          <div class="overview-mini-value">{{ dayStats.totalFat || 0 }}g</div>
          <div class="overview-progress">
            <div class="progress-bar">
              <div class="progress-fill" :style="{ width: Math.min((dayStats.totalFat || 0) / 60 * 100, 100) + '%', background: '#a855f7' }"></div>
            </div>
            <span class="progress-text">目标 60g</span>
          </div>
        </div>
      </div>
    </div>

    <el-row :gutter="16" style="margin-top: 16px;">
      <!-- 食物库 -->
      <el-col :span="10">
        <el-card class="food-library-card">
          <template #header>
            <div class="card-header">
              <span class="card-title">食物库</span>
              <el-button type="primary" size="small" @click="showAddFoodDialog">
                <el-icon><Plus /></el-icon> 添加食物
              </el-button>
            </div>
          </template>

          <el-input
            v-model="foodKeyword"
            placeholder="搜索食物名称"
            @input="loadFoodList"
            style="margin-bottom: 14px;"
            clearable
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>

          <div class="food-grid">
            <div
              v-for="food in foodList"
              :key="food.id"
              class="food-item"
              @click="selectFood(food)"
            >
              <div class="food-item-header">
                <span class="food-name">{{ food.name }}</span>
                <el-tag size="small" type="success">{{ food.category }}</el-tag>
              </div>
              <div class="food-nutrients">
                <span class="nutrient">{{ food.calories }} kcal</span>
                <span class="nutrient-sep">·</span>
                <span class="nutrient">蛋白 {{ food.protein }}g</span>
              </div>
              <div class="food-per">每100g</div>
            </div>
          </div>

          <el-pagination
            v-model:current-page="foodPage"
            :page-size="10"
            :total="foodTotal"
            layout="prev, pager, next"
            @current-change="loadFoodList"
            style="margin-top: 14px; text-align: center;"
            small
          />
        </el-card>
      </el-col>

      <!-- 今日饮食记录 -->
      <el-col :span="14">
        <el-card>
          <template #header>
            <div class="card-header">
              <span class="card-title">今日饮食记录</span>
              <div class="header-actions">
                <el-date-picker
                  v-model="recordDate"
                  type="date"
                  placeholder="选择日期"
                  @change="loadDietRecords"
                  size="small"
                  style="width: 160px;"
                />
                <el-button type="primary" size="small" @click="showAddRecordDialog">
                  <el-icon><Plus /></el-icon> 添加记录
                </el-button>
              </div>
            </div>
          </template>

          <!-- 营养环形图 -->
          <div class="nutrition-chart-section">
            <div ref="nutritionChart" style="height: 200px;"></div>
          </div>

          <!-- 饮食记录表 -->
          <el-table :data="dietRecords" max-height="340" :stripe="true" class="clean-table">
            <el-table-column label="餐次" width="70" align="center">
              <template #default="{ row }">
                <el-tag size="small" :type="mealTagType(row.mealType)">{{ getMealTypeText(row.mealType) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="calories" label="热量(kcal)" width="100" align="center">
              <template #default="{ row }">
                <span class="data-highlight">{{ row.calories }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="protein" label="蛋白质(g)" width="90" align="center" />
            <el-table-column prop="carbohydrate" label="碳水(g)" width="85" align="center" />
            <el-table-column prop="fat" label="脂肪(g)" width="80" align="center" />
            <el-table-column prop="note" label="备注" show-overflow-tooltip />
            <el-table-column label="操作" width="70" align="center" fixed="right">
              <template #default="{ row }">
                <el-button type="danger" size="small" plain @click="handleDeleteRecord(row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!-- 添加食物弹窗 -->
    <el-dialog v-model="foodDialogVisible" title="添加食物" width="480px" :close-on-click-modal="false">
      <el-form :model="foodForm" :rules="foodRules" ref="foodFormRef" label-width="110px">
        <el-form-item label="食物名称" prop="name">
          <el-input v-model="foodForm.name" placeholder="如：鸡胸肉" />
        </el-form-item>
        <el-form-item label="分类" prop="category">
          <el-select v-model="foodForm.category" style="width: 100%;">
            <el-option label="主食" value="主食" />
            <el-option label="肉类" value="肉类" />
            <el-option label="蛋类" value="蛋类" />
            <el-option label="奶类" value="奶类" />
            <el-option label="蔬菜" value="蔬菜" />
            <el-option label="水果" value="水果" />
            <el-option label="鱼类" value="鱼类" />
          </el-select>
        </el-form-item>
        <el-form-item label="热量(kcal/100g)" prop="calories">
          <el-input-number v-model="foodForm.calories" :precision="1" :min="0" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="蛋白质(g/100g)">
          <el-input-number v-model="foodForm.protein" :precision="1" :min="0" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="碳水(g/100g)">
          <el-input-number v-model="foodForm.carbohydrate" :precision="1" :min="0" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="脂肪(g/100g)">
          <el-input-number v-model="foodForm.fat" :precision="1" :min="0" style="width: 100%;" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="foodDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAddFood">确定添加</el-button>
      </template>
    </el-dialog>

    <!-- 添加饮食记录弹窗 -->
    <el-dialog v-model="recordDialogVisible" title="添加饮食记录" width="520px" :close-on-click-modal="false">
      <el-form :model="recordForm" :rules="recordRules" ref="recordFormRef" label-width="100px">
        <el-form-item label="食物" prop="foodIds">
          <el-select v-model="recordForm.foodIds" placeholder="可多选食物" style="width: 100%;" filterable multiple collapse-tags collapse-tags-tooltip>
            <el-option
              v-for="food in foodList"
              :key="food.id"
              :label="`${food.name} (${food.calories}kcal/100g)`"
              :value="food.id"
            />
          </el-select>
          <div v-if="recordForm.foodIds.length > 0" class="selected-foods-tip">已选择 {{ recordForm.foodIds.length }} 种食物，请在下方设置每种食物的数量</div>
        </el-form-item>
        
        <el-form-item v-if="recordForm.foodIds.length > 0" label="食物数量">
          <div class="food-amount-list">
            <div v-for="foodId in recordForm.foodIds" :key="foodId" class="food-amount-item">
              <span class="food-name-label">{{ getFoodName(foodId) }}</span>
              <el-input-number 
                v-model="recordForm.foodAmounts[foodId]" 
                :min="1" 
                :max="10000" 
                :step="10" 
                size="small" 
                style="width: 120px;"
              />
              <span class="food-unit">g</span>
            </div>
          </div>
        </el-form-item>
        
        <el-form-item label="日期" prop="recordDate">
          <el-date-picker
            v-model="recordForm.recordDate"
            type="date"
            placeholder="选择日期"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="餐次" prop="mealType">
          <el-radio-group v-model="recordForm.mealType">
            <el-radio-button label="breakfast">早餐</el-radio-button>
            <el-radio-button label="lunch">午餐</el-radio-button>
            <el-radio-button label="dinner">晚餐</el-radio-button>
            <el-radio-button label="snack">加餐</el-radio-button>
          </el-radio-group>
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
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Bowl, Plus, Search, Coffee, Grape, Apple } from '@element-plus/icons-vue'
import request from '@/utils/request'
import dayjs from 'dayjs'
import * as echarts from 'echarts'

const foodList = ref([])
const foodTotal = ref(0)
const foodPage = ref(1)
const foodKeyword = ref('')
const foodDialogVisible = ref(false)
const recordDialogVisible = ref(false)
const dietRecords = ref([])
const recordDate = ref(dayjs().format('YYYY-MM-DD'))
const dayStats = ref({})
const foodFormRef = ref(null)
const recordFormRef = ref(null)
const nutritionChart = ref(null)
let nutritionChartInstance = null

const foodForm = reactive({
  name: '',
  category: '',
  calories: null,
  protein: null,
  carbohydrate: null,
  fat: null
})

const recordForm = reactive({
  foodIds: [],
  foodAmounts: {},
  recordDate: '',
  mealType: 'breakfast',
  note: ''
})

const foodRules = {
  name: [{ required: true, message: '请输入食物名称', trigger: 'blur' }],
  category: [{ required: true, message: '请选择分类', trigger: 'change' }],
  calories: [{ required: true, message: '请输入热量', trigger: 'blur' }]
}

const recordRules = {
  foodIds: [{ required: true, message: '请选择至少一种食物', trigger: 'change', validator: (rule, value, callback) => {
    if (!value || value.length === 0) {
      callback(new Error('请选择至少一种食物'))
    } else {
      callback()
    }
  }}],
  recordDate: [{ required: true, message: '请选择日期', trigger: 'change' }],
  mealType: [{ required: true, message: '请选择餐次', trigger: 'change' }]
}

const mealTypeMap = { breakfast: '早餐', lunch: '午餐', dinner: '晚餐', snack: '加餐' }
const getMealTypeText = (type) => mealTypeMap[type] || type
const mealTagType = (type) => ({ breakfast: 'warning', lunch: '', dinner: 'danger', snack: 'success' }[type] || 'info')

const loadFoodList = async () => {
  try {
    const res = await request.get('/diet/food', {
      params: { keyword: foodKeyword.value, page: foodPage.value, size: 10 }
    })
    foodList.value = res.data?.records || []
    foodTotal.value = res.data?.total || 0
  } catch (error) {
    console.error(error)
  }
}

const loadDietRecords = async () => {
  try {
    const date = dayjs(recordDate.value).format('YYYY-MM-DD')
    const [recordsRes, statsRes] = await Promise.all([
      request.get(`/diet/record?recordDate=${date}`),
      request.get(`/diet/statistics?startDate=${date}&endDate=${date}`)
    ])
    dietRecords.value = recordsRes.data || []
    dayStats.value = statsRes.data || {}
    await nextTick()
    initNutritionChart()
  } catch (error) {
    console.error(error)
  }
}

const initNutritionChart = () => {
  if (!nutritionChart.value) return
  const protein = dayStats.value.totalProtein || 0
  const carbs = dayStats.value.totalCarbohydrate || 0
  const fat = dayStats.value.totalFat || 0

  if (!nutritionChartInstance) {
    nutritionChartInstance = echarts.init(nutritionChart.value)
  }

  nutritionChartInstance.setOption({
    tooltip: {
      trigger: 'item',
      backgroundColor: 'rgba(15,23,42,0.9)',
      borderColor: 'transparent',
      textStyle: { color: '#f8fafc' },
      formatter: '{b}: {c}g ({d}%)'
    },
    legend: {
      bottom: 10, left: 'center',
      textStyle: { color: '#64748b', fontSize: 11 },
      itemWidth: 12, itemHeight: 8
    },
    series: [{
      type: 'pie',
      radius: ['42%', '68%'],
      center: ['50%', '45%'],
      avoidLabelOverlap: false,
      itemStyle: { borderRadius: 6, borderColor: '#fff', borderWidth: 2 },
      label: { show: false },
      emphasis: { label: { show: true, fontSize: 14, fontWeight: 'bold' } },
      data: [
        { value: protein, name: '蛋白质', itemStyle: { color: '#16c784' } },
        { value: carbs, name: '碳水化合物', itemStyle: { color: '#f97316' } },
        { value: fat, name: '脂肪', itemStyle: { color: '#a855f7' } }
      ]
    }]
  })
}

const showAddFoodDialog = () => {
  Object.assign(foodForm, { name: '', category: '', calories: null, protein: null, carbohydrate: null, fat: null })
  foodDialogVisible.value = true
}

const handleAddFood = async () => {
  if (!foodFormRef.value) return
  await foodFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await request.post('/diet/food', foodForm)
        ElMessage.success('添加成功')
        foodDialogVisible.value = false
        loadFoodList()
      } catch (error) {
        console.error(error)
      }
    }
  })
}

const selectFood = (food) => {
  if (!recordForm.foodIds.includes(food.id)) {
    recordForm.foodIds.push(food.id)
    recordForm.foodAmounts[food.id] = 100
  }
  recordForm.recordDate = dayjs().format('YYYY-MM-DD')
  recordForm.mealType = 'breakfast'
  recordForm.note = ''
  recordDialogVisible.value = true
}

const getFoodName = (foodId) => {
  const food = foodList.value.find(f => f.id === foodId)
  return food ? food.name : ''
}

const showAddRecordDialog = () => {
  Object.assign(recordForm, { 
    foodIds: [], 
    foodAmounts: {},
    recordDate: dayjs().format('YYYY-MM-DD'), 
    mealType: 'breakfast', 
    note: '' 
  })
  recordDialogVisible.value = true
}

const handleAddRecord = async () => {
  if (!recordFormRef.value) return
  await recordFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const date = dayjs(recordForm.recordDate).format('YYYY-MM-DD')
        const promises = recordForm.foodIds.map(foodId => {
          const data = {
            foodId: foodId,
            recordDate: date,
            mealType: recordForm.mealType,
            amount: recordForm.foodAmounts[foodId] || 100,
            note: recordForm.note
          }
          return request.post('/diet/record', data)
        })
        await Promise.all(promises)
        ElMessage.success(`成功添加 ${recordForm.foodIds.length} 条记录`)
        recordDialogVisible.value = false
        loadDietRecords()
      } catch (error) {
        console.error(error)
      }
    }
  })
}

const handleDeleteRecord = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这条记录吗？', '提示', { type: 'warning' })
    await request.delete(`/diet/record/${id}`)
    ElMessage.success('删除成功')
    loadDietRecords()
  } catch (error) {
    if (error !== 'cancel') console.error(error)
  }
}

onMounted(() => {
  loadFoodList()
  loadDietRecords()
})
</script>

<style scoped>
.diet { width: 100%; }

/* 营养概览卡片 */
.nutrition-overview {
  display: grid;
  grid-template-columns: 1.2fr 1fr 1fr 1fr;
  gap: 14px;
  margin-bottom: 16px;
}

.overview-card {
  background: white;
  border-radius: 14px;
  padding: 18px;
  box-shadow: var(--shadow-md);
  transition: all 0.25s ease;
}

.overview-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-lg);
}

.overview-card--main {
  display: flex;
  align-items: center;
  gap: 14px;
  background: linear-gradient(135deg, #ecfdf5, #d1fae5);
  border: 1px solid #a7f3d0;
}

.overview-icon {
  width: 52px; height: 52px;
  background: rgba(22,199,132,0.2);
  border-radius: 12px;
  display: flex; align-items: center; justify-content: center;
  font-size: 24px; color: #059669;
  flex-shrink: 0;
}

.overview-content { flex: 1; }

.overview-label {
  font-size: 12px;
  color: var(--color-text-secondary);
  margin-bottom: 4px;
}

.overview-value {
  font-size: 28px;
  font-weight: 800;
  color: var(--color-text-primary);
  line-height: 1.1;
}

.overview-unit {
  font-size: 13px;
  font-weight: 400;
  color: var(--color-text-secondary);
  margin-left: 2px;
}

.overview-sub {
  font-size: 11px;
  color: var(--color-text-muted);
  margin-top: 3px;
}

.overview-mini-icon {
  width: 36px; height: 36px;
  border-radius: 10px;
  display: flex; align-items: center; justify-content: center;
  font-size: 18px;
  margin-bottom: 10px;
}

.overview-mini-content { }

.overview-mini-label {
  font-size: 11px;
  color: var(--color-text-secondary);
  margin-bottom: 4px;
}

.overview-mini-value {
  font-size: 20px;
  font-weight: 700;
  color: var(--color-text-primary);
  margin-bottom: 8px;
}

.overview-progress {
  display: flex;
  align-items: center;
  gap: 6px;
}

.progress-bar {
  flex: 1;
  height: 5px;
  background: #e2e8f0;
  border-radius: 3px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  border-radius: 3px;
  transition: width 0.3s ease;
}

.progress-text {
  font-size: 10px;
  color: var(--color-text-muted);
  white-space: nowrap;
}

/* 食物库 */
.food-library-card :deep(.el-card__body) {
  padding: 16px 20px;
}

.food-grid {
  max-height: 420px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.food-item {
  padding: 12px 14px;
  background: #f8fafc;
  border: 1px solid #e2e8f0;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.food-item:hover {
  background: #ecfdf5;
  border-color: #a7f3d0;
  transform: translateX(3px);
}

.food-item-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 6px;
}

.food-name {
  font-size: 14px;
  font-weight: 600;
  color: var(--color-text-primary);
}

.food-nutrients {
  font-size: 12px;
  color: var(--color-text-secondary);
  margin-bottom: 2px;
}

.nutrient { font-weight: 500; }
.nutrient-sep { margin: 0 4px; color: var(--color-text-muted); }

.food-per {
  font-size: 10px;
  color: var(--color-text-muted);
}

/* 营养图表 */
.nutrition-chart-section {
  background: #f8fafc;
  border-radius: 10px;
  margin-bottom: 14px;
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

.selected-foods-tip {
  font-size: 12px;
  color: var(--color-primary);
  margin-top: 8px;
}

.food-amount-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
  max-height: 200px;
  overflow-y: auto;
  padding: 10px;
  background: #f8fafc;
  border-radius: 8px;
}

.food-amount-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.food-name-label {
  flex: 1;
  font-size: 13px;
  color: var(--color-text-primary);
}

.food-unit {
  font-size: 12px;
  color: var(--color-text-secondary);
}
</style>
