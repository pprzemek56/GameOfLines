package com.example.lines;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.io.*;
import java.util.*;

import static com.example.lines.HelloApplication.*;

public class Game {
    public static final Pane boardPane = new Pane();
    public static final EventHandler<MouseEvent> ballEvent = new BallHandler();
    public static final Points points = new Points();
    public static Ball[][] board = new Ball[9][9];
    public static ArrayList<Point2D> road = new ArrayList<>();
    public static Map<Point2D,Point2D> neighbors = new HashMap<>();
    public static ArrayList<Point2D> keys = new ArrayList<>();
    public static ArrayList<Point2D> values = new ArrayList<>();
    public static ArrayList<Point2D> sameColors = new ArrayList<>();

    public static void clearBoard(){
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(board[i][j] != null){
                    Ball ball = board[i][j];

                    board[i][j] = null;

                    KeyValue keyValueStart = new KeyValue(ball.radiusProperty(),ball.getRadius());
                    KeyFrame keyFrameStart = new KeyFrame(Duration.millis(0),keyValueStart);
                    KeyValue keyValueEnd = new KeyValue(ball.radiusProperty(),-1);
                    KeyFrame keyFrameEnd = new KeyFrame(Duration.millis(200),keyValueEnd);
                    timeline.getKeyFrames().addAll(keyFrameStart, keyFrameEnd);
                }
            }
        }
        timeline.play();
    }

    public static void spawnBalls(){
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);

        int left = Math.min(left(), 3);

        for(int i = 0; i < left; i++){
            Ball ball = new Ball();
            if(board[ball.getI()][ball.getJ()] == null) {
                board[ball.getI()][ball.getJ()] = ball;

                ball.setCenterX((BLOCK_SIZE * ball.getJ()) + 20);
                ball.setCenterY((BLOCK_SIZE * ball.getI()) + 20);
                ball.setRadius(0);
                ball.setStroke(Color.BLACK);
                ball.addEventHandler(MouseEvent.MOUSE_CLICKED, ballEvent);
                boardPane.getChildren().add(ball);
                KeyValue keyValueStart = new KeyValue(board[ball.getI()][ball.getJ()].radiusProperty(), board[ball.getI()][ball.getJ()].getRadius());
                KeyFrame keyFrameStart = new KeyFrame(Duration.millis(0), keyValueStart);
                KeyValue keyValueEnd = new KeyValue(board[ball.getI()][ball.getJ()].radiusProperty(), 15);
                KeyFrame keyFrameEnd = new KeyFrame(Duration.millis(200), keyValueEnd);
                timeline.getKeyFrames().addAll(keyFrameStart, keyFrameEnd);
            }
            else i--;
        }
        timeline.play();

        left = left();
        if(left == 0){
            endPane.setVisible(true);
            if(Points.getPoints() > Points.getRecord()){
                FileWriter fileWriter = null;
                try{
                    fileWriter = new FileWriter("records.txt");
                    fileWriter.write(Points.getPoints());
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(fileWriter != null){
                        try {
                            fileWriter.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        timeline.setOnFinished((e) -> scorePoint());
    }

    public static boolean scorePoint(){

        boolean scored = false;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(i > 4 && j > 4)continue;

                if(board[i][j] != null){

                    if(getSameColors(i, j)){
                        scored = true;
                        Points.setPoints(sameColors.size());

                        Timeline timeline = new Timeline();
                        timeline.setCycleCount(1);

                        for (Point2D sameColor : sameColors) {
                            Ball ball = board[(int) sameColor.getY()][(int) sameColor.getX()];
                            KeyValue keyValueStart = new KeyValue(ball.radiusProperty(), ball.getRadius());
                            KeyFrame keyFrameStart = new KeyFrame(Duration.millis(0), keyValueStart);
                            KeyValue keyValueEnd = new KeyValue(ball.radiusProperty(), -1);
                            KeyFrame keyFrameEnd = new KeyFrame(Duration.millis(200), keyValueEnd);
                            timeline.getKeyFrames().addAll(keyFrameStart, keyFrameEnd);

                            board[(int) sameColor.getY()][(int) sameColor.getX()] = null;
                        }
                        timeline.play();
                    }
                }
            }
        }
        return scored;
    }

    public static void moveBall(int startI, int startJ, int endI, int endJ){
        if(board[startI][startJ] == null) return;

        if(isRoadExist(startI,startJ,endI,endJ)){
            getRoad(startI,startJ);

            Path path = new Path();
            MoveTo start = new MoveTo((startJ*40) + 20,(startI*40) + 20);
            path.getElements().add(start);

            for (Point2D point2D : road) {
                LineTo lineTo = new LineTo((point2D.getX() * 40) + 20, (point2D.getY() * 40) + 20);
                path.getElements().add(lineTo);
            }

            PathTransition pathTransition = new PathTransition(Duration.millis(road.size()*100),path,board[startI][startJ]);
            pathTransition.setOrientation(PathTransition.OrientationType.NONE);
            pathTransition.setCycleCount(1);
            pathTransition.play();

            board[startI][startJ].setCenterX(endJ*40+20);
            board[startI][startJ].setCenterY(endI*40+20);
            board[startI][startJ].setI(endI);
            board[startI][startJ].setJ(endJ);
            board[endI][endJ] = board[startI][startJ];
            board[startI][startJ] = null;
            pathTransition.setOnFinished((e) -> {
                if(!scorePoint()){
                    spawnBalls();
                }
            });

        }

    }

    private static int left(){
        int counter = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(board[i][j] == null)counter++;
            }
        }
        return counter;
    }

    private static void getRoad(int startI, int startJ) {
        road.clear();

        for(int i = 0; i < keys.size(); i++){
            neighbors.put(keys.get(i),values.get(i));
        }

        road.add(keys.get(keys.size()-1));

        while(true){
            Point2D value = neighbors.get(road.get(road.size()-1));
            if(value.getX() == startJ && value.getY() == startI)break;
            road.add(value);
        }

        Collections.reverse(road);
    }

    private static boolean getSameColors(int i, int j) {

        for(int k = 0; k < 4; k ++){
            sameColors.clear();
            sameColors.add(new Point2D(j,i));
            int x = j;
            int y = i;
            warunek:
            while(true){
                switch (k) {
                    case 0 -> {
                        {
                            x--;
                            y++;
                            if (x < 0 || x > 8 || y < 0 || y > 8) {
                                if (sameColors.size() >= 5) return true;

                                break warunek;
                            }
                            if (board[y][x] != null) {
                                if (board[i][j].getFill().equals(board[y][x].getFill())) {
                                    sameColors.add(new Point2D(x, y));
                                } else if (sameColors.size() >= 5) return true;
                                else {
                                    break warunek;
                                }
                            } else {
                                if (sameColors.size() >= 5) return true;
                                else break warunek;
                            }
                        }
                    }
                    case 1 -> {
                        {
                            y++;
                            if (x < 0 || x > 8 || y < 0 || y > 8) {
                                if (sameColors.size() >= 5) return true;

                                break warunek;
                            }
                            if (board[y][x] != null) {
                                if (board[i][j].getFill().equals(board[y][x].getFill())) {
                                    sameColors.add(new Point2D(x, y));
                                } else if (sameColors.size() >= 5) return true;
                                else break warunek;
                            } else {
                                if (sameColors.size() >= 5) return true;
                                else break warunek;
                            }
                        }
                    }
                    case 2 -> {
                        {
                            x++;
                            y++;
                            if (x < 0 || x > 8 || y < 0 || y > 8) {
                                if (sameColors.size() >= 5) return true;

                                break warunek;
                            }
                            if (board[y][x] != null) {
                                if (board[i][j].getFill().equals(board[y][x].getFill())) {
                                    sameColors.add(new Point2D(x, y));
                                } else if (sameColors.size() >= 5) return true;
                                else break warunek;
                            } else {
                                if (sameColors.size() >= 5) return true;
                                else break warunek;
                            }
                        }
                    }
                    case 3 -> {
                        x++;
                        if (x < 0 || x > 8 || y < 0 || y > 8) {
                            if (sameColors.size() >= 5) return true;

                            break warunek;
                        }
                        if (board[y][x] != null) {
                            if (board[i][j].getFill().equals(board[y][x].getFill())) {
                                sameColors.add(new Point2D(x, y));
                            } else if (sameColors.size() >= 5) return true;
                            else break warunek;
                        } else {
                            if (sameColors.size() >= 5) return true;
                            else break warunek;
                        }
                    }
                }

            }
        }
        return false;
    }

    private static boolean isRoadExist(int startI, int startJ, int endI, int endJ){
        keys.clear();
        values.clear();

        List<Point2D> queue = new ArrayList<>();

        while(startI != endI || startJ != endJ){
            for(int i = startI - 1; i <= startI+1; i++){
                if(i < 0 || i > 8)continue;
                for(int j = startJ - 1; j <= startJ+1; j++){
                    if(j < 0 || j > 8)continue;
                    if((i == startI - 1 && j == startJ) || (i == startI && j == startJ - 1) || (i == startI && j == startJ + 1)
                            || (i == startI + 1 && j == startJ)){
                        if(board[i][j] == null){
                            if(!keys.contains(new Point2D(j,i))){
                                queue.add(new Point2D(j,i));
                                keys.add(new Point2D(j,i));
                                values.add(new Point2D(startJ,startI));
                                if(i == endI && j == endJ)return true;
                            }
                        }
                    }
                }
            }
            if(queue.isEmpty())break;

            startI = (int) queue.get(0).getY();
            startJ = (int) queue.get(0).getX();
            queue.remove(0);
        }

        return false;
    }

}
