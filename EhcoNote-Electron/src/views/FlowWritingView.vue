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
        'collapsed': sidebarCollapsed && !sidebarHovered,
        'hover-expanded': sidebarCollapsed && sidebarHovered
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
            :class="{ 'collapsed': sidebarCollapsed }"
            @click="toggleSidebar"
            :title="sidebarCollapsed ? '展开侧边栏' : '收缩侧边栏'"
          >
            <Hide :size="12" :class="{ 'rotated': sidebarCollapsed }" />
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

          <div class="category-list">
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

          <div class="doc-list">
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
                <span class="doc-date">{{ formatDate(doc.updatedAt) }}</span>
                <span class="word-count">{{ doc.wordCount }} 字</span>
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
        'sidebar-hover': !focusMode && sidebarCollapsed && sidebarHovered
      }"
    >
      <!-- 写作工具栏 -->
      <div class="writing-toolbar" v-show="!focusMode">
        <div class="toolbar-left">
          <input
            v-model="currentDoc.title"
            class="title-input"
            placeholder="输入标题..."
            @input="saveDocument"
          />
        </div>
        <div class="toolbar-right">
          <div class="format-tools">
            <button class="tool-btn" @click="formatDocument" title="一键格式化">
              <DocumentCopy :size="12" />
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
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, nextTick } from "vue";
import { useRouter } from "vue-router";
import {
  Hide,
  Plus,
  Aim,
  Setting,
  DocumentCopy,
  DocumentAdd,
  Delete,
} from "@element-plus/icons-vue";

const router = useRouter();

// 状态管理
const focusMode = ref(false);
const focusBeam = ref(false);
const showSettings = ref(false);
const showAddCategory = ref(false);
const selectedCategory = ref(null);
const selectedDoc = ref(null);
const newCategoryName = ref("");
const sidebarCollapsed = ref(false); // 侧边栏收缩状态
const sidebarHovered = ref(false); // 侧边栏悬停状态

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
const categories = ref([
  {
    id: 1,
    name: "个人日记",
  },
  {
    id: 2,
    name: "工作笔记",
  },
  {
    id: 3,
    name: "学习笔记",
  },
]);

// 文档列表
const documents = ref([
  {
    id: 1,
    title: "欢迎使用EchoNote",
    content:
      "    这是一个样例文档，您可以开始在这里写作...\n\n    支持**加粗**和*斜体*格式。",
    categoryId: 1,
    preview: "    这是一个样例文档，您可以开始在这里写作...",
    createdAt: new Date("2024-01-01"),
    updatedAt: new Date("2024-01-01"),
    wordCount: 35,
  },
]);

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

// 方法
const goHome = () => {
  router.push("/");
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

const createNewDocument = () => {
  const defaultCategoryId =
    selectedCategory.value ||
    categories.value[0]?.id ||
    null;

  const newDoc = {
    id: Date.now(),
    title: "",
    content: "",
    categoryId: defaultCategoryId,
    createdAt: new Date(),
    updatedAt: new Date(),
    wordCount: 0,
    preview: "",
  };

  documents.value.unshift(newDoc);
  selectDocument(newDoc);
  nextTick(() => {
    editor.value?.focus();
    updateEditorContent();
  });
};

const saveDocument = () => {
  if (!currentDoc.value.id) return;

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
};

const addCategory = () => {
  if (!newCategoryName.value.trim()) return;

  const newCategory = {
    id: Date.now(),
    name: newCategoryName.value.trim(),
  };

  categories.value.push(newCategory);
  newCategoryName.value = "";
  showAddCategory.value = false;
};

const deleteCategory = (categoryId) => {
  if (confirm("确定要删除这个分类吗？所有文档都将被删除。")) {
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
      saveDocument();
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
  saveDocument();
};

const saveSettings = () => {
  localStorage.setItem("flowWritingSettings", JSON.stringify(settings.value));
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
  const now = new Date();
  const diff = now - new Date(date);
  const days = Math.floor(diff / (1000 * 60 * 60 * 24));

  if (days === 0) return "今天";
  if (days === 1) return "昨天";
  if (days < 7) return `${days}天前`;

  return new Date(date).toLocaleDateString();
};

// 输入事件处理器
const handleInput = () => {
  if (!editor.value || isFormatting.value) return;

  // 直接保存内容（默认开启自动保存）
  const newContent = extractPlainTextForSave();
  if (newContent !== currentDoc.value.content) {
    currentDoc.value.content = newContent;
    saveDocument();
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

// 生命周期
onMounted(() => {
  loadSettings();

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
});

onUnmounted(() => {
  document.removeEventListener("keydown", handleGlobalKeydown);
});
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
  box-shadow: 
    0 0 0 1px rgba(255, 255, 255, 0.05),
    0 4px 32px rgba(0, 0, 0, 0.08),
    0 8px 64px rgba(0, 0, 0, 0.04);
  overflow: hidden;
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  transform: translateX(0);
}

/* 收缩状态 */
.fixed-sidebar.collapsed {
  width: 60px;
  transform: translateX(-50px);
  box-shadow: 
    0 0 0 1px rgba(255, 255, 255, 0.1),
    0 2px 16px rgba(0, 0, 0, 0.04);
}

/* 悬停展开 */
.fixed-sidebar.hover-expanded {
  width: 320px;
  transform: translateX(0);
  box-shadow: 
    0 0 0 1px rgba(255, 255, 255, 0.05),
    0 8px 48px rgba(0, 0, 0, 0.12),
    0 16px 96px rgba(0, 0, 0, 0.06);
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
  background: linear-gradient(135deg, rgba(66, 185, 131, 0.05) 0%, rgba(102, 126, 234, 0.05) 100%);
  backdrop-filter: blur(10px);
  position: relative;
}

.sidebar-header::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 1px;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.5), transparent);
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
  content: '📝';
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
  background: linear-gradient(135deg, rgba(66, 185, 131, 0.3), rgba(102, 126, 234, 0.3));
  border-radius: 2px;
}

.sidebar-content::-webkit-scrollbar-thumb:hover {
  background: linear-gradient(135deg, rgba(66, 185, 131, 0.5), rgba(102, 126, 234, 0.5));
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
  content: '';
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
  0%, 100% {
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
  font-size: 10px;
  color: #94a3b8;
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
  box-shadow: 
    0 0 0 1px rgba(255, 255, 255, 0.05),
    0 4px 32px rgba(0, 0, 0, 0.2),
    0 8px 64px rgba(0, 0, 0, 0.1);
}

.dark .sidebar-header {
  background: linear-gradient(135deg, rgba(76, 217, 100, 0.05) 0%, rgba(167, 139, 250, 0.05) 100%);
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
