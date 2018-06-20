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
          .background
            h3.title.is-size-4 It seems like there're no rooms beyond this point.
            h3.title.is-size-5 Make your own room!
          .contents
            table.table.is-hoverable.is-fullwidth
              tbody
                lobby-table-room(v-for="room in rooms", :key="room.id", :roomName="room.name")
    .hero-footer
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
}
</script>

<style lang="scss">
@import "~bulma/sass/utilities/initial-variables";
@import "~bulma/sass/utilities/derived-variables";
@import "~bulma/sass/utilities/mixins";

.lobby {
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
  }

  .background {
    display: none;
    bottom: 3.5rem;
    width: 100%;
    position: absolute;
    margin: 0 auto;

    h2.title,
    h3.title {
      color: #cccccc;
    }
  }

  .box {
    .contents {
      overflow-y: scroll;
      height: 11rem;
      position: relative;
    }

    .header {
      padding-right: 16px;
    }

    h2.title {
      margin-bottom: 0.5rem;
      color: #363636;
    }
  }

  @media only screen and (min-width: 768px) {
    tbody td,
    thead th {
      width: calc(7.5rem);
      &:first-child {
        width: calc(100% - 15rem);
      }
    }

    .box .contents {
      height: 22rem;
    }

    .background {
      display: block;
    }
  }
}
</style>
