package com.example.ucarpooling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class EditarAutoActivity extends AppCompatActivity {

    private static final String TAG = "Editar auto";
    private EditText matricula, marca, modelo, color;
    private String idPersona;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_auto);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //asignamos los id de los editText a los que creamos en esta clase y ahi debemos asignar todoo lo que se trae de la base de datos
        matricula=findViewById(R.id.editMatricula_id);
        marca=findViewById(R.id.editMarca_id);
        modelo=findViewById(R.id.editModelo_id);
        color=findViewById(R.id.editColor_id);

        //traemos el correo del authenticator, para traer el email y luego usar en la query
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = null;
        if (user != null) {
            userEmail = user.getEmail();
        } else {
            // No user is signed in
            userEmail=null;
        }

        //buscamos el id de la persona para poner en la ruta y luego buscar el auto
        db.collection("usuarios").whereEqualTo("correo",userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                //ponemos lo que esta en el documento en los EditText
                                idPersona=document.getId();
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        //buscamos el id del auto
        db.collection("usuarios").document(userEmail).collection("vehiculo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                //ponemos lo que esta en el documento en los EditText
                                matricula.setText((String) document.get("matricula")) ;
                                marca.setText((String)document.get("marca"));
                                modelo.setText((String)document.get("modelo"));
                                color.setText((String)document.get("color"));
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void volver_editar_perfil(View view){
        this.finish();
    }

    public void boton_hacer_cambios(View view){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String usuario=user.getEmail();

        String mat_conductor = matricula.getText().toString().trim().toUpperCase();
        String marca_conductor = marca.getText().toString().trim();
        String model_conductor = modelo.getText().toString().trim();
        String color_conductor = color.getText().toString().trim();

        if (validar_reg()){
            // Crea auto
            Map<String, Object> vehiculo = new HashMap<>();
            vehiculo.put("matricula", mat_conductor);
            vehiculo.put("marca", marca_conductor);
            vehiculo.put("modelo", model_conductor);
            vehiculo.put("color", color_conductor);

            //la ruta del vehiculo de la persona sera una coleccion del documento de su usuario
            db.collection("usuarios").document(usuario.toLowerCase()).collection("vehiculo").document("auto").set(vehiculo);
        }
    }

    private boolean validar_reg() {
        Boolean resultado = false;

        String mat = matricula.getText().toString();
        String mar = marca.getText().toString();
        String mod = modelo.getText().toString();
        String col = color.getText().toString();

        if (mat.isEmpty() || mar.isEmpty() || mod.isEmpty() || col.isEmpty()) {
            Toast.makeText(EditarAutoActivity.this, "RELLENE TODOS LOS CAMPOS!", Toast.LENGTH_SHORT).show();
        } else {
            resultado = true;
        }

        return resultado;
    }
}