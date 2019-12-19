package com.example.ucarpooling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ucarpooling.Chat.ChatMejoradoActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class EsperaViajeActivity extends AppCompatActivity {

    TextView pas1, pas2, pas3, pas4;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    final String mail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espera_viaje);

        pas1 = findViewById(R.id.pasajero1);
        pas2 = findViewById(R.id.pasajero2);
        pas3 = findViewById(R.id.pasajero3);
        pas4 = findViewById(R.id.pasajero4);

        ejecutarActualizacion();

    }

    public void boton_ir_chat(View view){
        startActivity(new Intent(EsperaViajeActivity.this, ChatMejoradoActivity.class));
    }

    public void traer_pasajeros(){
        db.collection("sala_espera").whereEqualTo("Conductor", mail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String aux = document.get("pasajero1").toString();
                                if (aux.isEmpty()) {}
                                else{
                                    pas1.setText(document.get("pasajero1").toString());

                                    if (document.get("pasajero2").toString().isEmpty()) {}
                                    else{
                                        pas2.setText(document.get("pasajero2").toString());

                                        if (document.get("pasajero3").toString().isEmpty()) {}
                                        else{
                                            pas3.setText(document.get("pasajero3").toString());

                                            if (document.get("pasajero4").toString().isEmpty()) {}
                                            else{
                                                pas4.setText(document.get("pasajero4").toString());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
    }

    private void ejecutarActualizacion(){
        final Handler handler= new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                metodoEjecutar();//llamamos nuestro metodo
                handler.postDelayed(this,3000);//se ejecutara cada 10 segundos
            }
        },1000);//empezara a ejecutarse después de 5 milisegundos
    }

    private void metodoEjecutar() {
        traer_pasajeros();
    }

    public void cancelar_viaje(View view) {
        finish();
    }

    public void ir_a_ruta(View view) {
        startActivity(new Intent(EsperaViajeActivity.this, RutaActivity.class));
        finish();
    }
}
