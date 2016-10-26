package com.tjz.pri.simrom;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Method;

/**
 * Created by tjz on 2016/10/25.
 */
@RunWith(AndroidJUnit4.class)
public class DataProcessorTest  extends TestCase{

    @Test
    public void testCreateDb(){
        TestEntity entity = new TestEntity();
        DataProcessor.init(InstrumentationRegistry.getContext());
        DataProcessor processor = DataProcessor.getInstance( );
        processor.createDb(entity.getClass());
    }

    @Test
    public void testReflect(){
        TestEntity entity = new TestEntity();
        Method[] methods = entity.getClass().getDeclaredMethods();
        for(Method m : methods){
            StringBuilder sb = new StringBuilder();
                sb.append("method:")
                        .append(m.getName())
                        .append("||param:");
            Class [] paramTypes = m.getParameterTypes();
            if(paramTypes == null){
                sb.append("null");
            }else if(paramTypes.length == 0){
                sb.append("0");
            }else{
                for(Class clazz: paramTypes){
                    sb.append(clazz.getSimpleName())
                            .append("  ");
                }
            }
            Log.i("SSSS",sb.toString());
        }
        Log.i("SSSS",""+(entity.getClass()==TestEntity.class));
    }
}
