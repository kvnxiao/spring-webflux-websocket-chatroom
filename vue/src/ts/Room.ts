export interface ChatMessage {
  type: number
  user: string
  message: string
}

export default interface Room {
  count: number
  hasPwd: boolean
  id: string
  name: string
}
