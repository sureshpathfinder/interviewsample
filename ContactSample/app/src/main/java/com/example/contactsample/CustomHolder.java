package com.example.contactsample;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class CustomHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    //OUR VIEWS
    TextView title;
    TextView intialTitle;
    TextView nameTxt;
    ItemClickListener itemClickListener;
    public CustomHolder(View itemView) {
        super(itemView);
        this.intialTitle= (TextView) itemView.findViewById(R.id.profileInitialTitle);
        this.title= (TextView) itemView.findViewById(R.id.profileTitle);
        this.nameTxt= (TextView) itemView.findViewById(R.id.contactname);
        itemView.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClick(v,getLayoutPosition());
    }
    public void setItemClickListener(ItemClickListener ic)
    {
        this.itemClickListener=ic;
    }
}
