<template>
  <nav class="navbar">
    <div class="navbar-container">
      <div class="navbar-left">
        <router-link to="/" class="logo">
          <span class="logo-icon">✈️</span>
          <span class="logo-text">AI旅游规划</span>
        </router-link>
      </div>
      <div class="navbar-center">
        <router-link to="/" class="nav-item">首页</router-link>
        <router-link to="/attractions" class="nav-item">热门景点</router-link>
        <router-link to="/hotels" class="nav-item">热门酒店</router-link>
        <router-link to="/plan" class="nav-item">AI规划</router-link>
        <router-link to="/favorites" class="nav-item">我的收藏</router-link>
        <router-link to="/history" class="nav-item">历史记录</router-link>
      </div>
      <div class="navbar-right">
        <template v-if="userStore.isLoggedIn">
          <router-link to="/profile" class="nav-item user-item">
            <el-avatar :size="32" :icon="User" />
            <span>{{ userStore.username }}</span>
          </router-link>
          <el-button text size="small" @click="handleLogout">退出</el-button>
        </template>
        <template v-else>
          <router-link to="/login" class="nav-item">登录</router-link>
          <router-link to="/register" class="nav-item">注册</router-link>
        </template>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import { User } from '@element-plus/icons-vue'

const userStore = useUserStore()

function handleLogout() {
  userStore.logout()
  ElMessage.success('退出成功')
}
</script>

<style scoped>
.navbar {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(0, 0, 0, 0.05);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
}

.navbar-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.navbar-left .logo {
  display: flex;
  align-items: center;
  text-decoration: none;
  color: #303133;
}

.logo-icon {
  font-size: 24px;
  margin-right: 8px;
}

.logo-text {
  font-size: 18px;
  font-weight: 600;
  color: #667eea;
}

.navbar-center {
  display: flex;
  gap: 30px;
}

.nav-item {
  text-decoration: none;
  color: #606266;
  font-size: 14px;
  transition: color 0.3s;
}

.nav-item:hover {
  color: #667eea;
}

.navbar-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.user-item {
  display: flex;
  align-items: center;
  gap: 8px;
}
</style>
