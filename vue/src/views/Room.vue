<template lang="pug">
  section.room.hero.is-primary.is-fullheight
    .hero-head
      h1.title You're in room: {{ room }}
    .hero-body
      .container.has-text-centered
        h1.title Chat
        p.subtitle {{ subtitle }}
        .field
          .control
            input.input.is-rounded.is-medium(type="text", placeholder="Chat here")
        button.button.circular.bigger.is-info
          span Send
          span.icon
            i.fas.fa-paper-plane
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

  input.input {
    margin: 0 auto;
    display: flex;
    width: 30rem;
    max-width: 80vw;
  }
}
</style>
