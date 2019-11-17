package com.qgailab.authsystem.utils;

import java.lang.reflect.Field;

/**
 * @Description : 对象工具
 * @Param :
 * @Return :
 * @Author : SheldonPeng
 * @Date : 2019-11-17
 */
public class ObjectUtil {

    /**
     * @Description : 如果两个相同对象中同一属性都有值,那么sourceBean中的值会覆盖tagetBean重点的值
     * @Param : [sourceBean  被提取的对象bean , targetBean 用于合并的对象bean]
     * @Return : java.lang.Object 合并后的对象
     * @Author : SheldonPeng
     * @Date : 2019-11-17
     */
    public static Object combineSydwCore(Object sourceBean, Object targetBean) {

        Class sourceBeanClass = sourceBean.getClass();
        Class targetBeanClass = targetBean.getClass();

        Field[] sourceFields = sourceBeanClass.getDeclaredFields();
        Field[] targetFields = sourceBeanClass.getDeclaredFields();
        for (int i = 0; i < sourceFields.length; i++) {
            Field sourceField = sourceFields[i];
            Field targetField = targetFields[i];
            sourceField.setAccessible(true);
            targetField.setAccessible(true);
            try {
                if (!(sourceField.get(sourceBean) == null)) {
                    targetField.set(targetBean, sourceField.get(sourceBean));
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return targetBean;
    }
}
