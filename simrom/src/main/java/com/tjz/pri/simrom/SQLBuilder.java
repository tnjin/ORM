package com.tjz.pri.simrom;

import android.text.TextUtils;

import java.lang.reflect.Method;
import java.util.Locale;

/**
 * Created by Lenovo on 2016/10/25.
 */
public class SQLBuilder {

    /**
     * 创建建表语句
     *
     * @param clazz
     * @return 此类对应的建表语句
     */
    public String buildCreationSQL(Class clazz) {

        String tableName = clazz.getSimpleName();
        Method[] methods = clazz.getDeclaredMethods();
        StringBuffer sb = new StringBuffer();
        sb.append("create table  if not exists ").append(tableName)
                .append(" (");

        for (Method m : methods) {
            String name = m.getName();
            if (name.startsWith("get")
                    || (name.startsWith("is") && m.getReturnType().equals(Boolean.class))) {
                name = trimPrefix(name);
                sb.append(name)
                        .append(" ")
                        .append(java2SqlType(m.getReturnType()));
                sb.append(" ,");
            }

        }
        //增加父类属性
        methods = clazz.getSuperclass().getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Method m = methods[i];
            String name = m.getName();
            if (name.startsWith("get")
                    || (name.startsWith("is") && m.getReturnType().equals(Boolean.class))) {
                name = trimPrefix(name);
                sb.append(name)
                        .append(" ")
                        .append(java2SqlType(m.getReturnType()));
                if (i < methods.length - 1) {
                    sb.append(" ,");
                }
            }

        }

        sb.append(")");
        String sql = sb.toString();
        Logs.i(sql);
        return sql;
    }


    public String buildQuerySQL(BaseEntity entity) {
        if (entity.getEntityId() != null) {
            return buildQuerySQLByID(entity);
        } else {
            return buildQuerySQLNormally(entity);
        }
    }

    private String buildQuerySQLNormally(BaseEntity entity) {
        Class clazz = entity.getClass();
        StringBuffer sb = new StringBuffer();
        sb.append("select * from ")
                .append(clazz.getSimpleName())
                .append(" where ");
        Method[] methods = clazz.getDeclaredMethods();
        for (Method m : methods) {
            String name = m.getName();
            if (m.getParameterTypes() !=null){

            }
            if (name.startsWith("get")
                    || (name.startsWith("is") && m.getReturnType().equals(Boolean.class))) {
                name = trimPrefix(name);
                if(name == null){
                    continue;
                }


            }
        }
        return null;
    }

    /**
     * 去除方法名中的get或者is前缀
     *
     * @param name
     * @return
     */
    private String trimPrefix(String name) {
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        name = name.toLowerCase(Locale.ENGLISH);
        if (name.startsWith("get")) {
            return name.substring(3);
        }
        if (name.startsWith("is")) {
            return name.substring(2);
        }
        return null;
    }

    /**
     * 创建根据entityID查询的语句
     *
     * @param entity
     * @return
     */
    private String buildQuerySQLByID(BaseEntity entity) {
        StringBuffer sb = new StringBuffer();
        sb.append("select * from ")
                .append(entity.getClass().getSimpleName())
                .append(" where _entityid=")
                .append(entity.getEntityId());
        String sql = sb.toString();
        Logs.i(sql);
        return sql;
    }


    public String buildUpdateSQL(BaseEntity entity) {
        return null;
    }

    public String buildInsertSQL(BaseEntity entity) {
        return null;
    }

    public String builldDeleteSQL(BaseEntity entity) {
        return null;
    }


    /**
     * java类型转到对应的SQL类型
     *
     * @param clazz
     * @return
     */
    private String java2SqlType(Class clazz) {
        String type = clazz.getSimpleName();
        if (TextUtils.isEmpty(type)){
            return "varchar";
        }
        String result = null;
        switch (type){
            case "Integer":
            case "int":
            case "Short":
            case "short":
            case "Long":
            case "long":
            result = "int";
                break;
            case "Double":
            case "double":
                result = "double";
                break;
            case "Float":
            case "float":
                result = "float";
                break;
            case "Boolean":
            case "bool":
                result = "boolean";
                break;
            case "Byte":
            case "byte":
                result = "blob";
                break;
                default:
                    result = "varchar";
        }
        return result;
    }

    /**
     * 是否是八个简单类型之一
     * @param clazz
     * @return
     */
    private boolean isSimpleType(Class clazz){
        if(clazz == null){
            return false;
        }
      return   clazz==Byte.class
                ||clazz==Short.class
                ||clazz==Integer.class
                ||clazz==Long.class
                ||clazz==Character.class
                ||clazz==Boolean.class
                ||clazz==Float.class
                ||clazz==Double.class
                ||clazz==int.class
              ||clazz == byte.class
              ||clazz == short.class
              ||clazz == long.class
              ||clazz == char.class
              ||clazz == boolean.class
              ||clazz == float.class
              ||clazz == double.class;
    }

    /**
     * 是否是java内建类型
     * @param clazz
     * @return
     */
    private boolean isInternalType(Class clazz){
        if(clazz == null){
            return false;
        }
        return clazz==String.class||isSimpleType(clazz);
    }

    /**
     * 是否是BaseEntity类型
     * @param clazz
     * @return
     */

    private boolean isORMType(Class clazz){
        if(clazz == null){
            return false;
        }
        return clazz==BaseEntity.class
                ||clazz.getSuperclass()==BaseEntity.class;
    }
}
