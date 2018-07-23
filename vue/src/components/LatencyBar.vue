<template lang="pug">
  .latency(:class="style")
    span.icon
      i.fas.fa-signal
    |  {{ latency }}ms
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator"

@Component
export default class LatencyBar extends Vue {
  private style: string = "green"

  get latency(): number {
    const latency = this.$store.getters.getLatency
    if (latency <= 35) {
      this.setStyle("green")
    } else if (latency <= 75) {
      this.setStyle("orange")
    } else {
      this.setStyle("red")
    }
    return latency <= 0 ? 1 : latency
  }

  private setStyle(style: string) {
    this.style = style
  }
}
</script>

<style lang="scss">
.latency {
  position: absolute;
  font-size: 1.25rem;
  right: 0;
  margin-right: 2rem;

  &.green {
    color: hsl(141, 71%, 48%);
  }

  &.orange {
    color: #f29718;
  }

  &.red {
    color: hsl(348, 100%, 61%);
  }
}
</style>
