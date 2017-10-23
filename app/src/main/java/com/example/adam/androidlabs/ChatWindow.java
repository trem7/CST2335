package com.example.adam.androidlabs;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.adam.androidlabs.ChatDatabaseHelper.KEY_ID;
import static com.example.adam.androidlabs.ChatDatabaseHelper.KEY_MESSAGE;
import static com.example.adam.androidlabs.ChatDatabaseHelper.TABLE_NAME;

public class ChatWindow extends Activity {

    ListView listView;
    EditText chatText;
    Button sendButton;
    final ArrayList<String> messageStore = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        ChatDatabaseHelper chatDatabaseHelper = new ChatDatabaseHelper(this);
        final SQLiteDatabase db = chatDatabaseHelper.getWritableDatabase();

        //This does a query and stores the results:
        Cursor results = db.query(false, TABLE_NAME, new String[] {KEY_ID, KEY_MESSAGE},
                null, null , null, null, null, null);

        //How many rows in the results:
        int numResults = results.getCount();

        //How many columns in the results:
        int numColumns = results.getColumnCount();

        int messageIndex = results.getColumnIndex(KEY_MESSAGE);

        results.moveToFirst();//resets the iteration of results

        //This is an example of a SimpleCursorAdapter to fill a list view with database results:
        int [] arr = new int[]{R.id.chatText};
        listView = findViewById(R.id.chatListView);
        SimpleCursorAdapter adptr = new SimpleCursorAdapter(this, R.layout.cursor_layout, results,
                new String[] {KEY_ID, KEY_MESSAGE}, arr, 0);

        String returnedMessage;
        listView.setAdapter(adptr);

        for(int i = 0; i < numResults; i++){
            returnedMessage = results.getString(messageIndex);
            Log.i("Results:", returnedMessage);
            results.moveToNext();
        }

        results.moveToFirst(); //resets the iteration of results
        while(!results.isAfterLast()){
            returnedMessage = results.getString(messageIndex);
            Log.i("Results:", returnedMessage);
            results.moveToNext();
        }

        chatText = findViewById(R.id.chatText);
        sendButton = findViewById(R.id.sendButton);

        //in this case, “this” is the ChatWindow, which is-A Context object
        final ChatAdapter messageAdapter = new ChatAdapter( this );
        //listView.setAdapter(messageAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageContent;
                //get the strings typed
                messageContent = ((EditText)findViewById(R.id.chatText)).getText().toString();

                //Use a content values object to insert them in the database
                ContentValues newData = new ContentValues();
                newData.put(KEY_MESSAGE, messageContent);

                //Then insert
                db.insert(TABLE_NAME,"", newData);

                messageStore.add(chatText.getText().toString());
                messageAdapter.notifyDataSetChanged(); //this restarts the process of getCount()/getView()
                chatText.setText("");
            }
        });
    }
    public class ChatAdapter extends ArrayAdapter<String>{
        public ChatAdapter(Context ctx){
            super(ctx, 0);
        }
        public int getCount(){
            return messageStore.size();
        }

        public String getItem(int position){
            return messageStore.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;
            if(position%2 == 0) {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            }
            else{
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            }
            TextView message = (TextView)result.findViewById(R.id.message_text);
            message.setText(getItem(position)); // get the string at position
            return result;
        }
    }
}
