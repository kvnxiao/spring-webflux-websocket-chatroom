export enum NotificationType {
  Primary = "is-primary",
  Link = "is-link",
  Info = "is-info",
  Success = "is-success",
  Warning = "is-warning",
  Danger = "is-danger",
}

export default interface NotificationMessage {
  type: NotificationType
  msg: string
}
