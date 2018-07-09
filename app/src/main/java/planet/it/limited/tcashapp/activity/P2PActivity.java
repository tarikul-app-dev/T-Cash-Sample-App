package planet.it.limited.tcashapp.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import planet.it.limited.tcashapp.R;
import planet.it.limited.tcashapp.adapter.ContactsAdapter;
import planet.it.limited.tcashapp.database.ContactsDB;
import planet.it.limited.tcashapp.database.UtillDB;
import planet.it.limited.tcashapp.model.ContactModel;
import planet.it.limited.tcashapp.model.SortBasedOnName;
import planet.it.limited.tcashapp.utill.ConstantAPI;

import static android.Manifest.permission.READ_CONTACTS;
import static planet.it.limited.tcashapp.utill.SaveValueSharedPreference.getBoleanValueSharedPreferences;
import static planet.it.limited.tcashapp.utill.SaveValueSharedPreference.getValueFromSharedPreferences;
import static planet.it.limited.tcashapp.utill.SaveValueSharedPreference.saveBoleanValueSharedPreferences;
import static planet.it.limited.tcashapp.utill.SaveValueSharedPreference.saveToSharedPreferences;

public class P2PActivity extends AppCompatActivity {
    private static final int REQUEST_READ_CONTACTS = 444;
    private ListView lvAllContacts,lvRecentList;
    // private ProgressDialog pDialog;
    private Handler updateBarHandler;

    ArrayList<ContactModel> contactList;
    ArrayList<ContactModel> recentList;

    Cursor cursor;
    int counter;
    String name = "";
    String phoneNumber = "";
    String imageUri = "";
    Button btnExContacts;
    ContactsAdapter adapter;
    String contact_id = "";
    InputStream inputStream;

    //Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    FileWriter fileWriter = null;
    public static final int REQUEST_READ_CALL_LOG = 102;
    Toolbar toolbar;

    EditText edtSearchInfo,edtAmount,edtPin;
    boolean isReadContacts;
    ContactsDB contactsDB;
    RelativeLayout selectedListItem, selectedAmount,rlLayoutAllContacts,rlSendMoneyLayoutF;
    TextView txvName, txvNumber,txvSuccess,txvFSendNum,txvBackToHome;

    Cursor cursorLog;
    LinearLayout recentListLayout;
    byte[] img;
    String encodedImageString = "";
    Bitmap photo = null;
    de.hdodenhof.circleimageview.CircleImageView contactPic;
    ImageView sendMoney,imgvSendP2P;
    static boolean checkLayout;
    private PopupWindow pw;

    Button btnSendMoney;
    UtillDB utillDB;
    //TextView text1;
    int i;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    String sendMoneyAPI = "";
    ProgressBar sendProgress;
    String userNumber = "";
    String sPinNumber = "";
    String amount = "";
    String sendingNumber = "";
    String pinNumber = " ";
    String status = "pending";
    InputMethodManager imm;
    String respReqAPI = "";
    String trxID = "";
    String reqLastRes = "";
    MyTimerTask myTask;
    Timer myTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p2_p);
        toolbar = (Toolbar) findViewById(R.id.toolbar_p2p);
        //saveToSharedPreferences("layout","",P2PActivity.this);
        //checkLayout = getValueFromSharedPreferences("layout",P2PActivity.this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();
                if(checkLayout){

                        if(checkLayout){

                            //edtSearchInfo.setVisibility(View.VISIBLE);
                            //lvAllContacts.setVisibility(View.VISIBLE);
                            //rlLayoutAllContacts.setVisibility(View.VISIBLE);
                            selectedListItem.setVisibility(View.VISIBLE);
                            selectedAmount.setVisibility(View.VISIBLE);
                            rlSendMoneyLayoutF.setVisibility(View.GONE);
                            checkLayout = false;


                        }
                    }else {
                            Intent intent = new Intent(P2PActivity.this,TCashHomeActivity.class);
                            startActivity(intent);
                }






            }
        });

        isReadContacts = getBoleanValueSharedPreferences("is_read", P2PActivity.this);
        contactsDB = new ContactsDB(P2PActivity.this);
        contactsDB.open();
