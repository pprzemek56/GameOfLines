package com.example.lines;

import javafx.animation.Animation;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class SquareHandler implements EventHandler<MouseEvent> {

    public static int nextI;
    public static int nextJ;

    @Override
    public void handle(MouseEvent mouseEvent) {
        if(BallHandler.timeline.getStatus() == Animation.Status.RUNNING){
            BallHandler.timeline.stop();
            BallHandler.timeline.getKeyFrames().clear();
            Game.board[BallHandler.currentI][BallHandler.currentJ].setRadius(15);
        }

        int x = (int) mouseEvent.getX();
        int y = (int) mouseEvent.getY();


        nextI = getIJ(y);
        nextJ = getIJ(x);

        Game.moveBall(BallHandler.currentI, BallHandler.currentJ, nextI, nextJ);
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
