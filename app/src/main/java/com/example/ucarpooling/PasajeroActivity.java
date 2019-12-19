package com.example.ucarpooling;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class PasajeroActivity extends AppCompatActivity {

    EditarPerfilActivity aux = new EditarPerfilActivity();
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    final String correo = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    TextView promPasajero;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasajero);
        promPasajero = findViewById(R.id.promedioPasajero);

        aux.setUser(0);
    }

    public void boton_ir_editar_perfil_pasajero(View view){
        aux.setUser(2);
        startActivity(new Intent(this, EditarPerfilActivity.class));
    }

    public void boton_cerrar_sesion(View view){
        Map<String, Object> estado = new HashMap<>();
        estado.put("rol", "");
        db.collection("usuarios").document(correo.toLowerCase()).update(estado);
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    public void buscar_viajes (View view){
        startActivity(new Intent(PasajeroActivity.this, PasajeroOrigenDestinoActivity.class));
    }
}
