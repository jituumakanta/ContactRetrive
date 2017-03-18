package com.example.volmopc1.contactretrive;

import android.content.CursorLoader;
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
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //CREATING ContentResolver OBJECT
        ContentResolver cr = getContentResolver();
        //CREATING URI
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        //COLUMN TO RETURN//column_to_return can be null to return all the column in the tables
        String[] column_to_return = {ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP, ContactsContract.Contacts._ID};
        //cr.query returns cursor
        Cursor c = cr.query(uri, column_to_return, null, null, null); //Cursor c = cr.query(ContactsContract.Contacts.CONTENT_URI, new String[] {ContactsContract.Contacts.DISPLAY_NAME_SOURCE}, null, null, null);       //Cursor c = cr.query(uri, null, null, null, null);
        //ITERATING CURSOR
        if (c.moveToFirst()) {
            do {

                Log.d("id", c.getString(c.getColumnIndex(ContactsContract.Contacts._ID)));
                Log.d("name", c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));

                //getting the mob number
                String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));

                //CREATING URI FOR CONTACTNUMBER
                Uri uri1 = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                //CREATING SELECTION ARGUMENT
                String SelectionArg = ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + id;
                //CREATING CURSOR OBJECT TO GET  THE CONTACT NUMBER
                Cursor pCur = cr.query(uri1, null, SelectionArg, null, null);

                //ITERATING CURSOR
                while (pCur.moveToNext()) {
                    Log.d("no", pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                }
                pCur.close();
                /**
                 * GETTING EMAIL............
                 */
                Cursor emails = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + id, null, null);
                while (emails.moveToNext()) {
                    // This would allow you get several email addresses
                    String emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    Log.d("email", emailAddress);
                }

                emails.close();
            } while (c.moveToNext());
        }
        //    if (Integer.parseInt(c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        //Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + "8727", null, null);
        while (pCur.moveToNext()) {
            // Do something with phones
            //Log.d("no", pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
        }
        pCur.close();
        // }


    }


}



