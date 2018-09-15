package com.engineerskasa.smswatch.Model;

public class SMS_Sent {

    public static final String SMS_TABLE_NAME = "sent_sms";


    public static final String SMS_COLUMN_ID = " id";
    public static final String SMS_COLUMN_SENDER = "sender";
    public static final String SMS_COLUMN_MESSAGE = "message";
    public static final String SMS_COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private String sender,message,timestamp;

    public static final String CREATE_TABLE = "CREATE TABLE "+SMS_TABLE_NAME+
            " ("+SMS_COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
            SMS_COLUMN_SENDER+" TEXT, "+
            SMS_COLUMN_MESSAGE+" TEXT, "+
            SMS_COLUMN_TIMESTAMP+" DATETIME DEFAULT CURRENT_TIMESTAMP"+")";



    public SMS_Sent() {
    }

    public SMS_Sent(int id, String sender, String message, String timestamp) {
        this.id = id;
        this.sender = sender;
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
