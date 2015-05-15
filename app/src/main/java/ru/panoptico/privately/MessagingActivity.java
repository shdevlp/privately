package ru.panoptico.privately;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import ru.panoptico.privately.db.PrivatelyDbAdapter;


public class MessagingActivity extends Activity implements View.OnClickListener, INewMessageListener {

    private SimpleCursorAdapter dataAdapter;

    String targetUsr;
    String currentUser;
    EditText editTextMessage;
    Button btnSend;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainActivity.msgListener = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        Intent intent = getIntent();
        String usr = intent.getStringExtra(MainActivity.USER_ID);
        targetUsr = usr;

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        currentUser = sharedPref.getString(SettingsActivity.PREF_LOG, "");

        ActionBar ab = getActionBar();
        ab.setTitle(usr);

        editTextMessage = (EditText) findViewById(R.id.editTextMessage);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);

        displayList();
        MainActivity.msgListener = this;
    }

    private void displayList() {
        Cursor cursor = MainActivity.dbHelper.fetchMesagesByCreds(targetUsr, currentUser);

        // Allow the activity to manage lifetime of the cursor
        startManagingCursor(cursor);

        int[] to = new int[] {
                R.id.usr,
                R.id.text
        };

        String[] columns = new String[] {
                PrivatelyDbAdapter.KEY_FROM,
                PrivatelyDbAdapter.KEY_TEXT
        };

        dataAdapter = new SimpleCursorAdapter(
                this,
                R.layout.message,
                cursor,
                columns,
                to,
                0
        );

        ListView lvMsg = (ListView) findViewById(R.id.lvMsg);
        lvMsg.setAdapter(dataAdapter);
        lvMsg.setSelection(lvMsg.getCount() - 1);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSend){
            sendMessage();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_messaging, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendMessage() {
        String msg = editTextMessage.getText().toString();
        editTextMessage.setText("");
        MainActivity.dbHelper.createMessage(msg, currentUser, targetUsr);
        displayList();
        MainActivity.doMessage(targetUsr, msg);
    }

    @Override
    public void onMessage(String from) {
        displayList();
    }
}
