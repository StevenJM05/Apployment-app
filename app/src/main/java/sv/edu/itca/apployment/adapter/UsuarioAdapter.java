package sv.edu.itca.apployment.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import sv.edu.itca.apployment.R;
import sv.edu.itca.apployment.modelos.Usuario;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder> {

    private List<Usuario> usuariosList;
    private List<Usuario> usuariosListFull;

    public UsuarioAdapter(List<Usuario> usuariosList) {
        this.usuariosList = usuariosList;
        usuariosListFull = new ArrayList<>(usuariosList);  // Hacemos una copia para poder filtrar
    }


    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuario, parent, false);
        return new UsuarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UsuarioViewHolder holder, int position) {
        Usuario usuario = usuariosList.get(position);
        holder.nombre.setText(usuario.getNombre() + " " + usuario.getApellido());
        holder.profesion.setText(usuario.getProfesion());
        holder.habilidad.setText(usuario.getHabilidad());
        holder.ciudad.setText(usuario.getCiudad());
        holder.correo.setText(usuario.getCorreo());
        holder.telefono.setText(usuario.getTelefono());
        // Si quieres cargar la foto, puedes usar Glide o Picasso
        Glide.with(holder.fotoPerfil.getContext()).load(usuario.getFotoPerfil()).into(holder.fotoPerfil);
    }

    @Override
    public int getItemCount() {
        return usuariosList.size();
    }

    public class UsuarioViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, profesion, habilidad, ciudad, correo, telefono;
        ImageView fotoPerfil;

        public UsuarioViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tv_nombre);
            profesion = itemView.findViewById(R.id.tv_profesion);
            habilidad = itemView.findViewById(R.id.tv_habilidad);
            ciudad = itemView.findViewById(R.id.tv_ciudad);
            correo = itemView.findViewById(R.id.tv_correo);
            telefono = itemView.findViewById(R.id.tv_telefono);
            fotoPerfil = itemView.findViewById(R.id.iv_fotoPerfil);
        }
    }

    // Filtrar la lista según la búsqueda
    public void filterList(String query) {
        usuariosList.clear();
        if (query.isEmpty()) {
            usuariosList.addAll(usuariosListFull);
        } else {
            query = query.toLowerCase();
            for (Usuario usuario : usuariosListFull) {
                if (usuario.getNombre().toLowerCase().contains(query) ||
                        usuario.getApellido().toLowerCase().contains(query) ||
                        usuario.getProfesion().toLowerCase().contains(query) ||
                        usuario.getHabilidad().toLowerCase().contains(query) ||
                        usuario.getCiudad().toLowerCase().contains(query) ||
                        usuario.getCorreo().toLowerCase().contains(query) ||
                        usuario.getTelefono().contains(query)) {
                    usuariosList.add(usuario);
                }
            }
        }
        notifyDataSetChanged();
    }
}
