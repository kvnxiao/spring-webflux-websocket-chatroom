<template lang="pug">
  section.room.hero.is-primary.is-fullheight
    .hero-head
      h1.title You're in room: {{ roomName }}
    .hero-body
      .container.has-text-centered
        #chat.box
          latency-bar
          ul#chat-msgs
            li(v-for="message in messages")
              .user {{ message.user }}
              .msg  {{ message.message }}
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
import LatencyBar from "@/components/LatencyBar.vue"
import LatencyTest from "@/ts/LatencyTest"
import axios, {AxiosResponse} from "axios"
import { Component, Vue } from "vue-property-decorator"
import NativeWebSocket from "../ts/NativeWebSocket"
import Room, {ChatMessage} from "../ts/Room"
import WebSocketEvent, {EventType, MessageFromServerEvent, UserConnectDisconnectEvent} from "../ts/WebSocketEvent"

@Component({
  components: {
    LatencyBar,
  },
})
export default class Home extends Vue {
  private name: string = ""
  private roomName: string = "..."
  private messages: ChatMessage[] = []
  private currMessage: string = ""
  private ws!: NativeWebSocket<WebSocketEvent>
  private latencyTest!: LatencyTest

  /**
   * Vue beforeCreate lifecycle hook.
   *
   * Initializes WebSocket connection to server and sets the WebSocket event handlers.
   */
  public beforeCreate() {
    // Connect to websocket endpoint
    this.ws = new NativeWebSocket(undefined, "/ws")

    // set latency test interval
    this.latencyTest = new LatencyTest(10_000, () => {
      if (this.ws.readyState === this.ws.OPEN) {
        this.ws.send({ "@type": EventType.LatencyTest })
      }
    })

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
      this.latencyTest.clearInterval()
      // this.messages.push({ msg: "Connection to the server has been closed.", type: NotificationType.Warning })
    }
  }

  public created() {
    this.ws.connect()

    this.name = this.$store.getters.getName
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
      const msg = {
        "@type": EventType.MessageToServer,
        "msg": this.currMessage,
      }
      this.ws.send(msg)
      this.currMessage = ""
    }
  }

  /**
   * Parses incoming WebSocket event data
   */
  public parseEvent(event: any) {
    if (event["@type"]) {
      switch (event["@type"]) {
        case EventType.LatencyTest: {
          const latency = this.latencyTest.latency
          this.$store.commit("setLatency", latency)
          break
        }
        case EventType.MessageFromServer: {
          const message = event as MessageFromServerEvent
          this.messages.push({ type: 0, user: `${message.user.name}:`, message: message.msg })
          break
        }
        case EventType.UserConnected: {
          const message = event as UserConnectDisconnectEvent
          this.messages.push({ type: 1, user: message.user.name, message: "has joined the room." })
          break
        }
        case EventType.UserDisconnected: {
          const message = event as UserConnectDisconnectEvent
          this.messages.push({ type: 1, user: message.user.name, message: "has left the room." })
          break
        }
      }
    }
  }
}
</script>

<style lang="scss">
#chat.box {
  position: relative;
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

    .user {
      font-weight: 700;
    }

    .user, .msg {
      display: inline;
    }
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
