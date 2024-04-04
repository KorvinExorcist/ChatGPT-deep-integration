package com.mygdx.game.core;

import com.badlogic.gdx.Gdx;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Input {
    MyGdx mygdx;
    Input(MyGdx mygdx){
        this.mygdx =mygdx;
    }
    void input(){
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ENTER)){
            System.out.println(mygdx.chat.textOrPicturs);
            if(!mygdx.chat.textOrPicturs) {
                try {
                    mygdx.chat.sendMessageText();
                } catch (IOException | ClassNotFoundException | InvocationTargetException | InstantiationException |
                         IllegalAccessException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }else{mygdx.graphics.setPicturs();}
        }
    }
}
