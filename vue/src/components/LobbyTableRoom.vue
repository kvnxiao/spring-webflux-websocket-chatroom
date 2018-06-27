<template lang="pug">
  tr
    td {{ room.name }}
    td {{ users }}
    td
      a.button.is-primary.is-outlined(:href="`/room/${room.id}`")
        span.icon
          i.fas.fa-check
        span JOIN
</template>

<script lang="ts">
import Room from "@/ts/Room"
import { Component, Prop, Vue } from "vue-property-decorator"

@Component
export default class LobbyTableRoom extends Vue {
  @Prop() private room!: Room

  get users(): string {
    return `${this.room.count}/8`
  }
}
</script>

<style lang="scss">
@media only screen and (max-width: 768px) {
  td button.button {
    border-radius: 2px;
    font-size: 0.75rem;
  }
}
</style>
