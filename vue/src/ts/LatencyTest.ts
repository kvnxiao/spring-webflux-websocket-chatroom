export default class LatencyTest {
  private windowInterval: number
  private timestamp: number = Date.now()

  constructor(delayMs: number, func: () => void) {
    this.windowInterval = window.setInterval(() => {
      this.timestamp = Date.now()
      func()
    }, delayMs)
  }

  /**
   * Clears interval for recurrent latency tests
   */
  public clearInterval() {
    window.clearInterval(this.windowInterval)
  }

  get latency(): number {
    return Math.round((Date.now() - this.timestamp) / 2)
  }
}
