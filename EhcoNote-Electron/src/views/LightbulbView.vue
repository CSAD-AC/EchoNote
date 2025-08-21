<template>
  <Layout>
    <!-- 功能内容区域 -->
    <section class="feature-content">
      <div class="feature-header">
        <h2>灵光胶囊</h2>
        <p>捕捉瞬时闪现的灵感</p>
        <el-button
          @click="showAddForm"
          class="add-button"
          type="primary"
          icon="Plus"
        >
          添加灵感
        </el-button>
      </div>

      <!-- 添加/编辑表单 -->
      <el-dialog
        v-model="dialogVisible"
        :title="editingBulb ? '编辑灵感' : '添加灵感'"
        width="500px"
      >
        <el-form ref="bulbForm" :model="currentBulb" label-width="80px">
          <el-form-item label="标题" prop="title">
            <el-input
              v-model="currentBulb.title"
              placeholder="请输入灵感标题"
            ></el-input>
          </el-form-item>
          <el-form-item label="地点" prop="location">
            <el-input
              v-model="currentBulb.location"
              placeholder="请输入地点"
            ></el-input>
          </el-form-item>
          <el-form-item label="心情">
            <div class="mood-input-container">
              <el-input
                v-model="newMoodName"
                placeholder="输入心情名称或选择"
                style="width: 150px; display: inline-block; margin-right: 10px"
                @focus="showMoodSelect = true"
                @blur="handleMoodInputBlur"
              ></el-input>
              <el-button type="primary" size="small" @click="addMood"
                >添加</el-button
              >
            </div>

            <!-- 心情选择下拉框 -->
            <div v-if="showMoodSelect" class="mood-select-dropdown">
              <div class="mood-select-header">选择预设心情</div>
              <div class="mood-select-items">
                <el-tag
                  v-for="mood in presetMoods"
                  :key="mood.id"
                  size="small"
                  @click="addPresetMood(mood)"
                  class="selectable-mood"
                >
                  {{ mood.name }}
                </el-tag>
              </div>
            </div>

            <div class="added-moods">
              <el-tag
                v-for="(mood, index) in currentBulb.moods"
                :key="index"
                size="small"
                closable
                @close="removeMood(index)"
              >
                {{ mood.name }}
              </el-tag>
            </div>
          </el-form-item>
          <el-form-item label="描述" prop="description">
            <el-input
              v-model="currentBulb.description"
              type="textarea"
              placeholder="请输入描述内容"
              rows="4"
            ></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveBulb">确定</el-button>
        </template>
      </el-dialog>

      <!-- 胶囊列表 -->
      <div class="bulb-list">
        <div v-if="bulbs.length === 0" class="empty-state">
          <Plus size="64" class="empty-icon" />
          <p>暂无灵感记录，点击右上角按钮添加</p>
        </div>
        <div v-else class="bulb-grid">
          <el-card
            v-for="bulb in bulbs"
            :key="bulb.id"
            class="bulb-card"
            :shadow="'hover'"
          >
            <template #header>
              <div class="card-header">
                <h3>{{ bulb.title }}</h3>
                <div class="card-actions">
                  <el-button
                    size="small"
                    icon="Edit"
                    @click="showEditForm(bulb)"
                  ></el-button>
                  <el-button
                    size="small"
                    icon="Delete"
                    type="danger"
                    @click="deleteBulb(bulb.id)"
                  ></el-button>
                </div>
              </div>
            </template>
            <div class="card-content">
              <div class="card-location">
                <el-icon><MapLocation /></el-icon> {{ bulb.location }}
              </div>
              <p class="content-preview">
                {{ truncateContent(bulb.description, 100) }}
              </p>

              <div class="card-moods">
                <span class="mood-label">心情：</span>
                <template v-for="mood in bulb.moods" :key="mood.id">
                  <el-tag
                    v-if="mood.id <= 40"
                    size="small"
                    :class="'mood-tag-' + mood.id"
                  >
                    {{ mood.name }}
                  </el-tag>
                  <span v-else class="text-mood">{{ mood.name }}</span>
                </template>
              </div>
              <div class="card-time">
                <span>{{ formatDate(bulb.updateTime) }}</span>
              </div>
            </div>
          </el-card>
        </div>
      </div>
    </section>
  </Layout>
