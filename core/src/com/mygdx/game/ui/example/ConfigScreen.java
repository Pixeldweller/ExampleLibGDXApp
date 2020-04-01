package com.mygdx.game.ui.example;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.Triton;

public class ConfigScreen extends CustomScreen {

	private OrthographicCamera cam;
	private SpriteBatch batch;	
	private Skin skin, color_pick_skin; // Global @ Triton.class

	private Texture mainBackground;

	private Stage stage;
	private TextButton saveConfigBttn, reloadBttn, backBttn;
	private Label playerNameLabel, vsServerIPLabel, vsServerBackUpIPLabel;
	private TextField playerName, mainBrokerServerIP, brokerBackUpIP;
	private Slider music_vol_slider;
	private CheckBox low_res_mode;
	private Image image;
	private Texture color_pick_tex;
	private Slider colorSlider;

	private int touchCount;

	@Override
	public void initScreen() {
		cam = new OrthographicCamera(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		cam.update();
		color_pick_skin = AppConfig.skinFactory
				.createSkinWithUIColor(new float[] { 1f, 1f, 1f });
		stage = Triton.getMainStage();
		batch = Triton.getSpriteBatch();
	}

	
	@Override
	public Group preloadScreenStage() {		
		skin = Triton.getUISkin();
		touchCount = 0;
//		mainBackground = new Texture(
//				Gdx.files.internal("objects/menu/space_transparent.png"));

		Container<Table> tableContainer = new Container<Table>();
		tableContainer.setTransform(true);
		float sw = Gdx.graphics.getWidth();
		float sh = Gdx.graphics.getHeight();

		float cw = sw * 0.7f;
		float ch = sh * 0.5f;

		tableContainer.setSize(cw, ch);
		tableContainer.setPosition((sw - cw) / 2.0f, (sh - ch) / 2.0f);
		tableContainer.fillX();

		final Table table = new Table(skin);

		playerNameLabel = new Label("Enter your Profile Name here", skin);
		playerNameLabel.setAlignment(Align.center);
		playerName = new TextField(Triton.getProfile().getProfile_name(), skin);
		playerName.setAlignment(Align.center);

		vsServerIPLabel = new Label("Broker Server IP Adress", skin);
		vsServerIPLabel.setAlignment(Align.center);
		mainBrokerServerIP = new TextField(Triton.getProfile()
				.getMain_broker_ip(), skin);
		mainBrokerServerIP.setAlignment(Align.center);

		vsServerBackUpIPLabel = new Label("BackUp Broker Server IP Adress",
				skin);
		vsServerBackUpIPLabel.setAlignment(Align.center);
		brokerBackUpIP = new TextField(Triton.getProfile()
				.getBackup_broker_ip(), skin);
		brokerBackUpIP.setAlignment(Align.center);

		Label topLabel = new Label("Music Volume", skin);
		topLabel.setAlignment(Align.center);
		music_vol_slider = new Slider(0, 100, 1, false, skin);
		music_vol_slider.setValue(Triton.getProfile().getMusicVolume() * 100f);
		Label anotherLabel = new Label("Misc Options", skin);
		anotherLabel.setAlignment(Align.center);
		low_res_mode = new CheckBox("Low Resolution Mode", skin);
		low_res_mode.setChecked(Triton.getProfile().isLow_res_mode());

		Table buttonTable = new Table(skin);

		saveConfigBttn = new TextButton("Save", skin);
		reloadBttn = new TextButton("Reload", skin);
		backBttn = new TextButton("Back", skin);

		table.row().colspan(3).expandX().fillX();
		table.add(playerNameLabel).fillX();
		table.row().colspan(1).expandX().fillX();
		table.add(playerName).padBottom(50f);

		table.row().colspan(3).expandX().fillX();
		table.add(vsServerIPLabel).fillX();
		table.row().colspan(1).expandX().fillX();
		table.add(mainBrokerServerIP).padBottom(10f);

		table.row().colspan(3).expandX().fillX();
		table.add(vsServerBackUpIPLabel).fillX();
		table.row().colspan(1).expandX().fillX();
		table.add(brokerBackUpIP).padBottom(40f);

		table.row().colspan(3).expandX().fillX();
		table.add(topLabel).fillX().pad(20f);
		table.row().colspan(3).expandX().fillX();
		table.add(music_vol_slider).fillX();
		table.row().colspan(3).expandX().fillX();

		table.add(anotherLabel).fillX().padTop(75f);
		table.row().expandX().fillX();
		table.row().expandX().fillX();
		table.add(low_res_mode).expandX().fillX();
		table.row().expandX().fillX();
		table.row().expandX().fillX();
		table.add(buttonTable).colspan(3);

		buttonTable.row().fillX().expandX().padTop(80f);
		buttonTable.add(saveConfigBttn).width(cw / 3.0f);
		buttonTable.add(reloadBttn).width(cw / 3.0f);
		buttonTable.add(backBttn).width(cw / 3.0f);

		tableContainer.setActor(table);
		tableContainer.addAction(Actions.fadeOut(0f));
		tableContainer.addAction(Actions.fadeIn(1f, Interpolation.fade));
		

		// Color Picker Group
		Group colorPickerGroup = new Group();
		colorSlider = new Slider(0, 255, 1, true, color_pick_skin);
		Color color = Triton.getUISkin().getColor("color");
		colorSlider
				.setValue((MiscUtil.RGB2HSV(color.r, color.g, color.b)[0]) * 255f);
		colorSlider.setHeight(255f);

		color_pick_tex = new Texture(Gdx.files.internal("neon/color_pick.png"));
		image = new Image(color_pick_tex);
		image.addAction(Actions.sequence(Actions.moveBy(Triton.centerX()
				+ Triton.centerX() * 0.915f, Triton.centerY() - 255 / 2f)));
		colorSlider.addAction(Actions.moveBy(
				Triton.centerX() + Triton.centerX() * 0.915f
						- colorSlider.getWidth() / 2 - 25f, Triton.centerY()
						- colorSlider.getHeight() / 2));
		colorSlider.setColor(skin.getColor("color"));
		final TextButton colorChangeBttn = new TextButton("<->",
				color_pick_skin);
		colorChangeBttn.addAction(Actions.moveBy(
				Triton.centerX() + Triton.centerX() * 0.95f
						- colorChangeBttn.getWidth() / 2 - 25f,
				Triton.centerY() - colorChangeBttn.getHeight() * 3));
		colorChangeBttn.setColor(skin.getColor("color"));

		colorPickerGroup.addActor(colorChangeBttn);
		colorPickerGroup.addActor(image);
		colorPickerGroup.addActor(colorSlider);
		

		colorPickerGroup.addAction(Actions.moveBy(100, 0));
		colorPickerGroup.addAction(Actions.moveBy(-100, 0, 0.5f,
				Interpolation.fade));

		colorSlider.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (!color_pick_tex.getTextureData().isPrepared()) {
					color_pick_tex.getTextureData().prepare();
				}
				Pixmap pixmap = color_pick_tex.getTextureData().consumePixmap();
				Color newColor = new Color(pixmap.getPixel(1,
						(255 - (int) colorSlider.getValue()) % 254));
				colorSlider.setColor(newColor);
				colorChangeBttn.setColor(newColor);
			}
		});

		colorChangeBttn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (!color_pick_tex.getTextureData().isPrepared()) {
					color_pick_tex.getTextureData().prepare();
				}
				Pixmap pixmap = color_pick_tex.getTextureData().consumePixmap();
				if (colorSlider.getValue() == 0) {
					colorSlider.setValue(255f);
				} else if (colorSlider.getValue() == 255) {
					colorSlider.setValue(0f);
				}
				Color newColor = new Color(pixmap.getPixel(1,
						((int) colorSlider.getValue()) % 254));
				skin.remove("color", Color.class);
				skin = Triton.getUpdatedUISkin(newColor);
				
				Triton.changeScreen(ConfigScreen.this, ConfigScreen.class);				
			}
		});

		saveConfigBttn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				saveProfile();
				touchCount = 0;
			}
		});

		reloadBttn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Triton.reloadProfile();
				Triton.changeScreen(ConfigScreen.this, ConfigScreen.class);	
