package com.example.ucarpooling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ConductorActivity extends AppCompatActivity {

    EditarPerfilActivity aux = new EditarPerfilActivity();
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    final String correo = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    String cadena = "";
    TextView promConductor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conductor);
        promConductor = findViewById(R.id.promedioConductor);
        aux.setUser(0);
    }

    public void boton_ir_editar_perfil_conductor(View view){
        aux.setUser(1);
        startActivity(new Intent(this, EditarPerfilActivity.class));
    }

    public void boton_cerrar_sesion(View view){
        Map<String, Object> estado = new HashMap<>();
        estado.put("rol", "");
        db.collection("usuarios").document(correo.toLowerCase()).update(estado);
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    public void boton_ir_crear_viaje(View view){
        db.collection("usuarios").document(correo).collection("vehiculo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                cadena = document.get("matricula").toString();
                            }
                        }

                        if (cadena.isEmpty()){
                            Toast.makeText(ConductorActivity.this, "USUARIO NO TIENE AUTO!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(ConductorActivity.this, "USUARIO VALIDO!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ConductorActivity.this, ViajeActivity.class));
                        }
                    }
                });
    }
}
