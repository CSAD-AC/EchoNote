<template>
  <Layout>
    <!-- 欢迎区域 -->
    <section class="hero-section">
      <div class="hero-content">
        <div class="hero-text">
          <h1 class="hero-title">
            <span class="title-line">欢迎来到</span>
            <span class="title-main">
              <span class="title-echo">Echo</span
              ><span class="title-note">Note</span>
            </span>
            <span class="title-subtitle">你的思维回响平台</span>
          </h1>
          <p class="hero-description">
            在这里，每一个灵感都能找到属于它的回响。让我们一起开始思维的旅程吧！
          </p>
        </div>
        <div class="hero-visual">
          <div class="floating-elements">
            <div class="float-item float-1">💡</div>
            <div class="float-item float-2">✨</div>
            <div class="float-item float-3">🎆</div>
            <div class="float-item float-4">🔍</div>
            <div class="float-item float-5">🎨</div>
          </div>
        </div>
      </div>
    </section>

    <!-- 功能卡片区域 -->
    <section class="features-container">
      <div class="features-header">
        <h2 class="features-title">选择您的创作模式</h2>
        <p class="features-subtitle">四种不同的思维工具，满足您各种创作需求</p>
      </div>

      <div class="features-grid">
        <!-- 灵光胶囊 -->
        <div class="feature-item" @click="navigateToFeature('capture')">
          <div class="feature-card lightbulb-card">
            <div class="card-background"></div>
            <div class="card-header">
              <div class="icon-container lightbulb">
                <Plus :size="32" class="feature-icon" />
                <div class="icon-glow"></div>
              </div>
              <div class="feature-badge">热门</div>
            </div>
            <div class="card-content">
              <h3 class="feature-title">✨ 灵光胶囊</h3>
              <p class="feature-goal">瞬时捕捉</p>
              <p class="feature-description">
                在灵感闪现的瞬间，将它们安全地封存在胶囊中，等待未来的回味与探索。
              </p>
              <div class="feature-tags">
                <span class="tag">灵感捕捉</span>
                <span class="tag">情绪记录</span>
              </div>
            </div>
            <div class="card-footer">
              <div class="action-button" data-type="lightbulb">
                <span>开始使用</span>
                <div class="btn-arrow">→</div>
              </div>
            </div>
          </div>
        </div>

        <!-- 心流写作 -->
        <div class="feature-item" @click="navigateToFeature('writing')">
          <div class="feature-card writing-card">
            <div class="card-background"></div>
            <div class="card-header">
              <div class="icon-container edit">
                <Edit :size="32" class="feature-icon" />
                <div class="icon-glow"></div>
              </div>
              <div class="feature-badge">推荐</div>
            </div>
            <div class="card-content">
              <h3 class="feature-title">✍️ 心流写作</h3>
              <p class="feature-goal">高效输出</p>
              <p class="feature-description">
                沉浸在纯粹的写作环境中，让思维如水流般顺畅流淌，创造出您的作品。
              </p>
              <div class="feature-tags">
                <span class="tag">沉浸式</span>
                <span class="tag">专注模式</span>
              </div>
            </div>
            <div class="card-footer">
              <div class="action-button" data-type="writing">
                <span>开始使用</span>
                <div class="btn-arrow">→</div>
              </div>
            </div>
          </div>
        </div>

        <!-- 思维慢发酵 -->
        <div
          class="feature-item"
          @click="navigateToFeature('slow-fermentation')"
        >
          <div class="feature-card fermentation-card">
            <div class="card-background"></div>
            <div class="card-header">
              <div class="icon-container coffee">
                <ChatDotRound :size="32" class="feature-icon" />
                <div class="icon-glow"></div>
              </div>
              <div class="feature-badge">深度</div>
            </div>
            <div class="card-content">
              <h3 class="feature-title">☕ 思维慢发酵</h3>
              <p class="feature-goal">沉淀反思</p>
              <p class="feature-description">
                像美酒一样，让您的思考在时间中慢慢发酵，沉淀出更深层的智慧。
              </p>
              <div class="feature-tags">
                <span class="tag">深度思考</span>
                <span class="tag">慢节奏</span>
              </div>
            </div>
            <div class="card-footer">
              <div class="action-button" data-type="fermentation">
                <span>开始使用</span>
                <div class="btn-arrow">→</div>
              </div>
            </div>
          </div>
        </div>

        <!-- 思维迷宫 -->
        <div class="feature-item" @click="navigateToFeature('maze')">
          <div class="feature-card maze-card">
            <div class="card-background"></div>
            <div class="card-header">
              <div class="icon-container puzzle">
                <Guide :size="32" class="feature-icon" />
                <div class="icon-glow"></div>
              </div>
              <div class="feature-badge">挑战</div>
            </div>
            <div class="card-content">
              <h3 class="feature-title">🧩 思维迷宫</h3>
              <p class="feature-goal">逻辑谜题</p>
              <p class="feature-description">
                在复杂的思维迷宫中探索，锻炼您的逻辑思维和问题解决能力。
              </p>
              <div class="feature-tags">
                <span class="tag">逻辑挑战</span>
                <span class="tag">系统思维</span>
              </div>
            </div>
            <div class="card-footer">
              <div class="action-button" data-type="maze">
                <span>开始使用</span>
                <div class="btn-arrow">→</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
    <div class="footer-beian">
      <img
        src="https://inews.gtimg.com/newsapp_bt/0/0613150938895_706/0"
        alt="备案图标"
        class="beian-icon"
      />
      <a
        href="https://beian.miit.gov.cn"
        target="_blank"
        rel="noopener"
        class="beian-link"
      >
        粤ICP备2025461907号-1
      </a>
    </div>
  </Layout>