//        pDialog = new ProgressDialog(P2PActivity.this);
//        pDialog.setMessage("Reading contacts...");
//        pDialog.setCancelable(false);
//        pDialog.show();

        lvAllContacts = (ListView) findViewById(R.id.list);
        //lvRecentList = (ListView) findViewById(R.id.recent_list);
        edtSearchInfo = (EditText) findViewById(R.id.edt_search_no);
        selectedListItem = (RelativeLayout) findViewById(R.id.layout_p2p_listItem);
        selectedAmount = (RelativeLayout) findViewById(R.id.layout_p2p_amount);
        rlLayoutAllContacts = (RelativeLayout) findViewById(R.id.rlayout_all_contacts);
        txvName = (TextView) findViewById(R.id.txv_name);
        txvNumber = (TextView) findViewById(R.id.txv_user_number);
       // recentListLayout = (LinearLayout) findViewById(R.id.layout_recent_list);
         contactPic = (de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.select_profile_image);
         sendMoney = (ImageView)findViewById(R.id.imgv_send_money);
         imgvSendP2P = (ImageView)findViewById(R.id.imgv_send_p2p);
        rlSendMoneyLayoutF = (RelativeLayout) findViewById(R.id.layout_p2p_amount_final);
        edtAmount = (EditText)findViewById(R.id.edt_amount);
        edtPin = (EditText)findViewById(R.id.edt_pin);
         imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
          myTask = new MyTimerTask();
         myTimer = new Timer();


        userNumber = getValueFromSharedPreferences("user_number",P2PActivity.this);
        sPinNumber = getValueFromSharedPreferences("pin_number",P2PActivity.this);



        utillDB = new UtillDB(P2PActivity.this);
        utillDB.open();


        contactList = new ArrayList<>();
        recentList = new ArrayList<>();
        updateBarHandler = new Handler();



        if (!isReadContacts) {
            getContacts();
        } else {
            contactsDB.open();
            contactList = contactsDB.getInputData();
            Collections.sort(contactList, new SortBasedOnName());
            adapter = new ContactsAdapter(contactList, P2PActivity.this);
            lvAllContacts.setAdapter(adapter);

//                    adapter = new ContactsAdapter(recentList, P2PActivity.this);
//                    lvRecentList.setAdapter(adapter);

        }

        // Set onclicklistener to the list item.
        lvAllContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //TODO Do whatever you want with the list data
                String name = contactList.get(position).getUserName();
                String number = contactList.get(position).getContactNumber();
                String image = contactList.get(position).getUserPic();
                // Toast.makeText(getApplicationContext(), "item clicked : \n" + name, Toast.LENGTH_SHORT).show();
                selectedListItem.setVisibility(View.VISIBLE);
                selectedAmount.setVisibility(View.VISIBLE);
                edtSearchInfo.setVisibility(View.GONE);
                lvAllContacts.setVisibility(View.GONE);
                rlLayoutAllContacts.setVisibility(View.GONE);
                rlSendMoneyLayoutF.setVisibility(View.GONE);

                txvName.setText(name);
                txvNumber.setText(number);
                byte[] bytarray = Base64.decode( image, Base64.DEFAULT);
                Bitmap bitmapImage = BitmapFactory.decodeByteArray(bytarray, 0,
                        bytarray.length);
                if(bitmapImage!=null){
                    contactPic.setImageBitmap(bitmapImage);
                }


            }
        });



        edtSearchInfo.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               // recentListLayout.setVisibility(View.GONE);
                //rlLayoutAllContacts.setVisibility(View.GONE);
                String text = edtSearchInfo.getText().toString();
                contactsDB.open();
                contactList = contactsDB.searchByName(text);
                adapter = new ContactsAdapter(contactList, P2PActivity.this);
                lvAllContacts.setAdapter(adapter);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {


            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

//        lvRecentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                //TODO Do whatever you want with the list data
//               // String name = recentList.get(position).getUserName();
//                String number = recentList.get(position).getContactNumber();
//                // Toast.makeText(getApplicationContext(), "item clicked : \n" + name, Toast.LENGTH_SHORT).show();
//                selectedListItem.setVisibility(View.VISIBLE);
//                selectedAmount.setVisibility(View.VISIBLE);
//                edtSearchInfo.setVisibility(View.GONE);
//                lvAllContacts.setVisibility(View.GONE);
//                recentListLayout.setVisibility(View.GONE);
//               // txvName.setText(name);
//                txvNumber.setText(number);
//
//
//            }
//        });

        sendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   selectedAmount.setVisibility(View.VISIBLE);
                rlSendMoneyLayoutF.setVisibility(View.VISIBLE);
                checkLayout = true;
                //Hide auto keyboard:
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });

        imgvSendP2P.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinNumber = edtPin.getText().toString();
                if(pinNumber.length()>0){
                    if(sPinNumber!=null){
                        if(sPinNumber.equals(pinNumber)){
                            initiatePopupWindow(v);
                            //Hide auto keyboard:
                            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        }else {
                            Toast.makeText(P2PActivity.this," Pin Number Not Match",Toast.LENGTH_SHORT).show();
                        }
                    }

                }else {
                    Toast.makeText(P2PActivity.this," Pin Number Not Match",Toast.LENGTH_SHORT).show();
                }



            }
        });

    }

    public void getContacts() {

        if (!mayRequestContacts()) {
            return;
        }

        contactList = new ArrayList<>();


        String email = null;

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;


        StringBuffer output;

        ContentResolver contentResolver = getContentResolver();

        cursor = contentResolver.query(CONTENT_URI, null, null, null, null);

        //  cursor = contentResolver.query(CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " DSC");

        // Iterate every contact in the phone
        if (cursor.getCount() > 0) {

            counter = 0;
            while (cursor.moveToNext()) {
                output = new StringBuffer();

                // Update the progress message
                updateBarHandler.post(new Runnable() {
                    public void run() {
                        //pDialog.setMessage("Reading contacts : " + counter++ + "/" + cursor.getCount());
                    }
                });

                contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                // contactModel.setUserName(name);

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));

                if (hasPhoneNumber > 0) {

                    output.append("\n First Name:" + name);

                    //This is to read multiple phone numbers associated with the same contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);

                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        output.append("\n Phone number:" + phoneNumber);
                        // contactModel.setContactNumber(phoneNumber);

                        try {
                            inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),
                                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contact_id)));

                            if (inputStream != null) {
                                photo = BitmapFactory.decodeStream(inputStream);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                img = stream.toByteArray();
                                encodedImageString = Base64.encodeToString(img, Base64.DEFAULT);

                            }
                            if(inputStream==null){
                                encodedImageString = "";
                            }
                            assert inputStream != null;
                            if (inputStream != null) {

                                inputStream.close();
                            }



                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    phoneCursor.close();

                    // Read every email id associated with the contact
                    Cursor emailCursor = contentResolver.query(EmailCONTENT_URI, null, EmailCONTACT_ID + " = ?", new String[]{contact_id}, null);

                    while (emailCursor.moveToNext()) {

                        email = emailCursor.getString(emailCursor.getColumnIndex(DATA));

                        output.append("\n Email:" + email);



                    }

                    emailCursor.close();

                    String columns[] = {
                            ContactsContract.CommonDataKinds.Event.START_DATE,
                            ContactsContract.CommonDataKinds.Event.TYPE,
                            ContactsContract.CommonDataKinds.Event.MIMETYPE,
                    };

                    String where = ContactsContract.CommonDataKinds.Event.TYPE + "=" + ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY +
                            " and " + ContactsContract.CommonDataKinds.Event.MIMETYPE + " = '" + ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE + "' and " + ContactsContract.Data.CONTACT_ID + " = " + contact_id;

                    String[] selectionArgs = null;
                    String sortOrder = ContactsContract.Contacts.DISPLAY_NAME;

                    Cursor birthdayCur = contentResolver.query(ContactsContract.Data.CONTENT_URI, columns, where, selectionArgs, sortOrder);
                    Log.d("BDAY", birthdayCur.getCount() + "");
                    if (birthdayCur.getCount() > 0) {
                        while (birthdayCur.moveToNext()) {
                            String birthday = birthdayCur.getString(birthdayCur.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE));
                            output.append("Birthday :" + birthday);
                            Log.d("BDAY", birthday);
                        }
                    }
                    birthdayCur.close();
                }








                // Add the contact to the ArrayList
                String phNumber = getMeMyNumber(phoneNumber);
                // contactList.add(new ContactModel(name,phNumber,photo));

                contactsDB.saveAllContacts(name, phNumber,encodedImageString);

                saveBoleanValueSharedPreferences("is_read", true, P2PActivity.this);
            }


            // ListView has to be updated using a ui thread
            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    // orderAlphabetically(contactList);
                    contactsDB.open();
                    contactList = contactsDB.getInputData();


                    adapter = new ContactsAdapter(contactList, P2PActivity.this);
                    lvAllContacts.setAdapter(adapter);
                }
            });

            // Dismiss the progressbar after 500 millisecondds
            updateBarHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    //  pDialog.cancel();
                }
            }, 500);
        }
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getContacts();
            }
        }
    }

    public static String getMeMyNumber(String number) {
        String out = number.replaceAll("[^0-9]", "");       //remove all the non numbers (brackets dashes spaces etc.) except the + signs

        return out;

    }

