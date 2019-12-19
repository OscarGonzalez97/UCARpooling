package com.example.ucarpooling;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.Map;

public class ConfirmarViajePasajeroDialog extends AppCompatDialogFragment {

    String creador_viaje_mail;
    final String mail = FirebaseAuth.getInstance().getCurrentUser().getEmail(); // PASAJERO ACTUAL
    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface OnInputListener{
        void sendInput(String input);
    }

    public OnInputListener mOnInputListener;

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.confirmar_viaje, null);

        builder.setView(view)
                .setTitle("DESEA SUBIRSE A ESTE VIAJE?")
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getDialog().dismiss();
                    }
                })
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String input = "si";

                        db.collection("sala_espera")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (final QueryDocumentSnapshot document : task.getResult()){
                                                final String correo = document.getId(); // CONDUCTOR
                                                if (correo.contains(creador_viaje_mail)){
                                                    Map<String, Object> pasajero = new HashMap<>();
                                                    String p1 = document.get("pasajero1").toString();
                                                    String p2 = document.get("pasajero2").toString();
                                                    String p3 = document.get("pasajero3").toString();
                                                    String p4 = document.get("pasajero4").toString();
                                                    if (p1.isEmpty()){
                                                        pasajero.put("pasajero1", mail);
                                                        db.collection("sala_espera").document(creador_viaje_mail).update(pasajero);
                                                    }
                                                    else if (p2.isEmpty()){
                                                        if (p1.contains(mail)){}
                                                        else{
                                                            pasajero.put("pasajero2", mail);
                                                            db.collection("sala_espera").document(creador_viaje_mail).update(pasajero);
                                                        }
                                                    }
                                                    else if (p3.isEmpty()){
                                                        if (p1.contains(mail) || p2.contains(mail)){}
                                                        else{
                                                            pasajero.put("pasajero3", mail);
                                                            db.collection("sala_espera").document(creador_viaje_mail).update(pasajero);
                                                        }
                                                    }
                                                    else if (p4.isEmpty()){
                                                        if (p1.contains(mail) || p2.contains(mail) || p3.contains(mail)){}
                                                        else{
                                                            pasajero.put("pasajero4", mail);
                                                            db.collection("sala_espera").document(creador_viaje_mail).update(pasajero);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                });

                        mOnInputListener.sendInput(input);
                        getDialog().dismiss();
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnInputListener = (OnInputListener) getActivity();
        }catch (ClassCastException e) {}
    }
}
