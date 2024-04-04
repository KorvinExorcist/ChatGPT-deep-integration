package com.mygdx.game.core;

import com.badlogic.gdx.ApplicationAdapter;
import com.mygdx.game.gpt.GPTpicturs;
import com.mygdx.game.gpt.GPTtext;

public class MyGdx extends ApplicationAdapter {
	public GPTpicturs gptPicturs;
	public GPTtext gptText;
	public Graphics graphics;
public	Input input;
public	Logic logic;
public Chat chat;

	@Override

	public void create () {
		gptPicturs=new GPTpicturs();
		gptText =new GPTtext(this);
		chat=new Chat(this);
		graphics=new Graphics(this);
		input=new Input(this);
		logic=new Logic(this);
    }
	@Override
	public void render () {
    graphics.render();
	input.input();
    logic.update();
	}
	@Override
	public void dispose () {
		graphics.stage.dispose();
	}
}
