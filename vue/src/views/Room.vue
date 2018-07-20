<template lang="pug">
  section.room.hero.is-primary.is-fullheight
    .hero-head
      h1.title You're in room: {{ roomName }}
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
import axios, {AxiosResponse} from "axios"
import { Component, Vue } from "vue-property-decorator"
import NativeWebSocket from "../ts/NativeWebSocket"
import {NotificationType} from "../ts/NotificationMessage"
import Room from "../ts/Room"
import WebSocketEvent, {EventType} from "../ts/WebSocketEvent"

@Component
export default class Home extends Vue {
  private name: string = ""
  private roomName: string = "..."
  private messages: string[] = []
  private currMessage: string = ""
  private timestamp: number = 0
  private latencyInterval!: number
  private ws!: NativeWebSocket<WebSocketEvent>

  /**
   * Vue beforeCreate lifecycle hook.
   *
   * Initializes WebSocket connection to server and sets the WebSocket event handlers.
   */
  public beforeCreate() {
    // Connect to websocket endpoint
    this.ws = new NativeWebSocket(undefined, "/ws")

    this.ws.onOpen = (event: Event) => {
      // this.messages.push({ msg: "Connection to the server has been established", type: NotificationType.Success })
    }

    this.ws.onMessage = (event: MessageEvent) => {
      try {
        const data = JSON.parse(event.data)
        this.parseEvent(data)
      } catch (e) {
        // ignore
      }
    }

    this.ws.onError = (error: Event) => {
      // this.messages.push({ msg: "An error occurred with the server connection...", type: NotificationType.Danger })
    }

    this.ws.onClose = (event: CloseEvent) => {
      window.clearInterval(this.latencyInterval)
      // this.messages.push({ msg: "Connection to the server has been closed.", type: NotificationType.Warning })
    }

    // test for latency in the lobby
    this.latencyInterval = window.setInterval(() => {
      if (this.ws.getReadyState() === this.ws.OPEN) {
        this.timestamp = Date.now()
        this.ws.send({ "@type": EventType.LatencyTest })
      }
    }, 10000)
  }

  public created() {
    this.ws.connect()

    this.name = this.$store.getters.getName.name
    axios.post(
        "/api/getroom",
        { id: this.$route.params.room },
        { headers: { "Content-Type": "application/json" } },
    )
    .then((res: AxiosResponse) => {
      if (res.status === 200) {
        this.roomName = (res.data as Room).name
      }
    })
  }

  get subtitle(): string {
    if (this.name === "" || this.name === undefined) {
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

  /**
   * Parses incoming WebSocket event data
   */
  public parseEvent(data: any) {
    const event = data as WebSocketEvent
    switch (event["@type"]) {
      case EventType.HeartBeat:
        console.log("Received heartbeat from server.")
        break
      case EventType.LatencyTest:
        console.log(`Received latency test from server: ${(Date.now() - this.timestamp) / 2}ms latency.`)
        break
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
