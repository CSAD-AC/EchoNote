<template>
  <div class="flow-writing-container" :class="{ 'focus-mode': focusMode }">
    <!-- 顶部标题栏 -->
    <div class="top-header" v-show="!focusMode">
      <div class="brand-title" @click="goHome">
        <span class="echo-text">Echo</span><span class="note-text">Note</span>
      </div>
      <div class="header-controls">
        <button class="control-btn" @click="toggleFocusMode" title="专注模式">
          <Hide :size="16" />
        </button>
      </div>
    </div>

    <!-- 固定侧边栏 -->
    <div
      class="fixed-sidebar"
      :class="{
        collapsed: sidebarCollapsed && !sidebarHovered,
        'hover-expanded': sidebarCollapsed && sidebarHovered,
      }"
      @mouseenter="handleSidebarMouseEnter"
      @mouseleave="handleSidebarMouseLeave"
      v-show="!focusMode"
    >
      <div class="sidebar-header">
        <h3 class="sidebar-title">文档管理</h3>
      </div>

      <div class="sidebar-content">
        <!-- 功能按钮区域 -->
        <div class="function-buttons">
          <button
            class="func-btn"
            @click="showSettings = !showSettings"
            :class="{ active: showSettings }"
          >
            <Setting :size="12" />
          </button>
          <!-- 收缩按钮 -->
          <button
            class="func-btn sidebar-toggle"
            :class="{ collapsed: sidebarCollapsed }"
            @click="toggleSidebar"
            :title="sidebarCollapsed ? '展开侧边栏' : '收缩侧边栏'"
          >
            <Hide :size="12" :class="{ rotated: sidebarCollapsed }" />
          </button>
        </div>

        <!-- 设置面板 -->
        <div class="settings-panel" v-show="showSettings">
          <div class="setting-item">
            <label class="setting-label">
              <input
                type="checkbox"
                v-model="settings.autoIndent"
                @change="saveSettings"
              />
              段落前空四格
            </label>
          </div>
          <div class="setting-item">
            <label class="setting-label">字体大小</label>
            <select v-model="settings.fontSize" @change="saveSettings">
              <option value="14">小</option>
              <option value="16">中</option>
              <option value="18">大</option>
            </select>
          </div>
          <div class="setting-item">
            <label class="setting-label">文字对齐</label>
            <select v-model="settings.textAlign" @change="saveSettings">
              <option value="left">左对齐</option>
              <option value="center">居中对齐</option>
            </select>
          </div>
          <div class="setting-item">
            <label class="setting-label">自动保存间隔</label>
            <select v-model="settings.autoSaveInterval" @change="saveSettings">
              <option :value="30000">30秒</option>
              <option :value="60000">1分钟</option>
              <option :value="120000">2分钟</option>
              <option :value="180000">3分钟</option>
            </select>
          </div>
        </div>

        <!-- 分类管理 -->
        <div class="category-section">
          <div class="section-header">
            <h4>文档分类</h4>
            <button
              class="add-btn"
              @click="showAddCategory = true"
              title="添加分类"
            >
              <Plus :size="14" />
            </button>
          </div>

          <div class="category-list" v-loading="loading">
            <div
              v-for="category in categories"
              :key="category.id"
              class="category-item"
              :class="{ active: selectedCategory === category.id }"
              @click="selectCategory(category.id)"
            >
              <span class="category-icon">📁</span>
              <span class="category-name">{{ category.name }}</span>
              <div class="category-actions">
                <span class="doc-count">{{ getDocCount(category.id) }}</span>
                <button
                  class="action-btn"
                  @click.stop="showRenameCategoryDialog(category)"
                  title="重命名分类"
                >
                  <Setting :size="10" />
                </button>
                <button
                  class="action-btn delete-btn"
                  @click.stop="deleteCategory(category.id)"
                  title="删除分类"
                >
                  <Delete :size="10" />
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- 文档列表 -->
        <div class="doc-section">
          <div class="section-header">
            <h4>文档列表</h4>
            <button class="add-btn" @click="createNewDocument" title="新建文档">
              <DocumentAdd :size="14" />
            </button>
          </div>

          <div class="doc-list" v-loading="loading">
            <div
              v-for="doc in filteredDocs"
              :key="doc.id"
              class="doc-item"
              :class="{ active: selectedDoc === doc.id }"
              @click="selectDocument(doc)"
            >
              <div class="doc-title">{{ doc.title || "无标题" }}</div>
              <div class="doc-preview">{{ doc.preview }}</div>
              <div class="doc-meta">
                <div class="doc-info">
                  <span class="doc-date">{{ formatDate(doc.updatedAt) }}</span>
                  <span class="word-count">{{ doc.wordCount }} 字</span>
                </div>
                <button
                  class="action-btn delete-btn"
                  @click.stop="deleteText(doc.id)"
                  title="删除文档"
                >
                  <Delete :size="10" />
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 主写作区域 -->
    <div
      class="writing-area"
      :class="{
        'sidebar-active': !focusMode && !sidebarCollapsed,
        'sidebar-collapsed': !focusMode && sidebarCollapsed && !sidebarHovered,
        'sidebar-hover': !focusMode && sidebarCollapsed && sidebarHovered,
      }"
    >
      <!-- 写作工具栏 -->
      <div class="writing-toolbar" v-show="!focusMode">
        <div class="toolbar-left">
          <input
            v-model="currentDoc.title"
            class="title-input"
            placeholder="输入标题..."
          />
        </div>
        <div class="toolbar-right">
          <div class="format-tools">
            <button class="tool-btn" @click="formatDocument" title="一键格式化">
              <DocumentCopy :size="12" />
            </button>
            <button class="tool-btn" @click="showHistory" title="历史记录">
              <span style="font-size: 12px">🕰️</span>
            </button>
            <button
              class="tool-btn"
              @click="insertFormat('**')"
              title="加粗 (Ctrl+B)"
            >
              <strong>B</strong>
            </button>
            <button
              class="tool-btn"
              @click="insertFormat('*')"
              title="斜体 (Ctrl+E)"
            >
              <em>I</em>
            </button>
          </div>
          <div class="writing-stats">
            <span class="stat-item">{{ wordCount }} 字</span>
            <span class="stat-item">{{ readingTime }} 分钟阅读</span>
          </div>
          <button
            class="tool-btn"
            @click="toggleFocusBeam"
            :class="{ active: focusBeam }"
            title="聚焦光束"
          >
            <Aim :size="16" />
          </button>
        </div>
      </div>

      <!-- 写作编辑器 -->
      <div class="editor-container">
        <div
          class="focus-beam"
          v-show="focusBeam && (!focusMode || focusMode)"
          :style="focusBeamStyle"
        ></div>
        <!-- 统一编辑器 -->
        <div
          ref="editor"
          class="unified-editor"
          contenteditable="true"
          @input="handleInput"
          @keydown="handleKeydown"
          @click="updateFocusBeam"
          @select="handleEditorSelection"
          @paste="handlePaste"
          :style="{
            fontSize: settings.fontSize + 'px',
            textAlign: settings.textAlign,
          }"
          spellcheck="false"
        ></div>
      </div>
    </div>

    <!-- 添加分类对话框 -->
    <div
      class="modal-overlay"
      v-show="showAddCategory"
      @click="showAddCategory = false"
    >
      <div class="modal-content" @click.stop>
        <h3>添加分类</h3>
        <input
          v-model="newCategoryName"
          placeholder="输入分类名称"
          @keyup.enter="addCategory"
        />
        <div class="modal-actions">
          <button @click="addCategory">确定</button>
          <button @click="showAddCategory = false">取消</button>
        </div>
      </div>
    </div>

    <!-- 重命名分类对话框 -->
    <div
      class="modal-overlay"
      v-show="showRenameCategory"
      @click="showRenameCategory = false"
    >
      <div class="modal-content" @click.stop>
        <h3>重命名分类</h3>
        <input
          v-model="renameCategoryName"
          placeholder="输入新的分类名称"
          @keyup.enter="renameCategoryConfirm"
        />
        <div class="modal-actions">
          <button @click="renameCategoryConfirm">确定</button>
          <button @click="showRenameCategory = false">取消</button>
        </div>
      </div>
    </div>

    <!-- 历史记录对话框 -->
    <el-dialog
      v-model="showHistoryDialog"
      title="文章历史记录"
      width="80%"
      :close-on-click-modal="false"
    >
      <div v-loading="loadingHistory" class="history-content">
        <div v-if="historyRecords.length === 0" class="empty-history">
          <div class="empty-icon">📄</div>
          <p>暂无历史记录</p>
        </div>
        <div v-else class="history-list">
          <div
            v-for="record in historyRecords"
            :key="record.id"
            class="history-item"
          >
            <div class="history-header">
              <div class="version-info">
                <span class="version-badge">v{{ record.version }}</span>
                <h4 class="history-title">{{ record.title }}</h4>
              </div>
              <div class="history-meta">
                <span class="history-time">
                  {{ formatDate(record.createTime) }}
                </span>
              </div>
            </div>
            <div class="history-content-preview">
              {{ record.content.substring(0, 200) }}
              <span v-if="record.content.length > 200">...</span>
            </div>
            <div class="history-actions">
              <el-button size="small" @click="previewVersion(record)">
                👁️ 预览
              </el-button>
              <el-button
                size="small"
                type="warning"
                @click="revertToVersion(record)"
              >
                ⬅️ 回退到此版本
              </el-button>
            </div>
          </div>
        </div>
      </div>
      <template #footer>
        <el-button @click="showHistoryDialog = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, nextTick } from "vue";
