// DetailFragment.java
// Fragment subclass that displays one contact's details
package com.example.ishant.addressbook;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ishant.addressbook.data.DatabaseDescription.Contact;

public class DetailFragment extends Fragment
   implements LoaderManager.LoaderCallbacks<Cursor> {

   // callback methods implemented by MainActivity
   public interface DetailFragmentListener {
      void onContactDeleted(); 

      void onEditContact(Uri contactUri);
   }

   private static final int CONTACT_LOADER = 0;

   private DetailFragmentListener listener; // MainActivity
   private Uri contactUri; // Uri of selected contact

   private TextView nameTextView; // displays contact's name
   private TextView phoneTextView; // displays contact's phone
   private TextView emailTextView; // displays contact's email
   private TextView streetTextView; // displays contact's street
   private TextView cityTextView; // displays contact's city
   private TextView stateTextView; // displays contact's state
   private TextView zipTextView; // displays contact's zip

   // set DetailFragmentListener when fragment attached
   @Override
   public void onAttach(Context context) {
      super.onAttach(context);
      listener = (DetailFragmentListener) context;
   }

   // remove DetailFragmentListener when fragment detached
   @Override
   public void onDetach() {
      super.onDetach();
      listener = null;
   }

   // called when DetailFragmentListener's view needs to be created
   @Override
   public View onCreateView(
      LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
      super.onCreateView(inflater, container, savedInstanceState);
      setHasOptionsMenu(true);

     
      Bundle arguments = getArguments();

      if (arguments != null)
         contactUri = arguments.getParcelable(MainActivity.CONTACT_URI);

      // inflate DetailFragment's layout
      View view =
         inflater.inflate(R.layout.fragment_detail, container, false);

      // get the EditTexts
      nameTextView = (TextView) view.findViewById(R.id.nameTextView);
      phoneTextView = (TextView) view.findViewById(R.id.phoneTextView);
      emailTextView = (TextView) view.findViewById(R.id.emailTextView);
      streetTextView = (TextView) view.findViewById(R.id.streetTextView);
      cityTextView = (TextView) view.findViewById(R.id.cityTextView);
      stateTextView = (TextView) view.findViewById(R.id.stateTextView);
      zipTextView = (TextView) view.findViewById(R.id.zipTextView);

      // load the contact
      getLoaderManager().initLoader(CONTACT_LOADER, null, this);
      return view;
   }

   // display this fragment's menu items
   @Override
   public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
      super.onCreateOptionsMenu(menu, inflater);
      inflater.inflate(R.menu.fragment_details_menu, menu);
   }

   // handle menu item selections
   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case R.id.action_edit:
            listener.onEditContact(contactUri); 
            return true;
         case R.id.action_delete:
            deleteContact();
            return true;
      }

      return super.onOptionsItemSelected(item);
   }

   // delete a contact
   private void deleteContact() {
      confirmDelete.show(getFragmentManager(), "confirm delete");
   }

   private final DialogFragment confirmDelete =
      new DialogFragment() {
         @Override
         public Dialog onCreateDialog(Bundle bundle) {
            AlertDialog.Builder builder =
               new AlertDialog.Builder(getActivity());

            builder.setTitle(R.string.confirm_title);
            builder.setMessage(R.string.confirm_message);

            builder.setPositiveButton(R.string.button_delete,
               new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(
                     DialogInterface dialog, int button) {

                     getActivity().getContentResolver().delete(
                        contactUri, null, null);
                     listener.onContactDeleted();
                  }
               }
            );

            builder.setNegativeButton(R.string.button_cancel, null);
            return builder.create();
         }
      };

   @Override
   public Loader<Cursor> onCreateLoader(int id, Bundle args) {
      CursorLoader cursorLoader;

      switch (id) {
         case CONTACT_LOADER:
            cursorLoader = new CursorLoader(getActivity(),
               contactUri, 
               null, 
               null, 
               null, 
               null); 
            break;
         default:
            cursorLoader = null;
            break;
      }

      return cursorLoader;
   }

   @Override
   public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
   
      if (data != null && data.moveToFirst()) {
   
         int nameIndex = data.getColumnIndex(Contact.COLUMN_NAME);
         int phoneIndex = data.getColumnIndex(Contact.COLUMN_PHONE);
         int emailIndex = data.getColumnIndex(Contact.COLUMN_EMAIL);
         int streetIndex = data.getColumnIndex(Contact.COLUMN_STREET);
         int cityIndex = data.getColumnIndex(Contact.COLUMN_CITY);
         int stateIndex = data.getColumnIndex(Contact.COLUMN_STATE);
         int zipIndex = data.getColumnIndex(Contact.COLUMN_ZIP);

   
         nameTextView.setText(data.getString(nameIndex));
         phoneTextView.setText(data.getString(phoneIndex));
         emailTextView.setText(data.getString(emailIndex));
         streetTextView.setText(data.getString(streetIndex));
         cityTextView.setText(data.getString(cityIndex));
         stateTextView.setText(data.getString(stateIndex));
         zipTextView.setText(data.getString(zipIndex));
      }
   }

   
   @Override
   public void onLoaderReset(Loader<Cursor> loader) { }
}