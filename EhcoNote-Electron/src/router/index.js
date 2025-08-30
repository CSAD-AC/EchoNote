import { createRouter, createWebHashHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import LoginView from "../views/LoginView.vue";
import RegisterView from "../views/RegisterView.vue";
import { ElMessage } from "element-plus";
// 忘记密码页面路由占位符
const ForgotPasswordView = () => import("../views/ForgotPasswordView.vue");

const routes = [
  {
    path: "/",
    name: "home",
    component: HomeView,
  },
  {
    path: "/login",
    name: "login",
    component: LoginView,
  },
  {
    path: "/register",
    name: "register",
    component: RegisterView,
  },
  {
    path: "/forgot-password",
    name: "forgot-password",
    component: ForgotPasswordView,
  },
  {
    path: "/about",
    name: "about",
    component: () => import("../views/AboutView.vue"),
  },
  {
    path: "/capture",
    name: "capture",
    component: () => import("../views/LightbulbView.vue"),
  },
  {
    path: "/writing",
    name: "writing",
    component: () => import("../views/FlowWritingView.vue"),
  },
  {
    path: "/slow-fermentation",
    name: "slow-fermentation",
    component: () => import("../views/SlowFermentationView.vue"),
  },
  {
    path: "/maze",
    name: "maze",
    component: () => import("../views/MazeOfThoughtView.vue"),
  },
];

const router = createRouter({
  history: createWebHashHistory(),
  routes,
});

// 无需权限的页面白名单
const whiteList = ["/", "/login", "/register", "/forgot-password", "/about"];

// 导航守卫
router.beforeEach((to, from, next) => {
  // 检查是否在白名单中
  if (whiteList.includes(to.path)) {
    return next();
  }

  // 检查是否有token
  const token = localStorage.getItem("token");
  if (!token) {
    // 没有token，跳转到登录页
    ElMessage.warning("请先登录");
    return next("/login");
  }

  // 有token，继续访问
  next();
});

export default router;
