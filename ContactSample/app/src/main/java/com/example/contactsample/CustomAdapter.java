package com.example.contactsample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.recyclerview.widget.RecyclerView;

import com.example.contactsample.Model.ContactModel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomHolder> implements Filterable {
    Context c;
    List<ContactModel> contacts, filterList;
    public CustomFilter filter;
    ItemClickListener listener;

    public CustomAdapter(Context ctx, List<ContactModel> contacts, ItemClickListener listener) {
        this.c = ctx;
        this.contacts = contacts;
        this.filterList = contacts;
        this.listener = listener;
    }

    public void sortList(boolean isAscend)
    {
        final boolean isAscending = isAscend;
        Collections.sort(contacts, new Comparator<ContactModel>() {
            @Override
            public int compare(ContactModel o1, ContactModel o2) {
                return isAscending ? o1.getName().compareTo(o2.getName()) : o2.getName().compareTo(o1.getName());
            }
        });
        notifyDataSetChanged();
    }

    @Override
    public CustomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //CONVERT XML TO VIEW OBJ
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item_layout, null);
        //HOLDER
        CustomHolder holder = new CustomHolder(v);
        return holder;
    }

    //DATA BOUND TO VIEWS
    @Override
    public void onBindViewHolder(CustomHolder holder, int position) {
        //BIND DATA
        try {


            holder.nameTxt.setText(contacts.get(position).getName());
            String intial = contacts.get(position).getName().substring(0, 1).toUpperCase();
            holder.title.setText(intial);
            holder.intialTitle.setText(intial);
            if (position == 0) {
                holder.intialTitle.setVisibility(View.VISIBLE);
            } else {
                String previousIntial = contacts.get(position - 1).getName().substring(0, 1).toUpperCase();
                if (!intial.equalsIgnoreCase(previousIntial))
                    holder.intialTitle.setVisibility(View.VISIBLE);
                else
                    holder.intialTitle.setVisibility(View.GONE);
            }

            //IMPLEMENT CLICK LISTENER
            holder.setItemClickListener(listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //GET TOTAL NUM OF PLAYERS
    @Override
    public int getItemCount() {
        return contacts.size();
    }

    //RETURN FILTER OBJ
    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CustomFilter(filterList, this);
        }
        return filter;
    }
}
