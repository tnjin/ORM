package com.tjz.pri.TaskManager;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * Created by Lenovo on 2016/10/25.
 */
public class TaskManager {
    private static final String TAG = "Task_Manager_Thread";
    public static final int MSG_ADD_TASK = 1;
    public static final int MSG_TASK_DONE = 2;
    public static final int MSG_CANCEL_TASK = 3;


    private ExecutorService threadPool;
    private Queue<CallBackFutureTask> taskQueue;
    private HandlerThread managerThread;
    private TaskHandler handler ;
    public TaskManager (){
        threadPool = Executors.newFixedThreadPool(getCPUCount());
        taskQueue = new LinkedList<>();
        managerThread = new HandlerThread(TAG);
        managerThread.start();
        handler = new TaskHandler(managerThread.getLooper());

    }

    private int getCPUCount(){
       int count = 2;
        try{
            count =  Math.max(2, Math.min(Runtime.getRuntime().availableProcessors() - 1, 4));
        }catch (Exception e){

        }
        return count;
    }

   public void addTask(Task task){
       if(task == null){
           return;
       }
       Message msg = handler.obtainMessage();
       msg.what = MSG_ADD_TASK;
       msg.obj = task;
       msg.sendToTarget();
   }

    public void cancleTask(Task task){
        Message msg = handler.obtainMessage();
        msg.what = MSG_CANCEL_TASK;
        msg.obj = task;
        msg.sendToTarget();
    }
    public void clearTask(){

    }


    private void handleAddTask(Task task){
        CallBackFutureTask futureTask = new CallBackFutureTask(task);
        taskQueue.offer(futureTask);
        threadPool.submit(futureTask);

    }

    private void handleTaskDone(FutureTask futureTask){
        taskQueue.remove(futureTask);
    }

    private void handleCancelTask(Task task){
      FutureTask tempTask = null;
        for(CallBackFutureTask futureTask : taskQueue){
           if( futureTask.isSameTask(task)){
              tempTask = futureTask;
               break;
            }
        }
        if (tempTask == null){
            return;
        }
        tempTask.cancel(false);
        taskQueue.remove(tempTask);
    }



    private  void afterRunning(FutureTask task) {
        if(task == null){
            return;
        }
        Message msg = handler.obtainMessage();
        msg.what = MSG_TASK_DONE;
        msg.obj = task;
        msg.sendToTarget();

    }
    private class TaskHandler extends Handler {


        public TaskHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case MSG_ADD_TASK:
                    TaskManager.this.handleAddTask((Task) msg.obj);
                    break;
                case MSG_TASK_DONE:
                    TaskManager.this.handleTaskDone((FutureTask) msg.obj);
                    break;
                case MSG_CANCEL_TASK:
                    TaskManager.this.handleCancelTask((Task) msg.obj);
                    break;
                default:
                    break;
            }
        }
    }



class CallBackFutureTask<V> extends FutureTask<V>{

        Callable<V> callable;
    public CallBackFutureTask(Callable<V> callable) {
        super(callable);
        this.callable = callable;
    }

    private CallBackFutureTask(Runnable runnable, V result) {
        super(runnable, result);
    }


    @Override
    protected void done() {
        super.done();
        TaskManager.this.afterRunning(this);

    }

    public boolean isSameTask(Task task){
        return callable == task;
    }
}



}