//				ConfigScreen.this.preloadScreenStage();
			}
		});

		backBttn.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (touchCount > 2) {
					Dialog dialog = new Dialog("Warning", color_pick_skin,
							"default") {
						public void result(Object obj) {
							if (obj != null && (Boolean) obj == true) {
								saveProfile();
//								Triton.changeScreen(ConfigScreen.this,
//										MenuScreen.class);
							} else if (obj != null && (Boolean) obj == false) {
//								Triton.changeScreen(ConfigScreen.this,
//										MenuScreen.class);
							}
						}
					};
					dialog.text("Do you want to save your changes, before you leave?");
					dialog.button("Yes", true); // sends "true" as the result
					dialog.button("No", false); // sends "false" as the result
					dialog.button("Abort", null); // sends "false" as the result
					dialog.key(Keys.ENTER, true); // sends "true" when the ENTER
													// key is

					dialog.show(getScreenStage());
					dialog.sizeBy(2);
				} else {
//					Triton.changeScreen(ConfigScreen.this, MenuScreen.class);
				}
			}
		});
		
		Group configScreenGroup = new Group();
		configScreenGroup.addActor(tableContainer);
		configScreenGroup.addActor(colorPickerGroup);
		
		return configScreenGroup;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

		batch.begin();
//		StarEffect.renderStarBackground(batch, mainBackground);
		batch.end();

		if (Gdx.input.justTouched()) {
			touchCount++;
		}
	}

	@Override
	public Stage getScreenStage() {
		return stage;
	}

	private void saveProfile() {
		Triton.getProfile().setProfile_name(playerName.getText());
		Triton.getProfile().setMain_broker_ip(mainBrokerServerIP.getText().replaceAll(" ", ""));
		Triton.getProfile().setBackup_broker_ip(brokerBackUpIP.getText().replaceAll(" ", ""));
		Triton.getProfile().setMusicVolume(music_vol_slider.getValue() / 100f);

		Triton.getProfile().setLow_res_mode(low_res_mode.isChecked());

		Triton.getProfile().saveProfile(Triton.getProfile());

		AppConfig.skinFactory.createSkinWithUIColor(Triton.getProfile()
				.getUi_color());
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		super.hide();
	}

	@Override
	public void dispose() {		
//		mainBackground.dispose();
		color_pick_tex.dispose();
	}

}
