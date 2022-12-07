package server.entities;

import java.io.Serializable;
import java.awt.Color;

import server.utils.Pos;

public class Food implements Serializable {
  private Pos pos;
  private Color color;
  private boolean isEaten;
  private int id;
  private final int respawnRate = 45; // 45s (respawn food in the same pos after x seconds)

  public Food(Pos pos, Color color, int id) {
    this.pos = pos;
    this.id = id;
    this.color = color;
    this.isEaten = false;
  }

  public Pos getPos() {
    return pos;
  }

  public void eat() {
    this.isEaten = true;

    // Respawn food after x seconds
    (new Thread() {
      public void run() {
        try {
          Thread.sleep(respawnRate * 1000);

          spawn();
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }).start();
  }

  public boolean isEat() {
    return this.isEaten;
  }

  public void spawn() {
    this.isEaten = false;
  }

  public int getId() {
    return this.id;
  }

  public Color getColor() {
    return color;
  }
}
