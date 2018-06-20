<template lang="pug">
  tr
    td {{ roomName }}
    td {{ users }}
    td
      button.button.is-primary.is-outlined
        span.icon
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
@media only screen and (max-width: 768px) {
  td button.button {
    border-radius: 2px;
    font-size: 0.75rem;
  }
}
</style>