</template>

<script setup>
// 导入组件
import Layout from "@/components/Layout.vue";
import { ChatDotRound, Edit, Plus, Guide } from "@element-plus/icons-vue";
import { useRouter } from "vue-router";

const router = useRouter();

// 路由映射
const routes = {
  capture: "/capture",
  writing: "/writing",
  "slow-fermentation": "/slow-fermentation",
  maze: "/maze",
};

function navigateToFeature(feature) {
  router.push(routes[feature] || "/");
}
</script>

<style scoped>
/* 首页整体布局 */

/* 欢迎区域 */
.hero-section {
  position: relative;
  min-height: 60vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(
    135deg,
    rgba(66, 185, 131, 0.1) 0%,
    rgba(102, 126, 234, 0.1) 50%,
    rgba(168, 85, 247, 0.1) 100%
  );
  overflow: hidden;
}

.hero-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  max-width: 1200px;
  width: 100%;
  padding: 0 2rem;
  gap: 4rem;
}

.hero-text {
  flex: 1;
  max-width: 600px;
}

.hero-title {
  margin: 0 0 2rem 0;
  line-height: 1.2;
}

.title-line {
  display: block;
  font-size: 1.2rem;
  font-weight: 400;
  color: #666;
  margin-bottom: 0.5rem;
  letter-spacing: 1px;
}

.title-main {
  display: block;
  font-size: 3.5rem;
  font-weight: 800;
  margin-bottom: 0.5rem;
}

