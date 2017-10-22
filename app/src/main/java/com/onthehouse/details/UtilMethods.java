package com.onthehouse.details;

public class UtilMethods
{

    public static int tryParseInt(String str)
    {
        int value = 0;

        try
        {
            value = Integer.parseInt(str);
        }
        catch(Exception e)
        {
        }

        return value;
    }

    public static float tryParseFloat(String str)
    {
        float value = 0;

        try
        {
            value = Float.parseFloat(str);
        }
        catch(Exception e)
        {
        }

        return value;
    }
}
