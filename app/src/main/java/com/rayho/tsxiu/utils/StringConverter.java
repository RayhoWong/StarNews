package com.rayho.tsxiu.utils;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Rayho on 2019/3/14
 * List<T>转换器
 * 将Dao的list<T>数据转换成String存储到数据库中
 **/
public class StringConverter implements PropertyConverter<List<String>, String> {

    /**
     * 将数据库中的值，转化为实体Bean类对象(比如List<String>)
     * @param databaseValue
     * @return
     */
    @Override
    public List<String> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        } else {
            List<String> list = Arrays.asList(databaseValue.split(","));
            return list;
        }
    }

    /**
     * 将实体Bean类(比如List<String>)转化为数据库中的值(比如String)
     * @param entityProperty
     * @return
     */
    @Override
    public String convertToDatabaseValue(List<String> entityProperty) {
        if (entityProperty == null) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder();
            for (String link : entityProperty) {
                sb.append(link);
                sb.append(",");
            }
            return sb.toString();
        }
    }
}
