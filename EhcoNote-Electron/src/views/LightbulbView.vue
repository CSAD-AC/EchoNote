<template>
  <Layout>
    <section class="feature-content">
      <div class="feature-header">
        <div class="header-main">
          <div class="title-section">
            <h2 class="feature-title">✨ 时光胶囊</h2>
            <p class="feature-subtitle">
              捕捉瞬时闪现的灵感，记录珍贵的思维火花
            </p>
          </div>
          <el-button
            @click="showAddForm"
            class="add-button"
            type="primary"
            icon="Plus"
            size="large"
            round
          >
            <span>✨ 添加灵感</span>
          </el-button>
        </div>
        <div class="status-tabs-container">
          <div class="status-tabs">
            <div
              :class="{ active: currentTab === 'pending' }"
              @click="switchTab('pending')"
              class="tab-item"
            >
              <span class="tab-icon">⏳</span>
              <span>未处理</span>
              <span class="tab-count"
                >({{ bulbs.filter((b) => !b.status).length }})</span
              >
            </div>
            <div
              :class="{ active: currentTab === 'completed' }"
              @click="switchTab('completed')"
              class="tab-item"
            >
              <span class="tab-icon">✅</span>
              <span>已处理</span>
              <span class="tab-count"
                >({{ bulbs.filter((b) => b.status).length }})</span
              >
            </div>
          </div>
        </div>
      </div>

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
                v-model="currentBulb.newMood"
                placeholder="输入心情名称或选择"
                style="width: 100%"
                @focus="showMoodSelect = true"
              ></el-input>
            </div>
            <div v-if="showMoodSelect" class="mood-select-dropdown expanded">
              <div class="mood-select-header">选择预设心情</div>
              <div class="mood-select-items expanded">
                <el-tag
                  v-for="mood in preMoods"
                  :key="mood.id"
                  size="medium"
                  @mousedown.prevent="addPresetMood(mood)"
                  class="selectable-mood"
                >
                  {{ mood.name }}
                </el-tag>
              </div>
              <div class="mood-select-close" @click="showMoodSelect = false">
                <span>关闭</span>
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

      <div class="bulb-list">
        <div v-if="filteredBulbs.length === 0" class="empty-state">
          <div class="empty-illustration">
            <div class="empty-bulb-icon">💡</div>
          </div>
          <h3 class="empty-title">
            {{
              currentTab === "pending" ? "暂无未处理的灵感" : "暂无已处理的灵感"
            }}
          </h3>
          <p class="empty-description">
            {{
              currentTab === "pending"
                ? "点击上方“✨ 添加灵感”按钮，开始记录您的第一个灵感瞬间"
                : "当您处理完成一些灵感后，它们将会在这里显示"
            }}
          </p>
          <el-button
            v-if="currentTab === 'pending'"
            @click="showAddForm"
            type="primary"
            size="large"
            round
            class="empty-action-btn"
          >
            <span>✨ 立即添加</span>
          </el-button>
        </div>
        <div
          v-else
          class="bulb-grid"
          :style="{ transition: 'opacity 0.3s ease' }"
        >
          <el-card
            v-for="bulb in filteredBulbs"
            :key="bulb.id"
            class="bulb-card"
            :shadow="'hover'"
          >
            <template #header>
              <div class="card-header">
                <h3>{{ bulb.title }}</h3>
                <div class="card-actions">
                  <el-button
                    v-if="!bulb.status"
                    size="default"
                    icon="Check"
                    type="success"
                    @click="markAsSolved(bulb.id)"
                  />
                  <el-button
                    v-if="bulb.status"
                    size="default"
                    icon="CircleClose"
                    type="warning"
                    @click="markAsPending(bulb.id)"
                  />
                  <el-button
                    size="default"
                    icon="Edit"
                    @click="showEditForm(bulb.id)"
                  ></el-button>
                  <el-button
                    size="default"
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
import Layout from "@/components/Layout.vue";
import {
  getCapsulesByUserId,
  getCapsuleById,
  getPreMoods,
  changeCapsulesStatusById,
  createCapsule,
  updateCapsule,
  deleteCapsuleById,
} from "@/utils/api.js";
import { MapLocation } from "@element-plus/icons-vue";
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

const bulbs = ref([]);
const currentTab = ref("pending");
const filteredBulbs = ref([]);
const preMoods = ref([]);
const currentBulb = ref({
  id: null,
  title: "",
  location: "",
  description: "",
  moods: [],
  newMood: null,
});
const dialogVisible = ref(false);
const editingBulb = ref(false);
const showMoodSelect = ref(false);

