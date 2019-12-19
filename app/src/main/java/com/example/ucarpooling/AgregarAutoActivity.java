package com.example.ucarpooling;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;

public class AgregarAutoActivity extends AppCompatActivity {

    private static String email;
    private static int existe_user;

    EditText matricula, marca, modelo, color;
    Button end_auto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_auto);
    }

    // GUARDAMOS TODOS LOS DATOS DE LA PERSONA QUE SE REGISTRA.
    public void boton_guardaVehiculo(View v) {

            matricula = findViewById(R.id.matricula_id);
            marca = findViewById(R.id.marca_id);
            modelo = findViewById(R.id.modelo_id);
            color = findViewById(R.id.color_id);
            end_auto = findViewById(R.id.boton_finalizar_auto);

            FirebaseFirestore db = FirebaseFirestore.getInstance(); //instancia FirebaseFirestore

            if (validar_reg()) {

                String mat_conductor = matricula.getText().toString().trim().toUpperCase();
                String marca_conductor = marca.getText().toString().trim();
                String model_conductor = modelo.getText().toString().trim();
                String color_conductor = color.getText().toString().trim();

                // Crea auto
                Map<String, Object> vehiculo = new HashMap<>();
                vehiculo.put("matricula", mat_conductor);
                vehiculo.put("marca", marca_conductor);
                vehiculo.put("modelo", model_conductor);
                vehiculo.put("color", color_conductor);

                //la ruta del vehiculo de la persona sera una coleccion del documento de su usuario
                db.collection("usuarios").document(email.toLowerCase()).collection("vehiculo").document("auto").set(vehiculo);
            }
    }

    public static void setEmail(String correo) {
        email = correo;
    }

    public static void setExiste_user(Integer existe_usuario) {
        existe_user = existe_usuario;
    }

    public void boton_ir_registro(View view) {
        this.finish();
    }

    private boolean validar_reg() {
        Boolean resultado = false;

        String mat = matricula.getText().toString();
        String marc = marca.getText().toString();
        String model = modelo.getText().toString();
        String col = color.getText().toString();

        if (mat.isEmpty() || marc.isEmpty() || model.isEmpty() || col.isEmpty()) {
            Toast.makeText(AgregarAutoActivity.this, "EXISTEN CAMPOS VACIOS, RELLENE TODOS!", Toast.LENGTH_SHORT).show();
        } else if(existe_user == 0) {
            Toast.makeText(AgregarAutoActivity.this, "CREE UN USUARIO PRIMERO!", Toast.LENGTH_SHORT).show();
        } else {
            resultado = true;
            Toast.makeText(AgregarAutoActivity.this, "AUTO REGISTRADO CON EXITO!", Toast.LENGTH_SHORT).show();
        }

        return resultado;
    }
}
