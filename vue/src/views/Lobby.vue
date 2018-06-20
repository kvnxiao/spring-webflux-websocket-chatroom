<template lang="pug">
  section.lobby.hero.is-primary.is-fullheight
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
                lobby-table-room(v-for="room in rooms", :key="room.id", :roomName="room.name")
            .background
              h3.title.is-size-4 It seems like there're no rooms beyond this point.
              h3.title.is-size-5 Make your own room!
          hr
          button.button.is-primary.circular.bigger(@click="showModal()") Create a room
    .hero-footer
    .modal(:class="{ 'is-active': isModal }")
      .modal-background
      .modal-content
        .box
          header.modal-head
            p.modal-card-title Create a room
            button.delete(@click="hideModal()", aria-label="close")
          hr
          section.modal-body
            .field
              p.control.has-icons-left
                input.input.is-rounded.is-medium(type="text", placeholder="Room name")
                span.icon.is-small.is-left
                  i.fas.fa-tag
            .field
              p.control.has-icons-left
                input.input.is-rounded.is-medium(type="text", placeholder="Password")
                span.icon.is-small.is-left
                  i.fas.fa-lock
          hr
          footer.modal-footer
            button.button.circular.is-success Create
            button.button.circular.is-danger(@click="hideModal()") Cancel
</template>

<script lang="ts">
import LobbyTableRoom from "@/components/LobbyTableRoom.vue"
import Room from "@/ts/Room.ts"
import { Component, Vue } from "vue-property-decorator"

@Component({
  components: {
    LobbyTableRoom,
  },
})
export default class Lobby extends Vue {
  private rooms: Room[] = [Room.of(0, "Placeholder room name")]
  private isModal: boolean = false

  public showModal() {
    this.isModal = true
  }

  public hideModal() {
    this.isModal = false
  }
}
</script>

<style lang="scss">
@import "~bulma/sass/utilities/initial-variables";
@import "~bulma/sass/utilities/derived-variables";
@import "~bulma/sass/utilities/mixins";

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

  .hero-head,
  .hero-footer {
    padding: 2rem;
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
