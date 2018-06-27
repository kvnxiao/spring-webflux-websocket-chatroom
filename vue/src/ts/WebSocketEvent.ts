export default interface WebSocketEvent {
    "@type": EventType
}

export enum EventType {
  HeartBeat = "h",
  LatencyTest = "l",
  Room_Create = "rc",
}