</template>

<script setup>
/**
 * 灵光胶囊功能页面
 * 实现灵光胶囊的增删改查功能
 */
// 导入公共布局组件
import Layout from "@/components/Layout.vue";
// 导入所需图标
import { Plus, MapLocation } from "@element-plus/icons-vue";
import {
  ElButton,
  ElDialog,
  ElForm,
  ElFormItem,
  ElInput,
  ElTag,
  ElMessage,
  ElMessageBox,
} from "element-plus";
import { ref, onMounted } from "vue";

// 胶囊数据 - 使用模拟数据
const bulbs = ref([
  {
    id: 1,
    userId: 2,
    title: "2023-06-01",
    location: "上海",
    description: "今天天气不错",
    moods: [
      {
        id: 1,
        name: "开心",
        createTime: "2025-08-18T11:13:52",
      },
      {
        id: 12,
        name: "迷茫",
        createTime: "2025-08-18T11:13:52",
      },
    ],
    newMood: null,
    createTime: "2025-08-18T11:13:52",
    updateTime: "2025-08-18T11:13:52",
    status: true,
  },
  // 更多模拟数据
  {
    id: 2,
    userId: 2,
    title: "2023-06-02",
    location: "北京",
    description: "参加技术研讨会",
    moods: [
      {
        id: 3,
        name: "兴奋",
        createTime: "2025-08-19T09:30:15",
      },
      {
        id: 45,
        name: "疲惫",
        createTime: "2025-08-19T18:45:22",
      },
    ],
    newMood: null,
    createTime: "2025-08-19T09:30:15",
    updateTime: "2025-08-19T18:45:22",
    status: true,
  },
]);

// 当前编辑的胶囊
const currentBulb = ref({
  id: null,
  title: "",
  location: "",
  description: "",
  moods: [],
  newMood: null,
  media: null,
});

// 对话框状态
const dialogVisible = ref(false);
// 是否正在编辑
const editingBulb = ref(false);

// 预设心情列表
const presetMoods = ref([
  { id: 1, name: "开心" },
  { id: 2, name: "兴奋" },
  { id: 3, name: "平静" },
  { id: 4, name: "放松" },
  { id: 5, name: "专注" },
  { id: 6, name: "好奇" },
  { id: 7, name: "期待" },
  { id: 8, name: "满足" },
  { id: 9, name: "感激" },
  { id: 10, name: "爱" },
  { id: 11, name: "思考" },
  { id: 12, name: "迷茫" },
  { id: 13, name: "犹豫" },
  { id: 14, name: "焦虑" },
  { id: 15, name: "压力" },
  { id: 16, name: "疲惫" },
  { id: 17, name: "失望" },
  { id: 18, name: "悲伤" },
  { id: 19, name: "愤怒" },
  { id: 20, name: "厌烦" },
]);

// 新心情名称输入
const newMoodName = ref("");
// 是否显示心情选择下拉框
const showMoodSelect = ref(false);

// 处理心情输入框失焦事件
const handleMoodInputBlur = () => {
  setTimeout(() => {
    showMoodSelect.value = false;
  }, 200);
};

// 添加心情
function addMood() {
  if (newMoodName.value.trim()) {
    // 生成临时ID，自定义心情ID>40
    const newId = 41 + Math.floor(Math.random() * 60);
    currentBulb.value.moods.push({
      id: newId,
      name: newMoodName.value.trim(),
      createTime: new Date().toISOString(),
    });
    newMoodName.value = "";
    showMoodSelect.value = false;
  }
}

// 添加预设心情
function addPresetMood(mood) {
  // 检查是否已添加过相同的心情
  const exists = currentBulb.value.moods.some((m) => m.name === mood.name);
  if (!exists) {
    currentBulb.value.moods.push({
      ...mood,
      createTime: new Date().toISOString(),
    });
  }
  showMoodSelect.value = false;
}

// 移除心情
function removeMood(index) {
  currentBulb.value.moods.splice(index, 1);
}

