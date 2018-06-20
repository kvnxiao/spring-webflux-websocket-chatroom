export default class Room {
  public static of(id: number, name: string): Room {
    return new Room(id, name)
  }
  constructor(readonly id: number, readonly name: string) {}
}
