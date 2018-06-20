<template lang="pug">
  section.room.hero.is-primary.is-fullheight
    .hero-head
      h1.title Room {{ room }}
    .hero-body
      .container.has-text-centered
        p.title Chat
        p.subtitle {{ subtitle }}
        .field
          .control
            input.input.is-rounded.is-medium(type="text", placeholder="Chat here")
    .hero-footer
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator"

@Component
export default class Home extends Vue {
  private name: string = ""
  private room: string = ""

  public created() {
    this.room = this.$route.params.room
  }

  get subtitle(): string {
    if (this.name === "") {
      return "Welcome"
    } else {
      return `Hi ${this.name}, type and enter text below to start chatting.`
    }
  }
}
</script>

<style lang="scss">
.room {
  .hero-head,
  .hero-footer {
    padding: 2rem;
  }
}
</style>