// 显示添加表单
function showAddForm() {
  currentBulb.value = {
    id: null,
    title: "",
    location: "",
    description: "",
    moods: [],
    newMood: null,
    media: null,
  };
  newMoodName.value = "";
  editingBulb.value = false;
  dialogVisible.value = true;
}

// 显示编辑表单
function showEditForm(bulb) {
  currentBulb.value = { ...bulb };
  // 初始化心情输入
  newMoodName.value = "";
  editingBulb.value = true;
  dialogVisible.value = true;
}

// 保存胶囊
async function saveBulb() {
  if (!currentBulb.value.title.trim()) {
    ElMessage.error("请输入标题");
    return;
  }

  const now = new Date().toISOString();

  if (editingBulb.value) {
    // 更新现有胶囊
    const index = bulbs.value.findIndex((b) => b.id === currentBulb.value.id);
    if (index !== -1) {
      const updatedBulb = {
        ...bulbs.value[index],
        ...currentBulb.value,
        updateTime: now,
      };
      bulbs.value[index] = updatedBulb;
      // 调用API更新
      await updateBulbToApi(updatedBulb);
      ElMessage.success("灵感已更新");
    }
  } else {
    // 添加新胶囊
    const newBulb = {
      ...currentBulb.value,
      id: Date.now(),
      userId: 2, // 临时用户ID
      createTime: now,
      updateTime: now,
      status: true,
      moods: currentBulb.value.moods || [],
      newMood: null,
    };
    bulbs.value.unshift(newBulb);
    // 调用API添加
    await addBulbToApi(newBulb);
    ElMessage.success("灵感已添加");
  }

  dialogVisible.value = false;
}

// 删除胶囊
function deleteBulb(id) {
  ElMessageBox.confirm("确定要删除这个灵感吗？", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      bulbs.value = bulbs.value.filter((bulb) => bulb.id !== id);
      // 调用API删除
      deleteBulbFromApi(id);
      ElMessage.success("灵感已删除");
    })
    .catch(() => {
      // 取消删除
    });
}

// 多媒体相关函数已移除

// 截断内容
function truncateContent(content, length) {
  if (!content) return "";
  return content.length > length
    ? content.substring(0, length) + "..."
    : content;
}

// 格式化日期
function formatDate(dateString) {
  const date = new Date(dateString);
  return date.toLocaleString();
}

// API接口函数 - 预留
// 获取胶囊列表
async function fetchBulbs() {
  try {
    // 实际项目中替换为真实API调用
    // const response = await fetch('/api/bulbs');
    // const data = await response.json();
    // bulbs.value = data;
    console.log("API: 获取胶囊列表");
    // 模拟API延迟
    await new Promise((resolve) => setTimeout(resolve, 500));
  } catch (error) {
    ElMessage.error("获取灵感胶囊失败");
    console.error("Failed to fetch bulbs:", error);
  }
}

// 添加胶囊
async function addBulbToApi(bulb) {
  try {
    // 实际项目中替换为真实API调用
    // const response = await fetch('/api/bulbs', {
    //   method: 'POST',
    //   headers: {'Content-Type': 'application/json'},
    //   body: JSON.stringify(bulb)
    // });
    // const data = await response.json();
    console.log("API: 添加胶囊", bulb);
    // 模拟API延迟
    await new Promise((resolve) => setTimeout(resolve, 500));
  } catch (error) {
    ElMessage.error("添加灵感胶囊失败");
    console.error("Failed to add bulb:", error);
  }
}

// 更新胶囊
async function updateBulbToApi(bulb) {
  try {
    // 实际项目中替换为真实API调用
    // const response = await fetch(`/api/bulbs/${bulb.id}`, {
    //   method: 'PUT',
    //   headers: {'Content-Type': 'application/json'},
    //   body: JSON.stringify(bulb)
    // });
    // const data = await response.json();
    console.log("API: 更新胶囊", bulb);
    // 模拟API延迟
    await new Promise((resolve) => setTimeout(resolve, 500));
  } catch (error) {
    ElMessage.error("更新灵感胶囊失败");
    console.error("Failed to update bulb:", error);
  }
}

