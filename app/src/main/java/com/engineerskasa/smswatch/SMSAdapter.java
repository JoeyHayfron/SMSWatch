package com.engineerskasa.smswatch;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.engineerskasa.smswatch.Model.SMS_Sent;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class SMSAdapter extends RecyclerView.Adapter<SMSAdapter.ViewHolder> {

    private Context context;
    private List<SMS_Sent> sentList;

    public SMSAdapter(Context context, List<SMS_Sent> sentList) {
        this.context = context;
        this.sentList = sentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout,parent,false);

        return new ViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SMS_Sent sms = sentList.get(position);

        holder.sender.setText(sms.getSender());
        holder.message.setText(sms.getMessage());
        holder.timestamp.setText(formatDate(sms.getTimestamp()));


    }

    @Override
    public int getItemCount() {
        return sentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView sender,message,timestamp;

        public ViewHolder(View itemView) {
            super(itemView);
            sender = (TextView)itemView.findViewById(R.id.sender);
            message = (TextView)itemView.findViewById(R.id.message_body);
            timestamp = (TextView)itemView.findViewById(R.id.timestamp);
        }
    }


    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
            return fmtOut.format(date);
        } catch (ParseException e) {

        }

        return "";
    }
}
