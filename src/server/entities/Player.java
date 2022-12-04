package server.entities;

import java.io.Serializable;
import java.awt.Color;

import server.utils.Pos;

public class Player implements Serializable {
  private Pos pos;
  private Color color;
  private int mass = 30;
  private String username;
  private boolean isDead;

  public Player(Pos pos, Color color, String username) {
    this.pos = pos;
    this.username = username;
    this.color = color;
  }

  public Pos getPos() {
    return this.pos;
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

  public void setMass(int mass) {
    this.mass = mass;
  }

  public int getMass() {
    return this.mass;
  }

  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public boolean isDead() {
    return this.isDead;
  }

  public void setDead(boolean isDead) {
    this.isDead = isDead;
  }

  public Color getColor() {
    return this.color;
  }

  public boolean collide(int w, int h, int x, int y) {
    if (w <= 0.0 || h <= 0.0) {
      return false;
    }
    // Normalize the rectangular coordinates compared to the ellipse
    // having a center at 0,0 and a radius of 0.5.
    double ellw = getMass();
    if (ellw <= 0.0) {
      return false;
    }
    double normx0 = (x - pos.getPosX()) / ellw - 0.5;
    double normx1 = normx0 + w / ellw;
    double ellh = getMass();
    if (ellh <= 0.0) {
      return false;
    }
    double normy0 = (y - pos.getPosY()) / ellh - 0.5;
    double normy1 = normy0 + h / ellh;
    // find nearest x (left edge, right edge, 0.0)
    // find nearest y (top edge, bottom edge, 0.0)
    // if nearest x,y is inside circle of radius 0.5, then intersects
    double nearx, neary;
    if (normx0 > 0.0) {
      // center to left of X extents
      nearx = normx0;
    } else if (normx1 < 0.0) {
      // center to right of X extents
      nearx = normx1;
    } else {
      nearx = 0.0;
    }
    if (normy0 > 0.0) {
      // center above Y extents
      neary = normy0;
    } else if (normy1 < 0.0) {
      // center below Y extents
      neary = normy1;
    } else {
      neary = 0.0;
    }
    return (nearx * nearx + neary * neary) < 0.25;
  }

}
