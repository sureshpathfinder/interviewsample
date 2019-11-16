package com.example.contactsample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactsample.Model.ContactModel;
import com.example.contactsample.Model.DatabaseHelper;
import com.example.contactsample.utils.GetContactUtil;
import com.example.contactsample.utils.IOperationCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements  ItemClickListener {

    private RecyclerView rv;
    private CustomAdapter adapter;
    private LinearLayout emptyState = null;
    List<ContactModel> Contacts = null;
    String searchQuery = "";
    androidx.appcompat.widget.SearchView searchView = null;
    ProgressBar loadingProgressBar;
    MyDataObserver dataObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DatabaseHelper.getInstance(this);

        loadingProgressBar = (ProgressBar) findViewById(R.id.emptystate_progressbar);
        emptyState = (LinearLayout) findViewById(R.id.nosearch_layout);
        rv = (RecyclerView) findViewById(R.id.myRecycler);
        rv.setLayoutManager(new LinearLayoutManager(this));

        //Get Internet permission
        getPermission();
        //Fetch from server
        GetContactUtil util = new GetContactUtil(new MyCallback(), this);
        util.start();

        refreshList();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if(adapter!=null) {
            try
            {
                adapter.unregisterAdapterDataObserver(dataObserver);
            }
            catch(Exception e)
            {
                // already unregistered
            }
        }
    }

    //Recycleviewer ItemClickListener
    @Override
    public void onItemClick(View v, int pos) {
        try{
        Intent i = new Intent(this, ContactProfileActivity.class);
        i.putExtra("id", Contacts.get(pos).getId());
        startActivity(i);

        Toast.makeText(MainActivity.this, Contacts.get(pos).getName(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    //ADD Contacts TO ARRAYLIST
    private List<ContactModel> getContacts() {
        return DatabaseHelper.getInstance(this).getAllNotes();
    }

    private void refreshList() {
        try {
            Contacts = getContacts();
            if (Contacts == null)
                Contacts = new ArrayList<>();
            adapter = new CustomAdapter(this, Contacts, this);

            dataObserver = new MyDataObserver();
            adapter.registerAdapterDataObserver(dataObserver);
            rv.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void getPermission() {
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.INTERNET)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.INTERNET},
                            0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class MyCallback implements IOperationCallback {
        public void onSuccess(final String msg) {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadingProgressBar.setVisibility(View.GONE);

                    MainActivity.this.refreshList();
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        searchView = (androidx.appcompat.widget.SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint("Search contacts");

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                searchQuery = "";
               if(hasFocus) {
                   loadingProgressBar.setVisibility(View.GONE);
                   adapter.getFilter().filter("");
               }
               else{
                   loadingProgressBar.setVisibility(View.GONE);
                   emptyState.setVisibility(View.GONE);
                   adapter.contacts = getContacts();
                   adapter.notifyDataSetChanged();
               }
            }
        });

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                searchQuery = query;
                adapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sort_contact) {
            adapter.sortList(true);
            return true;
        } else if (id == R.id.sortdesc_contact) {
            adapter.sortList(false);
            return true;
        } else if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class MyDataObserver extends RecyclerView.AdapterDataObserver
    {
        @Override
        public void onChanged() {
            super.onChanged();

            if (adapter.getItemCount() <= 0 && searchQuery.trim().length() > 0)
                emptyState.setVisibility(View.VISIBLE);
             else
                emptyState.setVisibility(View.GONE);
        }
    }
}
