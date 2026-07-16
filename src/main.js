import { createApp, onMounted } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import router from './router'
import { useUserStore } from '@/stores/user'

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(ElementPlus)

app.mount('#app')

onMounted(() => {
  const userStore = useUserStore()
  if (userStore.token) {
    userStore.fetchUserInfo()
  }
})
