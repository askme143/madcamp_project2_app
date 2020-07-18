package com.example.madcampserverapp.ui.contact;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcampserverapp.MainActivity;
import com.example.madcampserverapp.R;
import com.example.madcampserverapp.ThreadTask;
import com.example.madcampserverapp.server.MyResponse;
import com.example.madcampserverapp.server.RequestHttpURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class FragmentContact extends Fragment {
    private static final int REQUEST_PERMISSIONS_CODE_READ_CONTACT = 100;
    private static final String TAG = "ContactFragment";

    private ArrayList<Contact> mContactList;

    private Context mContext;
    private View mView;
    private ContactAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_contact, null);
        checkPermission();

        return null;
    }

    public void startFragment() {
        updateContacts();
    }

    public void updateContacts() {
        mContactList = getContactList();

        /* Make url */
        String url = ((MainActivity) getActivity()).getUrl() + "/contact_put";

        /* Make JSONObject of fb_id and contacts */
        JSONObject jsonObject = new JSONObject();
        try {
            /* Put fb_id of a user */
            jsonObject.put("fb_id", ((MainActivity) getActivity()).getFacebookID());

            /* Make JSONArray of contacts */
            JSONArray jsonArray = new JSONArray();
            for (Contact contact : mContactList) {
                JSONObject tempObject = new JSONObject();

                tempObject.put("name", contact.getName());
                tempObject.put("phone_number", contact.getPhoneNumber());

                jsonArray.put(tempObject);
            }

            /* Put JSONArary */
            jsonObject.put("contacts", jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* Make response */
        MyResponse response = new MyResponse() {
            @Override
            public void response(String result) {
                if (result == null) {
                    Log.e(TAG, "Error on updateContacts");
                } else if (result.equals("failed")) {
                    Log.e(TAG, "Failed on updateContacts");
                } else {
                    Log.e(TAG, result);
                }
            }
        };

        /* Request and receive a result */
        try {
            NetworkTask networkTask = new NetworkTask(url, jsonObject, response);
            networkTask.execute(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getContacts() {
    }

    public ArrayList<Contact> getContactList() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.Contacts.PHOTO_ID,
                ContactsContract.Contacts._ID
        };
        String[] selectionArgs = null;

        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME+" COLLATE LOCALIZED ASC";

        Cursor cursor = getActivity().getContentResolver().query(uri, projection,
                null, null, sortOrder);

        LinkedHashSet<Contact> hashSet = new LinkedHashSet<>();
        ArrayList<Contact> contactList;

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                String phoneNumber = cursor.getString(1);

                Contact contact = new Contact(name, phoneNumber);

                if (phoneNumber.startsWith("01")) {
                    hashSet.add(contact);
                }

            } while (cursor.moveToNext());
        }

        contactList = new ArrayList<>(hashSet);

        cursor.close();

        return contactList;
    }

    public void checkPermission() {
        String tmp = "";

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED)
            tmp += Manifest.permission.READ_CONTACTS + " ";

        if (!TextUtils.isEmpty(tmp)) {
            String[] tmpArray = tmp.trim().split(" ");

            requestPermissions(tmpArray,REQUEST_PERMISSIONS_CODE_READ_CONTACT);
            ActivityCompat.requestPermissions(getActivity(), tmpArray, REQUEST_PERMISSIONS_CODE_READ_CONTACT);
        } else {
            startFragment();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS_CODE_READ_CONTACT) {
            if (permissions[0].equals(Manifest.permission.READ_CONTACTS)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startFragment();
            } else {
                Log.e(TAG, "Permission not granted");
            }
        }
    }

    public static class NetworkTask extends ThreadTask<Void, String> {

        private String mUrl;
        private ContentValues mValues;
        private MyResponse mMyResponse;
        private JSONObject mJSONObject;

        public NetworkTask(String url, ContentValues values, MyResponse myResponse) {
            mUrl = url;
            mValues = values;
            mMyResponse = myResponse;
        }

        public NetworkTask(String url, JSONObject jsonObject, MyResponse myResponse) {
            mUrl = url;
            mJSONObject = jsonObject;
            mMyResponse = myResponse;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void arg) {
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();

            if (mValues == null)
                return requestHttpURLConnection.request(mUrl, mJSONObject);
            else
                return requestHttpURLConnection.request(mUrl, mValues);
        }

        @Override
        protected void onPostExecute(String result) {
            mMyResponse.response(result);
        }
    }
}
