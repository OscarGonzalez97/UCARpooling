package com.example.ucarpooling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ucarpooling.Chat.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {

    private static final String tNombre = "nombre";
    private static final String tApellido = "apellido";
    private static final String tCorreo = "correo";
    private static final String tInsititucion = "institucion";

    EditText nombre, apellido, correo, institucion, password;

    int registro_exitoso = 0;
    Button end_registro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
    }

    // GUARDAMOS TODOS LOS DATOS DE LA PERSONA QUE SE REGISTRA.
    public void boton_guardaDatos(View v) {

        final FirebaseFirestore db = FirebaseFirestore.getInstance();//instancia FirebaseFirestore
        final FirebaseAuth myFirebaseAuth;

        nombre = findViewById(R.id.nombre_id);
        apellido = findViewById(R.id.apellido_id);
        correo = findViewById(R.id.correo_id);
        institucion = findViewById(R.id.institucion_id);
        password = findViewById(R.id.password_id);
        end_registro = findViewById(R.id.boton_aceptar_registro);

        myFirebaseAuth = FirebaseAuth.getInstance();//instancia FirebaseAuth
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        if (validar_reg() && validar_persona()) {
            final String correo_usuario = correo.getText().toString().trim();
            final String pass_usuario = password.getText().toString().trim();
            final String nom_usuario = nombre.getText().toString().trim();
            final String ape_usuario = apellido.getText().toString().trim();
            final String inst_usuario = institucion.getText().toString().trim();

            // ACA GUARDAMOS CORREO Y CONTRASEÑA DE PERSONA

            myFirebaseAuth.createUserWithEmailAndPassword(correo_usuario, pass_usuario).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegistroActivity.this, "REGISTRO EXITOSO!", Toast.LENGTH_SHORT).show();
                        // Create a new user
                        Map<String, Object> user = new HashMap<>();
                        user.put(tNombre, nom_usuario);
                        user.put(tApellido, ape_usuario);
                        user.put(tCorreo, correo_usuario.toLowerCase());
                        user.put(tInsititucion, inst_usuario);
                        user.put("rol", "");
                        user.put("cantidad_viajes", 0);
                        user.put("puntuacion_total", 0);
                        registro_exitoso = 1;
                        Usuario usuario = new Usuario ();
                        usuario.setCorreo(correo_usuario);
                        usuario.setNombre(nom_usuario+" "+ape_usuario);

                        FirebaseUser currentUser = myFirebaseAuth.getCurrentUser();
                        DatabaseReference reference = database.getReference("Usuarios/"+currentUser.getUid());
                        reference.setValue(usuario);


                        //guardamos un documento con los datos del usuario donde la ruta es usuarios/nombre+correo
                        db.collection("usuarios").document(correo_usuario.toLowerCase()).set(user);
                    } else {
                        Toast.makeText(RegistroActivity.this, "EL REGISTRO HA FALLADO!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void boton_ir_principal(View view) {
        this.finish();
    }

    public void boton_ir_agregarAuto(View view) {
        correo = findViewById(R.id.correo_id);
        nombre = findViewById(R.id.nombre_id);
        AgregarAutoActivity.setEmail(correo.getText().toString().trim());
        AgregarAutoActivity.setExiste_user(registro_exitoso);
        startActivity(new Intent(this, AgregarAutoActivity.class));
    }

    private boolean validar_reg() {
        Boolean resultado = false;

        String nom = nombre.getText().toString();
        String ape = apellido.getText().toString();
        String mail = correo.getText().toString();
        String inst = institucion.getText().toString();
        String pwd = password.getText().toString();

        if (nom.isEmpty() || ape.isEmpty() || mail.isEmpty() || inst.isEmpty() || pwd.isEmpty()) {
            Toast.makeText(RegistroActivity.this, "RELLENE TODOS LOS CAMPOS!", Toast.LENGTH_SHORT).show();
        } else {
            resultado = true;
        }

        return resultado;
    }

    public boolean validar_persona(){
        Boolean t = true;
        return t;
    }
}
