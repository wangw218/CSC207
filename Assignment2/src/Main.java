import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.util.Duration;

/** Our take on the "classical" game Farm Ville */
public class Main extends Application {

  /** The width of the canvas. */
  static final int CAN_WIDTH = 480;
  /** The height of the canvas. */
  static final int CAN_HEIGHT = 640;
  /** The width of a character. */
  static final int CHAR_WIDTH = 10;
  /** The height of a character. */
  static final int CHAR_HEIGHT = 6;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("FarmVille");

    Group root = new Group();
    Scene theScene = new Scene(root);
    primaryStage.setScene(theScene);
    Canvas canvas = new Canvas(CAN_WIDTH, CAN_HEIGHT);
    root.getChildren().add(canvas);

    GraphicsContext gc = canvas.getGraphicsContext2D();

    // Create animals and Human in the farm.
    // seven Chickens
    int[] chickenArr = {0, 0, 6, 12, 17, 4, 15, 28, 23, 18, 16, 35, 16, 22, 16, 35, 16, 22};
    for (int i = 0; i < chickenArr.length; i = i + 2)
      Human.myFarmAnimals.add(new Chicken(i, i + 1));

    // two pigs
    Human.myFarmAnimals.add(new Pig(10, 20));
    Human.myFarmAnimals.add(new Pig(20, 10));

    // one Cow
    Human.myFarmAnimals.add(new Cow(15, 15));

    // one cat
    Human.myFarmAnimals.add(new Cat(20, 20));

    // a human
    Human.myFarmAnimals.add(new Human(30, 30));

    drawShapes(gc);
    Timeline gameLoop = new Timeline();
    gameLoop.setCycleCount(Timeline.INDEFINITE);

    KeyFrame kf =
        new KeyFrame(
            Duration.seconds(0.5),
            ae -> {
              for (int i = 0; i != Human.myFarmAnimals.size(); i++) {
                if (Human.myFarmAnimals.get(i) != null) {
                  if (Human.myFarmAnimals.get(i) instanceof Chicken) {
                    ((Chicken) Human.myFarmAnimals.get(i)).move();
                  } else if (Human.myFarmAnimals.get(i) instanceof Pig) {
                    ((Pig) Human.myFarmAnimals.get(i)).move();
                  } else if (Human.myFarmAnimals.get(i) instanceof Cow) {
                    ((Cow) Human.myFarmAnimals.get(i)).move();
                  } else if (Human.myFarmAnimals.get(i) instanceof Cat) {
                    ((Cat) Human.myFarmAnimals.get(i)).move();
                  } else if (Human.myFarmAnimals.get(i) instanceof Human) {
                    ((Human) Human.myFarmAnimals.get(i)).move();
                  }
                }
                if (Human.myFarmAnimals.get(i) instanceof AnimalFood) {
                  AnimalFood food = (AnimalFood) Human.myFarmAnimals.get(i);
                  // Figure out whether to float up or down.
                  food.d = Wind.windBlowingUp();
                  if (food.d == -1) food.blownUp();
                  if (food.d == 1) food.blownDown();
                  // Figure out whether to float left or right.
                  food.d = Wind.windBlowingLeft();
                  if (food.d == -1) food.blownLeft();
                  if (food.d == 1) food.blownRight();
                }
                if (Human.myFarmAnimals.get(i)
                    instanceof AnimalManure) { // Delete the manure that has enough time.
                  if ((System.currentTimeMillis()
                          - ((AnimalManure) Human.myFarmAnimals.get(i)).getStartTime())
                      >= 10000) {
                    Human.myFarmAnimals.remove(i);
                  }
                }
              }

              // Clear the canvas
              gc.clearRect(0, 0, CAN_WIDTH, CAN_HEIGHT);
              drawShapes(gc);
            });

    gameLoop.getKeyFrames().add(kf);
    gameLoop.play();
    primaryStage.show();
  }

  private void drawShapes(GraphicsContext gc) {
    // Tell all the farmyard items to draw themselves.
    for (int i = 0; i != Human.myFarmAnimals.size(); i++) {
      if (Human.myFarmAnimals.get(i) != null) {
        if (Human.myFarmAnimals.get(i) instanceof Chicken) {
          ((Chicken) Human.myFarmAnimals.get(i)).draw(gc);
        } else if (Human.myFarmAnimals.get(i) instanceof Pig) {
          ((Pig) Human.myFarmAnimals.get(i)).draw(gc);
        } else if (Human.myFarmAnimals.get(i) instanceof Cow) {
          ((Cow) Human.myFarmAnimals.get(i)).draw(gc);
        } else if (Human.myFarmAnimals.get(i) instanceof Cat) {
          ((Cat) Human.myFarmAnimals.get(i)).draw(gc);
        } else if (Human.myFarmAnimals.get(i) instanceof Human) {
          ((Human) Human.myFarmAnimals.get(i)).draw(gc);
        } else if (Human.myFarmAnimals.get(i) instanceof AnimalManure) {
          ((AnimalManure) Human.myFarmAnimals.get(i)).draw(gc);
        } else if (Human.myFarmAnimals.get(i) instanceof AnimalFood) {
          ((AnimalFood) Human.myFarmAnimals.get(i)).draw(gc);
        } else if (Human.myFarmAnimals.get(i) instanceof Egg) {
          ((Egg) Human.myFarmAnimals.get(i)).draw(gc);
        }
      }
    }
  }
}
