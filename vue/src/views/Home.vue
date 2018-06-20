<template lang="pug">
  section.home.hero.is-primary.is-fullheight
    .hero-head
      h1.title The Chatroom
    .hero-body
      .container.has-text-centered
        h1.title Welcome to the chatroom
        p.subtitle Enter your name below
        .field
          .control
            input.input.is-rounded.is-medium(type="text", placeholder="My name is...")
        button.button.circular.bigger.is-info
          span Enter
          span.icon
            i.fas.fa-sign-in-alt
    .hero-footer
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator"

@Component
export default class Home extends Vue {}
</script>

<style lang="scss">
.home {
  .hero-head,
  .hero-footer {
    padding: 2rem;
  }

  input.input {
    margin: 0 auto;
    display: flex;
    width: 30rem;
    max-width: 80vw;
  }
}
</style>
