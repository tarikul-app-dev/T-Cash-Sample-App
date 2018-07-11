package planet.it.limited.tcashapp.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import planet.it.limited.tcashapp.R;
import planet.it.limited.tcashapp.adapter.ButtonAdapter;
import planet.it.limited.tcashapp.utill.ConstantAPI;

import static planet.it.limited.tcashapp.utill.SaveValueSharedPreference.getValueFromSharedPreferences;

public class TCashHomeActivity extends AppCompatActivity implements ButtonAdapter.GridViewButtonInterface,NavigationView.OnNavigationItemSelectedListener{
    GridView btnGridView;
    public String[] filesnames = {
            "P2P",
            "Topup",
            "Cash Out",
            "Payment",
            "Fund Transfer",
            "Account Info"

    };
    public Drawable[] drawables = new Drawable[6];
    public int colors[] = new int[6];
    Toolbar toolbar;
    ImageView imgvMainMenu;
    DrawerLayout drawer;
    Button btnShowBlance;
    TextView txvShowBalance;
    String p2pAPI = "";
    String pinNumber = " ";
    String userNumber = "";
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar_tcash_home);
        setSupportActionBar(toolbar);

        initViews();
    }

    public void initViews(){
        drawables[0] = this.getResources().getDrawable(R.drawable.ic_p2p);
        drawables[1] = this.getResources().getDrawable(R.drawable.ic_topup);
        drawables[2] = this.getResources().getDrawable(R.drawable.ic_cash_out);
        drawables[3] = this.getResources().getDrawable(R.drawable.ic_payment);
        drawables[4] = this.getResources().getDrawable(R.drawable.ic_fund_transfer);
        drawables[5] = this.getResources().getDrawable(R.drawable.ic_account_info);

        colors[0] = ContextCompat.getColor(TCashHomeActivity.this, R.color.color_white);
        colors[1] =ContextCompat.getColor(TCashHomeActivity.this, R.color.color_white);
        colors[2] = ContextCompat.getColor(TCashHomeActivity.this, R.color.color_white);
        colors[3] =  ContextCompat.getColor(TCashHomeActivity.this, R.color.color_white);
        colors[4] =  ContextCompat.getColor(TCashHomeActivity.this, R.color.color_white);
        colors[5] =  ContextCompat.getColor(TCashHomeActivity.this, R.color.color_white);
        btnGridView = (GridView)findViewById(R.id.btn_gridview);

        btnGridView.setAdapter(new ButtonAdapter(TCashHomeActivity.this,filesnames,this,drawables,colors));

        //To use Navigation View
        imgvMainMenu = (ImageView)findViewById(R.id.imgv_main_menu) ;
        btnShowBlance = (Button)findViewById(R.id.btn_show_blance);
        txvShowBalance = (TextView) findViewById(R.id.txv_balance);
        userNumber = getValueFromSharedPreferences("user_number",TCashHomeActivity.this);
        pinNumber = getValueFromSharedPreferences("pin_number",TCashHomeActivity.this);



        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        imgvMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  drawer.openDrawer(Gravity.LEFT);
                if (drawer.isDrawerOpen(Gravity.LEFT)) {
                    drawer.closeDrawer(Gravity.LEFT);
                } else {
                    drawer.openDrawer(Gravity.LEFT);
                }

            }
        });

        btnShowBlance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(pinNumber!=null && userNumber!=null){
                        BlanceTask blanceTask = new BlanceTask("TRUSTMM BAL "+pinNumber,userNumber);
                        blanceTask.execute();
                    }
            }
        });

    }
    @Override
    public void getGridButtonPosition(int position) {
        if(position==0){
            Intent intent = new Intent(TCashHomeActivity.this,P2PActivity.class);
            startActivity(intent);
            // ActivityCompat.finishAffinity(LoginActivity.this);
        }
////      else if(position==1){
////            Intent intent = new Intent(LoginActivity.this,SMSDashboard.class);
////            startActivity(intent);
////            // ActivityCompat.finishAffinity(LoginActivity.this);
////        }else if(position==2){
////            Intent intent = new Intent(LoginActivity.this,VoiceDashboard.class);
////            startActivity(intent);
////        }
//        else if(position==3){
//
//            Intent intent = new Intent(LoginActivity.this,SettingsActivity.class);
//            startActivity(intent);
//            //  ActivityCompat.finishAffinity(LoginActivity.this);
//        }
//
//        //else if(position==4){
////            Intent intent = new Intent(LoginActivity.this,PeopleActivity.class);
////            startActivity(intent);
////            // ActivityCompat.finishAffinity(LoginActivity.this);
////        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_notification) {

        } else if (id == R.id.nav_settings) {

        }else if (id == R.id.nav_satements) {
                Intent intent = new Intent(TCashHomeActivity.this,StatementActivity.class);
                startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class BlanceTask extends AsyncTask<String, Integer, String> {
        String mMSG;
        String mGSM;
        // private Dialog loadingDialog;
        public BlanceTask (String msg,String gsm){
            mMSG = msg;
            mGSM = gsm;
            p2pAPI = ConstantAPI.p2pAPI + mGSM + "&smstext="+ mMSG + "&telcoid=7&shortCode=03590016201&encKey=122212";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // sendProgress.setVisibility(View.VISIBLE);
            loadingDialog = ProgressDialog.show(TCashHomeActivity.this, "Please wait", "Loading...");
            loadingDialog.setCancelable(true);

        }

        @Override
        protected String doInBackground(String... params) {

            OkHttpClient client = new OkHttpClient();

            try {

                Request request = new Request.Builder()
                        .url(p2pAPI)

                        .build();

                Response response = null;

                //client.setRetryOnConnectionFailure(true);
                response = client.newCall(request).execute();
                if (response.isSuccessful()){
                  // final String result = response.body().string();
                    final String result = "500.00 tk";

                    // Log.d(RESPONSE_LOG,result);


                            //utillDB.saveAllTransaction(sendingNumber,amount,trxID,status);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(result.length()>0){
                                        loadingDialog.dismiss();
                                        txvShowBalance.setText(result);
                                        //openDialog(result);

                                    }

                                }
                            });






                }
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final String result = "500.00";
                        loadingDialog.dismiss();
                        txvShowBalance.setText(result);
                        txvShowBalance.setVisibility(View.VISIBLE);
                        btnShowBlance.setVisibility(View.GONE);
                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                // This method will be executed once the timer is over
                                btnShowBlance.setVisibility(View.VISIBLE);
                                txvShowBalance.setVisibility(View.GONE);

                            }
                        },5000);// set time as per your requirement
                    }
                });

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
          //  loadingDialog.dismiss();

        }


    }
    public void openDialog(String msgId) {
        final Dialog dialog = new Dialog(TCashHomeActivity.this); // Context, this, etc.
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
}
