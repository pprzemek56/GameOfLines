package com.example.lines;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static com.example.lines.Game.boardPane;

public class HelloApplication extends Application {

    public static final int BLOCK_SIZE = 40;
    public static final int WINDOW_HEIGHT = 400;
    public static final int WINDOW_WIDTH = 660;
    public static final int BOARD_HEIGHT = BLOCK_SIZE * 9;
    public static final int BOARD_WIDTH = BLOCK_SIZE * 9;
    public static final int MENU_WIDTH = WINDOW_WIDTH - BOARD_WIDTH;
    public static final EventHandler<MouseEvent> squareEvent = new SquareHandler();
    public static final VBox menu = new VBox(30);
    public static final Pane root = new Pane();
    public static final BorderPane endPane = new BorderPane();
    public static Scene scene;
    public static final Font font = new Font("Bookman Old Style Pogrubiony", 30);
    public static boolean isLive = false;

    @Override
    public void start(Stage stage) {
        scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        /*SET PROPERTIES OF MENU*/
        menu.setPrefSize(MENU_WIDTH,BOARD_HEIGHT);
        menu.setLayoutX(0);
        menu.setLayoutY(0);
        menu.setAlignment(Pos.CENTER);
        Button button = new Button("NEW GAME");
        button.setFont(font);
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> restartingGame());
        Text points = new Text("POINTS");
        points.setFont(font);
        Text record = new Text("BEST SCORE");
        record.setFont(font);
        menu.getChildren().addAll(button,points,Game.points.getPointsText(),record,Game.points.getRecordsText());

        /*SET PROPERTIES OF BOARD*/
        boardPane.setPrefSize(BOARD_WIDTH,BOARD_HEIGHT);
        boardPane.setLayoutX(280);
        boardPane.setLayoutY(20);
        drawBoard();

        /*SET PROPERTIES OF END SCREEN*/
        endPane.setPrefSize(BOARD_WIDTH,BOARD_HEIGHT);
        endPane.setLayoutX(280);
        endPane.setLayoutY(20);
        endPane.setOpacity(0.8);
        Text text = new Text("GAME OVER");
        text.setFont(font);
        endPane.setCenter(text);
        BorderPane.setAlignment(text, Pos.CENTER);
        endPane.setVisible(false);


        root.getChildren().addAll(menu, boardPane, endPane);

        stage.setTitle("Lines");
        stage.setScene(scene);
        stage.show();
    }

    private void drawBoard(){
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                Rectangle rect = new Rectangle(
                        (BLOCK_SIZE*j),
                        (BLOCK_SIZE*i),
                        BLOCK_SIZE,
                        BLOCK_SIZE);

                rect.setStroke(Color.BLACK);
                rect.setFill(Color.WHITE);
                rect.addEventHandler(MouseEvent.MOUSE_CLICKED, squareEvent);

                boardPane.getChildren().add(rect);
            }
        }
    }

    private static void restartingGame(){
        Points.setRecord();
        Points.setPoints(0);

        endPane.setVisible(false);

        if(isLive){
            Game.clearBoard();
        }
        else isLive = true;

        Game.spawnBalls();

    }

    public static void main(String[] args) {
        launch(args);
    }
}