package com.example.ucarpooling;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class DialogActivity extends AppCompatDialogFragment {

    private EditText editTextCorreo;

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_dialog, null);
        editTextCorreo = view.findViewById(R.id.enviar_correo);

        builder.setView(view)
                .setTitle("Envio de contraseña")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String correo = editTextCorreo.getText().toString().trim();

                        if (correo.isEmpty()){
                            Toast.makeText(getContext(), "Ingrese correo primero!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            final FirebaseAuth myFirebaseAuth = FirebaseAuth.getInstance();
                            final FirebaseFirestore db = FirebaseFirestore.getInstance();

                            myFirebaseAuth.sendPasswordResetEmail(correo)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(getContext(), "Se ha enviado la contraseña con exito!", Toast.LENGTH_LONG).show();
                                            }
                                            else{
                                                Toast.makeText(getContext(), "Algo ha fallado. Intente de nuevo!", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                    }
                });

        return builder.create();
    }
}
