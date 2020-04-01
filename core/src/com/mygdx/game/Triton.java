package com.mygdx.game;

import java.util.HashMap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.ui.example.AppConfig;
import com.mygdx.game.ui.example.AppConfig.DefaultNetworkInterface;
import com.mygdx.game.ui.example.ConfigScreen;
import com.mygdx.game.ui.example.CustomScreen;
import com.mygdx.game.ui.example.DesktopGameProfile;

/**
 * Wird hauptsächlich als GameStateManager eingesetzt
 * 
 * @author Fabio
 * 
 */
public class Triton extends Game {

	private static DesktopGameProfile USER_PROFILE;

	private static Skin UI_SKIN;

	private static int WIDTH = 960, HEIGHT = 540; // Standard?
	private static float CENTER_X = WIDTH / 2, CENTER_Y = HEIGHT / 2; // Standard

	private static Stage GAME_MENU_STAGE;
	private static Class<CustomScreen> LAST_ACTIVE_SCREEN;
	private static Class<?> NEXT_ACTIVE_SCREEN;

	private static boolean RETURN_TO_MENU;
	private static boolean SCREEN_CHANGE;
	private static HashMap<Class<?>, CustomScreen> SCREEN_MAP;

	private static ShapeRenderer GAME_SHAPE_RENDERER;
	private static SpriteBatch GAME_SPRITE_BATCH;
	private static ModelBatch GAME_MODEL_BATCH;
	private static ModelBuilder GAME_MODEL_BUILDER;

	private static boolean LOADING, INIT_LOADING;
	private Texture loadingTexture;
	private Animation<TextureRegion> loadingIcon;
	private float animationDelta, fadeDelta;

	@Override
	public void create() {
		reloadProfile();
		UI_SKIN = AppConfig.skinFactory.createSkinWithUIColor(USER_PROFILE
				.getUi_color());
		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		// INIT_LOADING = true;

		if (AppConfig.networkInterface == null) {
			AppConfig.networkInterface = new DefaultNetworkInterface() {
				@Override
				public void reactTo(String message) {
					sendForwardToScreen(message);
				}		

			};
		}

		// Globaler SpriteBatch
		GAME_SPRITE_BATCH = new SpriteBatch();

		// Globaler Shaperenderer
		GAME_SHAPE_RENDERER = new ShapeRenderer();
		GAME_SHAPE_RENDERER.translate(Triton.centerX(), Triton.centerY(), 0);

		// Globale Stage
		GAME_MENU_STAGE = new Stage();

		// Globale Model Builder
		GAME_MODEL_BATCH = new ModelBatch();
		GAME_MODEL_BUILDER = new ModelBuilder();

		// Cached Screens
		SCREEN_MAP = new HashMap<Class<?>, CustomScreen>();
//		SCREEN_MAP.put(MenuScreen.class, new MenuScreen());
		SCREEN_MAP.put(ConfigScreen.class, new ConfigScreen());
//		SCREEN_MAP.put(ServerListScreen.class, new ServerListScreen());
//		SCREEN_MAP.put(ChatRoomScreen.class, new ChatRoomScreen());
		// TODO: ADD SCREENS
//		SCREEN_MAP.put(TestScreen.class, new TestScreen());

		// Lade Intensive Screens muessen mindestens 1x pre rendered werden,
		// damit micro stuttering verhindert wird.. was tun?

		// Lade Icon
		loadingTexture = new Texture(Gdx.files.internal("neon/loading.png"));
		TextureRegion[][] tmp = TextureRegion.split(loadingTexture,
				loadingTexture.getWidth() / 2, loadingTexture.getHeight() / 2);
		TextureRegion[] animFrames = new TextureRegion[2 * 2];
		int index = 0;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				animFrames[index++] = tmp[i][j];
			}
		}
		loadingIcon = new Animation<TextureRegion>(0.1f, animFrames);
		loadingIcon.setPlayMode(PlayMode.LOOP);
		animationDelta = 0f;
		fadeDelta = 0f;

		setScreen(SCREEN_MAP.get(ConfigScreen.class));
