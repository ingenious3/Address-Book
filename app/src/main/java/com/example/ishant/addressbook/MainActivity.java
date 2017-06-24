package com.example.ishant.addressbook;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity
   implements ContactsFragment.ContactsFragmentListener,
   DetailFragment.DetailFragmentListener,
   AddEditFragment.AddEditFragmentListener {

   public static final String CONTACT_URI = "contact_uri";

   private ContactsFragment contactsFragment;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      setSupportActionBar(toolbar);

      if (savedInstanceState == null &&
         findViewById(R.id.fragmentContainer) != null) {
         contactsFragment = new ContactsFragment();

         FragmentTransaction transaction =
            getSupportFragmentManager().beginTransaction();
         transaction.add(R.id.fragmentContainer, contactsFragment);
         transaction.commit();
      }
      else {
         contactsFragment =
            (ContactsFragment) getSupportFragmentManager().
               findFragmentById(R.id.contactsFragment);
      }
   }

   @Override
   public void onContactSelected(Uri contactUri) {
      if (findViewById(R.id.fragmentContainer) != null)
         displayContact(contactUri, R.id.fragmentContainer);
      else {
         getSupportFragmentManager().popBackStack();
         displayContact(contactUri, R.id.rightPaneContainer);
      }
   }

   @Override
   public void onAddContact() {
      if (findViewById(R.id.fragmentContainer) != null)
         displayAddEditFragment(R.id.fragmentContainer, null);
      else
         displayAddEditFragment(R.id.rightPaneContainer, null);
   }


   private void displayContact(Uri contactUri, int viewID) {
      DetailFragment detailFragment = new DetailFragment();

      Bundle arguments = new Bundle();
      arguments.putParcelable(CONTACT_URI, contactUri);
      detailFragment.setArguments(arguments);

      FragmentTransaction transaction =
         getSupportFragmentManager().beginTransaction();
      transaction.replace(viewID, detailFragment);
      transaction.addToBackStack(null);
      transaction.commit();
   }

   private void displayAddEditFragment(int viewID, Uri contactUri) {
      AddEditFragment addEditFragment = new AddEditFragment();

      if (contactUri != null) {
         Bundle arguments = new Bundle();
         arguments.putParcelable(CONTACT_URI, contactUri);
         addEditFragment.setArguments(arguments);
      }

      FragmentTransaction transaction =
         getSupportFragmentManager().beginTransaction();
      transaction.replace(viewID, addEditFragment);
      transaction.addToBackStack(null);
      transaction.commit();
   }

   @Override
   public void onContactDeleted() {
      getSupportFragmentManager().popBackStack();
      contactsFragment.updateContactList(); // refresh contacts
   }

   @Override
   public void onEditContact(Uri contactUri) {
      if (findViewById(R.id.fragmentContainer) != null)
         displayAddEditFragment(R.id.fragmentContainer, contactUri);
      else
         displayAddEditFragment(R.id.rightPaneContainer, contactUri);
   }

   @Override
   public void onAddEditCompleted(Uri contactUri) {

      getSupportFragmentManager().popBackStack();
      contactsFragment.updateContactList();

      if (findViewById(R.id.fragmentContainer) == null) {

         getSupportFragmentManager().popBackStack();

         displayContact(contactUri, R.id.rightPaneContainer);
      }
   }
}