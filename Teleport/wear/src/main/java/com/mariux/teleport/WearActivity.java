package com.mariux.teleport;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.wearable.DataMap;
import com.mariux.teleport.lib.TeleportClient;

public class WearActivity extends Activity {

    TeleportClient mTeleportClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wear);


        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
            }
        });

        //instantiate the TeleportClient with the application Context
        mTeleportClient = new TeleportClient(this);

        //let's set the two task to be executed when an item is synced or a message is received
        mTeleportClient.setOnSyncDataItemTask(new ShowToastOnSyncDataItemTask());
        mTeleportClient.setOnGetMessageTask(new ShowToastFromOnGetMessageTask());


        //alternatively, you can use the Builders like indicated here for SyncData and Message

        /*
        mTeleportClient.setOnSyncDataItemTaskBuilder(new TeleportClient.OnSyncDataItemTask.Builder() {
            @Override
            public TeleportClient.OnSyncDataItemTask build() {
                return new TeleportClient.OnSyncDataItemTask() {
                    @Override
                    protected void onPostExecute(DataMap result) {
                        String s = result.getString("string");
                        Toast.makeText(getApplicationContext(),"DataItem - "+s,Toast.LENGTH_SHORT).show();
                    }
                };
            }
        });

        */

        /*
        mTeleportClient.setOnGetMessageTaskBuilder(new TeleportClient.OnGetMessageTask.Builder() {
            @Override
            public TeleportClient.OnGetMessageTask build() {
                return new TeleportClient.OnGetMessageTask() {
                    @Override
                    protected void onPostExecute(String path) {
                        Toast.makeText(getApplicationContext(),"Message - "+path,Toast.LENGTH_SHORT).show();
                    }
                };
            }
        });
        */

    }

    @Override
    protected void onStart() {
        super.onStart();
        mTeleportClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mTeleportClient.disconnect();

    }

    //Task to show the String from DataMap with key "string" when a DataItem is synced
    public class ShowToastOnSyncDataItemTask extends TeleportClient.OnSyncDataItemTask {

        protected void onPostExecute(DataMap dataMap) {

            String s = dataMap.getString("string");

            Toast.makeText(getApplicationContext(),"DataItem - "+s,Toast.LENGTH_SHORT).show();

            mTeleportClient.setOnSyncDataItemTask(new ShowToastOnSyncDataItemTask());
        }
    }

    //Task that shows the path of a received message
    public class ShowToastFromOnGetMessageTask extends TeleportClient.OnGetMessageTask {

        @Override
        protected void onPostExecute(String  path) {

            Toast.makeText(getApplicationContext(),"Message - "+path,Toast.LENGTH_SHORT).show();

            //let's reset the task (otherwise it will be executed only once)
            mTeleportClient.setOnGetMessageTask(new ShowToastFromOnGetMessageTask());
        }
    }


    public void sendMessage(View v) {

        mTeleportClient.setOnGetMessageTask(new ShowToastFromOnGetMessageTask());

        mTeleportClient.sendMessage("test from wear", null);
    }


}
