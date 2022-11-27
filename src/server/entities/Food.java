package server.entities;

import java.io.Serializable;
import java.util.Date;

import server.utils.Pos;

public class Food implements Serializable {
  private Pos pos;
  private Date eatenTime = null;
  private int id;

  public Food(Pos pos, int id) {
    this.pos = pos;
    this.id = id;
  }

  public Pos getPos() {
    return pos;
  }

  public void eat() {
    this.eatenTime = new Date();
  }

  public boolean isEat() {
    return this.eatenTime != null;
  }

  public void spawn() {
    this.eatenTime = null;
  }

  public Date getEatenTime() {
    return eatenTime;
  }

  public int getId() {
    return this.id;
  }
}
