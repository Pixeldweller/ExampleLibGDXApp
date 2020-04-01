package com.mygdx.game.ui.example;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AppConfig {
	
	
	public static SkinFactory skinFactory = new DefaultSkinFactory();	
	
	public static NetworkInterface networkInterface = null; // Wird in jeweiligen Typen gesetzt

	public static DesktopGameProfile profile = new DesktopGameProfile();
	
	public interface SkinFactory {
		Skin createSkinWithUIColor(float[] colorcode);
	}
	
	public static class DefaultSkinFactory implements SkinFactory{
		
		@Override
		public Skin createSkinWithUIColor(float[] colorcode) {
			Skin skin = new Skin();
			skin.add("color", new Color(colorcode[0], colorcode[1],
					colorcode[2], 1), Color.class);
			skin.add("selected", new Color(colorcode[0], colorcode[1],
					colorcode[2], 1), Color.class);
			skin.add("text", new Color(colorcode[0] + 0.5f,
					colorcode[1] + 0.5f, colorcode[2] + 0.5f, 1), Color.class);
			skin.add("text-selected", new Color(colorcode[0] + 0.8f,
					colorcode[1] + 0.8f, colorcode[2] + 0.8f, 1), Color.class);		

			skin.add("font", new BitmapFont());
			skin.add("font-over", new BitmapFont());
			skin.add("font-pressed", new BitmapFont());

			FileHandle fileHandle = Gdx.files
					.internal("neon/skin/neon-ui.json");
			FileHandle atlasFile = Gdx.files
					.internal("neon/skin/neon-ui.atlas");
			if (atlasFile.exists()) {
				skin.addRegions(new TextureAtlas(atlasFile));
			}
			skin.load(fileHandle);
			return skin;
		}
	}
	
	public interface GameProfile {	

		public void saveProfile(GameProfile profile);
		
		public GameProfile readProfile();
		
		
		public GameProfile deserialize(byte[] bytes);

		public String getProfile_name();

		public void setProfile_name(String profile_name);
		
		public float[] getUi_color();

		public void setUi_color(float[] ui_color);

		public float getMusicVolume();

		public void setMusicVolume(float musicVolume);
		
		public boolean isLow_res_mode();

		public void setLow_res_mode(boolean low_res_mode);

		public String getMain_broker_ip();

		public void setMain_broker_ip(String main_broker_ip);

		public String getBackup_broker_ip();

		public void setBackup_broker_ip(String backup_broker_ip);

		public int getBroker_port();

		public void setBroker_port(int broker_port);

		public String getLast_active_game_ip();

		public void setLast_active_game_ip(String last_active_game_ip);

		public String getBackup_last_active_game_ip();

		public void setBackup_last_active_game_ip(String backup_last_active_game_ip);

		public int getLast_game_port();

		public void setLast_game_port(int last_game_port);
		
	}
	
	
	public interface NetworkInterface  {	
		void connectTo(String ip, int port);
		boolean isConnected();
		boolean isConnecting();
		void close();
		
		boolean sendTCP(String msg);
		boolean sendUDP(byte[] bytes);
		void reactTo(String message);
		
	}
	
	public abstract static class DefaultNetworkInterface implements NetworkInterface{

		@Override
		public void connectTo(String ip, int port) {
			System.out.println("- Connect not Supported on Default Mode - ");
		}
		
		@Override
		public boolean sendTCP(String msg) {
			System.out.println("- Default Mode - ");
			return false;
		}

		@Override
		public boolean sendUDP(byte[] bytes) {
			System.out.println("- Default Mode - ");
			return false;
		}
		
		@Override
		public boolean isConnected() {			
			return false;
		}
		
		@Override
		public void close() {
			System.out.println("- Default Mode - ");
		}
		
		@Override
		public boolean isConnecting() {			
			return false;
		}
		
		public abstract void reactTo(String message);
	}
	
	
}