onMounted(async () => {
  try {
    const userInfo = JSON.parse(localStorage.getItem("userInfo"));
    const userId = userInfo?.id;
    if (!userId) {
      ElMessage.error("未找到用户信息，请先登录");
      return;
    }

    // 同时获取胶囊数据和预设心情
    const [capsulesRes, moodsRes] = await Promise.all([
      getCapsulesByUserId(userId),
      getPreMoods(),
    ]);

    bulbs.value = capsulesRes.data || [];
    preMoods.value = moodsRes.data || [];
    filterBulbs();
  } catch (error) {
    console.error("初始化失败:", error);
    ElMessage.error("初始化失败，请重试");
  }
});

function switchTab(tab) {
  currentTab.value = tab;
  filterBulbs();
}

function filterBulbs() {
  if (currentTab.value === "pending") {
    filteredBulbs.value = bulbs.value.filter((bulb) => bulb && !bulb.status);
  } else {
    filteredBulbs.value = bulbs.value.filter((bulb) => bulb && bulb.status);
  }
}

async function toggleStatus(bulbId, newStatus) {
  try {
    await changeCapsulesStatusById({ id: bulbId });
    const index = bulbs.value.findIndex((bulb) => bulb.id === bulbId);
    if (index !== -1) {
      bulbs.value[index].status = newStatus;
      filterBulbs();
      ElMessage.success(newStatus ? "已标记为已解决" : "已标记为未处理");
    }
  } catch (error) {
    console.error("更新状态失败:", error);
    ElMessage.error("操作失败，请重试");
  }
}

const markAsSolved = (id) => toggleStatus(id, true);
const markAsPending = (id) => toggleStatus(id, false);

function addPresetMood(mood) {
  if (!currentBulb.value.moods.some((m) => m.name === mood.name)) {
    currentBulb.value.moods.push({
      ...mood,
      createTime: new Date().toISOString(),
    });
  }
  currentBulb.value.newMood = null;
  showMoodSelect.value = false;
}

function removeMood(index) {
  currentBulb.value.moods.splice(index, 1);
}

function showAddForm() {
  currentBulb.value = {
    id: null,
    title: "",
    location: "",
    description: "",
    moods: [],
    newMood: null,
  };
  editingBulb.value = false;
  dialogVisible.value = true;
}

async function showEditForm(bulbId) {
  try {
    const response = await getCapsuleById(bulbId);
    const bulbData = response.data;
    currentBulb.value = { ...bulbData, newMood: null };
    editingBulb.value = true;
    dialogVisible.value = true;
  } catch (error) {
    console.error("获取胶囊详情失败:", error);
    ElMessage.error("获取胶囊详情失败，请重试");
  }
}

async function saveBulb() {
  if (!currentBulb.value.title.trim()) {
    ElMessage.error("请输入标题");
    return;
  }

  try {
    const userInfo = JSON.parse(localStorage.getItem("userInfo"));
    const userId = userInfo?.id;
    if (!userId) {
      ElMessage.error("未找到用户信息，请先登录");
      return;
    }

    if (editingBulb.value) {
      const updateData = {
        id: currentBulb.value.id,
        title: currentBulb.value.title,
        location: currentBulb.value.location,
        description: currentBulb.value.description,
        moods: currentBulb.value.moods || [],
        newMood: currentBulb.value.newMood
          ? { name: currentBulb.value.newMood }
          : null,
      };
      const response = await updateCapsule(updateData);
      const updatedBulb = response.data;
      const index = bulbs.value.findIndex((b) => b.id === updatedBulb.id);
      if (index !== -1) {
        bulbs.value[index] = updatedBulb;
        filterBulbs();
      }
      ElMessage.success("灵感已更新");
    } else {
      const newBulbData = {
        userId: userId,
        title: currentBulb.value.title,
        location: currentBulb.value.location,
        description: currentBulb.value.description,
        status: false,
        moods: currentBulb.value.moods || [],
        newMood: currentBulb.value.newMood
          ? { name: currentBulb.value.newMood }
          : null,
      };
      const response = await createCapsule(newBulbData);
      const newBulb = response.data;
      bulbs.value.unshift(newBulb);
      filterBulbs();
      ElMessage.success("灵感已添加");
    }

    dialogVisible.value = false;
  } catch (error) {
    console.error("保存胶囊失败:", error);
    ElMessage.error(
      editingBulb.value ? "更新灵感失败，请重试" : "添加灵感失败，请重试"
    );
  }
}

async function deleteBulb(id) {
  try {
    await ElMessageBox.confirm("确定要删除这个灵感吗？", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    });
    await deleteCapsuleById(id);
    bulbs.value = bulbs.value.filter((bulb) => bulb.id !== id);
    filterBulbs();
    ElMessage.success("灵感已删除");
  } catch (error) {
    if (error !== "cancel") {
      console.error("删除胶囊失败:", error);
      ElMessage.error("删除灵感失败，请重试");
    }
  }
}

