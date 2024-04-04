package com.mygdx.game.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.gpt.GPTpicturs;
import com.mygdx.game.gpt.GPTtext;
import com.mygdx.game.utils.Text;
import com.theokanning.openai.utils.TikTokensUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Chat extends Group{
    public TextField textfieldchat;
    List<Label> lchat = new ArrayList<>();
    Group gtext, gchat;
    TextButton ok;
    Text text;
    public Label tokens;
    Label model;
    MyGdx mygdx;
    boolean textOrPicturs;

    float r=0.1f,g=0.8f,b=0.5f;

    public Chat(MyGdx mygdx) {
        this.mygdx =mygdx;
        text=new Text();
        gtext = new Group();
        gchat = new Group();
        addActor(gtext);
        textfieldchat = new TextField("", text.tgamechat);
        textfieldchat.setSize(1920, 55);
        textfieldchat.setPosition(0, 1025);
        textfieldchat.setMessageText("");
        textfieldchat.setOnlyFontChars(true);
        gchat.addActor(textfieldchat);
        ok = new TextButton("ok", text.ButtonStyle);
        ok.setSize(80, 57);
        ok.setPosition(1840, 1025);
        ok.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sendMessagePicturs();
                super.clicked(event, x, y);
            }
        });
        ok.setVisible(false);
        gchat.addActor(ok);
        gchat.setVisible(true);
        addActor(gchat);

        Label firstl = new Label("first test label",text.lgamechat);
        firstl.setPosition(0,1020);
        lchat.add(firstl);
        Label second = new Label("second test label",text.lgamechat);
        second.setPosition(0,1050);
        lchat.add(firstl);

        mygdx.gptText.tokens = TikTokensUtil.tokens(TikTokensUtil.ModelEnum.GPT_3_5_TURBO_0301.getName(), mygdx.gptText.messages);
        tokens=new Label(mygdx.gptText.tokens+"/4097",text.lgamechat);
        tokens.setPosition(1750,990);
        addActor(tokens);

        model=new Label("gpt-3.5-turbo",text.lgamechat);
        model.setPosition(1980-260,960);
        model.setColor(Color.RED);
        model.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(!textOrPicturs){
                    textOrPicturs=true;
                    model.setText("babbage-002");
                    model.setPosition(1980-250,960);
                    lchat.get(0).setColor(Color.GREEN);
                }else{
                    textOrPicturs=false;
                    model.setText("gpt-3.5-turbo");
                    model.setPosition(1980-260,960);
                }
                super.clicked(event, x, y);
            }
        });
        addActor(model);

    }

    public void addMessage(String name, String s) {
        if(s!=null){
        Label lname = new Label(name,text.lgamechat);
        lname.setY(lchat.getLast().getY()-30);
        lname.setColor(Color.GOLD);
        addActor(lname);
        lchat.add(lname);
        Label labeltext = new Label(perenosSlov(s,110), text.lgamechat);
        labeltext.setY(lname.getY()-labeltext.getHeight());
        labeltext.setColor(r,g,b,1);
        addActor(labeltext);
        lchat.add(labeltext);
        rebuildPositionMessage();
        }
    }
    void rebuildPositionMessage(){
    if(lchat.getLast().getY()<0){
        removeActor(lchat.remove(2));
        removeActor(lchat.remove(2));
        for(int i=2;i<lchat.size();i+=2){
        lchat.get(i).setY(lchat.get(i-1).getY()-30);
        lchat.get(i+1).setY(lchat.get(i).getY()-lchat.get(i+1).getHeight());
        }
        rebuildPositionMessage();
    }
    }
    public void sendMessagePicturs(){
    if (textfieldchat.getText().length() > 1) {
        addMessage("User",textfieldchat.getText());
        addMessage("GPT","Изображение готово");
        mygdx.gptPicturs.generetPicturs(textfieldchat.getText());
        textfieldchat.setText(""); }
    }

    public void sendMessageText() throws IOException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        if (textfieldchat.getText().length() > 1) {
            addMessage("User",textfieldchat.getText());
            addMessage("GPT", mygdx.gptText.sendTextAndGetAnswer(textfieldchat.getText()));
            textfieldchat.setText(""); }
    }

    public String perenosSlov(String dtext,int dlina) {
        StringTokenizer str = new StringTokenizer(dtext, " ", true);
        StringBuilder text = new StringBuilder();
        int dlinnastroki = 0;
        while (str.hasMoreTokens()) {
            String ts = str.nextToken();
            //если длина больше чем надо добавляем перенос и пропускаем пробел
            if (dlinnastroki > dlina && ts.equals(" ")) {
                dlinnastroki = 0;
                text.append("\n");
            } else {
                dlinnastroki += ts.length();
                text.append(ts);
            }
        }
        return text.toString();
    }
    public void cangeText(float r,float g,float b){
        this.r=r;
        this.g=g;
        this.b=b;
        System.out.println("r"+r+"g"+g+"g"+b);
        for(int i=1;i<lchat.size();i+=2){
            lchat.get(i).setColor(r,g,b,1);
        }
        addMessage("GPT","Цвет текста изменен.");
    }
}