//    public static void orderAlphabetically(final ArrayList<ContactModel> list) {
//        Collections.sort(list, new Comparator<ContactModel>() {
//            public int compare(ContactModel s1, ContactModel s2) {
//                String i1 = s1.getUserName();
//                String i2 = s2.getUserName();
//                // return i1.compareTo(i2);
//                return i1.compareTo(i2);
//            }
//        });
//
//    }

//    public String LastCall() {
//        StringBuffer sb = new StringBuffer();
//        String str = "";
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(P2PActivity.this, new String[]{Manifest.permission.READ_CALL_LOG}, REQUEST_READ_CALL_LOG);
//            }
//
//            if (ActivityCompat.checkSelfPermission(getApplicationContext(),
//                    Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
//
//                ActivityCompat.requestPermissions(P2PActivity.this,
//                        new String[]{Manifest.permission.READ_CALL_LOG}, REQUEST_READ_CALL_LOG);
//
//            } else {
//                Cursor cur = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, android.provider.CallLog.Calls.DATE + " DESC");
//
//                int number = cur.getColumnIndex( CallLog.Calls.NUMBER );
//                int duration = cur.getColumnIndex( CallLog.Calls.DURATION);
//                sb.append("Call Details : \n");
//                while ( cur.moveToNext() ) {
//                    String phNumber = cur.getString( number );
//                    String callDuration = cur.getString( duration );
//                    sb.append( "\nPhone Number:"+phNumber);
//                    break;
//                }
//                cur.close();
//                str=sb.toString();
//
//            }
//
//
//        }
//        return str;
//    }

    public void getRecentContacts(){
        //Map contactMap = new HashMap();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(P2PActivity.this, new String[]{Manifest.permission.READ_CALL_LOG}, REQUEST_READ_CALL_LOG);
            }

            if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(P2PActivity.this,
                        new String[]{Manifest.permission.READ_CALL_LOG}, REQUEST_READ_CALL_LOG);

            } else {
                Uri queryUri = android.provider.CallLog.Calls.CONTENT_URI;

                String[] projection = new String[] {
                        ContactsContract.Contacts._ID,
                        CallLog.Calls._ID,
                        CallLog.Calls.NUMBER,
                        CallLog.Calls.CACHED_NAME,
                        CallLog.Calls.DATE};

                String sortOrder = String.format("%s limit 3 ", CallLog.Calls.DATE + " DESC");


                 cursorLog = getContentResolver().query(queryUri, projection, null,null,sortOrder);


                while (cursorLog.moveToNext()) {
                    ContactModel recentConModel = new ContactModel();
                    String phoneNumber = cursorLog.getString(cursorLog
                            .getColumnIndex(CallLog.Calls.NUMBER));
                    recentConModel.setContactNumber(phoneNumber);
                    recentList.add(recentConModel);

                    String title = (cursorLog.getString(cursorLog.getColumnIndex(CallLog.Calls.CACHED_NAME)));

                    if(phoneNumber==null||title==null)continue;

//            String uri = "tel:" + phoneNumber ;
//            Intent intent = new Intent(Intent.ACTION_CALL);
//            intent.setData(Uri.parse(uri));
//            String intentUriString = intent.toUri(0);
//
//            contactMap.put(title,intentUriString);

                }
            }
        }


        cursorLog.close();
        //return contactMap;
    }

    private void initiatePopupWindow(View v) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) P2PActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View layout = inflater.inflate(R.layout.confirm_to_send_money,
                    (ViewGroup) findViewById(R.id.popup_element));
            btnSendMoney = (Button)layout.findViewById(R.id.btn_send_money);
            sendProgress = (ProgressBar)layout.findViewById(R.id.sendmoney_progress) ;
            txvSuccess = (TextView)layout.findViewById(R.id.txv_send_success);
            txvFSendNum = (TextView)layout.findViewById(R.id.txv_f_user_number);
            txvBackToHome = (TextView)layout.findViewById(R.id.txv_back_to_home);

            sendingNumber = txvNumber.getText().toString();
            if(sendingNumber.length()>0){
                txvFSendNum.setText(sendingNumber);
            }


            // create a 300px width and 470px height PopupWindow
            pw = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, true);
            // display the popup in the center
            pw.showAtLocation(v, Gravity.TOP, 80, 80);

