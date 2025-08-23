<template>
  <Layout>
    <section class="feature-content">
      <div class="feature-header">
        <h2>灵光胶囊</h2>
        <p>捕捉瞬时闪现的灵感</p>
        <div class="status-tabs">
          <div
            :class="{ active: currentTab === 'pending' }"
            @click="switchTab('pending')"
          >
            未处理
          </div>
          <div
            :class="{ active: currentTab === 'completed' }"
            @click="switchTab('completed')"
          >
            已处理
          </div>
        </div>
        <el-button
          @click="showAddForm"
          class="add-button"
          type="primary"
          icon="Plus"
        >
          添加灵感
        </el-button>
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
                @blur="handleMoodInputBlur"
              ></el-input>
            </div>
            <div v-if="showMoodSelect" class="mood-select-dropdown expanded">
              <div class="mood-select-header">选择预设心情</div>
              <div class="mood-select-items expanded">
                <el-tag
                  v-for="mood in preMoods"
                  :key="mood.id"
                  size="medium"
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

      <div class="bulb-list">
        <div v-if="filteredBulbs.length === 0" class="empty-state">
          <p>暂无灵感记录，点击右上角按钮添加</p>
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
                  >
                    已解决
                  </el-button>
                  <el-button
                    v-if="bulb.status"
                    size="default"
                    icon="CircleClose"
                    type="warning"
                    @click="markAsPending(bulb.id)"
                  >
                    未处理
                  </el-button>
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

    const response = await getCapsulesByUserId(userId);
    bulbs.value = response.data || [];
    filterBulbs();
  } catch (error) {
    console.error("获取胶囊数据失败:", error);
    ElMessage.error("获取胶囊数据失败，请重试");
  }
});

const fetchPreMoods = async () => {
  try {
    const response = await getPreMoods();
    preMoods.value = response.data || [];
  } catch (error) {
    console.error("获取预设心情失败:", error);
    ElMessage.error("获取预设心情失败，请重试");
  }
};
onMounted(fetchPreMoods);

function switchTab(tab) {
  currentTab.value = tab;
  filterBulbs();

  const bulbGrid = document.querySelector(".bulb-grid");
  if (bulbGrid) {
    bulbGrid.style.opacity = "0";
    setTimeout(() => {
      bulbGrid.style.opacity = "1";
    }, 300);
  }
}

function filterBulbs() {
  if (currentTab.value === "pending") {
    filteredBulbs.value = bulbs.value.filter((bulb) => bulb && !bulb.status);
  } else {
    filteredBulbs.value = bulbs.value.filter((bulb) => bulb && bulb.status);
  }
}

async function markAsSolved(bulbId) {
  try {
    await changeCapsulesStatusById({ id: bulbId });
    const index = bulbs.value.findIndex((bulb) => bulb.id === bulbId);
    if (index !== -1) {
      bulbs.value[index].status = true;
      filterBulbs();
      ElMessage.success("已标记为已解决");
    }
  } catch (error) {
    console.error("标记胶囊为已解决失败:", error);
    ElMessage.error("操作失败，请重试");
  }
}

async function markAsPending(bulbId) {
  try {
    await changeCapsulesStatusById({ id: bulbId });
    const index = bulbs.value.findIndex((bulb) => bulb.id === bulbId);
    if (index !== -1) {
      bulbs.value[index].status = false;
      filterBulbs();
      ElMessage.success("已标记为未处理");
    }
  } catch (error) {
    console.error("标记胶囊为未处理失败:", error);
    ElMessage.error("操作失败，请重试");
  }
}

const handleMoodInputBlur = () => {
  setTimeout(() => {
    showMoodSelect.value = false;
  }, 200);
};

function addPresetMood(mood) {
  const exists = currentBulb.value.moods.some((m) => m.name === mood.name);
  if (!exists) {
    currentBulb.value.moods.push({
      ...mood,
      createTime: new Date().toISOString(),
    });
    currentBulb.value.newMood = null;
  }
  showMoodSelect.value = false;
}

function removeMood(index) {
  currentBulb.value.moods.splice(index, 1);
  currentBulb.value.newMood = null;
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
  padding: 3rem 2rem;
  max-width: 1200px;
  margin: 0 auto;
  flex: 1;
}

.feature-header {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  text-align: center;
  gap: 1rem;
}

/* 状态标签样式 */
.status-tabs {
  display: flex;
  margin-top: 1rem;
  border-radius: 4px;
  overflow: hidden;
  background-color: #f5f5f5;
}

.status-tabs > div {
  padding: 0.5rem 1rem;
  cursor: pointer;
  transition: all 0.3s ease;
}

.status-tabs > div.active {
  background-color: #4096ff;
  color: white;
}

/* 标签切换动画 */
.bulb-grid {
  transition: opacity 0.3s ease;
}

/* 解决按钮样式调整 */
.el-button--success {
  margin-right: 0.5rem;
}

.feature-header h2 {
  font-size: 2rem;
  margin-bottom: 0.5rem;
  color: #42b983;
}

.dark .feature-header p {
  color: #e1e9e3;
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

/* 自定义心情文字样式 */
.text-mood {
  color: #fff;
  background-color: #409eff;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
  margin-right: 6px;
  margin-bottom: 6px;
  display: inline-block;
  border-radius: 4px; /* 确保是方形 */
}

/* 暗黑模式下自定义心情文字样式 */
.dark .text-mood {
  color: #fff;
  background-color: #2a62a9;
  border: 1px solid #409eff;
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
  gap: 8px;
  max-height: 200px;
  overflow-y: auto;
}

.mood-select-dropdown.expanded {
  padding: 15px;
  width: 100%;
  max-width: 400px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.mood-select-items.expanded {
  gap: 12px;
  max-height: 300px;
  padding: 10px 0;
}

.selectable-mood {
  cursor: pointer;
  transition: all 0.2s;
}

.selectable-mood:hover {
  transform: scale(1.05);
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
</style>
