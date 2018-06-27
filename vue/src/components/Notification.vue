<template lang="pug">
    transition-group.alert(name="notification", tag="ul")
      li.notification(v-for="(msg, index) in messages", v-bind:key="index", :class="[msg.type]")
        button.delete(@click="close(index)")
        | {{ msg.msg }}
</template>

<script lang="ts">
import NotificationMessage, {
  NotificationType,
} from "@/ts/NotificationMessage.ts"
import { Component, Prop, Vue } from "vue-property-decorator"

@Component
export default class Notification extends Vue {
  @Prop() private messages!: NotificationMessage[]

  public close(index: number) {
    this.messages.splice(index, 1)
  }
}
</script>

<style lang="scss">
.alert {
  position: absolute;
  bottom: 0;
  right: 0;
  display: flex;
  flex-direction: column-reverse;
  padding-bottom: 4rem;

  .notification {
    right: 2rem;
    margin-bottom: 1rem;
    width: 20rem;
    transition: all 0.3s ease;
  }
}

.notification-enter, .notification-leave-to {
  opacity: 0;
}

.notification-leave-active {
  position: absolute;
  right: 2rem;
}
</style>
