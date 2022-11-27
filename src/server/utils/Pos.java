package server.utils;

public class Pos {
  private int posX;
  private int posY;

  public Pos(int posX, int posY) {
    this.posX = posX;
    this.posY = posY;
  }

  public int getPosX() {
    return posX;
  }

  public void setPosX(int posX) {
    this.posX = posX;
  }

  public int getPosY() {
    return posY;
  }

  public void setPosY(int posY) {
    this.posY = posY;
  }

  public boolean collide(Pos pos, int size) {
    return false;
  }

}
