package com.example.contactsample;

import android.widget.Filter;

import com.example.contactsample.Model.ContactModel;

import java.util.ArrayList;
import java.util.List;

public class CustomFilter extends Filter {
    CustomAdapter adapter;
    List<ContactModel> filterList;
    ArrayList<ContactModel> filteredContacts;
    int count = 0;
    public CustomFilter(List<ContactModel> filterList, CustomAdapter adapter)
    {
        this.adapter=adapter;
        this.filterList=filterList;
    }

    //FILTERING OCURS
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();
        //CHECK CONSTRAINT VALIDITY
        if(constraint != null && constraint.length() > 0)
        {
            //CHANGE TO UPPER
            constraint=constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            filteredContacts=new ArrayList<>();
            for (int i=0;i<filterList.size();i++)
            {
                //CHECK
                if(filterList.get(i).getName().toUpperCase().startsWith(constraint.toString()))
                {
                    //ADD PLAYER TO FILTERED PLAYERS
                    filteredContacts.add(filterList.get(i));
                }
                else if(filterList.get(i).getName().toUpperCase().contains(constraint))
                {
                    filteredContacts.add(filterList.get(i));
                }
            }
            results.count=filteredContacts.size();
            results.values=filteredContacts;
        }
        else
        {
            //if(isExpanded) {
            results.count = 0;
            results.values = new ArrayList<>();
            //}
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapter.contacts = (ArrayList<ContactModel>) results.values;
        //REFRESH
        adapter.notifyDataSetChanged();
    }
}