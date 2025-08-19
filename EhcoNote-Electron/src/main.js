import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
// 引入Element Plus框架
import ElementPlus from "element-plus";
// 引入Element Plus样式
import "element-plus/dist/index.css";
// 引入Element Plus图标
import * as ElementPlusIconsVue from "@element-plus/icons-vue";

const app = createApp(App);
// 注册Element Plus
app.use(ElementPlus);
// 注册Element Plus图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}
app.use(router).mount("#app");
