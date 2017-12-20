package com.example.chuck_csp.androidlabs;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.Serializable;

/**
 * Created by chuck_csp on 12/12/17.
 */

public class MessageFragment extends Fragment {

    private static String DATABASE_NAME = "ChatDB";
    public static String TABLE_NAME = "ChatMessages";
    final static String KEY_ID = "Id";
    final static String KEY_MESSAGE = "Messages";

    private Activity parent;
    private ChatWindow chatWindow;
    private final String ACTIVITY_NAME = "MessageFragment";

    private long id;
    private String message;
    private boolean isTablet;
    private ChatWindow.ChatAdapter chatAdapter;

    public MessageFragment(){   }

    public void setChatWindow(ChatWindow chatWindow){
        this.chatWindow = chatWindow;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Bundle passedInfo = getArguments();

        //long id= 0;
        if(passedInfo != null) {
            isTablet = passedInfo.getBoolean("isTablet");
            id = passedInfo.getLong("id");
            message = passedInfo.getString("message");
        }
        Log.i("Passed key", ""+id);


        parent = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){


        View v = inflater.inflate(R.layout.activity_message_details, null);
        ChatWindow cw = new ChatWindow();
        ChatDatabaseHelper dbHelper = new ChatDatabaseHelper(parent);
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final Cursor c = db.rawQuery("SELECT * FROM ChatMessages", null);

        c.moveToPosition((int)id);

        TextView textViewMessage = (TextView)v.findViewById(R.id.textViewMessage);
        textViewMessage.setText(message);

        TextView textViewId = (TextView)v.findViewById(R.id.textViewID);
        textViewId.setText(Long.toString(id));

        final ListView lv = (ListView)v.findViewById(R.id.listViewChat);


        Button buttonDelete = (Button)v.findViewById(R.id.buttonDeleteMessage);

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.delete(TABLE_NAME, KEY_ID+" = "+id, null);
                if(isTablet){
                    lv.notifyAll();
                }
            }
        });

        return v;

    }

}
