package com.example.ucarpooling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.Map;

public class ViajeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private double lat = 0.0;
    private double lon = 0.0;
    TextView origen_lat, origen_lon, dest_lat, dest_lon;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    Button comenzar_viaje;
    double aux = 0.0;
    int cantidad_pasajeros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viaje);

        Spinner spinner = findViewById(R.id.id_spinner_pasajeros);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.numeros, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        comenzar_viaje = findViewById(R.id.boton_crear_viaje);

        origen_lat = findViewById(R.id.origen_msg);
        origen_lon = findViewById(R.id.origen_msg2);
        dest_lat = findViewById(R.id.destino_msg);
        dest_lon = findViewById(R.id.destino_msg2);
        origen_lat.setText("");
        origen_lon.setText("");
        dest_lat.setText("");
        dest_lon.setText("");

        db.collection("viajes_temp").whereEqualTo("correo", userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                lat = (Double) document.get("origen_lat");
                                lon = (Double) document.get("origen_lon");
                                String cadena = ""+lat;
                                origen_lat.setText(cadena);
                                String cadena2 = ""+lon;
                                origen_lon.setText(cadena2);

                                lat = (Double) document.get("destino_lat");
                                lon = (Double) document.get("destino_lon");
                                cadena = ""+lat;
                                dest_lat.setText(cadena);
                                cadena2 = ""+lon;
                                dest_lon.setText(cadena2);

                            }
                        } else {
                            Toast.makeText(ViajeActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void boton_ir_conductor(View view) {
        Map<String, Object> documento = new HashMap<>();
        documento.put("correo", "");
        documento.put("origen_lat", aux);
        documento.put("origen_lon", aux);
        documento.put("destino_lat", aux);
        documento.put("destino_lon", aux);
        db.collection("viajes_temp").document(userEmail.toLowerCase()).set(documento);
        this.finish();
    }

    public void boton_origen_ir_maps(View view){
        MapsActivity.setChoice(1);
        if (isServicesOK()){
            startActivity(new Intent(this, MapsActivity.class));
            finish();
        }
    }

    public void boton_destino_ir_maps(View view){
        MapsActivity.setChoice(2);
        if (isServicesOK()){
            startActivity(new Intent(this, MapsActivity.class));
            finish();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String number = parent.getItemAtPosition(position).toString();
        cantidad_pasajeros = Integer.parseInt(number);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void boton_comienzo_viaje(View view){
        if (origen_lat.getText().toString().isEmpty() || dest_lat.getText().toString().isEmpty()) {
            Toast.makeText(ViajeActivity.this, "PRIMERO SELECCIONE UN ORIGEN o DESTINO!", Toast.LENGTH_SHORT).show();
        }
        else{
            Map<String, Object> viaje = new HashMap<>();
            viaje.put("correo", userEmail);

            if (origen_lat.getText().toString().contains("-25.324493")){
                viaje.put("origen_lat", -25.324493);
                viaje.put("origen_lon", -57.635437);
                viaje.put("destino_lat", Double.parseDouble(dest_lat.getText().toString()));
                viaje.put("destino_lon", Double.parseDouble(dest_lon.getText().toString()));
            }
            else if (dest_lat.getText().toString().contains("-25.324493")){
                viaje.put("origen_lat", Double.parseDouble(origen_lat.getText().toString()));
                viaje.put("origen_lon", Double.parseDouble(origen_lon.getText().toString()));
                viaje.put("destino_lat", -25.324493);
                viaje.put("destino_lon", -57.635437);
            }

            viaje.put("cantidad pasajeros", 4);
            db.collection("viaje").document(userEmail.toLowerCase()).set(viaje);

            Map<String, Object> viaje_espera = new HashMap<>();
            viaje_espera.put("Conductor", userEmail);
            viaje_espera.put("pasajero1", "");
            viaje_espera.put("pasajero2", "");
            viaje_espera.put("pasajero3", "");
            viaje_espera.put("pasajero4", "");

            db.collection("sala_espera").document(userEmail).set(viaje_espera);

            startActivity(new Intent(ViajeActivity.this, EsperaViajeActivity.class));
            finish();
        }
    }

    public boolean isServicesOK(){
        String TAG="Parte de Oscar";
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(ViajeActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(ViajeActivity.this, available, 9001);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}

