package com.tjz.pri.simrom;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;

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
}
