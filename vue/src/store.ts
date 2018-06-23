import Vue from "vue"
import Vuex from "vuex"
import createPersistedState from "vuex-persistedstate"

Vue.use(Vuex)

interface RootState {
  name: string
}

export default new Vuex.Store<RootState>({
  // plugins: [
  //   createPersistedState({
  //     storage: window.sessionStorage,
  //   }),
  // ],
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
