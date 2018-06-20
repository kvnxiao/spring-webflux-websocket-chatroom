<template lang="pug">
  section.home.hero.is-primary.is-fullheight
    .hero-head
      h1.title
    .hero-body
      .container.has-text-centered
        p.title Welcome to the chat
        p.subtitle Enter your name below
        .field
          .control
            input.input.is-rounded.is-medium(type="text", placeholder="My name is...")
    .hero-footer
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator"

@Component
export default class Home extends Vue {}
</script>

<style lang="scss">
.home input.input {
    margin: 0 auto;
    display: flex;
    width: 30rem;
    max-width: 80vw;
}
</style>
