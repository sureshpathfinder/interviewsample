package com.example.contactsample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.contactsample.Model.Address;
import com.example.contactsample.Model.ContactModel;
import com.example.contactsample.Model.DatabaseHelper;
import com.google.gson.Gson;

public class ContactProfileActivity extends AppCompatActivity {

    RelativeLayout emailLayout, phoneLayout, websiteLayout, addressLayout;
    ContactModel contactModel = null;
    Address address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.contactdetailactivity_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        int id =  getIntent().getExtras().getInt("id");
        TextView phoneView = (TextView) findViewById(R.id.phoneTextView);
        TextView emailView = (TextView) findViewById(R.id.emailTextView);
        TextView websiteView = (TextView) findViewById(R.id.textWebsite);
        final TextView addressView = (TextView) findViewById(R.id.textAddress);

        phoneLayout = (RelativeLayout) findViewById(R.id.phoneNoLayout);
        emailLayout = (RelativeLayout) findViewById(R.id.emailLayout);
        websiteLayout = (RelativeLayout) findViewById(R.id.websiteLayout);
        addressLayout = (RelativeLayout) findViewById(R.id.addressLayout);

        contactModel = DatabaseHelper.getInstance(this).getContactModel(id);
        if(contactModel != null)
        {
            getSupportActionBar().setTitle(contactModel.getName());
            phoneView.setText(contactModel.getPhone());
            emailView.setText(contactModel.getEmail());
            websiteView.setText(contactModel.getWebsite());

            try {
                Gson gson = new Gson();
                address = gson.fromJson(contactModel.getAddressString(), Address.class);
                addressView.setText(address.getStreet()+ ", \n" +address.getSuite()+", \n"+ address.getCity()+" - "+address.getZipcode());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        emailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("plain/text");                                                                        // No I18N
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{contactModel.getEmail()});
                    startActivity(Intent.createChooser(intent, ""));
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        phoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("tel:"+contactModel.getPhone()));									// No I18N
                    startActivity(intent);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+contactModel.getPhone()));									// No I18N
                    startActivity(intent);
                }
            }
        });

        websiteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://"+contactModel.getWebsite()));									// No I18N
                    startActivity(intent);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        addressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri gmmIntentUri = Uri.parse("geo:" + address.getGeo().getLat() + "," + address.getGeo().getLng());
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                } catch (Exception e) {
                    Log.e(e.getMessage(), "");
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
