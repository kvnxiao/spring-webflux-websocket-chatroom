<template lang="pug">
  tr
    td {{ roomName }}
    td {{ users }}
    td
      button.button.is-primary.is-outlined.is-small
        span.icon.is-small
          i.fas.fa-check
        span JOIN
</template>

<script lang="ts">
import { Component, Prop, Vue } from "vue-property-decorator"

@Component
export default class LobbyTableRoom extends Vue {
  @Prop() private roomName!: string

  get users(): string {
    return "0/8"
  }
}
</script>

<style lang="scss">
</style>