//		changeScreen(SCREEN_MAP.get(ConfigScreen.class), ConfigScreen.class);
		Gdx.input.setCatchBackKey(true);
	}

	@Override
	public void render() {
		if (!INIT_LOADING) {
			super.render();
		} else {
			//StarEffect.setEnabled(true);
			if (!SCREEN_MAP.get(ConfigScreen.class).isReadyForShow()
					&& !SCREEN_MAP.get(ConfigScreen.class).isLoading()) {

				SCREEN_MAP.get(ConfigScreen.class).showWithoutDisplay();
				SCREEN_MAP.get(ConfigScreen.class).render(
						Gdx.graphics.getDeltaTime());
			}

			Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		}
//		StarEffect.renderStarSlideEffect(GAME_SHAPE_RENDERER);

		if (RETURN_TO_MENU) {
//			setScreen(SCREEN_MAP.get(MenuScreen.class));
			RETURN_TO_MENU = false;
		}

		if (SCREEN_CHANGE) {
//			StarEffect.setEnabled(true);
			LOADING = true;
			transitionScreen();
			SCREEN_CHANGE = false;
		}

		if (!LOADING
				&& (Gdx.input.isKeyJustPressed(Keys.ESCAPE) || Gdx.input
						.isKeyJustPressed(Keys.BACK))) {
			if (screen instanceof ConfigScreen) {
				showExitConfirmMessage();
			} else {
//				changeScreen(SCREEN_MAP.get(screen.getClass()),
//						MenuScreen.class);
			}
		}

		if (Gdx.input.isTouched(6) || Gdx.input.isKeyJustPressed(Keys.END)) {
			Gdx.input.setOnscreenKeyboardVisible(true);
		}

		showLoadingIndicator();

		GAME_MENU_STAGE.act(Gdx.graphics.getDeltaTime());
		GAME_MENU_STAGE.draw();

	}

	private void showLoadingIndicator() {
		if (INIT_LOADING || LOADING || fadeDelta > 0.01f) {
			Batch batch = GAME_MENU_STAGE.getBatch();
			batch.setProjectionMatrix(GAME_MENU_STAGE.getCamera().combined);
			batch.begin();

			if (LOADING || INIT_LOADING) {
				fadeDelta = Math.max(0.01f, Math.min(1f, fadeDelta * 1.1f));
			} else {
//				StarEffect.fadeOffsetFlowToTarget(2.5f);
				fadeDelta = fadeDelta / 1.1f;
			}
			batch.setColor(1f, 1f, 1f, fadeDelta);
			TextureRegion keyFrame = loadingIcon
					.getKeyFrame(animationDelta += Gdx.graphics.getDeltaTime());
			batch.draw(keyFrame, 0, 0, 90, 90);
			batch.setColor(1f, 1f, 1f, 1f);
			batch.end();
		}
	}

	private void showExitConfirmMessage() {
		final InputProcessor tmpInputProcessor = Gdx.input.getInputProcessor();
		Dialog dialog = new Dialog("Warning", UI_SKIN, "default") {
			public void result(Object obj) {
				if ((Boolean) obj == true) {
					GAME_MENU_STAGE.clear();
					Gdx.app.exit();
				} else {
					Gdx.input.setInputProcessor(tmpInputProcessor);
				}
			}
		};
		dialog.text("Do you want to Quit the Game?");
		dialog.button("Yes", true); // sends "true" as the result
		dialog.button("No", false); // sends "false" as the result
		dialog.key(Keys.ENTER, true); // sends "true" when the ENTER key is
										// pressed
		dialog.show(GAME_MENU_STAGE);
		Gdx.input.setInputProcessor(GAME_MENU_STAGE);
	}
	
	public static void showDialog(String dialogMessage) {
		final InputProcessor tmpInputProcessor = Gdx.input.getInputProcessor();
		Dialog dialog = new Dialog("INFO", UI_SKIN, "default") {
			public void result(Object obj) {
				Gdx.input.setInputProcessor(tmpInputProcessor);
			}
		};
		dialog.text(dialogMessage);
		dialog.button("OK", true); // sends "true" as the result
		dialog.key(Keys.ENTER, true); // sends "true" when the ENTER key is
										// pressed
		dialog.show(GAME_MENU_STAGE);
		dialog.moveBy(0, Triton.centerY());
		Gdx.input.setInputProcessor(GAME_MENU_STAGE);
	}

	/**
	 * Ermöglicht Aufrufe aus den CustomScreens
	 * 
	 * @param origin
	 * @param next
	 */
	public static void changeScreen(CustomScreen origin, Class<?> next) {
		if (origin != null && SCREEN_MAP.get(next) != null) {
			if (!LOADING) {
				LAST_ACTIVE_SCREEN = (Class<CustomScreen>) origin.getClass();
				NEXT_ACTIVE_SCREEN = next;
				SCREEN_CHANGE = true;
			}
		} else {
			final InputProcessor tmpInputProcessor = Gdx.input
					.getInputProcessor();
			Dialog dialog = new Dialog("Warning", UI_SKIN, "default") {
				public void result(Object obj) {
					if ((Boolean) obj == true) {
						RETURN_TO_MENU = true;
						GAME_MENU_STAGE.clear();
					} else {
						Gdx.input.setInputProcessor(tmpInputProcessor);
					}
				}
			};
			dialog.text("An weird error occured,\n do you want to go back to the Startscreen?");
			dialog.button("Yes", true); // sends "true" as the result
			dialog.button("No", false); // sends "false" as the result
			dialog.key(Keys.ENTER, true); // sends "true" when the ENTER key is
											// pressed
			dialog.show(GAME_MENU_STAGE);
			Gdx.input.setInputProcessor(GAME_MENU_STAGE);
		}
	}	

	private void transitionScreen() {
		final Stage screenStage = SCREEN_MAP.get(LAST_ACTIVE_SCREEN)
				.getScreenStage();

		Action loadScreen = new RunnableAction() {
			@Override
			public boolean act(float delta) {
				if (!SCREEN_MAP.get(NEXT_ACTIVE_SCREEN).isReadyForShow()) {
					SCREEN_MAP.get(NEXT_ACTIVE_SCREEN).prepareScreenShow();
					// SCREEN_MAP.get(NEXT_ACTIVE_SCREEN).render(Gdx.graphics.getDeltaTime());
				}
				return true;
			}
		};

		// TODO: Aufräumen

		screenStage.addAction(Actions.parallel(new RunnableAction() {
			public boolean act(float delta) {
				LOADING = true;
//				StarEffect.multiplyFactorToOffsetFlow(1.05f);
				return ((CustomScreen) screen).equals(SCREEN_MAP
						.get(NEXT_ACTIVE_SCREEN));
			};

		}, Actions.sequence(loadScreen), Actions.sequence(
				Actions.fadeOut(1.1f), new RunnableAction() {
					@Override
					public boolean act(float delta) {
						((CustomScreen) screen).getScreenStageGroup().remove();
						return true;
					}
				}, Actions.delay(0.1f), new RunnableAction() {
					@Override
					public boolean act(float delta) {
						setScreen(SCREEN_MAP.get(NEXT_ACTIVE_SCREEN));
						SCREEN_MAP.get(NEXT_ACTIVE_SCREEN).getScreenStage()
								.addAction(Actions.fadeIn(0.8f));
						LOADING = false;
						INIT_LOADING = false;
						SCREEN_MAP
								.get(NEXT_ACTIVE_SCREEN)
								.getScreenStage()
								.addAction(
										Actions.sequence(Actions.delay(0.5f,
												new RunnableAction() {
													@Override
													public boolean act(
															float delta) {
														LOADING = false;
														return true;
													}
												})));
						return true;
					}
				})));
	}

	public static ModelBuilder getModelBuilder() {
		return GAME_MODEL_BUILDER;
	}

	public static ModelBatch getModelBatch() {
		return GAME_MODEL_BATCH;
	}

	public static SpriteBatch getSpriteBatch() {
		if(GAME_SPRITE_BATCH == null) {
			GAME_SPRITE_BATCH = new SpriteBatch();
		}
		return GAME_SPRITE_BATCH;
	}

	public static Stage getMainStage() {
		if(GAME_MENU_STAGE == null) {
			GAME_MENU_STAGE = new Stage();
		}
		return GAME_MENU_STAGE;
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		CENTER_X = WIDTH / 2f;
		CENTER_Y = HEIGHT / 2f;
	}

	@Override
	public void dispose() {
		super.dispose();
		GAME_MENU_STAGE.dispose();
		GAME_SHAPE_RENDERER.dispose();
		GAME_SPRITE_BATCH.dispose();
		GAME_MODEL_BATCH.dispose();

		loadingTexture.dispose();
		AppConfig.profile.saveProfile(USER_PROFILE);
	}

	public static Skin getUpdatedUISkin(Color color) {
		USER_PROFILE.setUi_color(new float[] { color.r, color.b, color.g });
		UI_SKIN = AppConfig.skinFactory.createSkinWithUIColor(USER_PROFILE
				.getUi_color());
		return UI_SKIN;
	}

	public static void reloadProfile() {
		USER_PROFILE = AppConfig.profile.readProfile();
		UI_SKIN = AppConfig.skinFactory.createSkinWithUIColor(USER_PROFILE
				.getUi_color());
		// TODO: Sound Volume reconfig?

	}

	public void sendForwardToScreen(String network_msg) {
		SCREEN_MAP.get(NEXT_ACTIVE_SCREEN).reactTo(network_msg);
	}

	public static DesktopGameProfile getProfile() {
		if(USER_PROFILE == null) {
			USER_PROFILE = AppConfig.profile;
		}
		return USER_PROFILE;
	}

	public static Skin getUISkin() {
		if(UI_SKIN == null) {
			UI_SKIN = AppConfig.skinFactory.createSkinWithUIColor(new float[] {1f,1f,1f});
		}
		return UI_SKIN;
	}

	public static float centerX() {
		return CENTER_X;
	}

	public static float centerY() {
		return CENTER_Y;
	}

}