// 删除胶囊
async function deleteBulbFromApi(id) {
  try {
    // 实际项目中替换为真实API调用
    // const response = await fetch(`/api/bulbs/${id}`, {
    //   method: 'DELETE'
    // });
    console.log("API: 删除胶囊", id);
    // 模拟API延迟
    await new Promise((resolve) => setTimeout(resolve, 500));
  } catch (error) {
    ElMessage.error("删除灵感胶囊失败");
    console.error("Failed to delete bulb:", error);
  }
}

// 挂载时执行
onMounted(() => {
  // 调用API获取数据
  fetchBulbs();
});
</script>

<style scoped>
/* 基础样式 */
.home-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  font-family: "Avenir", "Helvetica", "Arial", sans-serif;
  color: #333;
}

/* 头部样式 */
.header {
  padding: 1rem 2rem;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.right-controls {
  display: flex;
  align-items: center;
  gap: 2rem;
}

.nav-container {
  margin: 0;
}

.logo-container {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.logo {
  height: 2.5rem;
}

.app-name {
  font-size: 1.5rem;
  font-weight: 700;
  margin: 0;
  background: linear-gradient(90deg, #42b983, #35495e);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}

/* 功能内容区域样式 */
.feature-content {
  padding: 3rem 2rem;
  max-width: 1200px;
  margin: 0 auto;
  flex: 1;
}

.feature-header {
  text-align: center;
  margin-bottom: 2rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
}

.feature-header h2 {
  font-size: 2rem;
  margin-bottom: 0.5rem;
  color: #42b983;
}

.add-button {
  margin-top: 1rem;
}

/* 胶囊列表样式 */
.bulb-list {
  margin-top: 2rem;
}

.bulb-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1.5rem;
}

.bulb-card {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-actions {
  display: flex;
  gap: 0.5rem;
}

.card-content {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  padding: 0.5rem 0;
}

.content-preview {
  margin: 0;
  line-height: 1.5;
  word-break: break-word;
}

.media-indicator {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #666;
  font-size: 0.9rem;
}

.card-location {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #42b983;
  font-size: 0.9rem;
}

.card-moods {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 0.5rem;
  margin-top: 0.5rem;
}

.mood-label {
  font-weight: 500;
  color: #666;
}

.text-mood {
  padding: 0.2rem 0.5rem;
  background-color: #f5f5f5;
  border-radius: 4px;
  color: #666;
}

dark-mode .text-mood {
  background-color: #333;
  color: #ddd;
}

/* 删除不再使用的标签样式 */

.card-time {
  font-size: 0.8rem;
  color: #999;
  text-align: right;
}

/* 空状态样式 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 3rem;
  text-align: center;
  color: #999;
}

.empty-icon {
  margin-bottom: 1rem;
  color: #ddd;
}

/* 上传文件样式 */
.uploaded-file {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 1rem;
  padding: 0.5rem;
  background-color: #f5f5f5;
  border-radius: 4px;
}

/* 美化心情标签样式 */
.card-moods .el-tag {
  border-radius: 16px !important;
  padding: 4px 12px;
  font-size: 12px;
  height: 24px;
  line-height: 16px;
  transition: all 0.2s;
  margin-right: 6px;
  margin-bottom: 6px;
}

.card-moods .el-tag:hover {
  transform: scale(1.05);
}

/* 为不同心情ID设置不同颜色 */
.mood-tag-1 {
  background-color: #f0f9eb;
  color: #67c23a;
  border-color: #e1f3d8;
}

.mood-tag-2 {
  background-color: #e6f7ff;
  color: #3ba6fc;
  border-color: #bae7ff;
}

.mood-tag-3 {
  background-color: #fff0f6;
  color: #f56c6c;
  border-color: #ffccd5;
}

.mood-tag-4 {
  background-color: #f3e5f5;
  color: #9c27b0;
  border-color: #e1bee7;
}

.mood-tag-5 {
  background-color: #fff8e1;
  color: #ff9800;
  border-color: #ffe0b2;
}

.mood-tag-6 {
  background-color: #e0f7fa;
  color: #00acc1;
  border-color: #b2ebf2;
}

.mood-tag-7 {
  background-color: #f1f8e9;
  color: #8bc34a;
  border-color: #dcedc8;
}

.mood-tag-8 {
  background-color: #ede7f6;
  color: #7b1fa2;
  border-color: #d1c4e9;
}

.mood-tag-9 {
  background-color: #fff3e0;
  color: #ff6d00;
  border-color: #ffcc80;
}

.mood-tag-10 {
  background-color: #e8f5e9;
  color: #43a047;
  border-color: #c8e6c9;
}
.mood-tag-11 {
  background-color: #e3f2fd;
  color: #2196f3;
  border-color: #bbdefb;
}
.mood-tag-12 {
  background-color: #fbe9e7;
  color: #ef5350;
  border-color: #ffcdd2;
}
.mood-tag-13 {
  background-color: #f3e5f5;
  color: #9575cd;
  border-color: #ce93d8;
}
.mood-tag-14 {
  background-color: #fffde7;
  color: #ffd600;
  border-color: #fff59d;
}
.mood-tag-15 {
  background-color: #e0f2f1;
  color: #26a69a;
  border-color: #b2dfdb;
}
.mood-tag-16 {
  background-color: #fafafa;
  color: #757575;
  border-color: #e0e0e0;
}
.mood-tag-17 {
  background-color: #fce4ec;
  color: #ec407a;
  border-color: #f8bbd0;
}
.mood-tag-18 {
  background-color: #e0e0e0;
  color: #616161;
  border-color: #bdbdbd;
}
.mood-tag-19 {
  background-color: #e3f2fd;
  color: #039be5;
  border-color: #90caf9;
}
.mood-tag-20 {
  background-color: #f1f8e9;
  color: #66bb6a;
  border-color: #a5d6a7;
}

/* 自定义心情文字样式 */
.text-mood {
  color: #606266;
  background-color: #f5f5f5;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
  margin-right: 6px;
  margin-bottom: 6px;
  display: inline-block;
}

.mood-input-container {
  margin-bottom: 0.5rem;
  position: relative;
}

.added-moods {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-top: 0.5rem;
}

.mood-select-dropdown {
  position: absolute;
  top: 100%;
  left: 0;
  width: 300px;
  background-color: white;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  z-index: 100;
  padding: 10px;
  margin-top: 5px;
}

.mood-select-header {
  font-weight: bold;
  margin-bottom: 8px;
  padding-bottom: 5px;
  border-bottom: 1px solid #eee;
}

/* 暗黑模式样式适配 */
.dark .mood-select-dropdown {
  background-color: #1f2937;
  border-color: #374151;
  color: #f3f4f6;
}

.dark .mood-select-header {
  border-bottom-color: #374151;
}

.dark .selectable-mood:hover {
  background-color: #374151;
}

/* 暗黑模式下心情标签样式 */
.dark .mood-tag-1 {
  background-color: #1a2e1a;
  color: #86efac;
  border-color: #2d3b2d;
}
.dark .mood-tag-2 {
  background-color: #1a2533;
  color: #60a5fa;
  border-color: #2d3b53;
}
.dark .mood-tag-3 {
  background-color: #331a25;
  color: #fca5a5;
  border-color: #532d3b;
}
.dark .mood-tag-4 {
  background-color: #2d1a33;
  color: #d8b4fe;
  border-color: #4b2d53;
}
.dark .mood-tag-5 {
  background-color: #332d1a;
  color: #fcd34d;
  border-color: #534b2d;
}
.dark .mood-tag-6 {
  background-color: #1a3330;
  color: #5eead4;
  border-color: #2d534b;
}
.dark .mood-tag-7 {
  background-color: #20331a;
  color: #86efac;
  border-color: #35532d;
}
.dark .mood-tag-8 {
  background-color: #301a33;
  color: #d8b4fe;
  border-color: #4b2d53;
}
.dark .mood-tag-9 {
  background-color: #33251a;
  color: #fdba74;
  border-color: #533b2d;
}
.dark .mood-tag-10 {
  background-color: #1a331f;
  color: #86efac;
  border-color: #2d5335;
}
.dark .mood-tag-11 {
  background-color: #1a2a33;
  color: #60a5fa;
  border-color: #2d4b53;
}
.dark .mood-tag-12 {
  background-color: #331f1a;
  color: #fca5a5;
  border-color: #53352d;
}
.dark .mood-tag-13 {
  background-color: #2d1a33;
  color: #c4b5fd;
  border-color: #4b2d53;
}
.dark .mood-tag-14 {
  background-color: #33301a;
  color: #fef08a;
  border-color: #534b2d;
}
.dark .mood-tag-15 {
  background-color: #1a3330;
  color: #5eead4;
  border-color: #2d534b;
}
.dark .mood-tag-16 {
  background-color: #2d2d2d;
  color: #d1d5db;
  border-color: #4b4b4b;
}
.dark .mood-tag-17 {
  background-color: #331a25;
  color: #f9a8d4;
  border-color: #532d3b;
}
.dark .mood-tag-18 {
  background-color: #2d2d2d;
  color: #9ca3af;
  border-color: #4b4b4b;
}
.dark .mood-tag-19 {
  background-color: #1a2a33;
  color: #38bdf8;
  border-color: #2d4b53;
}
.dark .mood-tag-20 {
  background-color: #1e331a;
  color: #86efac;
  border-color: #33532d;
}

/* 暗黑模式下自定义心情文字样式 */
.dark-mode .text-mood {
  color: #d1d5db;
  background-color: #374151;
}

/* 修复div颜色不协调问题 */
.dark .bulb-card {
  background-color: #1f2937;
  border-color: #374151;
}

.dark .card-content {
  color: #f3f4f6;
}
.dark .text-mood {
  color: #d1d5db;
  background-color: #374151;
}

/* 修复div颜色不协调问题 */
.dark-mode .bulb-card,
.dark .bulb-card {
  background-color: #1f2937;
  border-color: #374151;
}

.dark-mode .card-content,
.dark .card-content {
  color: #f3f4f6;
}

.mood-select-items {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  max-height: 200px;
  overflow-y: auto;
}

.selectable-mood {
  cursor: pointer;
  transition: all 0.2s;
}

.selectable-mood:hover {
  transform: scale(1.05);
}

dark-mode .mood-select-dropdown {
  background-color: #333;
  border-color: #444;
}

dark-mode .mood-select-header {
  border-bottom-color: #555;
}

/* 页脚样式 */
.footer {
  text-align: center;
  padding: 1.5rem;
  color: #666;
  border-top: 1px solid #eee;
}

/* 美化胶囊样式 */
.bulb-card {
  border-radius: 12px !important;
  overflow: hidden;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  border: none !important;
}

.bulb-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.card-content {
  padding: 20px;
}

/* 暗黑模式适配 */
.dark-mode {
  background-color: #1a1a1a;
  color: #eee;
}

.dark-mode .header {
  background-color: #2a2a2a;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
}

.dark-mode .footer {
  background-color: #2a2a2a;
  border-top: 1px solid #333;
  color: #aaa;
}

.dark-mode .uploaded-file {
  background-color: #333;
}

.dark-mode .bulb-card {
  background-color: #2a2a2a;
  border-color: #444;
}

.dark-mode .card-header h3 {
  color: #ddd;
}

.dark-mode .content-preview {
  color: #ccc;
}

.dark-mode .media-indicator {
  color: #aaa;
}

.dark-mode .card-time {
  color: #888;
}

.dark-mode .empty-state {
  color: #777;
}

.dark-mode .empty-icon {
  color: #555;
}

/* Element Plus 组件暗黑模式适配 */
.dark-mode .el-card__header {
  border-bottom: 1px solid #444;
}

.dark-mode .card-moods .el-tag {
  background-color: #3a3a3a;
  color: #ccc;
  border-color: #555;
}

.dark-mode .text-mood {
  background-color: #444;
  color: #ddd;
}

.dark-mode .uploaded-file {
  background-color: #333;
}
</style>
