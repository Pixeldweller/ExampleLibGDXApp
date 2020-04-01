package com.mygdx.game.ui.example;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.mygdx.game.Triton;

/**
 * ACHTUNG: in Screen render()- Methode muss unbedingt stage.act() auftauchen!
 * 
 * @author Fabio
 * 
 */
public abstract class CustomScreen implements Screen {

	private boolean readyState;
	private boolean loadingState;
	private boolean firstRender;
	private Group screenStageGroup;

	public CustomScreen() {
		initScreen();
		readyState = false;
		loadingState = false;
		firstRender = true;
	}

	public Group getScreenStageGroup() {
		return screenStageGroup;
	}

	public Stage getScreenStage() {
		return Triton.getMainStage();
	}

	public abstract void initScreen();

	public abstract Group preloadScreenStage();

	public InputProcessor getInputProcessor() {
		return getScreenStage();
	}

	public Dialog showConfirmDialog(String dialogText) {

		final InputProcessor tmpInputProcessor = Gdx.input.getInputProcessor();
		Dialog dialog = new Dialog("Warning", Triton.getUISkin(), "default") {
			public void result(Object obj) {
				if ((Boolean) obj == true) {

				} else {
					Gdx.input.setInputProcessor(tmpInputProcessor);
				}
			}
		};
		dialog.text(dialogText);
		dialog.button("Yes", true); // sends "true" as the result
		dialog.button("No", false); // sends "false" as the result
		dialog.key(Keys.ENTER, true); // sends "true" when the ENTER key is
										// pressed
		dialog.show(getScreenStage());
		Gdx.input.setInputProcessor(getScreenStage());
		return dialog;
	}

	@Override
	public void show() {
		showWithoutDisplay();
		Gdx.input.setInputProcessor(getInputProcessor());
		if (screenStageGroup != null) {
			getScreenStage().addActor(screenStageGroup);
		}
	}

	public void showWithoutDisplay() {
		if (!readyState) {
			screenStageGroup = preloadScreenStage();
			readyState = true;
			loadingState = false;
		}
	}

	@Override
	public void dispose() {
		readyState = false;
	}

	@Override
	public void hide() {
		readyState = false;
		dispose();
	}

	public void prepareScreenShow() {
		screenStageGroup = preloadScreenStage();
		readyState = true;
		loadingState = false;
		if (!firstRender) {
			firstRender = false;
			render(Gdx.graphics.getDeltaTime());
		}
	}

	public void reactTo(String network_msg) {
		System.out.println(this.getClass().getSimpleName() + "-> "
				+ network_msg);
	}

	public boolean isReadyForShow() {
		return readyState;
	}

	public boolean isLoading() {
		return loadingState;
	}

	public void setLoadingState(boolean loadingState) {
		this.loadingState = loadingState;
	}

}
