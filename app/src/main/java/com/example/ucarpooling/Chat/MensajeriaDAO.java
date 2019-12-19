package com.example.ucarpooling.Chat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MensajeriaDAO {

    private static MensajeriaDAO mensajeriaDAO;

    private FirebaseDatabase database;
    private DatabaseReference referenceChat;

    public static MensajeriaDAO getInstancia(){
        if(mensajeriaDAO==null) mensajeriaDAO = new MensajeriaDAO();
        return mensajeriaDAO;
    }

    private MensajeriaDAO(){
        database = FirebaseDatabase.getInstance();
        referenceChat = database.getReference("Chatv3");
        //referenceUsuarios = database.getReference("Usuarios");
    }

    public void nuevoMensaje(String keyEmisor, String keyReceptor, Mensaje mensaje) {
        DatabaseReference referenceEmisor = referenceChat.child(keyEmisor).child(keyReceptor);
        DatabaseReference referenceReceptor = referenceChat.child(keyReceptor).child(keyEmisor);
        referenceEmisor.push().setValue(mensaje);
        referenceReceptor.push().setValue(mensaje);
    }
}
