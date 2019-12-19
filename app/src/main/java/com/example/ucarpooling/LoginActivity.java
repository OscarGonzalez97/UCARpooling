package com.example.ucarpooling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginAuth";
    public static void setLogInAs(int logInAs) {
        LoginActivity.logInAs = logInAs;
    }
    private static int logInAs ;
    private FirebaseAuth mAuth;
    EditText campoMail, campoPass;
    CheckBox checkBox;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        checkBox = findViewById(R.id.mantener_sesion);

        campoMail = findViewById(R.id.correo_id1);
        campoPass = findViewById(R.id.password_id1);

        campoMail.getText().clear();
        campoPass.getText().clear();
    }

    public void boton_ir_principal(View view){
        this.finish();
    }

    public void boton_iniciar_sesion(View v){
        EditText email, password;
        email = findViewById(R.id.correo_id1);
        password = findViewById(R.id.password_id1);
        final String emailString, passwordString;

        emailString = email.getText().toString().trim();
        passwordString = password.getText().toString().trim();

        if (emailString.isEmpty() || passwordString.isEmpty()){
            Toast.makeText(LoginActivity.this, "RELLENE AMBOS CAMPOS!",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            mAuth.signInWithEmailAndPassword(emailString, passwordString)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                //Aqui vamos a la actividad correspondiente
                                Map<String, Object> estado = new HashMap<>();

                                if (logInAs == 1) {
                                    //inicia como pasajero
                                    estado.put("rol", "pasajero");

                                    db.collection("usuarios").document(emailString.toLowerCase())
                                            .update(estado);
                                    startActivity(new Intent(LoginActivity.this,PasajeroActivity.class));
                                }
                                else {
                                    //inicia como conductor
                                    estado.put("rol", "conductor");

                                    db.collection("usuarios").document(emailString.toLowerCase())
                                            .update(estado);
                                    startActivity(new Intent(LoginActivity.this,ConductorActivity.class));
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void openDialog(View view){
        DialogActivity dialogActivity = new DialogActivity();
        dialogActivity.show(getSupportFragmentManager(), "......");
    }
}
