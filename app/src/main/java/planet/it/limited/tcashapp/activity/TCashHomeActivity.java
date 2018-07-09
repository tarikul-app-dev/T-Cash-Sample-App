package planet.it.limited.tcashapp.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import android.widget.GridView;
import android.widget.ImageView;

import planet.it.limited.tcashapp.R;
import planet.it.limited.tcashapp.adapter.ButtonAdapter;

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

}
