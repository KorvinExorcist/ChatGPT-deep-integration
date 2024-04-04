package com.mygdx.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.HashMap;
import java.util.Map;

public class Graphics {
    MyGdx mygdx;
    Image fon;
    Stage stage;
    Map<Integer,Image> drawImages =new HashMap<Integer,Image>();
    Graphics(MyGdx mygdx){
        this.mygdx =mygdx;
        fon=new Image(new Texture("openai.png"));
        fon.setSize(1920,1080);
        stage = new Stage(new StretchViewport(1920, 1080));
        Gdx.input.setInputProcessor(stage);
        reloadActrosStage();
    }
    void render(){
        stage.draw();
    }
    private void reloadActrosStage(){
        stage.clear();
        stage.getActors().clear();
        stage.addActor(fon);
        stage.addActor(mygdx.chat);
        stage.setKeyboardFocus(mygdx.chat.textfieldchat);
    }
    void setPicturs(){
        mygdx.chat.sendMessagePicturs();
        fon.invalidate();
        fon =new Image(new Texture("google.jpg"));
        fon.validate();
        fon.setSize(1920,1080);
        reloadActrosStage();
    }
    void clearObjects(){
        reloadActrosStage();
        drawImages.clear();
    }
}
