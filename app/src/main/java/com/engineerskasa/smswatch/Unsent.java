package com.engineerskasa.smswatch;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.engineerskasa.smswatch.Model.SMS_Unsent;

import java.util.ArrayList;
import java.util.List;

public class Unsent extends Fragment {

    public UnsentAdapter mAdapter;
    public RecyclerView mRecyclerView;
    private List<SMS_Unsent> unsentList = new ArrayList<>();
    private static FragmentActivity ins;
    TextView noSMS;

    private SQLiteDBHelper db;
    SmsBroadcastReceiver receiver = new SmsBroadcastReceiver();


    /*private BroadcastReceiver intentReceiverr = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            createDbSMS(intent.getExtras().getString("sender"),intent.getExtras().getString("message"));
        }
    };*/


    public Unsent() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_unsent, container, false);
        db = new SQLiteDBHelper(getActivity());

        ins = getActivity();

        unsentList.addAll(db.getAllUnsentMessages());
        noSMS = (TextView)v.findViewById(R.id.emptyy_notes_view);

        //getActivity().registerReceiver(intentReceiverr,new IntentFilter("SMS_RECEIVEDD_ACTION"));


        mAdapter = new UnsentAdapter(getActivity(),unsentList);
        mRecyclerView = (RecyclerView)v.findViewById(R.id.recyclerr_view);
        // mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setAdapter(mAdapter);
        toggleEmptySMS();



        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //getActivity().unregisterReceiver(intentReceiverr);
    }


    private void createDbSMS(String sender,String message) {
        // inserting note in db and getting
        // newly inserted note id
        long id = db.insertUnsentSMS(sender,message);

        // get the newly inserted note from db
        SMS_Unsent n = db.getUnsent(id);

        if (n != null) {
            // adding new note to array list at 0 position
            unsentList.add(0, n);

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
