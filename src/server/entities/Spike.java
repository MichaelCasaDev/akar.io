package server.entities;

import java.io.Serializable;

import server.utils.Pos;

public class Spike implements Serializable {
  private Pos pos;
  private final int size = 60;
  private int id;

  public Spike(Pos pos, int id) {
    this.pos = pos;
    this.id = id;
  }

  public Pos getPos() {
    return pos;
  }

  public int getSize() {
    return size;
  }

  public int getId() {
    return this.id;
  }
}
