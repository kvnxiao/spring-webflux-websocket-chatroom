<template lang="pug">
  section.hero.is-fullheight
    .hero-body
      .container
        h1.title Chat
        h2.subtitle Hi {{ name }}, type and enter text below to start chatting.
        .field
          .control
            input.input.is-rounded.is-medium(type="text", placeholder="Chat here")
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator"

@Component
export default class Home extends Vue {

  private name: string = ""

}
</script>
