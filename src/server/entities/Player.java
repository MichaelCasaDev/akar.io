package server.entities;

import java.io.Serializable;

import server.utils.Pos;

public class Player implements Serializable {
  private Pos pos;
  private int mass = 30;
  private String username;

  public Player(Pos pos, String username) {
    this.pos = pos;
    this.username = username;
  }

  public Pos getPos() {
    return pos;
  }

  public void setPos(Pos pos) {
    this.pos = pos;
  }

  public void eatFood(int food) {
    this.mass += food;
  }

  public void loseFood(int food) {
    this.mass -= food;
  }

  public int getMass() {
    return mass;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

}
