<template lang="pug">
  section.lobby.hero.is-primary.is-fullheight
    transition(name="fade")
      .modal.is-active(v-show="isModal")
        .modal-background
        .modal-content
          .box
            header.modal-head
              p.modal-card-title.has-text-weight-bold Create a room
              button.delete(@click="showModal(false)", aria-label="close")
            hr
            section.modal-body
              .field
                p.control.has-icons-left
                  input.input.is-rounded.is-medium(type="text", placeholder="Room name", v-model="createRoomName", @keyup.enter="nextFocus")
                  span.icon.is-small.is-left
                    i.fas.fa-tag
              .field
                p.control.has-icons-left
                  input#pwd.input.is-rounded.is-medium(type="text", placeholder="Password (optional)", v-model="createRoomPwd", @keyup.enter="createRoom")
                  span.icon.is-small.is-left
                    i.fas.fa-lock
            hr
            footer.modal-footer
              button.button.circular.is-success(@click="createRoom") Create
              button.button.circular.is-danger(@click="showModal(false)") Cancel
    .hero-head
      h1.title Lobby
    .hero-body
      .container.has-text-centered
        .box
          h2.title.is-size-4 Join a Room
          .header
            table.table.is-hoverable.is-fullwidth
              thead
                tr
                  th.a Room
                  th.b Users
                  th.c Join
          .contents
            table.table.is-hoverable.is-fullwidth
              tbody
                lobby-table-room(v-for="room in rooms", :key="room.id", :room="room")
            .background
              h3.title.is-size-4 It seems like there're no rooms beyond this point.
              h3.title.is-size-5 Make your own room!
          hr
          latency-bar
          button.button.is-primary.circular.bigger(@click="showModal(true)") Create a room
    .hero-footer
    notification(:messages="messages")
</template>

<script lang="ts">
import LatencyBar from "@/components/LatencyBar.vue"
import LobbyTableRoom from "@/components/LobbyTableRoom.vue"
import Notification from "@/components/Notification.vue"
import LatencyTest from "@/ts/LatencyTest"
import NativeWebSocket from "@/ts/NativeWebSocket"
import NotificationMessage, { NotificationType } from "@/ts/NotificationMessage"
import Room from "@/ts/Room.ts"
import WebSocketEvent, { EventType } from "@/ts/WebSocketEvent"
import axios, { AxiosResponse } from "axios"
import { Component, Vue } from "vue-property-decorator"

@Component({
  components: {
    Notification,
    LobbyTableRoom,
    LatencyBar,
  },
})
export default class Lobby extends Vue {
  private rooms: Room[] = []
  private isModal: boolean = false
  private ws!: NativeWebSocket<WebSocketEvent>
  private latencyTest!: LatencyTest

  // Notifications
  private messages: NotificationMessage[] = []

  // For room creation
  private createRoomName: string = ""
  private createRoomPwd: string = ""

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
      this.messages.push({
        msg: "Connection to the server has been established",
        type: NotificationType.Success,
      })
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
      this.messages.push({
        msg: "An error occurred with the server connection...",
        type: NotificationType.Danger,
      })
    }

    this.ws.onClose = (event: CloseEvent) => {
      this.latencyTest.clearInterval()
      this.messages.push({
        msg: "Connection to the server has been closed.",
        type: NotificationType.Warning,
      })
    }
  }

  /**
   * Vue created lifecycle hook.
   *
   * Initiates WebSocket connection to the server.
   */
  public created() {
    this.ws.connect()

    this.messages.push({
      msg: "Connecting to the server...",
      type: NotificationType.Link,
    })
  }

  /**
   * Toggles the room creation modal.
   */
  public showModal(bool: boolean) {
    this.isModal = bool
  }

  /**
   * Sends request to server to create a new room.
   */
  public createRoom() {
    const roomReq =
      this.createRoomPwd === ""
        ? {
            name: this.createRoomName,
          }
        : {
            name: this.createRoomName,
            pwd: this.createRoomPwd,
          }
    axios
      .post("/api/createroom", roomReq, {
        headers: { "Content-Type": "application/json" },
      })
      .then((res: AxiosResponse) => {
        if (res.status === 200) {
          // redirect user to new room
          const room = res.data as Room
          window.location.pathname = `/room/${room.id}`
        }
      })
  }

  /**
   * Used to change input focus to 'password field' from 'room name field' in the room creation modal.
   */
  public nextFocus(event: Event) {
    const target = event.target as Element
    const parent = target.parentElement!.parentElement!.parentElement!
    const input = parent.querySelector("#pwd") as HTMLElement
    input.focus()
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
        case EventType.LobbyList: {
          this.rooms = event.rooms as Room[]
          break
        }
      }
    }
  }
}
</script>

<style lang="scss">
@import "~bulma/sass/utilities/initial-variables";
@import "~bulma/sass/utilities/derived-variables";
@import "~bulma/sass/utilities/mixins";

.fade-enter-active,
.fade-leave-active {
  transition: all 0.3s ease;
}

.fade-enter,
.fade-leave-to {
  opacity: 0;
}

.lobby {
  .modal-head {
    display: flex;
  }

  .modal-footer {
    button.button.circular {
      margin: 0 0.5rem;
      font-weight: 700;
      height: 2.5rem;
      padding: 0 1rem;
    }
  }

  table.header {
    margin-bottom: 0;
  }

  tbody td,
  thead th {
    width: calc(33.3%);
    max-width: 0;
    text-overflow: ellipsis;
    overflow-x: hidden;
    white-space: nowrap;
    line-height: 1.75rem;
  }

  .background {
    display: none;
    bottom: 3rem;
    width: 100%;
    position: absolute;
    margin: 0 auto;

    h2.title,
    h3.title {
      color: #cccccc;
    }
  }

  .box {
    border-radius: 1.5rem;
    box-shadow: 0 1px 2px 0 rgba(60, 64, 67, 0.302),
      0 1px 3px 1px rgba(60, 64, 67, 0.149);
    transition: all 0.08s linear;
    &:hover,
    &:focus {
      box-shadow: 0 1px 3px 0 rgba(60, 64, 67, 0.302),
        0 4px 8px 3px rgba(60, 64, 67, 0.149);
    }

    .contents {
      overflow-y: scroll;
      height: 11rem;
      position: relative;
    }

    @include desktop {
      .header {
        padding-right: 16px;
      }
    }

    h2.title {
      margin-bottom: 0.5rem;
      color: #363636;
    }
  }

  @media only screen and (min-width: 768px) {
    tbody td,
    thead th {
      width: 7rem;
      &:first-child {
        width: calc(100% - 14rem);
      }
      line-height: 2.2rem;
    }

    .box .contents {
      height: 34.375vh;
    }

    .background {
      display: block;
    }
  }
}
</style>
