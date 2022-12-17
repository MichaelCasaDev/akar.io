package server.managers;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.awt.Color;
import java.time.Instant;

import server.entities.Food;
import server.utils.Pos;

public class FoodManager {
  private ArrayList<Food> foods;

  public FoodManager() {
    this.foods = new ArrayList<Food>();
  }

  public void initFood(int tot) {
    System.out.println("[GAME MAP " + Instant.ofEpochSecond(new Date().getTime() / 1000) + "] | Generating foods...");

    for (int i = 0; i < tot; i++) {
      int posX = (int) ((Math.random() * (9000 - 1000)) + 1000);
      int posY = (int) ((Math.random() * (9000 - 1000)) + 1000);

      foods.add(new Food(new Pos(posX, posY), randomColor(), i));
    }

    System.out.println("[GAME MAP " + Instant.ofEpochSecond(new Date().getTime() / 1000) + "] | Food generated!");
  }

  public void eatFood(Food food) {
    for (int i = 0; i < foods.size(); i++) {
      if (foods.get(i).getId() == food.getId() && !food.isEat()) {
        foods.get(i).eat();
      }
    }
  }

  public int getTotalFoods() {
    return foods.size();
  }

  public Food[] getFoods() {
    Food[] foodArr = new Food[foods.size()];
    for (int i = 0; i < foods.size(); i++) {
      if (!foods.get(i).isEat()) {
        foodArr[i] = foods.get(i);
      }
    }

    return foodArr;
  }

  private Color randomColor() {
    Random rand = new Random();

    int r = (int) (rand.nextFloat() * 255);
    int g = (int) (rand.nextFloat() * 255);
    int b = (int) (rand.nextFloat() * 255);
    Color randomColor = new Color(r, g, b);

    return randomColor;
  }
}
