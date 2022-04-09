package com.example.lines;

import javafx.scene.text.Text;

import java.io.*;

public class Points implements Serializable {

    public static Text pointsText = new Text("0");
    public static Text recordsText = new Text();
    private static int points;
    private static int record;
    private static BufferedReader bufferedReader = null;

    public Text getRecordsText() {
        return recordsText;
    }
    public Points(){
        recordsText.setFont(HelloApplication.font);
        pointsText.setFont(HelloApplication.font);
        points = 0;
        setRecord();
    }

    public static int getRecord() {
        return record;
    }

    public Text getPointsText() {
        return pointsText;
    }

    public static void setPointsText() {
        pointsText.setText(String.valueOf(getPoints()));
    }

    public static void setRecordsText() {
        recordsText.setText(String.valueOf(getRecord()));
    }

    public static int getPoints() {
        return points;
    }

    public static synchronized void setPoints(int points) {
        if(points == 0){
            Points.points = 0;
        }
        else{
            Points.points += points;
        }
        setPointsText();
    }

    public static synchronized void setRecord(){
        try{
            bufferedReader = new BufferedReader(new FileReader("records.txt"));
            record = bufferedReader.read();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        setRecordsText();
    }
}
