package com.mikroacse.blend.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mikroacse.blend.Blend;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Blend Alpha";
		config.width = 560;
		config.height = 940;
		new LwjglApplication(new Blend(), config);
	}
}