//            TextView mResultText = (TextView) layout.findViewById(R.id.server_status_text);
//            Button cancelButton = (Button) layout.findViewById(R.id.end_data_send_button);
            btnSendMoney.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    amount = edtAmount.getText().toString();
                    saveToSharedPreferences("amount",amount,P2PActivity.this);
                    sendingNumber = txvNumber.getText().toString();

                    SendMoneyTask sendMoneyTask = new SendMoneyTask("TRUSTMM PAY "+
                            " "+sendingNumber+" "+amount+" "+pinNumber,userNumber);
                    sendMoneyTask.execute();
                }
            });

            txvBackToHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(P2PActivity.this,TCashHomeActivity.class);
                    startActivity(intent);
                    ActivityCompat.finishAffinity(P2PActivity.this);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class SendMoneyTask extends AsyncTask<String, Integer, String> {
        String mMSG;
        String mGSM;
        // private Dialog loadingDialog;
        public SendMoneyTask (String msg,String gsm){
            mMSG = msg;
            mGSM = gsm;
            sendMoneyAPI = ConstantAPI.sendMoneyAPI;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sendProgress.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {

            OkHttpClient client = new OkHttpClient();

            try {

                JSONObject json = new JSONObject();
                json.put("msg",mMSG);
                json.put("gsm",mGSM);


                RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
//                RequestBody requestBody = new FormBody.Builder()
//
//                        .add("from",mFromSender)
//                        .add("to",mToRecipients)
//                        .add("text",mContentMsg)
//                        .build();


                Request request = new Request.Builder()
                        .url(sendMoneyAPI)
                        .post(requestBody)
                        .build();


                Response response = null;
                //client.setRetryOnConnectionFailure(true);
                response = client.newCall(request).execute();
                if (response.isSuccessful()){
                    final String result = response.body().string();
                    // Log.d(RESPONSE_LOG,result);

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String trxID = jsonObject.getString("id");

                        respReqAPI = ConstantAPI.reqTrxID + trxID;

                        if(trxID.length()>0){
                            utillDB.saveAllTransaction(sendingNumber,amount,trxID,status);
//                            SendRequest sendRequest = new SendRequest();
//                            sendRequest.execute();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    myTimer.schedule(myTask, 10000, 1500);
                                }
                            });
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            sendProgress.setVisibility(View.GONE);
            txvSuccess.setVisibility(View.VISIBLE);
            txvBackToHome.setVisibility(View.VISIBLE);
        }


    }

    public class SendRequest extends AsyncTask<String, Integer, String> {




        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {

            OkHttpClient client = new OkHttpClient();

            try {


                Request request = new Request.Builder()
                        .url(respReqAPI)

                        .build();


                Response response = null;




                //client.setRetryOnConnectionFailure(true);
                response = client.newCall(request).execute();
                if (response.isSuccessful()){
                    final String result = response.body().string();
                    // Log.d(RESPONSE_LOG,result);

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                         reqLastRes = jsonObject.getString("resp");

                        if(reqLastRes.length()>0){
                           // utillDB.saveAllTransaction(sendingNumber,amount,trxID,status);
                            myTimer.cancel();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    sendProgress.setVisibility(View.GONE);
                                    openDialog(reqLastRes);
                                }
                            });
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            sendProgress.setVisibility(View.VISIBLE);
            txvSuccess.setVisibility(View.VISIBLE);
            txvBackToHome.setVisibility(View.VISIBLE);
        }


    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void openDialog(String msgId) {
        final Dialog dialog = new Dialog(P2PActivity.this); // Context, this, etc.
        dialog.setContentView(R.layout.custom_dialog);
        TextView txvResponseMsg = (TextView) dialog.findViewById(R.id.dialog_info);
        txvResponseMsg.setText("Your Message Sent Success" + " Your Msg Id " + msgId);
        Button okButton = (Button) dialog.findViewById(R.id.dialog_ok);
//        Button cancleButton = (Button) dialog.findViewById(R.id.dialog_cancel);

        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                singleSMSActivity.finish();
//                Intent intent = new Intent(mContext,SingleSMSActivity.class);
//                mContext.startActivity(intent);
//
//                SingleSMSActivity.txtPhoneNo.setText("");
//                SingleSMSActivity.edtContentMsg.setText("");
                dialog.dismiss();
            }
        });
//        cancleButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });

        dialog.show();
    }

    class MyTimerTask extends TimerTask {
        public void run() {
            SendRequest sendRequest = new SendRequest();
            sendRequest.execute();
        }
    }
}
