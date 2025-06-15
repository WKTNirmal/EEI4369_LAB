package com.s23010459.thilina;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    EditText username, password;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainConstrainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //assign elements
        myDb = new DatabaseHelper(this);
        loginButton = findViewById(R.id.btnLogin);
        username = findViewById(R.id.inputFieldUsername);
        password = findViewById(R.id.inputFieldPassword);

        //add data
        addData();



    }

    //add data to the db
    public void addData(){
        loginButton.setOnClickListener(new View.OnClickListener() {

            // Handle login button click
            @Override
            public void onClick(View v) {
                boolean isInserted = myDb.insertData(username.getText().toString(), password.getText().toString());

                if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    //check if data is inserted
                    if (isInserted == true)
                        Toast.makeText(LoginActivity.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(LoginActivity.this, "Data not Inserted", Toast.LENGTH_SHORT).show();
                }


                //navigate to the next activity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();  //disable the back stack
            }


        });
    }
}
