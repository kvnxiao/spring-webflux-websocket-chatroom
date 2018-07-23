export default interface WebSocketEvent {
  "@type": EventType
}

export enum EventType {
  LatencyTest = "l",
  LobbyList = "lb",
  MessageToServer = "m",
  MessageFromServer = "s",
  UserConnected = "uc",
  UserDisconnected = "ud",
}

export interface User {
  name: string,
  id: string
}

export interface MessageFromServerEvent {
  msg: string,
  user: User
}

export interface UserConnectDisconnectEvent {
  user: User
}
