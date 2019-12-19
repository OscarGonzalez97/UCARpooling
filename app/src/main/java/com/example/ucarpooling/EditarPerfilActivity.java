package com.example.ucarpooling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class EditarPerfilActivity extends AppCompatActivity {

    private EditText nombre, apellido, correo, institucion;
    private int user = 0; // 1 es Conductor, 2 es Pasajero
    private String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //asignamos los id de los editText a los que creamos en esta clase y ahi debemos asignar todoo lo que se trae de la base de datos
        nombre = findViewById(R.id.editNombre_id);
        apellido = findViewById(R.id.editApellido_id);
        correo = findViewById(R.id.editCorreo_id);
        institucion = findViewById(R.id.editInstitucion_id);

        //traemos el correo del authenticator, para traer el email y luego usar en la query
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = null;
        if (user != null) {
            userEmail = user.getEmail();
        } else {
            // No user is signed in
            userEmail = null;
        }

        db.collection("usuarios").whereEqualTo("correo", userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Editar perfil: ", document.getId() + " => " + document.getData());
                                //ponemos lo que esta en el documento en los EditText
                                nombre.setText((String) document.get("nombre"));
                                apellido.setText((String) document.get("apellido"));
                                correo.setText((String) document.get("correo"));
                                institucion.setText((String) document.get("institucion"));
                                idUsuario = document.getId();
                            }
                        } else {
                            Toast.makeText(EditarPerfilActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void boton_editar_perfil(View view) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String correo_usuario = correo.getText().toString().trim();
        String nom_usuario = nombre.getText().toString().trim();
        String ape_usuario = apellido.getText().toString().trim();
        String inst_usuario = institucion.getText().toString().trim();
        final FirebaseAuth myFirebaseAuth = FirebaseAuth.getInstance();

        if (validar_reg()){
            Map<String, Object> user = new HashMap<>();
            user.put("nombre", nom_usuario);
            user.put("apellido", ape_usuario);
            user.put("correo", correo_usuario.toLowerCase());
            user.put("institucion", inst_usuario);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference mDatabaseRef = database.getReference();
            FirebaseUser currentUser = myFirebaseAuth.getCurrentUser();
            mDatabaseRef.child("Usuarios/"+currentUser.getUid()).child("nombre").setValue(nom_usuario+" "+ape_usuario);

            //guardamos un documento con los datos del usuario donde la ruta es usuarios/nombre+correo
            db.collection("usuarios").document(idUsuario).update(user);
        }
    }

    // PASAJERO
    public void boton_ir_editarAuto(View view){
        startActivity(new Intent(this, EditarAutoActivity.class));
    }

    public void volver_usuario(View view) {
        if (getUser() == 1) {
            startActivity(new Intent(this, ConductorActivity.class));
        } else if (getUser() == 2) {
            startActivity(new Intent(this, PasajeroActivity.class));
        } else {
            finish();
        }
    }

    private boolean validar_reg() {
        Boolean resultado = false;

        String nom = nombre.getText().toString();
        String ape = apellido.getText().toString();
        String mail = correo.getText().toString();
        String inst = institucion.getText().toString();

        if (nom.isEmpty() || ape.isEmpty() || mail.isEmpty() || inst.isEmpty()) {
            Toast.makeText(EditarPerfilActivity.this, "RELLENE TODOS LOS CAMPOS!", Toast.LENGTH_SHORT).show();
        } else {
            resultado = true;
        }

        return resultado;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }
}
