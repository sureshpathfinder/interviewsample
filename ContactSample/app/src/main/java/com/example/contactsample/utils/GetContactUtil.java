package com.example.contactsample.utils;

import android.Manifest;
import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.example.contactsample.ConstantUrl;
import com.example.contactsample.MainActivity;
import com.example.contactsample.Model.Address;
import com.example.contactsample.Model.ContactModel;
import com.example.contactsample.Model.DatabaseHelper;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

public class GetContactUtil extends Thread {

    private IOperationCallback callback;
    private Context context;

    public GetContactUtil(IOperationCallback callback, Context context) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    public void run() {
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            String url = ConstantUrl.GetContactUrl;

            URL imageUrl = new URL(url);
            conn = (HttpURLConnection) imageUrl.openConnection();
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");                        // No I18N
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            int code = conn.getResponseCode();
            if (code == 200) {
                is = conn.getInputStream();
                BufferedReader br = new BufferedReader(new
                        InputStreamReader(is));
                String readLine;
                String responseBody = "";
                while (((readLine = br.readLine()) != null)) {
                    responseBody += readLine;
                }
                if (responseBody != null && responseBody.trim().length() > 0) {
                    ArrayList resdata = (ArrayList) HttpDataWrapper.getObject(responseBody);
                    try {
                        Iterator it = resdata.iterator();
                        while (it.hasNext()) {
                            Hashtable ht = (Hashtable) it.next();
                            int id = (int) ht.get("id");
                            String name = (String) ht.get("name");
                            String username = (String) ht.get("username");
                            String email = (String) ht.get("email");
                            String phone = (String) ht.get("phone");
                            String website = (String) ht.get("website");
                            Hashtable address = (Hashtable) ht.get("address");
                            Hashtable company = (Hashtable) ht.get("company");

                            ContactModel contactModel = new ContactModel(id, name, username, email, address, phone, website, company);

                            DatabaseHelper.getInstance(context).insertContact(contactModel);
                            Log.d(name, email);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (callback != null) {
                    callback.onSuccess("onRefresh");     // No I18N
                }
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