import { useRouter, onBeforeRouteLeave } from "vue-router";
import {
  Hide,
  Plus,
  Aim,
  Setting,
  DocumentCopy,
  DocumentAdd,
  Delete,
} from "@element-plus/icons-vue";
import {
  getCategories,
  getTexts,
  getTextHistory,
  resetTextVersion,
  addCategory as apiAddCategory,
  addText as apiAddText,
  deleteCategory as apiDeleteCategory,
  deleteText as apiDeleteText,
  renameCategory as apiRenameCategory,
  updateText as apiUpdateText,
} from "@/utils/api";
import { ElMessage, ElMessageBox } from "element-plus";

const router = useRouter();

// 状态管理
const focusMode = ref(false);
const focusBeam = ref(false);
const showSettings = ref(false);
const showAddCategory = ref(false);
const showRenameCategory = ref(false);
const selectedCategory = ref(null);
const selectedDoc = ref(null);
const newCategoryName = ref("");
const renameCategoryName = ref("");
const renameCategoryId = ref(null);
const sidebarCollapsed = ref(false); // 侧边栏收缩状态
const sidebarHovered = ref(false); // 侧边栏悬停状态
const loading = ref(false); // 加载状态
const showHistoryDialog = ref(false); // 显示历史记录对话框
const historyRecords = ref([]); // 历史记录列表
const loadingHistory = ref(false); // 历史记录加载状态

// 自动保存相关
const autoSaveTimer = ref(null);

// 编辑器引用
const editor = ref(null);

// 聚焦光束状态
const focusBeamStyle = ref({});

// 编辑器状态
const isFormatting = ref(false); // 添加格式化标志位

// 设置配置
const settings = ref({
  autoIndent: true,
  fontSize: 16,
  textAlign: "left", // 新增文字对齐设置，默认左对齐
  autoSaveInterval: 180000, // 自动保存间隔，默认3分钟
});

// 文档数据
const currentDoc = ref({
  id: null,
  title: "",
  content: "",
  categoryId: null,
  createdAt: new Date(),
  updatedAt: new Date(),
  wordCount: 0,
});

// 分类数据
const categories = ref([]);

// 文档列表
const documents = ref([]);

// 计算属性
const filteredDocs = computed(() => {
  if (!selectedCategory.value) {
    return documents.value;
  }
  return documents.value.filter(
    (doc) => doc.categoryId === selectedCategory.value
  );
});

const wordCount = computed(() => {
  return getPlainText(currentDoc.value.content).replace(/\s/g, "").length;
});

const readingTime = computed(() => {
  const wordsPerMinute = 200;
  return Math.ceil(wordCount.value / wordsPerMinute) || 1;
});

// 加载数据的方法
const loadCategories = async () => {
  try {
    loading.value = true;
    const response = await getCategories();
    if (response.code === 200) {
      categories.value = response.data || [];
    } else {
      ElMessage.error(response.message || "获取分类失败");
    }
  } catch (error) {
    console.error("获取分类错误:", error);
    ElMessage.error("获取分类失败");
  } finally {
    loading.value = false;
  }
};

const addCategory = async () => {
  if (!newCategoryName.value.trim()) return;

  try {
    // 获取当前用户ID（这里假设从localStorage获取，实际项目中可能需要从store或其他地方获取）
    const userInfo = JSON.parse(localStorage.getItem("userInfo") || "{}");
    const userId = userInfo.id || 1; // 默认用户ID为1

    const response = await apiAddCategory({
      userId: userId,
      name: newCategoryName.value.trim(),
    });

    if (response.code === 200) {
      ElMessage.success("分类添加成功");
      newCategoryName.value = "";
      showAddCategory.value = false;
      // 重新加载分类列表
      await loadCategories();
    } else {
      ElMessage.error(response.message || "添加分类失败");
    }
  } catch (error) {
    console.error("添加分类错误:", error);
    ElMessage.error("添加分类失败");
  }
};

const showRenameCategoryDialog = (category) => {
  renameCategoryId.value = category.id;
  renameCategoryName.value = category.name;
  showRenameCategory.value = true;
};

const renameCategoryConfirm = async () => {
  if (!renameCategoryName.value.trim()) return;

  try {
    const response = await apiRenameCategory({
      id: renameCategoryId.value,
      name: renameCategoryName.value.trim(),
    });

    if (response.code === 200) {
      ElMessage.success("分类重命名成功");
      showRenameCategory.value = false;
      // 更新分类名称
      const category = categories.value.find(
        (cat) => cat.id === renameCategoryId.value
      );
      if (category) {
        category.name = renameCategoryName.value.trim();
      }
    } else {
      ElMessage.error(response.message || "重命名分类失败");
    }
  } catch (error) {
    console.error("重命名分类错误:", error);
    ElMessage.error("重命名分类失败");
  }
};

