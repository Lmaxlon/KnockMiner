package com.badlogic.drop;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.drop.Drop;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
//		new MainGame();
//		int timer = 0;
//		while (timer < 500){
//			timer += timer;
//		}
//		initialize(new Drop(), config); !
		initialize(new MainGame(), config);
	}
}
