package com.example.automate;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class AdapterList extends RecyclerView.Adapter<AdapterList.ListViewHolder> {
    private ArrayList<ListItem> mListItems ;

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item , parent , false) ;
        ListViewHolder lvh = new ListViewHolder(v) ;
        return  lvh ;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        ListItem items = mListItems.get(position) ;
        holder.textView.setText(items.getString());
    }

    @Override
    public int getItemCount() {
        //Log.e("size : " , ""+mListItems.size() );
        return mListItems.size() ;
    }

    public AdapterList(ArrayList<ListItem> listItems) {
        //Log.e("list : " , ""+mListItems );
        mListItems = listItems ;
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {

        public TextView textView ;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_container) ;
        }
    }
}