const loadTexts = async () => {
  try {
    const response = await getTexts();
    if (response.code === 200) {
      // 将API返回的数据格式转换为组件需要的格式
      documents.value = (response.data || []).map((item) => ({
        id: item.id,
        title: item.title || "无标题",
        content: item.content || "",
        categoryId: item.categoryId,
        preview: item.content
          ? item.content.replace(/[#*`]/g, "").slice(0, 100) +
            (item.content.length > 100 ? "..." : "")
          : "无内容",
        createdAt: item.createTime ? new Date(item.createTime) : new Date(),
        updatedAt: item.updateTime ? new Date(item.updateTime) : new Date(),
        wordCount: item.content ? item.content.replace(/\s/g, "").length : 0,
      }));
    } else {
      ElMessage.error(response.message || "获取文章失败");
    }
  } catch (error) {
    console.error("获取文章错误:", error);
    ElMessage.error("获取文章失败");
  } finally {
    loading.value = false;
  }
};

const loadData = async () => {
  await Promise.all([loadCategories(), loadTexts()]);
};

// 历史记录相关方法
const showHistory = async () => {
  if (!currentDoc.value.id) {
    ElMessage.warning("请先选择一个文档");
    return;
  }

  try {
    loadingHistory.value = true;
    showHistoryDialog.value = true;
    const response = await getTextHistory(currentDoc.value.id);
    if (response.code === 200) {
      // 过滤掉当前版本的历史记录
      const currentContent = currentDoc.value.content || "";
      const currentTitle = currentDoc.value.title || "";
      historyRecords.value = (response.data || []).filter(
        (record) =>
          record.content !== currentContent || record.title !== currentTitle
      );
    } else {
      ElMessage.error(response.message || "获取历史记录失败");
    }
  } catch (error) {
    console.error("获取历史记录错误:", error);
    ElMessage.error("获取历史记录失败");
  } finally {
    loadingHistory.value = false;
  }
};

const revertToVersion = async (record) => {
  try {
    await ElMessageBox.confirm(
      `确定要回退到版本 ${record.version} 吗？\n\n标题：${
        record.title
      }\n创建时间：${formatDate(
        record.createTime
      )}\n\n注意：此操作将会覆盖当前文档内容！`,
      "版本回退确认",
      {
        confirmButtonText: "确定回退",
        cancelButtonText: "取消",
        type: "warning",
        dangerouslyUseHTMLString: true,
      }
    );

    const response = await resetTextVersion({
      textId: currentDoc.value.id,
      version: record.version,
    });

    if (response.code === 200) {
      ElMessage.success("版本回退成功");
      // 更新当前文档内容
      currentDoc.value.title = record.title;
      currentDoc.value.content = record.content;
      // 更新编辑器内容
      updateEditorContent();
      // 关闭对话框
      showHistoryDialog.value = false;
      // 重新加载文档列表以更新显示
      await loadTexts();
    } else {
      ElMessage.error(response.message || "版本回退失败");
    }
  } catch (error) {
    if (error !== "cancel") {
      console.error("版本回退错误:", error);
      ElMessage.error("版本回退失败");
    }
  }
};

const previewVersion = (record) => {
  // 显示版本预览（可以在对话框中展开显示全文）
  ElMessageBox.alert(
    `<div style="max-height: 400px; overflow-y: auto; line-height: 1.6;">${record.content.replace(
      /\n/g,
      "<br>"
    )}</div>`,
    `预览版本 ${record.version} - ${record.title}`,
    {
      dangerouslyUseHTMLString: true,
      confirmButtonText: "关闭",
    }
  );
};

const toggleFocusMode = () => {
  focusMode.value = !focusMode.value;
};

const toggleFocusBeam = () => {
  focusBeam.value = !focusBeam.value;
  if (focusBeam.value) {
    nextTick(() => {
      updateFocusBeam();
    });
  }
};

const toggleSidebar = () => {
  sidebarCollapsed.value = !sidebarCollapsed.value;
};

const handleSidebarMouseEnter = () => {
  sidebarHovered.value = true;
};

const handleSidebarMouseLeave = () => {
  sidebarHovered.value = false;
};

const selectCategory = (categoryId) => {
  selectedCategory.value = categoryId;
};

const selectDocument = (doc) => {
  selectedDoc.value = doc.id;
  currentDoc.value = { ...doc };
  nextTick(() => {
    updateEditorContent();
    if (focusBeam.value) {
      updateFocusBeam();
    }
  });
};

const createNewDocument = async () => {
  const defaultCategoryId =
    selectedCategory.value || categories.value[0]?.id || null;

  // 如果没有选中分类且没有默认分类，提示用户先创建分类
  if (!defaultCategoryId) {
    ElMessage.warning("请先创建一个分类");
    showAddCategory.value = true;
    return;
  }

  try {
    const response = await apiAddText({
      categoryId: defaultCategoryId,
    });

    if (response.code === 200) {
      ElMessage.success("文档创建成功");

      // 从响应中获取textId
      const textId = response.data?.textId || Date.now();

      // 创建本地文档对象
      const newDoc = {
        id: textId,
        title: "这是一个示例标题",
        content: "这是示例内容",
        categoryId: defaultCategoryId,
        createdAt: new Date(),
        updatedAt: new Date(),
        wordCount: 0,
        preview: "",
      };

      documents.value.unshift(newDoc);
      selectDocument(newDoc);

      // 创建文档后立即保存一次
      await saveDocument();

      nextTick(() => {
        editor.value?.focus();
        updateEditorContent();
      });
    } else {
      ElMessage.error(response.message || "创建文档失败");
    }
  } catch (error) {
    console.error("创建文档错误:", error);
    ElMessage.error("创建文档失败");
  }
};

// 添加一个标志位，防止重复保存
let isSaving = false;

// 修改 saveDocument 函数，添加防重复机制
const saveDocument = async () => {
  // 如果正在保存，则直接返回
  if (isSaving) {
    return;
  }

  // 如果没有文档ID，则直接返回
  if (!currentDoc.value.id) return;

  // 设置保存标志位
  isSaving = true;

  try {
    // 获取最新历史记录进行比较
    try {
      const historyResponse = await getTextHistory(currentDoc.value.id);
      if (
        historyResponse.code === 200 &&
        historyResponse.data &&
        historyResponse.data.length > 0
      ) {
        // 获取最新的历史记录（按版本号排序）
        const sortedHistory = historyResponse.data.sort(
          (a, b) => b.version - a.version
        );
        const latestVersion = sortedHistory[0];

        // 计算字数差异
        const currentContent = currentDoc.value.content || "";
        const latestContent = latestVersion.content || "";

        const currentWordCount = currentContent.replace(/\s/g, "").length;
        const latestWordCount = latestContent.replace(/\s/g, "").length;
        const wordCountDifference = Math.abs(
          currentWordCount - latestWordCount
        );

        // 如果字数差异小于20字，则不提交更新
        if (wordCountDifference < 20) {
          console.log(
            `字数差异过小（${wordCountDifference}字），跳过本次更新提交`
          );
          return;
        }
      }
    } catch (error) {
      console.warn("获取历史记录失败，将继续保存文档:", error);
    }

    const docIndex = documents.value.findIndex(
      (doc) => doc.id === currentDoc.value.id
    );
    if (docIndex !== -1) {
      currentDoc.value.updatedAt = new Date();
      currentDoc.value.wordCount = wordCount.value;
      currentDoc.value.preview =
        currentDoc.value.content.replace(/[#*`]/g, "").slice(0, 100) +
        (currentDoc.value.content.length > 100 ? "..." : "");
      documents.value[docIndex] = { ...currentDoc.value };
    }

    // 调用API保存文档
    try {
      const response = await apiUpdateText({
        textId: currentDoc.value.id,
        title: currentDoc.value.title || "无标题",
        content: currentDoc.value.content,
      });

      if (response.code !== 200) {
        console.error("保存文档失败:", response.message);
      }
    } catch (error) {
      console.error("保存文档错误:", error);
    }
  } finally {
    // 保存完成后重置标志位
    isSaving = false;
  }
};

const deleteCategory = async (categoryId) => {
  try {
    await ElMessageBox.confirm(
      "确定要删除这个分类吗？分类下的所有文档也将被删除。",
      "删除分类确认",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }
    );

    const response = await apiDeleteCategory(categoryId);

    if (response.code === 200) {
      ElMessage.success("分类删除成功");

      // 删除该分类下的所有文档
      documents.value = documents.value.filter(
        (doc) => doc.categoryId !== categoryId
      );

      // 删除分类
      categories.value = categories.value.filter(
        (cat) => cat.id !== categoryId
      );

      // 如果当前选中的分类被删除，清空选中
      if (selectedCategory.value === categoryId) {
        selectedCategory.value = null;
      }
    } else {
      ElMessage.error(response.message || "删除分类失败");
    }
  } catch (error) {
    if (error !== "cancel") {
      console.error("删除分类错误:", error);
      ElMessage.error("删除分类失败");
    }
  }
};

const deleteText = async (textId) => {
  try {
    await ElMessageBox.confirm("确定要删除这个文档吗？", "删除文档确认", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    });

    const response = await apiDeleteText(textId);

    if (response.code === 200) {
      ElMessage.success("文档删除成功");

      // 删除文档
      documents.value = documents.value.filter((doc) => doc.id !== textId);

      // 如果当前选中的文档被删除，清空选中
      if (selectedDoc.value === textId) {
        selectedDoc.value = null;
        currentDoc.value = {
          id: null,
          title: "",
          content: "",
          categoryId: null,
          createdAt: new Date(),
          updatedAt: new Date(),
          wordCount: 0,
        };
      }
    } else {
      ElMessage.error(response.message || "删除文档失败");
    }
  } catch (error) {
    if (error !== "cancel") {
      console.error("删除文档错误:", error);
      ElMessage.error("删除文档失败");
    }
  }
};

const getDocCount = (categoryId) => {
  return documents.value.filter((doc) => doc.categoryId === categoryId).length;
};

const insertFormat = (prefix) => {
  if (!editor.value) return;

  isFormatting.value = true;

  const selection = window.getSelection();
  if (!selection.rangeCount) {
    isFormatting.value = false;
    return;
  }

  const range = selection.getRangeAt(0);
  const selectedText = range.toString();

  try {
    if (selectedText) {
      // 检查是否选择了格式化元素
      let startContainer = range.startContainer;

      // 向上查找格式化元素
      let formatElement = null;
      let currentElement =
        startContainer.nodeType === Node.TEXT_NODE
          ? startContainer.parentElement
          : startContainer;

      while (currentElement && currentElement !== editor.value) {
        if (
          (prefix === "**" && currentElement.tagName === "STRONG") ||
          (prefix === "*" && currentElement.tagName === "EM")
        ) {
          // 检查是否选择了整个格式化元素的内容
          if (currentElement.textContent === selectedText) {
            formatElement = currentElement;
            break;
          }
        }
        currentElement = currentElement.parentElement;
      }

      if (formatElement) {
        // 移除格式 - 用文本节点替换格式化元素
        const textNode = document.createTextNode(selectedText);
        formatElement.parentNode.replaceChild(textNode, formatElement);

        // 重新选择文本
        const newRange = document.createRange();
        newRange.selectNode(textNode);
        selection.removeAllRanges();
        selection.addRange(newRange);
      } else {
        // 添加格式 - 用格式化元素包装选中文本
        const formatTag = prefix === "**" ? "strong" : "em";
        const formatElement = document.createElement(formatTag);

        // 提取选中内容并包装
        const contents = range.extractContents();
        formatElement.appendChild(contents);
        range.insertNode(formatElement);

        // 选择新插入的格式化元素内容
        const newRange = document.createRange();
        newRange.selectNodeContents(formatElement);
        selection.removeAllRanges();
        selection.addRange(newRange);
      }
    } else {
      // 没有选中文本，在光标位置插入空的格式化元素
      const formatTag = prefix === "**" ? "strong" : "em";
      const formatElement = document.createElement(formatTag);
      formatElement.appendChild(document.createTextNode("\u00a0")); // 插入一个非断空格作为占位符

      range.insertNode(formatElement);

      // 选择格式化元素的内容，这样用户直接打字就会替换占位符
      const newRange = document.createRange();
      newRange.selectNodeContents(formatElement);
      selection.removeAllRanges();
      selection.addRange(newRange);
    }
  } catch (error) {
    console.warn("Format operation failed:", error);
  } finally {
    // 立即更新保存的内容并重置标志
    setTimeout(() => {
      isFormatting.value = false;
      // 直接从编辑器获取纯文本保存，不触发重新渲染
      currentDoc.value.content = extractPlainTextForSave();
      updateFocusBeam();
    }, 0);
  }
};

const formatDocument = () => {
  if (!currentDoc.value.content) return;

  let content = currentDoc.value.content;

  if (settings.value.autoIndent) {
    // 段落前空四格
    content = content.replace(/^(?!\s)/gm, "    ");
  }

  // 清理多余的空行
  content = content.replace(/\n\s*\n\s*\n/g, "\n\n");

  currentDoc.value.content = content;
  updateEditorContent();
};

const saveSettings = () => {
  localStorage.setItem("flowWritingSettings", JSON.stringify(settings.value));
  // 重新启动自动保存定时器
  startAutoSaveTimer();
};

const loadSettings = () => {
  const saved = localStorage.getItem("flowWritingSettings");
  if (saved) {
    settings.value = { ...settings.value, ...JSON.parse(saved) };
  }
};

// ContentEditable 相关方法

// 从编辑器直接提取纯文本用于保存（不经过HTML转换）
const extractPlainTextForSave = () => {
  if (!editor.value) return "";

  const extractText = (node) => {
    if (node.nodeType === Node.TEXT_NODE) {
      return node.textContent;
    } else if (node.nodeType === Node.ELEMENT_NODE) {
      if (node.tagName === "BR") {
        return "\n";
      } else if (node.tagName === "STRONG") {
        const innerText = Array.from(node.childNodes)
          .map((child) => extractText(child))
          .join("");
        return "**" + innerText + "**";
      } else if (node.tagName === "EM") {
        const innerText = Array.from(node.childNodes)
          .map((child) => extractText(child))
          .join("");
        return "*" + innerText + "*";
      } else if (node.classList && node.classList.contains("placeholder")) {
        return ""; // 忽略占位符
      } else {
        let text = "";
        for (let child of node.childNodes) {
          text += extractText(child);
        }
        return text;
      }
    }
    return "";
  };

  return extractText(editor.value);
};

// 从 HTML 字符串获取纯文本（仅用于显示，不用于保存）
const getPlainText = (html) => {
  if (!html) return "";
  const tempDiv = document.createElement("div");
  tempDiv.innerHTML = html;
  return tempDiv.textContent || tempDiv.innerText || "";
};

// 从纯文本生成 HTML
const generateHTML = (plainText) => {
  if (!plainText)
    return '<div class="placeholder">开始写作...支持<strong>**加粗**</strong>和<em>*斜体*</em>格式</div>';

  let html = plainText;

  // 使用临时占位符避免冲突
  // 先处理加粗文本，用占位符替换
  const boldPlaceholder = "___BOLD_PLACEHOLDER___";
  const boldMatches = [];
  html = html.replace(/\*\*(.*?)\*\*/g, (match, content) => {
    const placeholder = `${boldPlaceholder}${boldMatches.length}`;
    boldMatches.push(`<strong>${content}</strong>`);
    return placeholder;
  });

  // 再处理斜体文本
  html = html.replace(/\*([^*\n]+?)\*/g, "<em>$1</em>");

  // 恢复加粗标记
  boldMatches.forEach((boldHtml, index) => {
    html = html.replace(`${boldPlaceholder}${index}`, boldHtml);
  });

  // 换行
  html = html.replace(/\n/g, "<br>");

  return html;
};

// 更新编辑器内容
const updateEditorContent = () => {
  if (!editor.value) return;
  const html = generateHTML(currentDoc.value.content);
  editor.value.innerHTML = html;
};

// 在 saveSettings 函数后添加新的函数
const startAutoSaveTimer = () => {
  // 清除现有的定时器
  if (autoSaveTimer.value) {
    clearInterval(autoSaveTimer.value);
  }

  // 设置新的定时器
  if (settings.value.autoSaveInterval > 0) {
    autoSaveTimer.value = setInterval(() => {
      if (currentDoc.value.id && !isSaving) {
        saveDocument();
      }
    }, settings.value.autoSaveInterval);
  }
};

const stopAutoSaveTimer = () => {
  if (autoSaveTimer.value) {
    clearInterval(autoSaveTimer.value);
    autoSaveTimer.value = null;
  }
};

// 全局键盘事件处理器（主要处理ESC键）
const handleGlobalKeydown = (e) => {
  // ESC 键退出专注模式
  if (e.key === "Escape" && focusMode.value) {
    e.preventDefault();
    toggleFocusMode();
  }
};

const handleKeydown = (e) => {
  // 只在编辑器内处理格式化快捷键
  if (!editor.value || e.target !== editor.value) {
    return;
  }

  // Ctrl+B 加粗
  if (e.ctrlKey && e.key === "b") {
    e.preventDefault();
    insertFormat("**");
    return;
  }

  // Ctrl+E 斜体
  if (e.ctrlKey && e.key === "e") {
    e.preventDefault();
    insertFormat("*");
    return;
  }

  // 回车键自动添加段首缩进
  if (e.key === "Enter") {
    e.preventDefault();

    const selection = window.getSelection();
    if (!selection.rangeCount) return;

    const range = selection.getRangeAt(0);

    // 创建新行
    const br = document.createElement("br");
    range.deleteContents();
    range.insertNode(br);
    range.collapse(false);

    // 如果开启了段落前空四格设置，则添加缩进
    if (settings.value.autoIndent) {
      const indentText = document.createTextNode("\u00a0\u00a0\u00a0\u00a0"); // 4个非断空格
      range.insertNode(indentText);
      range.setStartAfter(indentText);
    } else {
      range.setStartAfter(br);
    }

    range.collapse(false);
    selection.removeAllRanges();
    selection.addRange(range);

    // 触发输入事件
    handleInput();
    return;
  }
};

const updateFocusBeam = () => {
  if (!editor.value || !focusBeam.value) return;

  const selection = window.getSelection();
  if (!selection.rangeCount) return;

  const range = selection.getRangeAt(0);
  const rect = range.getBoundingClientRect();
  const editorRect = editor.value.getBoundingClientRect();

  // 计算相对位置
  const top = rect.top - editorRect.top;
  const lineHeight = parseInt(getComputedStyle(editor.value).lineHeight) || 24;

  focusBeamStyle.value = {
    top: `${Math.max(0, top - lineHeight)}px`,
    left: "0",
    right: "0",
    height: `${lineHeight * 3}px`,
  };
};

const formatDate = (date) => {
  const dateObj = new Date(date);
  const year = dateObj.getFullYear();
  const month = String(dateObj.getMonth() + 1).padStart(2, "0");
  const day = String(dateObj.getDate()).padStart(2, "0");
  const hours = String(dateObj.getHours()).padStart(2, "0");
  const minutes = String(dateObj.getMinutes()).padStart(2, "0");
  const seconds = String(dateObj.getSeconds()).padStart(2, "0");

  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
};

// 输入事件处理器
const handleInput = () => {
  if (!editor.value || isFormatting.value) return;

  // 更新内容但不立即保存，由定时器控制自动保存
  const newContent = extractPlainTextForSave();
  if (newContent !== currentDoc.value.content) {
    currentDoc.value.content = newContent;
  }

  if (focusBeam.value) {
    updateFocusBeam();
  }
};

const handlePaste = (e) => {
  e.preventDefault();

  // 获取粘贴板的纯文本
  const text = e.clipboardData.getData("text/plain");

  // 插入到当前位置
  const selection = window.getSelection();
  if (selection.rangeCount) {
    const range = selection.getRangeAt(0);
    range.deleteContents();
    range.insertNode(document.createTextNode(text));
    range.collapse(false);
  }

  // 触发输入事件
  handleInput();
};

const handleEditorSelection = () => {
  if (focusBeam.value) {
    updateFocusBeam();
  }
};

// 添加窗口关闭前的事件监听器
const handleBeforeUnload = () => {
  // 静默保存当前文档
  if (currentDoc.value.id && !isSaving) {
    // 使用同步 XMLHttpRequest 实现静默保存，确保在页面关闭前发送请求
    try {
      // 准备要发送的数据
      const saveData = {
        textId: currentDoc.value.id,
        title: currentDoc.value.title || "无标题",
        content: currentDoc.value.content,
      };

      // 获取保存文档的API URL
      const apiUrl = "http://localhost:8080/api/writing/text";

      // 获取认证token
      const token = localStorage.getItem("token");

      // 创建同步 XMLHttpRequest
      const xhr = new XMLHttpRequest();
      xhr.open("PUT", apiUrl, false); // false 表示同步请求

      // 设置请求头
      xhr.setRequestHeader("Content-Type", "application/json;charset=utf-8");
      if (token) {
        xhr.setRequestHeader("Authorization", `Bearer ${token}`);
      }

      // 发送请求
      xhr.send(JSON.stringify(saveData));

      // 检查响应状态
      if (xhr.status >= 200 && xhr.status < 300) {
        console.log("文档保存成功");
      } else {
        console.error("文档保存失败，状态码:", xhr.status);
      }
    } catch (error) {
      console.error("使用 XMLHttpRequest 保存失败:", error);
      try {
        // 如果 XMLHttpRequest 失败，尝试普通保存
        saveDocument();
      } catch (saveError) {
        console.error("普通保存也失败了:", saveError);
      }
    }
  }
  // 不阻止默认行为，实现静默保存
};

// 生命周期
onMounted(async () => {
  loadSettings();

  // 加载数据
  await loadData();

  // 选中第一个文档
  if (documents.value.length > 0) {
    selectDocument(documents.value[0]);
    selectedCategory.value = documents.value[0].categoryId;
  } else {
    createNewDocument();
  }

  // 初始化编辑器内容
  nextTick(() => {
    updateEditorContent();
  });

  // 监听全局键盘事件（主要为ESC键）
  document.addEventListener("keydown", handleGlobalKeydown);

  // 启动自动保存定时器
  startAutoSaveTimer();

  // 添加窗口关闭前的事件监听器
  window.addEventListener("beforeunload", handleBeforeUnload);
});

onUnmounted(() => {
  document.removeEventListener("keydown", handleGlobalKeydown);
  // 清理自动保存定时器
  stopAutoSaveTimer();
  // 移除窗口关闭前的事件监听器
  window.removeEventListener("beforeunload", handleBeforeUnload);

  // 在组件卸载前保存当前文档
  if (currentDoc.value.id && !isSaving) {
    saveDocument();
  }
});

// 添加路由离开前的守卫，实现静默保存
onBeforeRouteLeave((to, from, next) => {
  // 静默保存当前文档
  if (currentDoc.value.id && !isSaving) {
    saveDocument();
  }
  // 允许路由切换
  next();
});

// 方法
const goHome = () => {
  // 在导航到首页前保存当前文档
  if (currentDoc.value.id && !isSaving) {
    saveDocument();
  }
  router.push("/");
};
</script>

<style scoped>
/* 主容器样式 */
.flow-writing-container {
  height: 100vh;
  width: 100vw;
  position: relative;
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 50%, #cbd5e1 100%);
  overflow: hidden;
  font-family: "Inter", -apple-system, BlinkMacSystemFont, "Segoe UI",
    sans-serif;
}

.flow-writing-container.focus-mode {
  background: #1a1a1a;
}

/* 顶部标题栏 */
.top-header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 60px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  z-index: 1001;
}

.brand-title {
  font-size: 24px;
  font-weight: 800;
  cursor: pointer;
  transition: all 0.2s ease;
}

.echo-text {
  background: linear-gradient(135deg, #42b983 0%, #369870 100%);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}

.note-text {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}

.brand-title:hover {
  transform: scale(1.05);
}

.header-controls {
  display: flex;
  gap: 12px;
}

.control-btn {
  width: 32px;
  height: 32px;
  border: 1px solid rgba(0, 0, 0, 0.1);
  background: rgba(255, 255, 255, 0.8);
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s ease;
  color: #64748b;
}

.control-btn:hover {
  background: rgba(255, 255, 255, 1);
  color: #334155;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* 固定侧边栏 */
.fixed-sidebar {
  position: fixed;
  left: 0;
  top: 60px;
  height: calc(100vh - 60px);
  width: 320px;
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(25px) saturate(180%);
  border-right: 1px solid rgba(255, 255, 255, 0.2);
  z-index: 1000;
  box-shadow: 0 0 0 1px rgba(255, 255, 255, 0.05),
    0 4px 32px rgba(0, 0, 0, 0.08), 0 8px 64px rgba(0, 0, 0, 0.04);
  overflow: hidden;
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  transform: translateX(0);
}

/* 收缩状态 */
.fixed-sidebar.collapsed {
  width: 60px;
  transform: translateX(-50px);
  box-shadow: 0 0 0 1px rgba(255, 255, 255, 0.1), 0 2px 16px rgba(0, 0, 0, 0.04);
}

/* 悬停展开 */
.fixed-sidebar.hover-expanded {
  width: 320px;
  transform: translateX(0);
  box-shadow: 0 0 0 1px rgba(255, 255, 255, 0.05),
    0 8px 48px rgba(0, 0, 0, 0.12), 0 16px 96px rgba(0, 0, 0, 0.06);
}

/* 侧边栏内容区域 */
.fixed-sidebar.collapsed .sidebar-content,
.fixed-sidebar.collapsed .sidebar-header {
  opacity: 0;
  visibility: hidden;
  transform: translateX(-20px);
  transition: all 0.3s ease;
}

.fixed-sidebar.hover-expanded .sidebar-content,
.fixed-sidebar.hover-expanded .sidebar-header {
  opacity: 1;
  visibility: visible;
  transform: translateX(0);
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94) 0.1s;
}

.sidebar-header {
  padding: 24px 20px 16px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  background: linear-gradient(
    135deg,
    rgba(66, 185, 131, 0.05) 0%,
    rgba(102, 126, 234, 0.05) 100%
  );
  backdrop-filter: blur(10px);
  position: relative;
}

.sidebar-header::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 1px;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(255, 255, 255, 0.5),
    transparent
  );
}

.sidebar-title {
  margin: 0;
  font-size: 15px;
  font-weight: 700;
  background: linear-gradient(135deg, #42b983 0%, #667eea 100%);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  display: flex;
  align-items: center;
  gap: 8px;
  letter-spacing: 0.5px;
}

.sidebar-title::before {
  content: "📝";
  background: none;
  -webkit-background-clip: unset;
  background-clip: unset;
  color: #42b983;
  filter: drop-shadow(0 0 8px rgba(66, 185, 131, 0.3));
}

.sidebar-content {
  padding: 20px;
  overflow-y: auto;
  height: calc(100% - 80px);
  scrollbar-width: thin;
  scrollbar-color: rgba(66, 185, 131, 0.3) transparent;
}

.sidebar-content::-webkit-scrollbar {
  width: 4px;
}

.sidebar-content::-webkit-scrollbar-track {
  background: transparent;
}

.sidebar-content::-webkit-scrollbar-thumb {
  background: linear-gradient(
    135deg,
    rgba(66, 185, 131, 0.3),
    rgba(102, 126, 234, 0.3)
  );
  border-radius: 2px;
}

.sidebar-content::-webkit-scrollbar-thumb:hover {
  background: linear-gradient(
    135deg,
    rgba(66, 185, 131, 0.5),
    rgba(102, 126, 234, 0.5)
  );
}

/* 功能按钮区域 */
.function-buttons {
  display: flex;
  gap: 6px;
  margin-bottom: 16px;
}

.func-btn {
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(66, 185, 131, 0.1);
  border: 1px solid rgba(66, 185, 131, 0.2);
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
  color: #42b983;
}

.func-btn:hover {
  background: rgba(66, 185, 131, 0.15);
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(66, 185, 131, 0.2);
}

.func-btn.active {
  background: linear-gradient(135deg, #42b983 0%, #667eea 100%);
  color: white;
  border-color: transparent;
  box-shadow: 0 4px 12px rgba(66, 185, 131, 0.3);
}

/* 侧边栏收缩按钮特殊样式 */
.func-btn.sidebar-toggle {
  position: relative;
  transition: all 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}

/* 收缩状态下的按钮样式 */
.func-btn.sidebar-toggle.collapsed {
  background: linear-gradient(135deg, #f59e0b 0%, #ef4444 100%);
  color: white;
  border-color: transparent;
  box-shadow: 0 4px 12px rgba(245, 158, 11, 0.4);
  transform: scale(1.05);
}

.func-btn.sidebar-toggle.collapsed:hover {
  background: linear-gradient(135deg, #d97706 0%, #dc2626 100%);
  box-shadow: 0 6px 16px rgba(245, 158, 11, 0.5);
  transform: scale(1.08) translateY(-1px);
}

/* 收缩按钮旋转动画 */
.func-btn.sidebar-toggle .rotated {
  transform: rotate(180deg);
  transition: transform 0.3s ease;
}

/* 收缩状态指示器 */
.func-btn.sidebar-toggle.collapsed::after {
  content: "";
  position: absolute;
  top: -2px;
  right: -2px;
  width: 8px;
  height: 8px;
  background: linear-gradient(135deg, #ef4444 0%, #dc2626 100%);
  border-radius: 50%;
  border: 2px solid white;
  box-shadow: 0 2px 6px rgba(239, 68, 68, 0.4);
  animation: pulse-indicator 2s infinite;
}

@keyframes pulse-indicator {
  0%,
  100% {
    transform: scale(1);
    opacity: 1;
  }
  50% {
    transform: scale(1.2);
    opacity: 0.8;
  }
}

/* 设置面板 */
.settings-panel {
  background: rgba(0, 0, 0, 0.02);
  border-radius: 8px;
  padding: 12px;
  margin-bottom: 16px;
}

.setting-item {
  margin-bottom: 12px;
}

.setting-label {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 12px;
  color: #64748b;
  cursor: pointer;
}

/* 分类管理 */
.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
}

.section-header h4 {
  margin: 0;
  font-size: 14px;
  font-weight: 600;
  color: #1e293b;
}

.add-btn {
  width: 20px;
  height: 20px;
  border: 1px solid rgba(0, 0, 0, 0.1);
  background: rgba(0, 0, 0, 0.03);
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s ease;
  color: #64748b;
}

.add-btn:hover {
  background: linear-gradient(135deg, #42b983 0%, #667eea 100%);
  color: white;
}

.category-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
  margin-bottom: 4px;
  border: 1px solid transparent;
}

.category-item:hover {
  background: rgba(0, 0, 0, 0.03);
}

.category-item.active {
  background: rgba(66, 185, 131, 0.1);
  border: 1px solid rgba(66, 185, 131, 0.3);
}

.category-icon {
  font-size: 14px;
}

.category-name {
  flex: 1;
  font-size: 13px;
  font-weight: 500;
  color: #334155;
}

.category-actions {
  display: flex;
  align-items: center;
  gap: 6px;
}

.doc-count {
  font-size: 11px;
  color: #94a3b8;
  background: rgba(0, 0, 0, 0.05);
  padding: 2px 6px;
  border-radius: 10px;
  min-width: 16px;
  text-align: center;
}

.action-btn {
  width: 20px;
  height: 20px;
  border: none;
  background: rgba(0, 0, 0, 0.05);
  border-radius: 3px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s ease;
  color: #64748b;
}

.action-btn:hover {
  background: rgba(66, 185, 131, 0.1);
  color: #42b983;
}

.action-btn.delete-btn:hover {
  background: rgba(239, 68, 68, 0.1);
  color: #ef4444;
}

/* 文档列表 */
.doc-list {
  max-height: 300px;
  overflow-y: auto;
}

.doc-item {
  padding: 10px;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
  margin-bottom: 6px;
  border: 1px solid transparent;
}

.doc-item:hover {
  background: rgba(0, 0, 0, 0.03);
}

.doc-item.active {
  background: rgba(66, 185, 131, 0.1);
  border-color: rgba(66, 185, 131, 0.3);
}

.doc-title {
  font-weight: 600;
  font-size: 13px;
  color: #1e293b;
  margin-bottom: 4px;
}

.doc-preview {
  font-size: 11px;
  color: #64748b;
  line-height: 1.3;
  margin-bottom: 6px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.doc-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 10px;
  color: #94a3b8;
}

.doc-info {
  display: flex;
  gap: 8px;
}

/* 写作工具栏 */
.format-tools {
  display: flex;
  gap: 8px;
}

.writing-stats {
  display: flex;
  gap: 12px;
}

.stat-item {
  font-size: 11px;
  color: #64748b;
}

/* 模态框 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
}

.modal-content {
  background: white;
  border-radius: 12px;
  padding: 20px;
  width: 300px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.modal-content h3 {
  margin: 0 0 16px 0;
  font-size: 16px;
  color: #1e293b;
}

.modal-content input {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid rgba(0, 0, 0, 0.1);
  border-radius: 6px;
  font-size: 14px;
  margin-bottom: 16px;
}

.modal-actions {
  display: flex;
  gap: 8px;
  justify-content: flex-end;
}

.modal-actions button {
  padding: 6px 16px;
  border: 1px solid rgba(0, 0, 0, 0.1);
  border-radius: 6px;
  cursor: pointer;
  font-size: 12px;
  transition: all 0.2s ease;
}

.modal-actions button:first-child {
  background: linear-gradient(135deg, #42b983 0%, #667eea 100%);
  color: white;
  border-color: transparent;
}

.modal-actions button:last-child {
  background: rgba(0, 0, 0, 0.03);
  color: #64748b;
}

/* 写作区域 */
.writing-area {
  height: 100vh;
  margin-left: 0;
  margin-top: 60px;
  transition: margin-left 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  display: flex;
  flex-direction: column;
}

.writing-area.sidebar-active {
  margin-left: 320px;
}

.writing-area.sidebar-collapsed {
  margin-left: 10px;
}

.writing-area.sidebar-hover {
  margin-left: 320px;
}

.writing-toolbar {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
  flex-shrink: 0;
}

.toolbar-left {
  flex: 1;
}

.title-input {
  background: none;
  border: none;
  font-size: 18px;
  font-weight: 600;
  color: #1e293b;
  outline: none;
  width: 100%;
  max-width: 400px;
  padding: 8px 0;
}

.title-input::placeholder {
  color: #94a3b8;
}

.toolbar-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.tool-btn {
  width: 28px;
  height: 28px;
  border: 1px solid rgba(0, 0, 0, 0.1);
  background: rgba(255, 255, 255, 0.8);
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s ease;
  color: #64748b;
  font-size: 12px;
}

.tool-btn:hover {
  background: rgba(255, 255, 255, 1);
  color: #334155;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.tool-btn.active {
  background: linear-gradient(135deg, #42b983 0%, #667eea 100%);
  color: white;
  border-color: transparent;
}

/* 编辑器区域 */
.editor-container {
  flex: 1;
  position: relative;
  overflow: hidden;
  min-height: 0;
}

.focus-beam {
  position: absolute;
  background: linear-gradient(
    to bottom,
    transparent 0%,
    rgba(66, 185, 131, 0.1) 20%,
    rgba(66, 185, 131, 0.2) 50%,
    rgba(66, 185, 131, 0.1) 80%,
    transparent 100%
  );
  pointer-events: none;
  transition: all 0.3s ease;
  z-index: 1;
}

/* Markdown编辑器增强 */
.markdown-editor {
  font-family: "Inter", -apple-system, BlinkMacSystemFont, "Segoe UI",
    sans-serif;
  line-height: 1.8;
}

/* 统一编辑器 */
.unified-editor {
  width: 100%;
  height: 100%;
  border: none;
  outline: none;
  font-size: 16px;
  line-height: 1.8;
  color: #1e293b;
  background: transparent;
  padding: 40px;
  font-family: "Inter", -apple-system, BlinkMacSystemFont, "Segoe UI",
    sans-serif;
  box-sizing: border-box;
  min-height: calc(100vh - 180px);
  white-space: pre-wrap;
  word-wrap: break-word;
  overflow-y: auto;
  cursor: text;
  user-select: text;
  -webkit-user-select: text;
  -moz-user-select: text;
  -ms-user-select: text;
  position: relative;
  z-index: 2;
}

.unified-editor:focus {
  outline: none;
}

.unified-editor:empty::before {
  content: "开始写作...支持**加粗**和*斜体*格式";
  color: #94a3b8;
  font-style: italic;
  pointer-events: none;
  position: absolute;
}

.unified-editor strong {
  font-weight: 700;
  color: #0f172a;
}

.unified-editor em {
  font-style: italic;
  color: #374151;
}

.unified-editor::selection {
  background: rgba(66, 185, 131, 0.3);
  color: inherit;
}

.unified-editor::-moz-selection {
  background: rgba(66, 185, 131, 0.3);
  color: inherit;
}

.unified-editor .placeholder {
  color: #94a3b8;
  font-style: italic;
  user-select: none;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
}

/* 专注模式修复 */
.focus-mode {
  background: #1a1a1a !important;
}

.focus-mode .fixed-sidebar,
.focus-mode .top-header,
.focus-mode .writing-toolbar {
  opacity: 0;
  pointer-events: none;
}

.focus-mode .writing-area {
  margin-left: 0 !important;
  margin-top: 0 !important;
}

.focus-mode .editor-container {
  height: 100vh !important;
  min-height: 100vh !important;
}

.focus-mode .unified-editor {
  background: #1a1a1a;
  color: #e2e8f0;
  padding: 80px 60px;
  height: 100vh !important;
  min-height: 100vh !important;
}

.focus-mode .unified-editor:empty::before {
  color: #64748b;
}

.focus-mode .unified-editor strong {
  color: #f1f5f9;
}

.focus-mode .unified-editor em {
  color: #cbd5e1;
}

.focus-mode .unified-editor::selection {
  background: rgba(76, 217, 100, 0.3);
}

.focus-mode .focus-beam {
  display: block !important;
  background: linear-gradient(
    to bottom,
    transparent 0%,
    rgba(76, 217, 100, 0.15) 20%,
    rgba(76, 217, 100, 0.25) 50%,
    rgba(76, 217, 100, 0.15) 80%,
    transparent 100%
  );
}

/* 暗黑模式修复 */
.dark .flow-writing-container {
  background: linear-gradient(135deg, #0f172a 0%, #1e293b 50%, #334155 100%);
}

.dark .top-header {
  background: rgba(15, 23, 42, 0.95);
  border-bottom-color: rgba(255, 255, 255, 0.1);
}

.dark .brand-title .echo-text {
  background: linear-gradient(135deg, #4cd964 0%, #34d058 100%);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}

.dark .brand-title .note-text {
  background: linear-gradient(135deg, #a78bfa 0%, #8b5cf6 100%);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}

.dark .control-btn {
  background: rgba(255, 255, 255, 0.05);
  border-color: rgba(255, 255, 255, 0.1);
  color: #94a3b8;
}

.dark .control-btn:hover {
  background: rgba(255, 255, 255, 0.1);
  color: #e2e8f0;
}

.dark .fixed-sidebar {
  background: rgba(15, 23, 42, 0.95);
  border-right-color: rgba(255, 255, 255, 0.1);
}

.dark .settings-panel {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.dark .setting-label {
  color: #94a3b8;
}

.dark .section-header h4 {
  color: #e2e8f0;
}

.dark .add-btn {
  background: rgba(255, 255, 255, 0.05);
  border-color: rgba(255, 255, 255, 0.1);
  color: #94a3b8;
}

.dark .add-btn:hover {
  background: linear-gradient(135deg, #4cd964 0%, #a78bfa 100%);
  color: white;
}

.dark .category-item {
  border-color: rgba(255, 255, 255, 0.05);
}

.dark .category-item:hover {
  background: rgba(255, 255, 255, 0.03);
}

.dark .category-item.active {
  background: rgba(76, 217, 100, 0.1);
  border-color: rgba(76, 217, 100, 0.3);
}

.dark .category-name {
  color: #e2e8f0;
}

.dark .doc-count {
  background: rgba(255, 255, 255, 0.1);
  color: #94a3b8;
}

.dark .action-btn {
  background: rgba(255, 255, 255, 0.05);
  color: #94a3b8;
}

.dark .action-btn:hover {
  background: rgba(76, 217, 100, 0.2);
  color: #4cd964;
}

.dark .action-btn.delete-btn:hover {
  background: rgba(239, 68, 68, 0.2);
  color: #f87171;
}

.dark .doc-item {
  border-color: rgba(255, 255, 255, 0.05);
}

.dark .doc-item:hover {
  background: rgba(255, 255, 255, 0.03);
  border-color: rgba(255, 255, 255, 0.1);
}

.dark .doc-item.active {
  background: rgba(76, 217, 100, 0.1);
  border-color: rgba(76, 217, 100, 0.3);
}

.dark .doc-title {
  color: #e2e8f0;
}

.dark .doc-preview {
  color: #94a3b8;
}

.dark .doc-meta {
  color: #64748b;
}

.dark .writing-toolbar {
  background: rgba(15, 23, 42, 0.8);
  border-bottom-color: rgba(255, 255, 255, 0.1);
}

.dark .title-input {
  color: #e2e8f0;
}

.dark .title-input::placeholder {
  color: #64748b;
}

.dark .tool-btn {
  background: rgba(255, 255, 255, 0.05);
  border-color: rgba(255, 255, 255, 0.1);
  color: #94a3b8;
}

.dark .tool-btn:hover {
  background: rgba(255, 255, 255, 0.1);
  color: #e2e8f0;
}

.dark .tool-btn.active {
  background: linear-gradient(135deg, #4cd964 0%, #a78bfa 100%);
  color: white;
  border-color: transparent;
}

.dark .stat-item {
  color: #94a3b8;
}

/* 暗黑模式样式 */
.dark .unified-editor {
  color: #e2e8f0;
}

.dark .unified-editor:empty::before {
  color: #64748b;
}

.dark .unified-editor strong {
  color: #f1f5f9;
  font-weight: 700;
}

.dark .unified-editor em {
  color: #cbd5e1;
  font-style: italic;
}

.dark .unified-editor::selection {
  background: rgba(76, 217, 100, 0.3);
  color: inherit;
}

.dark .unified-editor::-moz-selection {
  background: rgba(76, 217, 100, 0.3);
  color: inherit;
}

.dark .unified-editor .placeholder {
  color: #64748b;
}

.dark .fixed-sidebar {
  background: rgba(15, 23, 42, 0.85);
  backdrop-filter: blur(25px) saturate(180%);
  border-right: 1px solid rgba(255, 255, 255, 0.1);
  box-shadow: 0 0 0 1px rgba(255, 255, 255, 0.05), 0 4px 32px rgba(0, 0, 0, 0.2),
    0 8px 64px rgba(0, 0, 0, 0.1);
}

.dark .sidebar-header {
  background: linear-gradient(
    135deg,
    rgba(76, 217, 100, 0.05) 0%,
    rgba(167, 139, 250, 0.05) 100%
  );
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}

.dark .sidebar-title {
  background: linear-gradient(135deg, #4cd964 0%, #a78bfa 100%);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}

.dark .sidebar-title::before {
  color: #4cd964;
  filter: drop-shadow(0 0 8px rgba(76, 217, 100, 0.4));
}

/* 暗黑模式下的收缩按钮样式 */
.dark .func-btn {
  background: rgba(255, 255, 255, 0.05);
  border-color: rgba(255, 255, 255, 0.1);
  color: #94a3b8;
}

.dark .func-btn:hover {
  background: rgba(255, 255, 255, 0.1);
  color: #e2e8f0;
}

.dark .func-btn.active {
  background: linear-gradient(135deg, #4cd964 0%, #a78bfa 100%);
  color: white;
  border-color: transparent;
}

.dark .func-btn.sidebar-toggle.collapsed {
  background: linear-gradient(135deg, #f59e0b 0%, #ef4444 100%);
  color: white;
  border-color: transparent;
  box-shadow: 0 4px 12px rgba(245, 158, 11, 0.6);
}

.dark .func-btn.sidebar-toggle.collapsed:hover {
  background: linear-gradient(135deg, #d97706 0%, #dc2626 100%);
  box-shadow: 0 6px 16px rgba(245, 158, 11, 0.8);
}

.dark .func-btn.sidebar-toggle.collapsed::after {
  background: linear-gradient(135deg, #ef4444 0%, #dc2626 100%);
  border-color: rgba(15, 23, 42, 1);
  box-shadow: 0 2px 8px rgba(239, 68, 68, 0.6);
}

.dark .modal-content {
  background: rgba(15, 23, 42, 0.95);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.dark .modal-content h3 {
  color: #e2e8f0;
}

.dark .modal-content input {
  background: rgba(255, 255, 255, 0.05);
  border-color: rgba(255, 255, 255, 0.1);
  color: #e2e8f0;
}

.dark .modal-content input::placeholder {
  color: #64748b;
}

/* 历史记录对话框样式 */
.history-content {
  max-height: 60vh;
  overflow-y: auto;
}

/* 历史记录对话框内容适配 */
.dark .history-content {
  background: transparent;
}

.dark .history-content::-webkit-scrollbar {
  width: 6px;
}

.dark .history-content::-webkit-scrollbar-track {
  background: rgba(255, 255, 255, 0.05);
  border-radius: 3px;
}

.dark .history-content::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.2);
  border-radius: 3px;
}

.dark .history-content::-webkit-scrollbar-thumb:hover {
  background: rgba(255, 255, 255, 0.3);
}

.empty-history {
  text-align: center;
  padding: 2rem;
  color: #6b7280;
}

.dark .empty-history {
  color: #9ca3af;
}

.empty-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
}

.history-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.history-item {
  border: 1px solid rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  padding: 1rem;
  background: rgba(255, 255, 255, 0.5);
  transition: all 0.3s ease;
}

.history-item:hover {
  background: rgba(66, 185, 131, 0.05);
  border-color: rgba(66, 185, 131, 0.2);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.dark .history-item {
  border-color: rgba(255, 255, 255, 0.1);
  background: rgba(255, 255, 255, 0.05);
}

.dark .history-item:hover {
  background: rgba(76, 217, 100, 0.05);
  border-color: rgba(76, 217, 100, 0.2);
}

.history-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.5rem;
}

.version-info {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.version-badge {
  background: linear-gradient(135deg, #42b983 0%, #667eea 100%);
  color: white;
  padding: 0.25rem 0.5rem;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 600;
}

.dark .version-badge {
  background: linear-gradient(135deg, #4cd964 0%, #5ac8fa 100%);
}

.history-title {
  margin: 0;
  font-size: 1rem;
  font-weight: 600;
  color: #1e293b;
}

.dark .history-title {
  color: #e2e8f0;
}

.history-meta {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.history-time {
  font-size: 0.75rem;
  color: #374151;
  font-weight: 500;
}

.dark .history-time {
  color: #d1d5db;
  font-weight: 500;
}

.history-content-preview {
  color: #1f2937;
  line-height: 1.5;
  margin-bottom: 1rem;
  font-size: 0.9rem;
  font-weight: 400;
}

.dark .history-content-preview {
  color: #f3f4f6;
  font-weight: 400;
}

.history-actions {
  display: flex;
  gap: 0.5rem;
  justify-content: flex-end;
}

/* 历史记录按钮样式 */
.history-actions {
  display: flex;
  gap: 0.5rem;
  justify-content: flex-end;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.history-item {
  animation: fadeInUp 0.3s ease;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .history-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }

  .history-meta {
    align-items: flex-start;
  }

  .history-actions {
    justify-content: flex-start;
    flex-wrap: wrap;
  }
}

.dark .modal-actions button:first-child {
  background: linear-gradient(135deg, #4cd964 0%, #a78bfa 100%);
}

.dark .modal-actions button:last-child {
  background: rgba(255, 255, 255, 0.05);
  color: #94a3b8;
  border-color: rgba(255, 255, 255, 0.1);
}

/* 响应式修复 */
@media (max-width: 768px) {
  .fixed-sidebar {
    width: 280px;
  }

  .fixed-sidebar.collapsed {
    width: 50px;
    transform: translateX(-40px);
  }

  .writing-area.sidebar-active {
    margin-left: 280px;
  }

  .writing-area.sidebar-collapsed {
    margin-left: 10px;
  }

  .writing-area.sidebar-hover {
    margin-left: 280px;
  }

  .writing-stats {
    display: none;
  }

  .unified-editor {
    padding: 20px;
    font-size: 14px;
  }

  .focus-mode .unified-editor {
    padding: 40px 20px;
  }
}
</style>

<!-- 全局样式，不使用 scoped，用于 Element Plus 组件覆盖 -->
<style>
/* Element Plus 对话框黑夜模式全局适配 */
.dark .el-dialog {
  background: rgba(15, 23, 42, 0.95) !important;
  border: 1px solid rgba(255, 255, 255, 0.1) !important;
  backdrop-filter: blur(20px) !important;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.5) !important;
}

.dark .el-dialog .el-dialog__body {
  background: transparent !important;
}

.dark .el-dialog__header {
  background: transparent !important;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1) !important;
}

.dark .el-dialog__title {
  color: #e2e8f0 !important;
}

.dark .el-dialog__headerbtn .el-icon {
  color: #94a3b8 !important;
}

.dark .el-dialog__headerbtn:hover .el-icon {
  color: #e2e8f0 !important;
}

.dark .el-dialog__footer {
  border-top: 1px solid rgba(255, 255, 255, 0.1) !important;
  background: transparent !important;
}

.dark .el-overlay {
  background: rgba(0, 0, 0, 0.8) !important;
}

.dark .el-dialog__wrapper {
  background: rgba(0, 0, 0, 0.8) !important;
}

/* 针对不同类型的对话框 */
.dark .el-dialog.el-dialog--center {
  background: rgba(15, 23, 42, 0.95) !important;
}

.dark .el-dialog[role="dialog"] {
  background: rgba(15, 23, 42, 0.95) !important;
}

/* 对话框按钮适配 */
.dark .el-dialog .el-button {
  background: rgba(255, 255, 255, 0.05) !important;
  border-color: rgba(255, 255, 255, 0.1) !important;
  color: #e2e8f0 !important;
}

.dark .el-dialog .el-button:hover {
  background: rgba(255, 255, 255, 0.1) !important;
  border-color: rgba(255, 255, 255, 0.2) !important;
  color: #f1f5f9 !important;
}

.dark .el-dialog .el-button--warning {
  background: rgba(245, 158, 11, 0.1) !important;
  border-color: rgba(245, 158, 11, 0.3) !important;
  color: #fbbf24 !important;
}

.dark .el-dialog .el-button--warning:hover {
  background: rgba(245, 158, 11, 0.2) !important;
  border-color: rgba(245, 158, 11, 0.5) !important;
  color: #fcd34d !important;
}
</style>
