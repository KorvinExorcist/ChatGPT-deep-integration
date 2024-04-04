package com.mygdx.game.core;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.HashMap;
import java.util.Map;

public class Logic {
    MyGdx mygdx;
    ParcerCoordinates parcer;
    Map<Integer, Objectt> objects = new HashMap<>();
    boolean run;
    public Logic(MyGdx myGdx){
       this.mygdx =myGdx;
       parcer= new ParcerCoordinates(myGdx);
    }
    int i=1;
    void update(){
        if(i<998&&run){i++;
            for(Objectt obj:objects.values()){
                mygdx.graphics.drawImages.get(obj.getNumber()).setPosition(obj.getX(i),obj.getY(i));
            }
            if(i==997)clearObjects();}
    }
    public void createObjects(){
        parcer.StartParcer();
        for (Objectt obj:objects.values()){
            System.out.println("Create Objects");
            Image img=new Image(new Texture("object.png"));
            mygdx.graphics.drawImages.put(obj.getNumber(),img);
            img.setSize(50,50);
            mygdx.graphics.stage.addActor(img);
        }
        run=true;
    }
    void clearObjects(){
        i=1;
        run=false;
        objects.clear();
        mygdx.graphics.clearObjects();
    }
}
