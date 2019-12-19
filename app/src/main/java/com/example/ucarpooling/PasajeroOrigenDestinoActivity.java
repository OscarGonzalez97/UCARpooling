package com.example.ucarpooling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PasajeroOrigenDestinoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasajero_origen_destino);
    }

    public void select_ubi_pasajero(View view){

    }

    public void ver_viajes(View view){
        startActivity(new Intent(PasajeroOrigenDestinoActivity.this, VerViajes_OrigenUCAActivity.class));
    }

    public void boton_volver_pasajero(View view){
        finish();
    }
}
