package GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Practice extends Application {

    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 400;
    private static final int ROBOT_SIZE = 50;

    private double robotX = (double) WINDOW_WIDTH / 2 - (double) ROBOT_SIZE / 2;
    private double robotY = (double) WINDOW_HEIGHT / 2 - (double) ROBOT_SIZE / 2;

    @Override
    public void start(Stage primaryStage) {
        Rectangle robot = createRobot(robotX, robotY, ROBOT_SIZE);

        Pane root = new Pane();
        root.getChildren().add(robot);

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.setOnKeyPressed(event -> moveRobot(event.getCode(), robot));

        primaryStage.setTitle("Robot");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Rectangle createRobot(double x, double y, double size) {
        Rectangle robot = new Rectangle(x, y, size, size);
        robot.setFill(Color.RED);
        return robot;
    }

    private void moveRobot(KeyCode keyCode, Rectangle robot) {
        switch (keyCode) {
            case UP -> robotY -= 10;
            case DOWN -> robotY += 10;
            case LEFT -> robotX -= 10;
            case RIGHT -> robotX += 10;
        }
        robot.setX(robotX);
        robot.setY(robotY);
    }

    public static void main(String[] args) {
        launch(args);
    }
}