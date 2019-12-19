package com.example.ucarpooling.Chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ucarpooling.R;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdapterMensaje extends RecyclerView.Adapter<HolderMensaje> {

    private List<LMensaje> listMensaje = new ArrayList<>();
    private Context c;

    public AdapterMensaje(Context c) {
        this.c = c;
    }

    public int addMensaje(LMensaje lMensaje){
        listMensaje.add(lMensaje);
        int position = listMensaje.size()-1;
        notifyItemInserted(listMensaje.size());
        return position;
    }

    public void actualizarMensaje(int position, LMensaje lMensaje) {
        listMensaje.set(position, lMensaje);
        notifyItemChanged(position);
    }

    @NonNull
    @Override
    public HolderMensaje onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1){
            view = LayoutInflater.from(c).inflate(R.layout.card_view_mensaje_emisor, parent, false);
        }else{
            view = LayoutInflater.from(c).inflate(R.layout.card_view_mensaje_receptor, parent, false);
        }
        return new HolderMensaje(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderMensaje holder, int position) {
        LMensaje lMensaje = listMensaje.get(position);
        LUsuario lUsuario = lMensaje.getlUsuario();

        if (lUsuario != null) {
            holder.getNombrePersonaMensaje().setText(lUsuario.getUsuario().getNombre());
        }
        holder.getMensaje().setText(lMensaje.getMensaje().getMensaje());
        holder.getHoraMensaje().setText(lMensaje.fechaDeCreacionDelMensaje());
    }

    @Override
    public int getItemCount() {
        return listMensaje.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(listMensaje.get(position).getlUsuario() != null) {
            if(listMensaje.get(position).getlUsuario().getKey().equals(UsuarioDAO.getInstancia().getKeyUsuario())){
                return 1;
            }else{
                return -1;
            }
        }else{
            return -1;
        }
    }

}
