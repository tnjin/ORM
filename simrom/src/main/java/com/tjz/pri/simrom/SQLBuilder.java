package com.tjz.pri.simrom;

import java.lang.reflect.Field;

/**
 * Created by Lenovo on 2016/10/25.
 */
public class SQLBuilder {

    /**
     * 创建建表语句
     * @param clazz
     * @return
     * @throws BaseSimRomException 反射无法获取属性时，扔出异常
     */
    public String buildCreationSQL(Class clazz)  {

        String tableName = clazz.getName();
        Field[] fields = clazz.getDeclaredFields();

        if(fields == null || fields.length == 0){
            throw new RuntimeException("no declaredField found in "+clazz.getName());
        }
        StringBuffer sb = new StringBuffer();
        sb.append("create table ").append(tableName)
                .append("if not exists (");

        for(int i = 0;i <fields.length;i++){
            Field f = fields[i];
            sb.append(f.getName())
                    .append(" ")
                    .append(java2SqlType(f.getClass()));
            if(i < fields.length -1){
                sb.append(" ,");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    public String buildQuerySQL(BaseEntity entity) {

        return null;
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
     * @param clazz
     * @return
     */
    private String java2SqlType(Class clazz) {
        if (clazz == null) {
            throw new IllegalStateException("java2Sql type :the class cannt be null");
        }
        String type = clazz.getName();
        if (type.equals("Integer") || type.equals("Short") || type.equals("long")) {
            return "int";
        }
        if (type.equals("String")) {
            return "varchar";
        }
        if (type.equals("Double")) {
            return "double";
        }
        if (type.equals("Float")) {
            return "float";
        }
        if (type.equals("Short")) {
            return "short";
        }

        if (type.equals("Boolean")) {
            return "boolean";
        }
        if (type.equals("Byte")) {
            return "blob";
        }
        return "varchar";
    }
}
