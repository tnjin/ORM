package com.tjz.pri.simrom;

/**
 * Created by Lenovo on 2016/10/17.
 */
public class BaseSimRomException extends  Exception{

    private BaseSimRomException(){
        super();
    }

    public BaseSimRomException(String msg){
        super(msg);
    }

}
