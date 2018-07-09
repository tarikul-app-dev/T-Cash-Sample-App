package planet.it.limited.tcashapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import planet.it.limited.tcashapp.R;

import static planet.it.limited.tcashapp.utill.SaveValueSharedPreference.getValueFromSharedPreferences;
import static planet.it.limited.tcashapp.utill.SaveValueSharedPreference.saveToSharedPreferences;

public class LoginActivity extends AppCompatActivity {
    ImageView imgvGoHome;
    EditText edtMobileNo,edtPass;
    Button btnForgetPin,btnVoiceSound;
    String sUserNumber = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }

    public void initViews(){
        imgvGoHome = (ImageView) findViewById(R.id.imgv_go_home);
        edtMobileNo = (EditText)findViewById(R.id.edt_mobile_no);
        edtPass = (EditText)findViewById(R.id.edt_password);
        btnForgetPin = (Button)findViewById(R.id.btn_forget_pin);
        btnVoiceSound = (Button)findViewById(R.id.btn_voice__sound);
        btnForgetPin.setTransformationMethod(null);
        btnVoiceSound.setTransformationMethod(null);

        if(sUserNumber!=null){
            sUserNumber = getValueFromSharedPreferences("user_number",LoginActivity.this);
            edtMobileNo.setText(sUserNumber);

        }


        imgvGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userNumber = edtMobileNo.getText().toString();
                String pinNumber = edtPass.getText().toString();
                saveToSharedPreferences("user_number",userNumber,LoginActivity.this);
                saveToSharedPreferences("pin_number",pinNumber,LoginActivity.this);
                sUserNumber = getValueFromSharedPreferences("user_number",LoginActivity.this);
                String sPinNumber = getValueFromSharedPreferences("pin_number",LoginActivity.this);


                if(userNumber.length()>0&&pinNumber.length()>0){
                    if(sUserNumber!=null && sPinNumber!=null){
                        if(sUserNumber.equals(userNumber) && sPinNumber.equals(pinNumber)){
                            Intent intent = new Intent(LoginActivity.this,TCashHomeActivity.class);
                            startActivity(intent);
                        }
                    }else {
                        Intent intent = new Intent(LoginActivity.this,TCashHomeActivity.class);
                        startActivity(intent);
                    }


                }else {
                    Toast.makeText(LoginActivity.this,"Give Your Number and Pin Number",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
