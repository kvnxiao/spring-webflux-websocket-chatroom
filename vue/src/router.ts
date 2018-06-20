import Vue from "vue"
import Router from "vue-router"
import Home from "./views/Home.vue"
import Lobby from "./views/Lobby.vue"
import Room from "./views/Room.vue"

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: "/",
      name: "home",
      component: Home,
    },
    {
      path: "/room/:room",
      name: "room",
      component: Room,
    },
    {
      path: "/lobby",
      name: "lobby",
      component: Lobby,
    },
  ],
  mode: "history",
})
