<template>
  <div :class="['home-container', darkMode ? 'dark-mode' : '']">
    <!-- 顶部导航 -->
    <header class="header">
      <div class="logo-container">
        <img alt="EchoNote Logo" src="../assets/logo.png" class="logo" />
        <h1 class="app-name">EchoNote</h1>
      </div>
      <div class="theme-toggle">
        <el-switch
          v-model="darkMode"
          active-text="黑夜模式"
          inactive-text="白天模式"
          @change="toggleDarkMode"
        ></el-switch>
      </div>
    </header>

    <!-- 功能卡片区域 -->
    <section class="features-container">
      <el-row :gutter="40">
        <!-- 灵光胶囊 -->
        <el-col :xs="24" :sm="12" :md="12">
          <el-card
            class="feature-card"
            :body-style="{ padding: '0' }"
            shadow="hover"
          >
            <div class="card-content">
              <div class="icon-container lightbulb">
                <LightbulbIcon size="40" class="feature-icon" />
              </div>
              <div class="text-container">
                <h3 class="feature-title">灵光胶囊</h3>
                <p class="feature-goal">瞬时捕捉</p>
                <div class="feature-details">
                  <p class="feature-design">
                    关键设计：预制填空框架+多媒体锚点（截图/音频）
                  </p>
                  <p class="feature-scenario">适合场景：娱乐时闪现灵感</p>
                </div>
                <el-button type="primary" class="feature-button" size="default"
                  >开始使用</el-button
                >
              </div>
            </div>
          </el-card>
        </el-col>

        <!-- 心流写作 -->
        <el-col :xs="24" :sm="12" :md="12">
          <el-card
            class="feature-card"
            :body-style="{ padding: '0' }"
            shadow="hover"
          >
            <div class="card-content">
              <div class="icon-container edit">
                <EditIcon size="40" class="feature-icon" />
              </div>
              <div class="text-container">
                <h3 class="feature-title">心流写作</h3>
                <p class="feature-goal">沉浸输出</p>
                <div class="feature-details">
                  <p class="feature-design">
                    关键设计：动态聚焦光束+白噪音自适应+退出保存点
                  </p>
                  <p class="feature-scenario">适合场景：深度表达需求</p>
                </div>
                <el-button type="primary" class="feature-button" size="default"
                  >开始使用</el-button
                >
              </div>
            </div>
          </el-card>
        </el-col>

        <!-- 思维慢发酵 -->
        <el-col :xs="24" :sm="12" :md="12">
          <el-card
            class="feature-card"
            :body-style="{ padding: '0' }"
            shadow="hover"
          >
            <div class="card-content">
              <div class="icon-container coffee">
                <CoffeeIcon size="40" class="feature-icon" />
              </div>
              <div class="text-container">
                <h3 class="feature-title">思维慢发酵</h3>
                <p class="feature-goal">认知迭代</p>
                <div class="feature-details">
                  <p class="feature-design">
                    关键设计：24小时延迟回看+AI挑衅提问
                  </p>
                  <p class="feature-scenario">适合场景：需要反思的严肃话题</p>
                </div>
                <el-button type="primary" class="feature-button" size="default"
                  >开始使用</el-button
                >
              </div>
            </div>
          </el-card>
        </el-col>

        <!-- 思维迷宫 -->
        <el-col :xs="24" :sm="12" :md="12">
          <el-card
            class="feature-card"
            :body-style="{ padding: '0' }"
            shadow="hover"
          >
            <div class="card-content">
              <div class="icon-container puzzle">
                <PuzzleIcon size="40" class="feature-icon" />
              </div>
              <div class="text-container">
                <h3 class="feature-title">思维迷宫</h3>
                <p class="feature-goal">主动训练</p>
                <div class="feature-details">
                  <p class="feature-design">
                    关键设计：哲学闯关+隐喻生成+逻辑漏洞扫描
                  </p>
                  <p class="feature-scenario">适合场景：无聊时思维健身</p>
                </div>
                <el-button type="primary" class="feature-button" size="default"
                  >开始使用</el-button
                >
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </section>

    <!-- 页脚 -->
    <footer class="footer">
      <p>© 2023 EchoNote. 保留所有权利。</p>
    </footer>
  </div>
</template>

<script>
/**
 * 首页组件
 * 展示四大功能矩阵，提供简洁美观的用户界面
 */
// 导入所需图标
import {
  LightbulbIcon,
  EditIcon,
  CoffeeIcon,
  PuzzleIcon,
} from "@element-plus/icons-vue";
import { ElSwitch } from "element-plus";

export default {
  name: "HomeView",
  components: {
    LightbulbIcon,
    EditIcon,
    CoffeeIcon,
    PuzzleIcon,
    ElSwitch,
  },
  data() {
    return {
      darkMode: false,
    };
  },
  methods: {
    toggleDarkMode() {
      if (this.darkMode) {
        document.documentElement.classList.add("dark");
      } else {
        document.documentElement.classList.remove("dark");
      }
    },
  },
  mounted() {
    // 检查系统偏好
    if (
      window.matchMedia &&
      window.matchMedia("(prefers-color-scheme: dark)").matches
    ) {
      this.darkMode = true;
      document.documentElement.classList.add("dark");
    }
    // 解决ResizeObserver错误
    this.resizeObserver = new ResizeObserver(() => {
      // 空处理函数
    });
    this.resizeObserver.observe(this.$el);
  },
  beforeUnmount() {
    if (this.resizeObserver) {
      this.resizeObserver.disconnect();
    }
  },
};
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

/* 功能卡片区域样式 */
.features-container {
  padding: 3rem 2rem;
  max-width: 1400px;
  margin: 0 auto;
  width: 100%;
  box-sizing: border-box;
}

/* 确保卡片为正方形的关键样式 */
.el-col {
  display: flex;
}

.el-card {
  flex: 1;
  display: flex;
  flex-direction: column;
  height: 100%;
}

/* 卡片内容样式 */
.card-content {
  display: flex;
  flex-direction: column;
  height: 100%;
}

/* 图标容器样式 */
.icon-container {
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 1rem;
  color: white;
}

/* 方块颜色 */
.icon-container.lightbulb {
  background-color: #ff9f1c;
  box-shadow: 0 4px 15px rgba(255, 159, 28, 0.3);
}

.icon-container.edit {
  background-color: #3a86ff;
  box-shadow: 0 4px 15px rgba(58, 134, 255, 0.3);
}

.icon-container.coffee {
  background-color: #9b5de5;
  box-shadow: 0 4px 15px rgba(155, 93, 229, 0.3);
}

.icon-container.puzzle {
  background-color: #f15bb5;
  box-shadow: 0 4px 15px rgba(241, 91, 181, 0.3);
}

/* 文本容器样式 */
.text-container {
  flex-grow: 1;
}

/* 标题样式 */
.feature-title {
  font-size: 1.25rem;
  margin-bottom: 0.5rem;
  color: #2c3e50;
}

/* 目标样式 */
.feature-goal {
  font-size: 0.9rem;
  font-weight: 500;
  color: #42b983;
  margin-bottom: 1rem;
}

/* 详情样式 */
.feature-details {
  margin-bottom: 1rem;
}

.feature-design,
.feature-scenario {
  margin: 0.5rem 0;
  font-size: 0.9rem;
  color: #666;
}

/* 页脚样式 */
.footer {
  text-align: center;
  padding: 2rem;
  background-color: #2c3e50;
  color: white;
  margin-top: auto;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .features-container {
    padding: 2rem 1rem;
  }
}
</style>
