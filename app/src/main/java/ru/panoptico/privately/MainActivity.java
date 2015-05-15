package ru.panoptico.privately;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.net.rtp.AudioCodec;
import android.net.rtp.AudioGroup;
import android.net.rtp.AudioStream;
import android.net.rtp.RtpStream;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;

import ru.panoptico.privately.SipEvent.SipEventType;
import ru.panoptico.privately.db.PrivatelyDbAdapter;


public class MainActivity extends Activity implements ISipEventListener {

    public static final String USER_ID = "Privately.USER_ID";

    public static PrivatelyDbAdapter dbHelper;
    public static INewMessageListener msgListener = null;

    String usr;
    String psw;
    String srv;
    AudioManager audio;
    AudioStream audioStream;
    AudioGroup audioGroup;

    SQLiteDatabase db;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Database
        dbHelper = new PrivatelyDbAdapter(this);
        dbHelper.open();

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        refreshCreds();

        // Show ID for tip
        showToast(this, "Your ID: " + usr);

        doRegister();

        // Fill users-list
        final ListView lvUsers = (ListView) findViewById(R.id.lv_users);
        /*
        String[] users = new String[] {
                "101", "102", "103", "104", "105", "106"
        };
        String[] icons = new String[] {
                "101", "102", "103", "104", "105", "106"
        };
        */

        ArrayList<User> users = new ArrayList<User>();
        users.add(new User(this, "101", R.drawable.ava1));
        users.add(new User(this, "102", R.drawable.ava2));
        users.add(new User(this, "103", R.drawable.ava3));
        users.add(new User(this, "104", R.drawable.ava4));
        users.add(new User(this, "105", R.drawable.ava5));
        users.add(new User(this, "106", R.drawable.ava6));

        //Define Adapter
        /*
        ArrayAdapter<String> uAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                users);
         */
        UsersAdapter uAdapter = new UsersAdapter(this, users);
/*
        ArrayAdapter<String> uAdapter = new ArrayAdapter<String>(this,
                R.layout.main_row, R.id.label, users, R.id.icon, icons);

*/
        // Set adapter
        lvUsers.setAdapter(uAdapter);

        // Create click listener
        lvUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String itemVal = (String) lvUsers.getItemAtPosition(position);
                User itemVal = (User) lvUsers.getItemAtPosition(position);
                goMessaging(itemVal);
            }

            private void goMessaging(/*String*/User itemVal) {
                Intent intent = new Intent(getApplicationContext(), MessagingActivity.class);
                intent.putExtra(USER_ID, itemVal.getName());
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            goSettings();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void goSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public static void log(String txt) {
        String dateTimeStr = DateFormat.getDateTimeInstance().format(new Date());
        Log.i("DEVLOG", txt + " : " + dateTimeStr);
    }

    public static byte[] getLocalIPAddress() {
        byte ip[] = null;
        try {
            for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en
                    .hasMoreElements();) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                for (Enumeration enumIpAddr = intf.getInetAddresses(); enumIpAddr
                        .hasMoreElements();) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr
                            .nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        ip = inetAddress.getAddress();
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return ip;

    }

    @Override
    public void onSipMessage(SipEvent sipEventObject) {
        System.out.println("Sip Event fired");
        if (sipEventObject.type == SipEventType.MESSAGE) {
            final String text = sipEventObject.content;
            final String to = usr;

            String from = sipEventObject.from;
            from = from.split("@")[0].split(":")[1];
            final String finalFrom = from;

            this.runOnUiThread(new Runnable() {
                public void run() {
                    showToast(MainActivity.this, "New message: " + finalFrom);
                    dbHelper.createMessage(text, finalFrom, to);
                    if (msgListener != null) {
                        msgListener.onMessage(finalFrom);
                    }
                }
            });

        } else if (sipEventObject.type == SipEventType.BYE) {
            audio.setMode(AudioManager.RINGER_MODE_NORMAL);
            audioStream.release();
            audioGroup.clear();
        }

    }

    public void refreshCreds() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        usr  = sharedPref.getString(SettingsActivity.PREF_LOG, "");
        psw  = sharedPref.getString(SettingsActivity.PREF_PSW, "");
        srv  = sharedPref.getString(SettingsActivity.PREF_SRV, "");
    }

    // Main actions
    public void doRegister() {
        new SipStackAndroid().execute(usr, psw, srv);
        SipStackAndroid.getInstance().addSipListener(this);
        new SipRegister().execute(usr, psw, srv);
    }

    public static void doMessage(String usr, String text) {
        String to = "sip:" + usr + "@5.9.201.234";
        new SipSendMessage().execute(to, text);
    }

    public void doCall() {
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
//                .permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//        try {
//            audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//            audio.setMode(AudioManager.MODE_IN_COMMUNICATION);
//            audioGroup = new AudioGroup();
//            audioGroup.setMode(AudioGroup.MODE_ECHO_SUPPRESSION);
//            audioStream = new AudioStream(
//                    InetAddress.getByAddress(getLocalIPAddress()));
//            audioStream.setCodec(AudioCodec.PCMU);
//            audioStream.setMode(RtpStream.MODE_NORMAL);
//            audioStream.associate(
//                    InetAddress.getByName(SipStackAndroid.getRemoteIp()),
//                    7078);
//            audioStream.join(audioGroup);
//
//            // Stream is setup now we initiate a call and specify our
//            // listening RTP port
//            new SipCall().execute(editTextTo.getText().toString(),
//                    editTextMessage.getText().toString(),
//                    String.valueOf(audioStream.getLocalPort()));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
