package com.mygdx.game.core;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ParcerCoordinates {
MyGdx myGdx;
public ParcerCoordinates(MyGdx myGdx){
    this.myGdx=myGdx;
}
     public  void StartParcer(){
         System.out.println("Start parsing file with coordinates");
         String fileName = "coordinates.txt";
         try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
             String line;
             while ((line = br.readLine()) != null) {
                 if (line.startsWith("Object")) {
                     String[] parts = line.split(" ");
                     try {
                     int objectNumber = Integer.parseInt(parts[1]);
                     parts[3]=parts[3].substring(1,parts[3].length()-1);
                     parts[4]=parts[4].substring(0,parts[4].length()-1);
                     float x = Float.parseFloat(parts[3]);
                     float y = Float.parseFloat(parts[4]);
                     if (myGdx.logic.objects.containsKey(objectNumber)) {
                         Objectt object = myGdx.logic.objects.get(objectNumber);
                         object.addCoordinates(x, y);
                     } else {
                         Objectt object = new Objectt(objectNumber, x, y);
                         myGdx.logic.objects.put(objectNumber, object);
                     }}
                     catch (ArrayIndexOutOfBoundsException e){
                         System.out.println("Error out of bound");
                     }
                 }
             }
         } catch (IOException e) {
             e.printStackTrace();
         }
     }
}

