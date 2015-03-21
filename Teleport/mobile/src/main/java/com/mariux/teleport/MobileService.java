package com.mariux.teleport;

import android.content.Intent;

import com.mariux.teleport.lib.TeleportService;

/**
 * Created by josejuansanchez on 21/03/15.
 */
public class MobileService extends TeleportService {

    @Override
    public void onCreate() {
        super.onCreate();

        //The quick way is to use setOnGetMessageTask, and set a new task
        setOnGetMessageTask(new StartActivityTask());
    }

    //Task that shows the path of a received message
    public class StartActivityTask extends TeleportService.OnGetMessageTask {

        @Override
        protected void onPostExecute(String  path) {

            if (path.equals("startActivity")){

                Intent startIntent = new Intent(getBaseContext(), MobileActivity.class);
                startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startIntent);
            }

            //let's reset the task (otherwise it will be executed only once)
            setOnGetMessageTask(new StartActivityTask());
        }
    }


}
