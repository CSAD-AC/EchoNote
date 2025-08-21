import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import LoginView from "../views/LoginView.vue";
import RegisterView from "../views/RegisterView.vue";
// 忘记密码页面路由占位符
const ForgotPasswordView = () =>
  import(
    /* webpackChunkName: "forgot-password" */ "../views/ForgotPasswordView.vue"
  );

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
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
