package server.utils;

import java.io.Serializable;

public class Pos implements Serializable {
  private int posX;
  private int posY;

  public Pos(int posX, int posY) {
    this.posX = posX;
    this.posY = posY;
  }

  public int getPosX() {
    return this.posX;
  }

  public void setPosX(int posX) {
    this.posX = posX;
  }

  public int getPosY() {
    return this.posY;
  }

  public void setPosY(int posY) {
    this.posY = posY;
  }

  @Override
  public String toString() {
    return "Pos [posX=" + this.posX + ", posY=" + this.posY + "]";
  }

}
