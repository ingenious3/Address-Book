package com.example.ishant.addressbook.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ishant.addressbook.data.DatabaseDescription.Contact;

class AddressBookDatabaseHelper extends SQLiteOpenHelper {
   private static final String DATABASE_NAME = "AddressBook.db";
   private static final int DATABASE_VERSION = 1;

   // constructor
   public AddressBookDatabaseHelper(Context context) {
      super(context, DATABASE_NAME, null, DATABASE_VERSION);
   }

   // creates the contacts table when the database is created
   @Override
   public void onCreate(SQLiteDatabase db) {
      // SQL for creating the contacts table
      final String CREATE_CONTACTS_TABLE =
         "CREATE TABLE " + Contact.TABLE_NAME + "(" +
         Contact._ID + " integer primary key, " +
         Contact.COLUMN_NAME + " TEXT, " +
         Contact.COLUMN_PHONE + " TEXT, " +
         Contact.COLUMN_EMAIL + " TEXT, " +
         Contact.COLUMN_STREET + " TEXT, " +
         Contact.COLUMN_CITY + " TEXT, " +
         Contact.COLUMN_STATE + " TEXT, " +
         Contact.COLUMN_ZIP + " TEXT);";
      db.execSQL(CREATE_CONTACTS_TABLE); // create the contacts table
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion,
      int newVersion) { }
}
