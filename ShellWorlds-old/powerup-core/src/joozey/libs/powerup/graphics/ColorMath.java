package joozey.libs.powerup.graphics;

import com.badlogic.gdx.graphics.Color;

public class ColorMath
{
    /** Color component*/
    public static enum ColorC
    {
        RED,GREEN,BLUE,HUE,SATURATION,LUMINANCE,ALPHA;
        public static ColorC fromJson (String json) {
            json.toLowerCase();
            if (json.contentEquals("r")){   return RED;     }
            if (json.contentEquals("g")){   return GREEN;   }
            if (json.contentEquals("b")){   return BLUE;    }
            if (json.contentEquals("a")){   return ALPHA;   }
            if (json.contentEquals("h")){   return HUE;     }
            if (json.contentEquals("s")){   return SATURATION;      }
            if (json.contentEquals("l")){   return LUMINANCE;       }
            throw new IllegalArgumentException("Invalid Color component json:"+json);
        }
    }


    /** Transforms color values.
     * @param color Starting color to be modified.
     * @param value Value 0 & 1 that the <u>target</u> should be set to. (i.e. RED to 0.7f)
     * @param target The target to be changed.
     * @param modifySource If true, the color object itself is modified.  Else, leaves source alone & returns new.
     * @return
     */
    public static Color xform (Color color, float value, ColorC target, boolean modifySource) {
        if (!modifySource) {    color = new Color(color);       }

        switch (target) {
            case RED: color.r = value; return color;
            case GREEN: color.g = value; return color;
            case BLUE: color.b = value; return color;
            case ALPHA: color.a = value; return color;
            case HUE: setHue(color, value);return color;//TODO
            case SATURATION: setSat(color, value);return color;
            case LUMINANCE: setLum(color, value);           return color;
            default:        return color;
        }
    }

    private static Color setHue (Color color, float hue) {
        float[] hsl = toHSL(color);
        return toRGB(hue, hsl[1], hsl[2], color);
    }
    private static Color setSat (Color color, float sat) {
        float[] hsl = toHSL(color);
        return toRGB(hsl[0], sat, hsl[2], color);
    }
    private static Color setLum (Color color, float lum) {
        float[] hsl = toHSL(color);
        return toRGB(hsl[0], hsl[1], lum, color);
    }


    private static float max (float ... vals) {float max = vals[0]; for (int i = 1; i < vals.length; i++) { if (vals[i] > max) max = vals[i];       }       return max;     }
    private static float min (float ... vals) {float min = vals[0]; for (int i = 1; i < vals.length; i++) { if (vals[i] < min) min = vals[i];       }       return min;     }


    public static float[] toHSL(Color color)
    {
        float r = color.r;
        float g = color.g;
        float b = color.b;
        float min = min(r,g,b);
        float max = max(r,g,b);
        float chroma = max-min;
        //  Calculate the Hue
        float h = 0;
        if (chroma == 0)                h = 0;
        else if (max == r)      h = (( (g - b) / (chroma) / 6f) + 1) % 1;
        else if (max == g)      h = ( (b - r) / (chroma) / 6f) + 1f/3f;
        else if (max == b)      h = ( (r - g) / (chroma) / 6f) + 2f/3f;
        //  Calculate the Luminance
        float l = (max + min) / 2;
        //  Calculate the Saturation
        float s = 0;
        if (max == min)         s = 0;
        else if (l <= .5f)      s = (chroma) / (max + min);
        else                            s = (chroma) / (2 - max - min);

        float[] hsl = new float[3];
        hsl[0] = h;             hsl[1] = s;             hsl[2] = l;

        return hsl;
    }

    /** Overrides target's RGB values*/
    public static Color toRGB(float h, float s, float l, Color target)
    {
        if (s <0.0f || s > 1.0f)        {       throw new IllegalArgumentException( "Color parameter outside of expected range - Saturation ("+s+")" ); }
        if (l <0.0f || l > 1.0f)        {       throw new IllegalArgumentException( "Color parameter outside of expected range - Luminance ("+l+")" );  }

        float chroma = 0;

        if (l < 0.5)    chroma = l * (1 + s);
        else                    chroma = (l + s) - (s * l);
        float p = 2 * l - chroma;

        if (target == null) {target = new Color(0,0,0,1);}
        target.r = (max(0, HueToRGB(p, chroma, h + (1.0f / 3.0f))));
        target.g = (max(0, HueToRGB(p, chroma, h)));
        target.b = (max(0, HueToRGB(p, chroma, h - (1.0f / 3.0f))));
        return target;
    }


    private static float HueToRGB(float p, float q, float h)
    {
        if (h < 0) h += 1;
        if (h > 1 ) h -= 1;
        if (6 * h < 1)  {       return p + ((q - p) * 6 * h);}
        if (2 * h < 1 ) {       return  q;      }
        if (3 * h < 2)  {       return p + ( (q - p) * 6 * ((2.0f / 3.0f) - h) );       }
        return p;
    }
}

