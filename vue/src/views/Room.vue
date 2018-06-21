<template lang="pug">
  section.room.hero.is-primary.is-fullheight
    .hero-head
      h1.title You're in room: {{ room }}
    .hero-body
      .container.has-text-centered
        #chat.box
          ul#chat-msgs
            li(v-for="message in messages") {{ message }}
        h1.title Chat
        p.subtitle {{ subtitle }}
        .field
          p.control.has-icons-left
            input.input.is-rounded.is-medium(type="text", placeholder="Chat here", v-model="currMessage", @keydown.enter="sendMessage")
            span.icon.is-small.is-left
              i.fas.fa-comment
        button.button.circular.bigger.is-info(@click="sendMessage")
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
  private messages: string[] = []
  private currMessage: string = ""

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

  public sendMessage(event: any) {
    if (this.currMessage !== "") {
      this.messages.push(this.currMessage)
      this.currMessage = ""
    }
  }
}
</script>

<style lang="scss">
#chat.box {
  text-align: left;
  border-radius: 1.5rem;
  margin: 0 auto 2rem auto;
  display: flex;
  width: 45rem;
  max-width: 85vw;
  height: 12rem;
  min-height: 33vh;
  ul {
    width: 100%;
    overflow-y: auto;
  }

  box-shadow: 0 1px 2px 0 rgba(60, 64, 67, 0.302),
    0 1px 3px 1px rgba(60, 64, 67, 0.149);
  transition: all 0.08s linear;
  &:hover,
  &:focus {
    box-shadow: 0 1px 3px 0 rgba(60, 64, 67, 0.302),
      0 4px 8px 3px rgba(60, 64, 67, 0.149);
  }
}
</style>
