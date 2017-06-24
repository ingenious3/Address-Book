package com.example.ishant.addressbook;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ishant.addressbook.data.DatabaseDescription.Contact;

public class ContactsAdapter
   extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

   public interface ContactClickListener {
      void onClick(Uri contactUri);
   }

   public class ViewHolder extends RecyclerView.ViewHolder {
      public final TextView textView;
      private long rowID;

      public ViewHolder(View itemView) {
         super(itemView);
         textView = (TextView) itemView.findViewById(android.R.id.text1);

         itemView.setOnClickListener(
            new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                  clickListener.onClick(Contact.buildContactUri(rowID));
               }
            }
         );
      }

      public void setRowID(long rowID) {
         this.rowID = rowID;
      }
   }

   private Cursor cursor = null;
   private final ContactClickListener clickListener;

   public ContactsAdapter(ContactClickListener clickListener) {
      this.clickListener = clickListener;
   }

   @Override
   public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(
         android.R.layout.simple_list_item_1, parent, false);
      return new ViewHolder(view); 
   }

   @Override
   public void onBindViewHolder(ViewHolder holder, int position) {
      cursor.moveToPosition(position);
      holder.setRowID(cursor.getLong(cursor.getColumnIndex(Contact._ID)));
      holder.textView.setText(cursor.getString(cursor.getColumnIndex(
         Contact.COLUMN_NAME)));
   }

   @Override
   public int getItemCount() {
      return (cursor != null) ? cursor.getCount() : 0;
   }

   public void swapCursor(Cursor cursor) {
      this.cursor = cursor;
      notifyDataSetChanged();
   }
}