.title-echo {
  background: linear-gradient(135deg, #42b983, #369870);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  text-shadow: 0 4px 20px rgba(66, 185, 131, 0.3);
}

.title-note {
  background: linear-gradient(135deg, #667eea, #764ba2);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  text-shadow: 0 4px 20px rgba(102, 126, 234, 0.3);
}

.title-subtitle {
  display: block;
  font-size: 1rem;
  font-weight: 400;
  color: #888;
  letter-spacing: 3px;
  opacity: 0.8;
}

.hero-description {
  font-size: 1.1rem;
  line-height: 1.8;
  color: #555;
  margin: 0;
  max-width: 500px;
}

.hero-visual {
  flex: 1;
  position: relative;
  height: 400px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.floating-elements {
  position: relative;
  width: 300px;
  height: 300px;
}

.float-item {
  position: absolute;
  font-size: 2rem;
  animation: float-around 20s infinite linear;
  opacity: 0.8;
}

.float-1 {
  top: 10%;
  left: 20%;
  animation-delay: 0s;
}

.float-2 {
  top: 60%;
  right: 10%;
  animation-delay: 4s;
}

.float-3 {
  bottom: 20%;
  left: 10%;
  animation-delay: 8s;
}

.float-4 {
  top: 30%;
  right: 30%;
  animation-delay: 12s;
}

.float-5 {
  bottom: 40%;
  left: 40%;
  animation-delay: 16s;
}

@keyframes float-around {
  0% {
    transform: translate(0, 0) rotate(0deg) scale(1);
  }
  25% {
    transform: translate(30px, -30px) rotate(90deg) scale(1.1);
  }
  50% {
    transform: translate(-20px, -40px) rotate(180deg) scale(0.9);
  }
  75% {
    transform: translate(-40px, 20px) rotate(270deg) scale(1.1);
  }
  100% {
    transform: translate(0, 0) rotate(360deg) scale(1);
  }
}

/* 功能区域 */
.features-container {
  padding: 4rem 2rem;
  max-width: 1400px;
  margin: 0 auto;
}

.features-header {
  text-align: center;
  margin-bottom: 4rem;
}

.features-title {
  font-size: 2.5rem;
  font-weight: 700;
  margin: 0 0 1rem 0;
  background: linear-gradient(135deg, #42b983, #667eea, #764ba2);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}

.features-subtitle {
  font-size: 1.1rem;
  color: #666;
  margin: 0;
  max-width: 600px;
  margin: 0 auto;
  line-height: 1.6;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 2rem;
  margin-top: 3rem;
}

.feature-item {
  cursor: pointer;
  transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.feature-item:hover {
  transform: translateY(-8px);
}

/* 功能卡片 */
.feature-card {
  position: relative;
  height: 420px;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(20px);
  border-radius: 24px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  padding: 0;
  overflow: hidden;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  display: flex;
  flex-direction: column;
}

.feature-card:hover {
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
  border-color: rgba(255, 255, 255, 0.4);
}

.card-background {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  opacity: 0;
  transition: opacity 0.3s ease;
  border-radius: 24px;
  z-index: 1;
}

.lightbulb-card .card-background {
  background: linear-gradient(
    135deg,
    rgba(255, 159, 28, 0.1) 0%,
    rgba(255, 193, 7, 0.1) 100%
  );
}

.writing-card .card-background {
  background: linear-gradient(
    135deg,
    rgba(58, 134, 255, 0.1) 0%,
    rgba(102, 126, 234, 0.1) 100%
  );
}

.fermentation-card .card-background {
  background: linear-gradient(
    135deg,
    rgba(155, 93, 229, 0.1) 0%,
    rgba(168, 85, 247, 0.1) 100%
  );
}

.maze-card .card-background {
  background: linear-gradient(
    135deg,
    rgba(241, 91, 181, 0.1) 0%,
    rgba(245, 101, 101, 0.1) 100%
  );
}

.feature-card:hover .card-background {
  opacity: 1;
}

.card-header {
  position: relative;
  z-index: 2;
  padding: 2rem 2rem 0 2rem;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.icon-container {
  width: 64px;
  height: 64px;
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  transition: transform 0.3s ease;
}

.icon-container.lightbulb {
  background: linear-gradient(135deg, #ff9f1c, #ffc107);
}

.icon-container.edit {
  background: linear-gradient(135deg, #3a86ff, #667eea);
}

.icon-container.coffee {
  background: linear-gradient(135deg, #9b5de5, #a855f7);
}

.icon-container.puzzle {
  background: linear-gradient(135deg, #f15bb5, #f56565);
}

.feature-card:hover .icon-container {
  transform: scale(1.05);
}

.feature-badge {
  background: linear-gradient(135deg, #42b983, #369870);
  color: white;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.card-content {
  position: relative;
  z-index: 2;
  padding: 1rem 2rem;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.feature-title {
  font-size: 1.5rem;
  font-weight: 700;
  margin: 0 0 0.5rem 0;
  color: #2c3e50;
}

.feature-goal {
  font-size: 1rem;
  font-weight: 600;
  color: #42b983;
  margin: 0 0 1rem 0;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.feature-description {
  font-size: 0.95rem;
  line-height: 1.6;
  color: #555;
  margin: 0 0 1.5rem 0;
  flex: 1;
}

.feature-tags {
  display: flex;
  gap: 0.5rem;
  flex-wrap: wrap;
}

.tag {
  background: rgba(66, 185, 131, 0.1);
  color: #42b983;
  padding: 4px 8px;
  border-radius: 8px;
  font-size: 0.75rem;
  font-weight: 500;
  border: 1px solid rgba(66, 185, 131, 0.2);
}

.card-footer {
  position: relative;
  z-index: 2;
  padding: 0 2rem 2rem 2rem;
}

.action-button {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  padding: 12px 20px;
  border-radius: 16px;
  font-weight: 600;
  font-size: 0.9rem;
  transition: all 0.3s ease;
  cursor: pointer;
  border: none;
  color: white;
}

.action-button[data-type="lightbulb"] {
  background: linear-gradient(135deg, #ff9f1c, #ffc107);
}

.action-button[data-type="writing"] {
  background: linear-gradient(135deg, #3a86ff, #667eea);
}

.action-button[data-type="fermentation"] {
  background: linear-gradient(135deg, #9b5de5, #a855f7);
}

.action-button[data-type="maze"] {
  background: linear-gradient(135deg, #f15bb5, #f56565);
}

.action-button:hover {
  transform: translateX(2px);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.15);
}

.btn-arrow {
  transition: transform 0.3s ease;
}

.action-button:hover .btn-arrow {
  transform: translateX(2px);
}

/* 暗黑模式 */
.dark .hero-section {
  background: linear-gradient(
    135deg,
    rgba(76, 217, 100, 0.1) 0%,
    rgba(168, 85, 247, 0.1) 50%,
    rgba(245, 101, 101, 0.1) 100%
  );
}

.dark .title-line {
  color: #9ca3af;
}

.dark .title-subtitle {
  color: #6b7280;
}

.dark .hero-description {
  color: #d1d5db;
}

.dark .features-subtitle {
  color: #9ca3af;
}

.dark .feature-card {
  background: rgba(31, 41, 55, 0.9);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.dark .feature-card:hover {
  border-color: rgba(255, 255, 255, 0.2);
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.4);
}

.dark .feature-title {
  color: #f3f4f6;
}

.dark .feature-description {
  color: #d1d5db;
}

.dark .tag {
  background: rgba(76, 217, 100, 0.1);
  color: #4cd964;
  border: 1px solid rgba(76, 217, 100, 0.2);
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .hero-content {
    flex-direction: column;
    text-align: center;
    gap: 2rem;
  }

  .hero-visual {
    height: 200px;
  }

  .floating-elements {
    width: 200px;
    height: 200px;
  }

  .features-grid {
    grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  }
}

@media (max-width: 768px) {
  .hero-section {
    min-height: 50vh;
    padding: 2rem 1rem;
  }

  .hero-content {
    padding: 0 1rem;
  }

  .title-main {
    font-size: 2.5rem;
  }

  .features-container {
    padding: 3rem 1rem;
  }

  .features-title {
    font-size: 2rem;
  }

  .features-grid {
    grid-template-columns: 1fr;
    gap: 1.5rem;
  }

  .feature-card {
    height: 380px;
  }
}

@media (max-width: 480px) {
  .title-main {
    font-size: 2rem;
  }

  .title-line {
    font-size: 1rem;
  }

  .hero-description {
    font-size: 1rem;
  }

  .features-title {
    font-size: 1.5rem;
  }

  .feature-card {
    height: 350px;
  }

  .card-header,
  .card-footer {
    padding-left: 1.5rem;
    padding-right: 1.5rem;
  }
}

/* 美化备案信息 */
.footer-beian {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 8px;
  margin-top: 2rem;
  padding: 1rem 0;
  border-top: 1px solid #eee;
  transition: all 0.3s ease;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(66, 185, 131, 0.05),
    transparent
  );
}

.beian-icon {
  width: 20px;
  height: 20px;
  opacity: 0.6;
  transition: all 0.3s ease;
  filter: grayscale(100%);
}

.beian-link {
  font-size: 0.9rem;
  color: #888;
  text-decoration: none;
  transition: all 0.3s ease;
  padding: 4px 8px;
  border-radius: 4px;
  position: relative;
  font-weight: 500;
}

.beian-link:hover {
  color: #42b983;
  background-color: rgba(66, 185, 131, 0.1);
  transform: translateY(-2px);
}

.beian-link::after {
  content: "公网安";
  position: relative;
  top: -2px;
  margin-left: 4px;
  font-size: 0.7rem;
  background: #42b983;
  color: white;
  padding: 1px 3px;
  border-radius: 2px;
}

.footer-beian:hover .beian-icon {
  opacity: 1;
  transform: scale(1.1);
  filter: grayscale(0%);
}

/* 响应式优化 */
@media (max-width: 768px) {
  .footer-beian {
    margin-top: 1.5rem;
    padding: 0.8rem 0;
  }

  .beian-link {
    font-size: 0.8rem;
  }

  .beian-icon {
    width: 18px;
    height: 18px;
  }
}

@media (max-width: 480px) {
  .footer-beian {
    margin-top: 1rem;
    padding: 0.6rem 0;
    gap: 6px;
  }

  .beian-link {
    font-size: 0.75rem;
  }

  .beian-icon {
    width: 16px;
    height: 16px;
  }
}
</style>
