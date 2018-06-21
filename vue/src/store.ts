import Vue from "vue"
import Vuex from "vuex"

Vue.use(Vuex)

interface RootState {
  name: string
}

export default new Vuex.Store<RootState>({
  state: {
    name: "",
  },
  mutations: {},
  actions: {},
})
