package com.mygdx.game.ui.example;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

public class MiscUtil {

	// Singleton
	private MiscUtil() {

	}

	static public final float map(float value, float istart, float istop,
			float ostart, float ostop) {
		return ostart + (ostop - ostart)
				* ((value - istart) / (istop - istart));
	}

	public static Color setColorHSV(Color target, float hue, float saturation,
			float value) {
		saturation = MathUtils.clamp(saturation, 0.0f, 1.0f);
		while (hue < 0)
			hue++;
		while (hue >= 1)
			hue--;
		value = MathUtils.clamp(value, 0.0f, 1.0f);

		float red = 0.0f;
		float green = 0.0f;
		float blue = 0.0f;

		final float hf = (hue - (int) hue) * 6.0f;
		final int ihf = (int) hf;
		final float f = hf - ihf;
		final float pv = value * (1.0f - saturation);
		final float qv = value * (1.0f - saturation * f);
		final float tv = value * (1.0f - saturation * (1.0f - f));

		switch (ihf) {
		case 0: // Red is the dominant color
			red = value;
			green = tv;
			blue = pv;
			break;
		case 1: // Green is the dominant color
			red = qv;
			green = value;
			blue = pv;
			break;
		case 2:
			red = pv;
			green = value;
			blue = tv;
			break;
		case 3: // Blue is the dominant color
			red = pv;
			green = qv;
			blue = value;
			break;
		case 4:
			red = tv;
			green = pv;
			blue = value;
			break;
		case 5: // Red is the dominant color
			red = value;
			green = pv;
			blue = qv;
			break;
		}

		return target.set(red, green, blue, target.a);
	}

	public static float[] RGBtoHSV(float r, float g, float b) {

		float h, s, v;
		float min, max, delta;

		min = Math.min(Math.min(r, g), b);
		max = Math.max(Math.max(r, g), b);

		// V
		v = max;
		delta = max - min;

		// S
		if (max != 0)
			s = delta / max;
		else {
			s = 0;
			h = -1;
			return new float[] { h, s, v };
		}

		// H
		if (r == max)
			h = (g - b) / delta; // between yellow & magenta
		else if (g == max)
			h = 2f + (b - r) / delta; // between cyan & yellow
		else
			h = 4f + (r - g) / delta; // between magenta & cyan

		h *= 60f; // degrees

		if (h < 0)
			h += 360f;

		return new float[] { h, s, v };
	}

	public static float[] RGB2HSV(float r, float g, float b) {
		float[] tmp = {r,g,b};
		float R = tmp[0] / 255.0f;
		float G = tmp[1] / 255.0f;
		float B = tmp[2] / 255.0f;

		float min = Math.min(Math.min(R, G), B);
		float max = Math.max(Math.max(R, G), B);
		float delta = max - min;

		float H = max;
		float S = max;
		float V = max;

		if (delta == 0) {
			H = 0;
			S = 0;
		} else {

			S = delta / max;

			float delR = (((max - R) / 6) + (delta / 2)) / delta;
			float delG = (((max - G) / 6) + (delta / 2)) / delta;
			float delB = (((max - B) / 6) + (delta / 2)) / delta;

			if (R == max) {
				H = delB - delG;
			} else if (G == max) {
				H = (1f / 3f) + delR - delB;
			} else if (B == max) {
				H = (2f / 3f) + delG - delR;
			}

			if (H < 0)
				H += 1;
			if (H > 1)
				H -= 1;
		}

		float[] hsv = new float[3];
		hsv[0] = H;
		hsv[1] = S;
		hsv[2] = V;
		return hsv;
	}

	public static float[] HSVtoRGB(float h, float s, float v) {
		// H is given on [0->6] or -1. S and V are given on [0->1].
		// RGB are each returned on [0->1].
		float m, n, f;
		int i;

		float[] hsv = new float[3];
		float[] rgb = new float[3];

		hsv[0] = h;
		hsv[1] = s;
		hsv[2] = v;

		if (hsv[0] == -1) {
			rgb[0] = rgb[1] = rgb[2] = hsv[2];
			return rgb;
		}
		i = (int) (Math.floor(hsv[0]));
		f = hsv[0] - i;
		if (i % 2 == 0) {
			f = 1 - f; // if i is even
		}
		m = hsv[2] * (1 - hsv[1]);
		n = hsv[2] * (1 - hsv[1] * f);
		switch (i) {
		case 6:
		case 0:
			rgb[0] = hsv[2];
			rgb[1] = n;
			rgb[2] = m;
			break;
		case 1:
			rgb[0] = n;
			rgb[1] = hsv[2];
			rgb[2] = m;
			break;
		case 2:
			rgb[0] = m;
			rgb[1] = hsv[2];
			rgb[2] = n;
			break;
		case 3:
			rgb[0] = m;
			rgb[1] = n;
			rgb[2] = hsv[2];
			break;
		case 4:
			rgb[0] = n;
			rgb[1] = m;
			rgb[2] = hsv[2];
			break;
		case 5:
			rgb[0] = hsv[2];
			rgb[1] = m;
			rgb[2] = n;
			break;
		}

		return rgb;

	}

}
