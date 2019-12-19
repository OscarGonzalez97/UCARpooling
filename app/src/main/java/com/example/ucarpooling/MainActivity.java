package com.example.ucarpooling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        setContentView(R.layout.activity_main);

        String userEmail = null;
        if (currentUser != null) {
            userEmail = currentUser.getEmail();
            //Toast.makeText(MainActivity.this, userEmail, Toast.LENGTH_SHORT).show();
        } else {
            // No user is signed in
            //Toast.makeText(MainActivity.this, "No hay sesion abierta!", Toast.LENGTH_SHORT).show();
        }
    }

    // CONDUCTOR
    public void boton_ir_login1(View view){
        startActivity(new Intent(this, LoginActivity.class));
        LoginActivity.setLogInAs(2); //si presiono boton de conductor pone el set del conductor
    }

    // PASAJERO
    public void boton_ir_login2(View view){
        startActivity(new Intent(this, LoginActivity.class));
        LoginActivity.setLogInAs(1);//si presiono boton de conductor pone el set del pasajero
    }

    public void boton_ir_registro(View view){
        startActivity(new Intent(this, RegistroActivity.class));
    }
}