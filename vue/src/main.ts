import "@/style.scss"
import axios, { AxiosError, AxiosResponse } from "axios"
import Vue from "vue"
import App from "./App.vue"
import router from "./router"
import store from "./store"

Vue.config.productionTip = false

axios.interceptors.response.use(
  (response: AxiosResponse) => {
    return response
  },
  (error: AxiosError) => {
    if (error.response!.status === 401) {
      window.location.href = "/"
    }
  },
)

new Vue({
  router,
  store,
  render: h => h(App),
}).$mount("#app")
