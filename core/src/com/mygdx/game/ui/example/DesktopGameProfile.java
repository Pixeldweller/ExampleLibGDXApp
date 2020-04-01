package com.mygdx.game.ui.example;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * Class to collect User configuration + important flags <br/>
 * <br/>
 * >> This class will be serialized and stored on the device when the app is
 * disposed and reloaded when the user resumes the app
 * 
 * @author fabio
 * 
 */
public class DesktopGameProfile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String profile_name;
	private float[] ui_color;
	private float musicVolume;
	private boolean low_res_mode;

	private String main_broker_ip;
	private String backup_broker_ip;
	private int broker_port;

	private String last_active_game_ip;
	private String backup_last_active_game_ip;
	private int last_game_port;

	
	/**
	 * Creates default Profile with standard configurations
	 */
	public DesktopGameProfile() {
		profile_name = "Player #" + new Random().nextInt(9) + ""
				+ new Random().nextInt(9);
		ui_color = new float[] {0,1,1};
 		musicVolume = 0.25f;
		low_res_mode = false;

		main_broker_ip = "localhost"; // TODO: DNS?
		backup_broker_ip = "localhost"; // TODO: FESTE IP?
		broker_port = 9090;

		last_active_game_ip = null;
		backup_last_active_game_ip = null;
		last_game_port = 9095;
	}

	public void saveProfile(DesktopGameProfile profile) {
		FileHandle file = Gdx.files.local("profile.dat");	
		DesktopGameProfile a_profile =  (DesktopGameProfile) profile;
		try {
			file.writeBytes(serialize(a_profile), false);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		System.out.println("-Saved Profile-");
	}
	
	public DesktopGameProfile readProfile(){
		DesktopGameProfile profile = null;
		FileHandle file = Gdx.files.local("profile.dat");
		if(file.exists()){
			try {
				profile = deserialize(file.readBytes());
				return profile;
			} catch (Exception e) {
				return new DesktopGameProfile();
			}
		} 
		return new DesktopGameProfile();
	}

	@SuppressWarnings("unused")
	private static byte[] serialize(DesktopGameProfile obj) throws IOException {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(bytes);
		out.writeObject(obj);
		return bytes.toByteArray();
	}

	public DesktopGameProfile deserialize(byte[] bytes) {
		ByteArrayInputStream b = new ByteArrayInputStream(bytes);
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(b);
			return (DesktopGameProfile) in.readObject();
		} catch (Exception e) {
			return new DesktopGameProfile();
		}		
	}

	public String getProfile_name() {
		return profile_name;
	}

	public void setProfile_name(String profile_name) {
		this.profile_name = profile_name;
	}

	public float[] getUi_color() {
		return ui_color;
	}

	public void setUi_color(float[] ui_color) {
		this.ui_color = ui_color;
	}

	public float getMusicVolume() {
		return musicVolume;
	}

	public void setMusicVolume(float musicVolume) {
		this.musicVolume = musicVolume;
	}

	public boolean isLow_res_mode() {
		return low_res_mode;
	}

	public void setLow_res_mode(boolean low_res_mode) {
		this.low_res_mode = low_res_mode;
	}

	public String getMain_broker_ip() {
		return main_broker_ip;
	}

	public void setMain_broker_ip(String main_broker_ip) {
		this.main_broker_ip = main_broker_ip;
	}

	public String getBackup_broker_ip() {
		return backup_broker_ip;
	}

	public void setBackup_broker_ip(String backup_broker_ip) {
		this.backup_broker_ip = backup_broker_ip;
	}

	public int getBroker_port() {
		return broker_port;
	}

	public void setBroker_port(int broker_port) {
		this.broker_port = broker_port;
	}

	public String getLast_active_game_ip() {
		return last_active_game_ip;
	}

	public void setLast_active_game_ip(String last_active_game_ip) {
		this.last_active_game_ip = last_active_game_ip;
	}

	public String getBackup_last_active_game_ip() {
		return backup_last_active_game_ip;
	}

	public void setBackup_last_active_game_ip(String backup_last_active_game_ip) {
		this.backup_last_active_game_ip = backup_last_active_game_ip;
	}

	public int getLast_game_port() {
		return last_game_port;
	}

	public void setLast_game_port(int last_game_port) {
		this.last_game_port = last_game_port;
	}

	

}
