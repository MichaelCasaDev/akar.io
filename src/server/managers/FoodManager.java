package server.managers;

import java.util.ArrayList;

import server.entities.Food;
import server.utils.Pos;

public class FoodManager {
  private ArrayList<Food> foods;
  private final int respawnRate = 30;

  public FoodManager() {
    this.foods = new ArrayList<Food>();
  }

  public void initFood(int sizeX, int sizeY) {
    for (int i = 0; i < sizeX * sizeY / 1000; i++) {
      int posX = (int) ((Math.random() * (sizeX - 0)) + 0);
      int posY = (int) ((Math.random() * (sizeY - 0)) + 0);

      foods.add(new Food(new Pos(posX, posY), i));
    }
  }

  public void eatFood(int id) {
    for (Food f : foods) {
      if (f.getId() == id) {
        f.eat();

        // Respawn food after x seconds
        (new Thread() {
          public void run() {
            try {
              Thread.sleep(respawnRate * 1000);

              f.spawn();
            } catch (Exception ex) {
              ex.printStackTrace();
            }
          }
        }).start();

      }
    }
  }

  public int getTotalFoods() {
    return foods.size();
  }

  public Food[] getFoods() {
    Food[] foodArr = new Food[foods.size()];
    for (int i = 0; i < foods.size(); i++) {
      foodArr[i] = foods.get(i);
    }

    return foodArr;
  }
}
