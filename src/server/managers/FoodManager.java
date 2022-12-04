package server.managers;

import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;

import server.entities.Food;
import server.utils.Pos;

public class FoodManager {
  private ArrayList<Food> foods;

  public FoodManager() {
    this.foods = new ArrayList<Food>();
  }

  public void initFood(int sizeX, int sizeY) {
    for (int i = 0; i < sizeX * sizeY / 100000; i++) {
      int posX = (int) ((Math.random() * (sizeX - 0)) + 0);
      int posY = (int) ((Math.random() * (sizeY - 0)) + 0);

      foods.add(new Food(new Pos(posX, posY), randomColor(), i));
    }
  }

  public void eatFood(Food food) {
    System.out.println("Food id: " + food.getId());

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
