<template>
  <div class="profile-container">
    <NavBar />
    <div class="profile-content">
      <div class="profile-header">
        <div class="avatar-section">
          <div class="avatar-wrapper">
            <el-avatar :size="120" :icon="User" class="user-avatar" />
            <label class="avatar-upload">
              <input type="file" accept="image/*" class="upload-input" @change="handleAvatarUpload" />
              <el-button size="small" type="primary">更换头像</el-button>
            </label>
          </div>
          <div class="user-info">
            <h1>{{ userStore.username }}</h1>
            <p class="user-role">普通用户</p>
            <p class="join-date">加入时间：{{ joinDate }}</p>
          </div>
        </div>
      </div>
      <div class="profile-tabs">
        <el-tabs v-model="activeTab" type="border-card">
          <el-tab-pane label="个人资料" name="info">
            <el-form :model="profileForm" class="profile-form">
              <el-form-item label="用户名">
                <el-input v-model="profileForm.username" disabled />
              </el-form-item>
              <el-form-item label="昵称">
                <el-input v-model="profileForm.nickname" placeholder="请输入昵称" />
              </el-form-item>
              <el-form-item label="邮箱">
                <el-input v-model="profileForm.email" placeholder="请输入邮箱" />
              </el-form-item>
              <el-form-item label="手机号">
                <el-input v-model="profileForm.phone" placeholder="请输入手机号" />
              </el-form-item>
              <el-form-item label="性别">
                <el-radio-group v-model="profileForm.gender">
                  <el-radio label="male">男</el-radio>
                  <el-radio label="female">女</el-radio>
                  <el-radio label="other">保密</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="个人简介">
                <el-input v-model="profileForm.bio" type="textarea" :rows="3" placeholder="介绍一下自己..." />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleSave">保存修改</el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="修改密码" name="password">
            <el-form ref="passwordFormRef" :model="passwordForm" :rules="passwordRules" class="profile-form">
              <el-form-item label="原密码" prop="oldPassword">
                <el-input v-model="passwordForm.oldPassword" type="password" placeholder="请输入原密码" show-password />
              </el-form-item>
              <el-form-item label="新密码" prop="newPassword">
                <el-input v-model="passwordForm.newPassword" type="password" placeholder="请输入新密码" show-password />
              </el-form-item>
              <el-form-item label="确认新密码" prop="confirmPassword">
                <el-input v-model="passwordForm.confirmPassword" type="password" placeholder="请确认新密码" show-password />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleChangePassword">修改密码</el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="账户安全" name="security">
            <div class="security-list">
              <div class="security-item">
                <div class="security-info">
                  <h4>登录密码</h4>
                  <p>保护账户安全的第一道防线</p>
                </div>
                <el-button text type="primary" @click="activeTab = 'password'">修改</el-button>
              </div>
              <div class="security-item">
                <div class="security-info">
                  <h4>绑定手机</h4>
                  <p>{{ profileForm.phone || '未绑定' }}</p>
                </div>
                <el-button text type="primary">绑定</el-button>
              </div>
              <div class="security-item">
                <div class="security-info">
                  <h4>绑定邮箱</h4>
                  <p>{{ profileForm.email || '未绑定' }}</p>
                </div>
                <el-button text type="primary">绑定</el-button>
              </div>
              <div class="security-item">
                <div class="security-info">
                  <h4>登录设备</h4>
                  <p>当前登录设备：{{ deviceInfo }}</p>
                </div>
                <el-button text type="primary">查看详情</el-button>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import { User } from '@element-plus/icons-vue'
import NavBar from '@/components/NavBar.vue'

const userStore = useUserStore()
const activeTab = ref('info')
const passwordFormRef = ref(null)

const profileForm = reactive({
  username: '',
  nickname: '',
  email: '',
  phone: '',
  gender: 'other',
  bio: ''
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const passwordRules = {
  oldPassword: [
    { required: true, message: '请输入原密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 32, message: '密码长度在6-32之间', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

const joinDate = '2024-01-15'
const deviceInfo = 'Windows 10 / Chrome'

onMounted(() => {
  profileForm.username = userStore.username
})

function handleAvatarUpload(event) {
  const file = event.target.files[0]
  if (file) {
    ElMessage.success('头像上传成功')
  }
}

function handleSave() {
  ElMessage.success('个人资料修改成功')
}

async function handleChangePassword() {
  if (!passwordFormRef.value) return
  await passwordFormRef.value.validate(async (valid) => {
    if (!valid) return
    try {
      ElMessage.success('密码修改成功，请重新登录')
      userStore.logout()
    } catch (error) {
      ElMessage.error('密码修改失败')
    }
  })
}
</script>

<style scoped>
.profile-container {
  min-height: 100vh;
  background: #f5f7fa;
}

.profile-content {
  padding: 80px 20px;
  max-width: 800px;
  margin: 0 auto;
}

.profile-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  padding: 40px;
  margin-bottom: 30px;
}

.avatar-section {
  display: flex;
  align-items: center;
  gap: 30px;
}

.avatar-wrapper {
  position: relative;
}

.user-avatar {
  background: rgba(255, 255, 255, 0.2);
}

.avatar-upload {
  position: absolute;
  bottom: -10px;
  left: 50%;
  transform: translateX(-50%);
}

.user-info {
  color: #fff;
}

.user-info h1 {
  font-size: 28px;
  margin-bottom: 10px;
}

.user-role {
  font-size: 14px;
  opacity: 0.8;
  margin-bottom: 8px;
}

.join-date {
  font-size: 12px;
  opacity: 0.6;
}

.profile-form {
  padding: 20px 0;
}

.security-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.security-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  background: #fafafa;
  border-radius: 12px;
}

.security-info h4 {
  font-size: 16px;
  color: #303133;
  margin-bottom: 8px;
}

.security-info p {
  font-size: 14px;
  color: #909399;
}
</style>
