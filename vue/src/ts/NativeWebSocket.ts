export default class NativeWebSocket<T> {
  public readonly CLOSED: number = WebSocket.CLOSED
  public readonly CLOSING: number = WebSocket.CLOSING
  public readonly CONNECTING: number = WebSocket.CONNECTING
  public readonly OPEN: number = WebSocket.OPEN

  private ws!: WebSocket

  constructor(
    private readonly baseUrl: string = window.location.protocol === "https:"
      ? "wss://"
      : "ws://" + window.location.host + window.location.pathname,
    private readonly appendToUrl: string = "",
  ) {}

  public onMessage: ((event: MessageEvent) => any) = () => {
    // no-op
  }

  public onClose: ((event: CloseEvent) => any) = () => {
    // no-op
  }

  public onOpen: ((event: Event) => any) = () => {
    // no-op
  }

  public onError: ((event: Event) => any) = () => {
    // no-op
  }

  public connect() {
    this.ws = new WebSocket(this.baseUrl + this.appendToUrl)
    this.ws.onopen = this.onOpen
    this.ws.onerror = this.onError
    this.ws.onmessage = this.onMessage
    this.ws.onclose = this.onClose
  }

  public send(obj: T) {
    if (typeof obj === "string") {
      this.ws.send(obj)
    } else {
      this.ws.send(JSON.stringify(obj))
    }
  }

  public getReadyState(): number {
    return this.ws.readyState
  }

  public getProtocol(): string {
    return this.ws.protocol
  }
}
