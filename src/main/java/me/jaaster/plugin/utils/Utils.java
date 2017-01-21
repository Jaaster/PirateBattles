package me.jaaster.plugin.utils;

import java.lang.reflect.Field;

/**
 * Created by Plado on 1/20/2017.
 */
public class Utils {

    public static Object getPrivateField(String fieldName, Class clazz, Object object)
    {
        Field field;
        Object o = null;

        try
        {
            field = clazz.getDeclaredField(fieldName);

            field.setAccessible(true);

            o = field.get(object);
        }
        catch(NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
        }


        return o;
    }
}
