import Vue from "vue"
import Vuex from "vuex"

Vue.use(Vuex)

interface RootState {
  name: string,
  latency: number,
}

export default new Vuex.Store<RootState>({
  state: {
    name: "",
    latency: 0,
  },
  getters: {
    getName(state: RootState): string {
      return state.name
    },
    getLatency(state: RootState): number {
      return state.latency
    },
  },
  mutations: {
    setName(state: RootState, name: string) {
      state.name = name
    },
    setLatency(state: RootState, latency: number) {
      state.latency = latency
    },
  },
  actions: {},
})
