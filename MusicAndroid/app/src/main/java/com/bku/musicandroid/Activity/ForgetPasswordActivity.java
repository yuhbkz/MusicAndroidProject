package com.bku.musicandroid.Activity;

import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bku.musicandroid.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.sdsmdg.tastytoast.TastyToast;


/**
 * Created by Administrator on 3/21/2018.
 */

public class ForgetPasswordActivity extends Activity {
    private EditText emailTxt;
    private Button forgetBtn;
    private TextView resultTxt;
    private String emailAddress;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.activity_forget_password);
        emailTxt=findViewById(R.id.email_txt);
        forgetBtn=findViewById(R.id.forget_btn);
        resultTxt=findViewById(R.id.result_txt);
        auth = FirebaseAuth.getInstance();
        Log.d("1abc","forget pass: "+emailAddress);
        forgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailAddress = emailTxt.getText().toString().trim();
                if(TextUtils.isEmpty(emailAddress)) {
                    TastyToast.makeText(getApplicationContext(), "Please type your Email!", TastyToast.LENGTH_SHORT, TastyToast.WARNING).show();
                }
                else
                forget_password();
            }
        });

    }
    protected void forget_password(){
                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    resultTxt.setText("An verify mail was sent to your email. Please check that mail to reset password.");
                                }
                                else{
                                    resultTxt.setText("Error occurs. Please make sure type your email correctly.");
                                }
                            }
                        });
    }
}
