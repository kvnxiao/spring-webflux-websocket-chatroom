export default interface WebSocketEvent {
  "@type": EventType
  data?: any
}

export enum EventType {
  HeartBeat = "h",
  LatencyTest = "l",
  LobbyList = "lb",
}
