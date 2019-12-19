package com.example.ucarpooling;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ucarpooling.Chat.ChatMejoradoActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class VerViajes_OrigenUCAActivity extends AppCompatActivity implements ConfirmarViajePasajeroDialog.OnInputListener{

    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    final String mail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    QueryDocumentSnapshot documentAux;
    ListView listView1;
    TextView textView;
    ArrayList<String> lista = new ArrayList<>();
    String creador_viaje;
    public String mInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_viajes__origen_uca);
        listView1 = findViewById(R.id.listView);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ConfirmarViajePasajeroDialog confirm = new ConfirmarViajePasajeroDialog();
                confirm.creador_viaje_mail = creador_viaje;
                confirm.show(getSupportFragmentManager(), ".....");
            }
        });
    }

    @Override
    public void sendInput(String input) {
        mInput = input;

        respuesta();
    }

    private void respuesta(){
        if (mInput.contains("si")){
            startActivity(new Intent(VerViajes_OrigenUCAActivity.this, ChatMejoradoActivity.class));
            finish();
        }
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public int getCount() {
            return lista.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.contenido_lista, null);
            textView = view.findViewById(R.id.textoView);
            textView.setText(lista.get(position));

            return view;
        }
    }

    public void ver_viajes(View view) {
        traer_viajes();
        CustomAdapter customAdapter = new CustomAdapter();
        listView1.setAdapter(customAdapter);
    }

    public void traer_viajes (){
        db.collection("viaje")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (final QueryDocumentSnapshot document : task.getResult()){
                                final String correo = document.getId();
                                db.collection("usuarios").whereEqualTo("correo", correo)
                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document2 : task.getResult()) {
                                                documentAux = document;
                                                // ORIGEN: CATOLICA
                                                if (correo.contains(mail)) { }
                                                else{
                                                    if ((Double)(documentAux.get("origen_lat")) == -25.324493){
                                                        String full_name = document2.get("nombre").toString()+" "+document2.get("apellido").toString();
                                                        if (lista.contains(full_name)) {}
                                                        else {
                                                            lista.add(full_name);
                                                            creador_viaje = correo;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
    }
}
