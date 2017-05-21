package com.chenzhanyang;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Description:
 * Auther: chen_zhanyang.
 * Email: zhanyang.chen@gmail.com
 * Data: 2017/5/19.
 */

public class FieldInject {
    /**
     * 注入
     * @param target
     */
    public static void inject(Object target){
        String className = target.getClass().getCanonicalName()+"_FieldInject";
        try {
            Class<?> clazz = Class.forName(className);
            Constructor<?> constructor = clazz.getConstructor(target.getClass());
            constructor.newInstance(target);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
