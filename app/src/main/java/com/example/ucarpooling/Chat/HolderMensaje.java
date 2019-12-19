package com.example.ucarpooling.Chat;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ucarpooling.R;

import org.w3c.dom.Text;

public class HolderMensaje extends RecyclerView.ViewHolder {

    private TextView nombrePersonaMensaje;
    private TextView mensaje;
    private TextView horaMensaje;

    public HolderMensaje(View itemView) {
        super(itemView);
        nombrePersonaMensaje = (TextView) itemView.findViewById(R.id.nombrePersonaMensaje);
        mensaje = (TextView) itemView.findViewById(R.id.mensaje);
        horaMensaje = (TextView) itemView.findViewById(R.id.horaMensaje);
    }

    public TextView getNombrePersonaMensaje() {
        return nombrePersonaMensaje;
    }

    public void setNombrePersonaMensaje(TextView nombrePersonaMensaje) {
        this.nombrePersonaMensaje = nombrePersonaMensaje;
    }

    public TextView getMensaje() {
        return mensaje;
    }

    public void setMensaje(TextView mensaje) {
        this.mensaje = mensaje;
    }

    public TextView getHoraMensaje() {
        return horaMensaje;
    }

    public void setHoraMensaje(TextView horaMensaje) {
        this.horaMensaje = horaMensaje;
    }
}
