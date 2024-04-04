package com.mygdx.game;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.game.core.MyGdx;

import java.awt.*;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Graphics.DisplayMode primaryMode = Lwjgl3ApplicationConfiguration.getDisplayMode();
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("TestGPT");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		//config.setWindowedMode((int) screenSize.getWidth(),(int) screenSize.getHeight());
		config.setFullscreenMode(primaryMode);
		new Lwjgl3Application(new MyGdx(), config);
	}
}
