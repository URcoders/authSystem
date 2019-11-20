package com.qgailab.authsystem.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qgailab.authsystem.model.dto.FingerInfoDto;
import com.qgailab.authsystem.model.dto.IdCardInfoDto;
import com.qgailab.authsystem.model.dto.MachineHealthDto;
import com.qgailab.authsystem.model.dto.SignatureInfoDto;
import com.qgailab.authsystem.model.pojo.FingerMachine;
import com.qgailab.authsystem.model.pojo.IdCardMachine;
import com.qgailab.authsystem.model.pojo.SignatureMachine;
import sun.jvm.hotspot.runtime.SignatureInfo;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @Description : 对象工具
 * @Param :
 * @Return :
 * @Author : SheldonPeng
 * @Date : 2019-11-17
 */
public class ObjectUtil {

    /**
     * @Description : 如果两个相同对象中同一属性都有值,那么sourceBean中的值会覆盖targetBean重点的值
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

    /**
     * @Description : 根据Json字符串解析成实体类对象
     * @Param : [msg]  传入的json字符串
     * @Return : java.lang.Object 解析后的对象
     * @Author : SheldonPeng
     * @Date : 2019-11-18
     */

    public static Object parseJson(String msg) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(msg, Map.class);
        if (map.size() == 1) {

            if (map.containsKey("idCardMachine")) {
                return mapToObject(map, IdCardMachine.class);
            } else if (map.containsKey("fingerMachine")) {
                return mapToObject(map, FingerMachine.class);
            } else if (map.containsKey("signatureMachine")) {
                return mapToObject(map, SignatureMachine.class);
            } else {
                return null;
            }


        } else if (map.size() == 2) {

            if (map.containsKey("health")) {
                return mapToObject(map, MachineHealthDto.class);
            } else if (map.containsKey("fingerInfo")) {
                return mapToObject(map, FingerInfoDto.class);
            } else if (map.containsKey("signature")) {
                return mapToObject(map, SignatureInfoDto.class);
            } else {
                return null;
            }
        } else if (  map.containsKey("idCard")){
            return mapToObject(map, IdCardInfoDto.class);
        } else {
            return null;
        }

    }


    /**
     * @Description : 将Map对象通过反射机制转换成Bean对象
     * @Param : [map 存放数据的map对象 , clazz 待转换的class]
     * @Return : java.lang.Object  转换后的Bean对象
     * @Author : SheldonPeng
     * @Date : 2019-11-18
     */
    private static Object mapToObject(Map<String, Object> map, Class<?> clazz) throws Exception {
        Object obj = clazz.newInstance();
        if(map != null && map.size() > 0) {
            for(Map.Entry<String, Object> entry : map.entrySet()) {
                String propertyName = entry.getKey();       //属性名
                Object value = entry.getValue();
                String setMethodName = "set"
                        + propertyName.substring(0, 1).toUpperCase()
                        + propertyName.substring(1);
                //获取属性对应的对象字段
                Field field = getClassField(clazz, propertyName);
                if(field==null)
                    continue;
                //获取字段类型
                Class<?> fieldTypeClass = field.getType();
                //根据字段类型进行值的转换
                value = convertValType(value, fieldTypeClass);
                try{
                    //调用对象对应的set方法
                    clazz.getMethod(setMethodName, field.getType()).invoke(obj, value);
                }catch(NoSuchMethodException e){
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }

    /**
     * @Description : 获取指定字段名称查找在class中的对应的Field对象(包括查找父类)
     * @Param : [clazz  指定的class , fieldName  字段名称 ]
     * @Return : java.lang.reflect.Field   Field对象
     * @Author : SheldonPeng
     * @Date : 2019-11-18
     */
    private static Field getClassField(Class<?> clazz, String fieldName) {
        if( Object.class.getName().equals(clazz.getName())) {
            return null;
        }
        Field []declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }

        Class<?> superClass = clazz.getSuperclass();
        if(superClass != null) {// 简单的递归一下
            return getClassField(superClass, fieldName);
        }
        return null;
    }


    /**
     * @Description : 将Object类型的值，转换成bean对象属性里对应的类型值
     * @Param : [value   Object对象值 , fieldTypeClass  属性的类型]
     * @Return : java.lang.Object  转换后的值
     * @Author : SheldonPeng
     * @Date : 2019-11-18
     */
    private static Object convertValType(Object value, Class<?> fieldTypeClass) {
        Object retVal = null;
        if(Long.class.getName().equals(fieldTypeClass.getName())
                || long.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Long.parseLong(value.toString());
        } else if(Integer.class.getName().equals(fieldTypeClass.getName())
                || int.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Integer.parseInt(value.toString());
        } else if(Float.class.getName().equals(fieldTypeClass.getName())
                || float.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Float.parseFloat(value.toString());
        } else if(Double.class.getName().equals(fieldTypeClass.getName())
                || double.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Double.parseDouble(value.toString());
        } else {
            retVal = value;
        }
        return retVal;
    }
}
