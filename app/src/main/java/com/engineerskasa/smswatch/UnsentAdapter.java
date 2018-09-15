package com.engineerskasa.smswatch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.engineerskasa.smswatch.Model.SMS_Sent;
import com.engineerskasa.smswatch.Model.SMS_Unsent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UnsentAdapter extends RecyclerView.Adapter<UnsentAdapter.ViewHolder> {
    private Context context;
    private List<SMS_Unsent> unsentList;

    public UnsentAdapter(Context context, List<SMS_Unsent> unsentList) {
        this.context = context;
        this.unsentList = unsentList;
    }

    @NonNull
    @Override
    public UnsentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.unsent_layout,parent,false);

        return new UnsentAdapter.ViewHolder(itemview);
    }


    @Override
    public void onBindViewHolder(@NonNull UnsentAdapter.ViewHolder holder, int position) {
        SMS_Unsent sms = unsentList.get(position);

        holder.sender.setText(sms.getSender());
        holder.message.setText(sms.getMessage());
        holder.timestamp.setText(formatDate(sms.getTimestamp()));


    }

    @Override
    public int getItemCount() {
        return unsentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView sender,message,timestamp;

        public ViewHolder(View itemView) {
            super(itemView);
            sender = (TextView)itemView.findViewById(R.id.un_sender);
            message = (TextView)itemView.findViewById(R.id.un_message_body);
            timestamp = (TextView)itemView.findViewById(R.id.un_timestamp);
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