function truncateContent(content, length) {
  if (!content) return "";
  return content.length > length
    ? content.substring(0, length) + "..."
    : content;
}

function formatDate(dateString) {
  const date = new Date(dateString);
  return date.toLocaleString();
}
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
  padding: 2rem;
  max-width: 1400px;
  margin: 0 auto;
  flex: 1;
  background: linear-gradient(
    135deg,
    rgba(66, 185, 131, 0.02) 0%,
    rgba(255, 255, 255, 0.8) 100%
  );
  min-height: calc(100vh - 120px);
}

.dark .feature-content {
  background: linear-gradient(
    135deg,
    rgba(76, 217, 100, 0.05) 0%,
    rgba(31, 41, 55, 0.8) 100%
  );
}

.feature-header {
  margin-bottom: 3rem;
  text-align: center;
}

.header-main {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  flex-wrap: wrap;
  gap: 1.5rem;
}

.title-section {
  text-align: left;
  flex: 1;
}

.feature-title {
  font-size: 2.5rem;
  font-weight: 700;
  margin-bottom: 0.5rem;
  background: linear-gradient(135deg, #42b983, #35495e, #667eea);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  text-shadow: 0 2px 10px rgba(66, 185, 131, 0.3);
}

.dark .feature-title {
  background: linear-gradient(135deg, #4cd964, #5ac8fa, #a855f7);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  text-shadow: 0 2px 10px rgba(76, 217, 100, 0.4);
}

.feature-subtitle {
  font-size: 1.1rem;
  color: #666;
  margin: 0;
  line-height: 1.6;
}

.dark .feature-subtitle {
  color: #9ca3af;
}

.add-button {
  background: linear-gradient(135deg, #42b983, #35495e);
  border: none;
  padding: 12px 24px;
  font-size: 1rem;
  font-weight: 600;
  box-shadow: 0 4px 15px rgba(66, 185, 131, 0.3);
  transition: all 0.3s ease;
  transform: translateY(0);
}

.add-button:hover {
  background: linear-gradient(135deg, #369870, #2c3e50);
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(66, 185, 131, 0.4);
}

.dark .add-button {
  background: linear-gradient(135deg, #4cd964, #5ac8fa);
}

.dark .add-button:hover {
  background: linear-gradient(135deg, #40c057, #339af0);
  box-shadow: 0 6px 20px rgba(76, 217, 100, 0.4);
}

.status-tabs-container {
  display: flex;
  justify-content: center;
  width: 100%;
}

/* 状态标签样式 */
.status-tabs {
  display: flex;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 50px;
  padding: 6px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  position: relative;
  overflow: hidden;
}

.dark .status-tabs {
  background: rgba(31, 41, 55, 0.8);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.tab-item {
  padding: 12px 24px;
  cursor: pointer;
  border-radius: 45px;
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  z-index: 2;
  color: #666;
  background: transparent;
  font-size: 0.95rem;
}

.tab-item.active {
  background: linear-gradient(135deg, #42b983, #369870);
  color: white;
  transform: scale(1.05);
  box-shadow: 0 4px 15px rgba(66, 185, 131, 0.3);
}

.dark .tab-item {
  color: #9ca3af;
}

.dark .tab-item.active {
  background: linear-gradient(135deg, #4cd964, #40c057);
  color: white;
}

.tab-icon {
  font-size: 1.1rem;
}

.tab-count {
  font-size: 0.85rem;
  opacity: 0.8;
  font-weight: 400;
}

/* 胶囊列表样式 */
.bulb-list {
  margin-top: 3rem;
}

.bulb-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 2rem;
  transition: opacity 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

/* 美化胶囊样式 */
.bulb-card {
  border-radius: 20px !important;
  overflow: hidden;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  border: none !important;
  background: linear-gradient(
    145deg,
    rgba(255, 255, 255, 0.9) 0%,
    rgba(255, 255, 255, 0.7) 100%
  );
  backdrop-filter: blur(20px);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1), 0 4px 16px rgba(66, 185, 131, 0.1),
    inset 0 1px 0 rgba(255, 255, 255, 0.2);
  position: relative;
}

.bulb-card::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(
    145deg,
    rgba(66, 185, 131, 0.05) 0%,
    rgba(53, 73, 94, 0.05) 50%,
    rgba(102, 126, 234, 0.05) 100%
  );
  border-radius: 20px;
  z-index: 0;
  transition: opacity 0.3s ease;
  opacity: 0;
}

.bulb-card:hover::before {
  opacity: 1;
}

.dark .bulb-card {
  background: linear-gradient(
    145deg,
    rgba(31, 41, 55, 0.9) 0%,
    rgba(31, 41, 55, 0.7) 100%
  );
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3), 0 4px 16px rgba(76, 217, 100, 0.1),
    inset 0 1px 0 rgba(255, 255, 255, 0.1);
}

.dark .bulb-card::before {
  background: linear-gradient(
    145deg,
    rgba(76, 217, 100, 0.08) 0%,
    rgba(90, 200, 250, 0.08) 50%,
    rgba(168, 85, 247, 0.08) 100%
  );
}

.bulb-card:hover {
  transform: translateY(-12px) scale(1.02);
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15),
    0 8px 24px rgba(66, 185, 131, 0.2), inset 0 1px 0 rgba(255, 255, 255, 0.3);
}

.dark .bulb-card:hover {
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.4), 0 8px 24px rgba(76, 217, 100, 0.2),
    inset 0 1px 0 rgba(255, 255, 255, 0.2);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 1.5rem 1.5rem 1rem 1.5rem;
  background: linear-gradient(
    135deg,
    rgba(66, 185, 131, 0.1) 0%,
    rgba(255, 255, 255, 0.1) 100%
  );
  border-bottom: 1px solid rgba(66, 185, 131, 0.1);
  position: relative;
  z-index: 1;
}

.dark .card-header {
  background: linear-gradient(
    135deg,
    rgba(76, 217, 100, 0.1) 0%,
    rgba(31, 41, 55, 0.1) 100%
  );
  border-bottom: 1px solid rgba(76, 217, 100, 0.1);
}

.card-header h3 {
  margin: 0;
  font-size: 1.3rem;
  font-weight: 600;
  color: #2c3e50;
  flex: 1;
  line-height: 1.4;
}

.dark .card-header h3 {
  color: #f3f4f6;
}

.card-actions {
  display: flex;
  gap: 8px;
  flex-shrink: 0;
  margin-left: 1rem;
  position: relative;
  z-index: 2;
}

.card-actions .el-button {
  border-radius: 50%;
  width: 36px;
  height: 36px;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
}

.card-actions .el-button:hover {
  transform: scale(1.1) rotate(5deg);
}

.card-content {
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  position: relative;
  z-index: 1;
}

.content-preview {
  margin: 0;
  line-height: 1.6;
  word-break: break-word;
  color: #4b5563;
  font-size: 0.95rem;
}

.dark .content-preview {
  color: #d1d5db;
}

.card-location {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #42b983;
  font-size: 0.9rem;
  font-weight: 500;
  padding: 8px 12px;
  background: rgba(66, 185, 131, 0.1);
  border-radius: 50px;
  width: fit-content;
}

.dark .card-location {
  color: #4cd964;
  background: rgba(76, 217, 100, 0.1);
}

.card-moods {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 8px;
  margin-top: 1rem;
  padding: 12px;
  background: rgba(66, 185, 131, 0.05);
  border-radius: 12px;
  border: 1px dashed rgba(66, 185, 131, 0.2);
}

.dark .card-moods {
  background: rgba(76, 217, 100, 0.05);
  border: 1px dashed rgba(76, 217, 100, 0.2);
}

.mood-label {
  font-weight: 600;
  color: #42b983;
  font-size: 0.9rem;
  margin-right: 8px;
}

.dark .mood-label {
  color: #4cd964;
}

.card-time {
  font-size: 0.85rem;
  color: #9ca3af;
  text-align: right;
  font-style: italic;
  margin-top: auto;
  padding: 8px 12px;
  background: rgba(156, 163, 175, 0.1);
  border-radius: 8px;
  border-left: 3px solid #42b983;
}

.dark .card-time {
  color: #6b7280;
  background: rgba(107, 114, 128, 0.1);
  border-left-color: #4cd964;
}

/* 空状态样式 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 4rem 2rem;
  text-align: center;
  background: linear-gradient(
    135deg,
    rgba(66, 185, 131, 0.05) 0%,
    rgba(255, 255, 255, 0.05) 100%
  );
  border-radius: 20px;
  border: 2px dashed rgba(66, 185, 131, 0.2);
  margin: 2rem 0;
  animation: fadeIn 0.6s ease;
}

.dark .empty-state {
  background: linear-gradient(
    135deg,
    rgba(76, 217, 100, 0.05) 0%,
    rgba(31, 41, 55, 0.05) 100%
  );
  border-color: rgba(76, 217, 100, 0.2);
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.empty-illustration {
  margin-bottom: 2rem;
}

.empty-bulb-icon {
  font-size: 4rem;
  margin-bottom: 1rem;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%,
  100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
}

.empty-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: #42b983;
  margin-bottom: 1rem;
}

.dark .empty-title {
  color: #4cd964;
}

.empty-description {
  font-size: 1rem;
  color: #6b7280;
  line-height: 1.6;
  margin-bottom: 2rem;
  max-width: 400px;
}

.dark .empty-description {
  color: #9ca3af;
}

.empty-action-btn {
  background: linear-gradient(135deg, #42b983, #35495e);
  border: none;
  padding: 12px 24px;
  font-size: 1rem;
  font-weight: 600;
  box-shadow: 0 4px 15px rgba(66, 185, 131, 0.3);
  transition: all 0.3s ease;
}

.empty-action-btn:hover {
  background: linear-gradient(135deg, #369870, #2c3e50);
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(66, 185, 131, 0.4);
}

.dark .empty-action-btn {
  background: linear-gradient(135deg, #4cd964, #5ac8fa);
}

.dark .empty-action-btn:hover {
  background: linear-gradient(135deg, #40c057, #339af0);
  box-shadow: 0 6px 20px rgba(76, 217, 100, 0.4);
}

/* 心情标签样式 */
.card-moods .el-tag {
  border-radius: 20px !important;
  padding: 6px 14px;
  font-size: 12px;
  height: 28px;
  line-height: 16px;
  transition: transform 0.3s ease;
  margin-right: 8px;
  margin-bottom: 8px;
  font-weight: 500;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.card-moods .el-tag:hover {
  transform: scale(1.05) translateY(-1px);
}

.text-mood {
  color: #fff;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 6px 14px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
  margin-right: 8px;
  margin-bottom: 8px;
  display: inline-block;
  transition: transform 0.3s ease;
}

.text-mood:hover {
  transform: scale(1.05) translateY(-1px);
}

.dark .text-mood {
  background: linear-gradient(135deg, #4c6ef5 0%, #9775fa 100%);
}

.mood-input-container {
  margin-bottom: 1rem;
  position: relative;
}

.added-moods {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 1rem;
  padding: 12px;
  background: rgba(66, 185, 131, 0.05);
  border-radius: 12px;
  border: 1px dashed rgba(66, 185, 131, 0.2);
  min-height: 44px;
  align-items: center;
}

.dark .added-moods {
  background: rgba(76, 217, 100, 0.05);
  border: 1px dashed rgba(76, 217, 100, 0.2);
}

.mood-select-dropdown {
  position: absolute;
  top: 100%;
  left: 0;
  width: 100%;
  max-width: 450px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(66, 185, 131, 0.2);
  border-radius: 16px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  padding: 16px;
  margin-top: 8px;
  animation: fadeInUp 0.3s ease;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.mood-select-header {
  font-weight: 600;
  font-size: 0.9rem;
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 2px solid rgba(66, 185, 131, 0.2);
  color: #42b983;
}

/* 暗黑模式样式适配 */
.dark .mood-select-dropdown {
  background: rgba(31, 41, 55, 0.95);
  border: 1px solid rgba(76, 217, 100, 0.2);
  color: #f3f4f6;
}

.dark .mood-select-header {
  border-bottom-color: rgba(76, 217, 100, 0.2);
  color: #4cd964;
}

.dark .selectable-mood:hover {
  background-color: rgba(76, 217, 100, 0.1);
  transform: scale(1.05);
}

.dark .card-content {
  color: #f3f4f6;
}

.dark-mode .card-content,
.dark .card-content {
  color: #f3f4f6;
}

.mood-select-items {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  max-height: 280px;
  overflow-y: auto;
  padding: 8px 0;
}

.mood-select-dropdown.expanded {
  padding: 20px;
  box-shadow: 0 15px 50px rgba(0, 0, 0, 0.15);
}

.mood-select-items.expanded {
  gap: 12px;
  max-height: 320px;
  padding: 12px 0;
}

.mood-select-close {
  display: flex;
  justify-content: center;
  padding: 12px 0 0 0;
  border-top: 1px solid rgba(66, 185, 131, 0.2);
  margin-top: 12px;
}

.mood-select-close span {
  cursor: pointer;
  color: #42b983;
  font-size: 0.9rem;
  font-weight: 500;
  padding: 6px 16px;
  border-radius: 16px;
  transition: all 0.3s ease;
  background: rgba(66, 185, 131, 0.1);
}

.mood-select-close span:hover {
  background: rgba(66, 185, 131, 0.2);
  transform: translateY(-1px);
}

.dark .mood-select-close {
  border-top-color: rgba(76, 217, 100, 0.2);
}

.dark .mood-select-close span {
  color: #4cd964;
  background: rgba(76, 217, 100, 0.1);
}

.dark .mood-select-close span:hover {
  background: rgba(76, 217, 100, 0.2);
}

.selectable-mood {
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border-radius: 20px !important;
  padding: 8px 16px !important;
  font-weight: 500;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  position: relative;
  overflow: hidden;
}

.selectable-mood::before {
  content: "";
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(255, 255, 255, 0.3),
    transparent
  );
  transition: left 0.4s ease;
}

.selectable-mood:hover {
  transform: scale(1.08) translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
}

.selectable-mood:hover::before {
  left: 100%;
}

/* 美化胶囊样式 */
.bulb-card {
  border-radius: 12px !important;
  overflow: hidden;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  border: none !important;
}

.dark .bulb-card {
  background-color: #1f2937;
  border-color: #374151;
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

.dark .card-header h3 {
  color: #ddd;
}

.dark .content-preview {
  color: #ccc;
}

.dark-mode .card-time {
  color: #888;
}

.dark .empty-state {
  color: #ccc5c5;
}

.mood-tag-1 {
  background-color: #f0f9eb;
  color: #67c23a;
  border-color: #e1f3d8;
}
.dark .mood-tag-1 {
  background-color: #1a2e1a;
  color: #86efac;
  border-color: #2d3b2d;
}

.mood-tag-2 {
  background-color: #e6f7ff;
  color: #3ba6fc;
  border-color: #bae7ff;
}
.dark .mood-tag-2 {
  background-color: #1a2533;
  color: #60a5fa;
  border-color: #2d3b53;
}

.mood-tag-3 {
  background-color: #fff0f6;
  color: #f56c6c;
  border-color: #ffccd5;
}
.dark .mood-tag-3 {
  background-color: #331a25;
  color: #fca5a5;
  border-color: #532d3b;
}

.mood-tag-4 {
  background-color: #f3e5f5;
  color: #9c27b0;
  border-color: #e1bee7;
}
.dark .mood-tag-4 {
  background-color: #2d1a33;
  color: #d8b4fe;
  border-color: #4b2d53;
}

.mood-tag-5 {
  background-color: #fff8e1;
  color: #ff9800;
  border-color: #ffe0b2;
}
.dark .mood-tag-5 {
  background-color: #332d1a;
  color: #fcd34d;
  border-color: #534b2d;
}

.mood-tag-6 {
  background-color: #e0f7fa;
  color: #00acc1;
  border-color: #b2ebf2;
}
.dark .mood-tag-6 {
  background-color: #1a3330;
  color: #5eead4;
  border-color: #2d534b;
}

.mood-tag-7 {
  background-color: #f1f8e9;
  color: #8bc34a;
  border-color: #dcedc8;
}
.dark .mood-tag-7 {
  background-color: #20331a;
  color: #86efac;
  border-color: #35532d;
}

.mood-tag-8 {
  background-color: #ede7f6;
  color: #7b1fa2;
  border-color: #d1c4e9;
}
.dark .mood-tag-8 {
  background-color: #301a33;
  color: #d8b4fe;
  border-color: #4b2d53;
}

.mood-tag-9 {
  background-color: #fff3e0;
  color: #ff6d00;
  border-color: #ffcc80;
}
.dark .mood-tag-9 {
  background-color: #33251a;
  color: #fdba74;
  border-color: #533b2d;
}

.mood-tag-10 {
  background-color: #e8f5e9;
  color: #43a047;
  border-color: #c8e6c9;
}
.dark .mood-tag-10 {
  background-color: #1a331f;
  color: #86efac;
  border-color: #2d5335;
}

.mood-tag-11 {
  background-color: #e3f2fd;
  color: #2196f3;
  border-color: #bbdefb;
}
.dark .mood-tag-11 {
  background-color: #1a2a33;
  color: #60a5fa;
  border-color: #2d4b53;
}

.mood-tag-12 {
  background-color: #fbe9e7;
  color: #ef5350;
  border-color: #ffcdd2;
}
.dark .mood-tag-12 {
  background-color: #331f1a;
  color: #fca5a5;
  border-color: #53352d;
}

.mood-tag-13 {
  background-color: #f3e5f5;
  color: #9575cd;
  border-color: #ce93d8;
}
.dark .mood-tag-13 {
  background-color: #2d1a33;
  color: #c4b5fd;
  border-color: #4b2d53;
}

.mood-tag-14 {
  background-color: #fffde7;
  color: #ffd600;
  border-color: #fff59d;
}
.dark .mood-tag-14 {
  background-color: #33301a;
  color: #fef08a;
  border-color: #534b2d;
}

.mood-tag-15 {
  background-color: #e0f2f1;
  color: #26a69a;
  border-color: #b2dfdb;
}
.dark .mood-tag-15 {
  background-color: #1a3330;
  color: #5eead4;
  border-color: #2d534b;
}

.mood-tag-16 {
  background-color: #fafafa;
  color: #757575;
  border-color: #e0e0e0;
}
.dark .mood-tag-16 {
  background-color: #2d2d2d;
  color: #d1d5db;
  border-color: #4b4b4b;
}

.mood-tag-17 {
  background-color: #fce4ec;
  color: #ec407a;
  border-color: #f8bbd0;
}
.dark .mood-tag-17 {
  background-color: #331a25;
  color: #f9a8d4;
  border-color: #532d3b;
}

.mood-tag-18 {
  background-color: #e0e0e0;
  color: #616161;
  border-color: #bdbdbd;
}
.dark .mood-tag-18 {
  background-color: #2d2d2d;
  color: #9ca3af;
  border-color: #4b4b4b;
}

.mood-tag-19 {
  background-color: #e3f2fd;
  color: #039be5;
  border-color: #90caf9;
}
.dark .mood-tag-19 {
  background-color: #1a2a33;
  color: #38bdf8;
  border-color: #2d4b53;
}

.mood-tag-20 {
  background-color: #f1f8e9;
  color: #66bb6a;
  border-color: #a5d6a7;
}
.dark .mood-tag-20 {
  background-color: #1e331a;
  color: #86efac;
  border-color: #33532d;
}

.mood-tag-21 {
  background-color: #fff5e8;
  color: #ff7043;
  border-color: #ffccbc;
}
.dark .mood-tag-21 {
  background-color: #33221a;
  color: #f9a071;
  border-color: #533a2d;
}

.mood-tag-22 {
  background-color: #e8f5e9;
  color: #4caf50;
  border-color: #c8e6c9;
}
.dark .mood-tag-22 {
  background-color: #1a331a;
  color: #81c784;
  border-color: #2d532d;
}

.mood-tag-23 {
  background-color: #e0f7fa;
  color: #00bcd4;
  border-color: #b2ebf2;
}
.dark .mood-tag-23 {
  background-color: #1a3330;
  color: #4dd0e1;
  border-color: #2d534b;
}

.mood-tag-24 {
  background-color: #fce4ec;
  color: #e91e63;
  border-color: #f8bbd0;
}
.dark .mood-tag-24 {
  background-color: #331a2d;
  color: #f06292;
  border-color: #532d4b;
}

.mood-tag-25 {
  background-color: #e3f2fd;
  color: #1e88e5;
  border-color: #bbdefb;
}
.dark .mood-tag-25 {
  background-color: #1a2a33;
  color: #64b5f6;
  border-color: #2d4b53;
}

.mood-tag-26 {
  background-color: #e8f5e9;
  color: #388e3c;
  border-color: #c8e6c9;
}
.dark .mood-tag-26 {
  background-color: #1a331a;
  color: #66bb6a;
  border-color: #2d532d;
}

.mood-tag-27 {
  background-color: #fde4e7;
  color: #d81b60;
  border-color: #f8bbd0;
}
.dark .mood-tag-27 {
  background-color: #331a2d;
  color: #ec407a;
  border-color: #532d4b;
}

.mood-tag-28 {
  background-color: #fbe9e7;
  color: #d32f2f;
  border-color: #ffcdd2;
}
.dark .mood-tag-28 {
  background-color: #331a1a;
  color: #e57373;
  border-color: #532d2d;
}

.mood-tag-29 {
  background-color: #e3f2fd;
  color: #1976d2;
  border-color: #bbdefb;
}
.dark .mood-tag-29 {
  background-color: #1a2a33;
  color: #90caf9;
  border-color: #2d4b53;
}

.mood-tag-30 {
  background-color: #fafafa;
  color: #9e9e9e;
  border-color: #e0e0e0;
}
.dark .mood-tag-30 {
  background-color: #2d2d2d;
  color: #bdbdbd;
  border-color: #4b4b4b;
}

.mood-tag-31 {
  background-color: #fbe9e7;
  color: #d84315;
  border-color: #ffcdd2;
}
.dark .mood-tag-31 {
  background-color: #331f1a;
  color: #f48fb1;
  border-color: #53352d;
}

.mood-tag-32 {
  background-color: #e8f5e9;
  color: #66bb6a;
  border-color: #c8e6c9;
}
.dark .mood-tag-32 {
  background-color: #1e331a;
  color: #a5d6a7;
  border-color: #33532d;
}

.mood-tag-33 {
  background-color: #e0f7fa;
  color: #80deea;
  border-color: #b2ebf2;
}
.dark .mood-tag-33 {
  background-color: #1a3330;
  color: #5eead4;
  border-color: #2d534b;
}

.mood-tag-34 {
  background-color: #e8f5e9;
  color: #81c784;
  border-color: #c8e6c9;
}
.dark .mood-tag-34 {
  background-color: #1a331a;
  color: #a5d6a7;
  border-color: #2d532d;
}

.mood-tag-35 {
  background-color: #fffde7;
  color: #fdd835;
  border-color: #fff59d;
}
.dark .mood-tag-35 {
  background-color: #33301a;
  color: #fef08a;
  border-color: #534b2d;
}

.mood-tag-36 {
  background-color: #e3f2fd;
  color: #64b5f6;
  border-color: #bbdefb;
}
.dark .mood-tag-36 {
  background-color: #1a2a33;
  color: #90caf9;
  border-color: #2d4b53;
}

.mood-tag-37 {
  background-color: #e8f5e9;
  color: #aed581;
  border-color: #c8e6c9;
}
.dark .mood-tag-37 {
  background-color: #1e331a;
  color: #c5e1a5;
  border-color: #33532d;
}

.mood-tag-38 {
  background-color: #ede7f6;
  color: #9575cd;
  border-color: #d1c4e9;
}
.dark .mood-tag-38 {
  background-color: #2d1a33;
  color: #c4b5fd;
  border-color: #4b2d53;
}

.mood-tag-39 {
  background-color: #fbe9e7;
  color: #ff8a65;
  border-color: #ffcdd2;
}
.dark .mood-tag-39 {
  background-color: #331f1a;
  color: #fca5a5;
  border-color: #53352d;
}

.mood-tag-40 {
  background-color: #e3f2fd;
  color: #42a5f5;
  border-color: #bbdefb;
}
.dark .mood-tag-40 {
  background-color: #1a2a33;
  color: #64b5f6;
  border-color: #2d4b53;
}

/* 美化胶囊卡片样式 */
.bulb-card {
  border-radius: 20px !important;
  overflow: hidden;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  border: none !important;
  background: linear-gradient(
    145deg,
    rgba(255, 255, 255, 0.9) 0%,
    rgba(255, 255, 255, 0.7) 100%
  );
  backdrop-filter: blur(20px);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1), 0 4px 16px rgba(66, 185, 131, 0.1),
    inset 0 1px 0 rgba(255, 255, 255, 0.2);
  position: relative;
  overflow: visible;
}

.bulb-card::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(
    145deg,
    rgba(66, 185, 131, 0.05) 0%,
    rgba(53, 73, 94, 0.05) 50%,
    rgba(102, 126, 234, 0.05) 100%
  );
  border-radius: 20px;
  z-index: -1;
  transition: opacity 0.3s ease;
  opacity: 0;
}

.bulb-card:hover::before {
  opacity: 1;
}

.dark .bulb-card {
  background: linear-gradient(
    145deg,
    rgba(31, 41, 55, 0.9) 0%,
    rgba(31, 41, 55, 0.7) 100%
  );
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3), 0 4px 16px rgba(76, 217, 100, 0.1),
    inset 0 1px 0 rgba(255, 255, 255, 0.1);
}

.dark .bulb-card::before {
  background: linear-gradient(
    145deg,
    rgba(76, 217, 100, 0.08) 0%,
    rgba(90, 200, 250, 0.08) 50%,
    rgba(168, 85, 247, 0.08) 100%
  );
}

.bulb-card:hover {
  transform: translateY(-12px) scale(1.02);
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15),
    0 8px 24px rgba(66, 185, 131, 0.2), inset 0 1px 0 rgba(255, 255, 255, 0.3);
}

.dark .bulb-card:hover {
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.4), 0 8px 24px rgba(76, 217, 100, 0.2),
    inset 0 1px 0 rgba(255, 255, 255, 0.2);
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

.dark .empty-state {
  color: #ccc5c5;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .feature-content {
    max-width: 100%;
    padding: 1.5rem;
  }

  .bulb-grid {
    grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
    gap: 1.5rem;
  }
}

@media (max-width: 768px) {
  .feature-content {
    padding: 1rem;
  }

  .header-main {
    flex-direction: column;
    text-align: center;
    gap: 1rem;
  }

  .title-section {
    text-align: center;
  }

  .feature-title {
    font-size: 2rem;
  }

  .bulb-grid {
    grid-template-columns: 1fr;
    gap: 1rem;
  }

  .status-tabs {
    width: 100%;
    max-width: 400px;
  }

  .tab-item {
    flex: 1;
    justify-content: center;
    padding: 10px 16px;
    font-size: 0.9rem;
  }

  .card-header {
    padding: 1rem;
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }

  .card-actions {
    margin-left: 0;
    width: 100%;
    justify-content: flex-end;
  }

  .mood-select-dropdown {
    width: calc(100vw - 3rem);
    max-width: none;
    left: 50%;
    transform: translateX(-50%);
  }
}

@media (max-width: 480px) {
  .feature-content {
    padding: 0.5rem;
  }

  .feature-title {
    font-size: 1.5rem;
  }

  .feature-subtitle {
    font-size: 1rem;
  }

  .tab-item {
    padding: 8px 12px;
    font-size: 0.85rem;
  }

  .tab-count {
    display: none;
  }

  .card-content {
    padding: 1rem;
  }

  .empty-state {
    padding: 2rem 1rem;
  }

  .empty-bulb-icon {
    font-size: 3rem;
  }

  .empty-title {
    font-size: 1.2rem;
  }

  .empty-description {
    font-size: 0.9rem;
  }
}
</style>
