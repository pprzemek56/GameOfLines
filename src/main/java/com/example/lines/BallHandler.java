package com.example.lines;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class BallHandler implements EventHandler<MouseEvent> {

    public static final Timeline timeline = new Timeline();
    public static int currentI;
    public static int currentJ;

    @Override
    public void handle(MouseEvent mouseEvent) {
        if(timeline.getStatus() == Animation.Status.RUNNING){
            timeline.jumpTo("start");
            timeline.stop();
            timeline.getKeyFrames().clear();
        }
        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();

        currentI = getIJ(y);
        currentJ = getIJ(x);

        timeline.setCycleCount(Animation.INDEFINITE);
        KeyValue keyValueStart = new KeyValue(Game.board[currentI][currentJ].radiusProperty(),Game.board[currentI][currentJ].getRadius());
        KeyFrame keyFrameStart = new KeyFrame(Duration.millis(0),"start",keyValueStart);
        KeyValue keyValueMid = new KeyValue(Game.board[currentI][currentJ].radiusProperty(),7);
        KeyFrame keyFrameMid = new KeyFrame(Duration.millis(200), keyValueMid);
        KeyValue keyValueEnd = new KeyValue(Game.board[currentI][currentJ].radiusProperty(),15);
        KeyFrame keyFrameEnd = new KeyFrame(Duration.millis(400), keyValueEnd);
        timeline.getKeyFrames().addAll(keyFrameStart,keyFrameMid,keyFrameEnd);
        timeline.play();

    }

    private static int getIJ(int n){
        int counter = 0;

        while(true){
            if(n-40 < 0){
                return counter;
            }
            else{
                n -= 40;
                counter++;
            }
        }

    }
}
