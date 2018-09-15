package com.engineerskasa.smswatch;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.engineerskasa.smswatch.Model.Post;
import com.engineerskasa.smswatch.Model.SMS_Sent;

import java.util.ArrayList;
import java.util.List;

public class Sent extends Fragment {

    public SMSAdapter mAdapter;
    public RecyclerView mRecyclerView;
    private List<SMS_Sent> sentList = new ArrayList<>();
    private static Sent ins;
    TextView noSMS;

    public SQLiteDBHelper db;


  //  private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
    //    @Override
      //  public void onReceive(Context context, Intent intent) {
        //    createDbSMS(intent.getExtras().getString("sender"),intent.getExtras().getString("message"));
        //}
    //};

    public Sent() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sent, container, false);

        db = new SQLiteDBHelper(getActivity());

        sentList.addAll(db.getAllSentMessages());
        noSMS = (TextView) v.findViewById(R.id.empty_notes_view);

        //ns = getActivity();
       // getActivity().registerReceiver(intentReceiver,new IntentFilter("SMS_RECEIVED_ACTION"));


        mAdapter = new SMSAdapter(getActivity(), sentList);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        // mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        toggleEmptySMS();


        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // getActivity().unregisterReceiver(intentReceiver);
    }



    public void createDbSMS(String sender,String message) {
        // inserting note in db and getting
        // newly inserted note id
        long id = db.insertSentSMS(sender,message);

        // get the newly inserted note from db
        SMS_Sent n = db.getSent(id);

        if (n != null) {
            // adding new note to array list at 0 position
            sentList.add(0, n);

            // refreshing the list
            mAdapter.notifyDataSetChanged();

            toggleEmptySMS();
        }
    }

    private void toggleEmptySMS() {
        // you can check notesList.size() > 0

        if (db.getSentCount() > 0) {
            noSMS.setVisibility(View.GONE);
        } else {
            noSMS.setVisibility(View.VISIBLE);
        }
    }


}
