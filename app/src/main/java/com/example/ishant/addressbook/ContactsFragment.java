package com.example.ishant.addressbook;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ishant.addressbook.data.DatabaseDescription.Contact;

public class ContactsFragment extends Fragment
   implements LoaderManager.LoaderCallbacks<Cursor> {

   public interface ContactsFragmentListener {
      void onContactSelected(Uri contactUri);

      void onAddContact();
   }

   private static final int CONTACTS_LOADER = 0; 

   private ContactsFragmentListener listener;

   private ContactsAdapter contactsAdapter; 

   @Override
   public View onCreateView(
      LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
      super.onCreateView(inflater, container, savedInstanceState);
      setHasOptionsMenu(true); 

      View view = inflater.inflate(
         R.layout.fragment_contacts, container, false);
      RecyclerView recyclerView =
         (RecyclerView) view.findViewById(R.id.recyclerView);

      recyclerView.setLayoutManager(
         new LinearLayoutManager(getActivity().getBaseContext()));

      contactsAdapter = new ContactsAdapter(
         new ContactsAdapter.ContactClickListener() {
            @Override
            public void onClick(Uri contactUri) {
               listener.onContactSelected(contactUri);
            }
         }
      );
      recyclerView.setAdapter(contactsAdapter); 

      recyclerView.addItemDecoration(new ItemDivider(getContext()));

      recyclerView.setHasFixedSize(true);

      FloatingActionButton addButton =
         (FloatingActionButton) view.findViewById(R.id.addButton);
      addButton.setOnClickListener(
         new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               listener.onAddContact();
            }
         }
      );

      return view;
   }

   @Override
   public void onAttach(Context context) {
      super.onAttach(context);
      listener = (ContactsFragmentListener) context;
   }

   @Override
   public void onDetach() {
      super.onDetach();
      listener = null;
   }

   @Override
   public void onActivityCreated(Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);
      getLoaderManager().initLoader(CONTACTS_LOADER, null, this);
   }

   public void updateContactList() {
      contactsAdapter.notifyDataSetChanged();
   }

   @Override
   public Loader<Cursor> onCreateLoader(int id, Bundle args) {
      switch (id) {
         case CONTACTS_LOADER:
            return new CursorLoader(getActivity(),
               Contact.CONTENT_URI,
               null, 
               null, 
               null, 
               Contact.COLUMN_NAME + " COLLATE NOCASE ASC"); 
         default:
            return null;
      }
   }

   @Override
   public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
      contactsAdapter.swapCursor(data);
   }

   @Override
   public void onLoaderReset(Loader<Cursor> loader) {
      contactsAdapter.swapCursor(null);
   }
}