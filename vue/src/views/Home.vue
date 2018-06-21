<template lang="pug">
  section.home.hero.is-primary.is-fullheight
    .hero-head
      h1.title The Chatroom
    .hero-body
      .container.has-text-centered
        h1.title Welcome to the chatroom
        p.subtitle Enter your name below
        .field
          p.control
            input.input.is-rounded.is-medium(type="text", placeholder="My name is...", v-model="name", @keydown.enter="setName")
        button.button.circular.bigger.is-info(@click="setName")
          span Enter
          span.icon
            i.fas.fa-sign-in-alt
    .hero-footer
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator"

@Component
export default class Home extends Vue {
  private name: string = ""

  public setName() {
    this.$store.commit({ type: "setName", name: this.name })
    this.$router.push("lobby")
  }
}
</script>

<style lang="scss">
</style>
