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
  getters: {
    getName(state: RootState): string {
      return state.name
    },
  },
  mutations: {
    setName(state: RootState, name: string) {
      state.name = name
    },
  },
  actions: {},
})
