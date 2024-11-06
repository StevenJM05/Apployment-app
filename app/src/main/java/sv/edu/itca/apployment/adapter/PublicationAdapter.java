package sv.edu.itca.apployment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sv.edu.itca.apployment.R;

public class PublicationAdapter extends RecyclerView.Adapter<PublicationAdapter.PublicationViewHolder> {

    private Context context;
    private List<String> publicationList;
    private List<String> publicationIdList;
    private List<String> titulosList;
    private List<String> descripcionesList;
    private List<String> fechasList;
    private List<String> usersnamesList;
    private OnPublicationClickListener listener;

    // Interfaz para manejar los clics
    public interface OnPublicationClickListener {
        void onPublicationClick(int publicationId);
    }

    // Constructor que recibe todas las listas necesarias
    public PublicationAdapter(Context context, List<String> publicationList, List<String> publicationIdList, List<String> titulosList, List<String> descripcionesList, List<String> fechasList, List<String> usersnamesList, OnPublicationClickListener listener) {
        this.context = context;
        this.publicationList = publicationList;
        this.publicationIdList = publicationIdList;
        this.titulosList = titulosList;
        this.descripcionesList = descripcionesList;
        this.fechasList = fechasList;
        this.usersnamesList = usersnamesList;
        this.listener = listener;  // Asegúrate de pasar la interfaz del fragmento aquí
    }

    @Override
    public PublicationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Infla el layout para cada ítem de publicación
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new PublicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PublicationViewHolder holder, int position) {
        // Enlaza la publicación correspondiente a la posición actual
        String publications = publicationList.get(position);
        String publicationsId = publicationIdList.get(position);
        String titulos = titulosList.get(position);
        String descripciones = descripcionesList.get(position);
        String fechaslist = fechasList.get(position);
        String usuarioname = usersnamesList.get(position);

        holder.textViewname.setText(usuarioname);
        holder.tvdescription.setText(descripciones);
        holder.tvdescriptiontittle.setText(titulos);
        holder.tvFechas.setText(fechaslist);

        // Configura el clic en el elemento
        holder.itemView.setOnClickListener(v -> {
            int publicationId = Integer.parseInt(publicationIdList.get(position));  // Obtén el ID de la publicación
            listener.onPublicationClick(publicationId);  // Llama al método de la interfaz
        });
    }

    @Override
    public int getItemCount() {
        return publicationList.size();
    }

    public static class PublicationViewHolder extends RecyclerView.ViewHolder {
        private TextView tvdescription;
        private TextView tvdescriptiontittle;
        private TextView textViewname;
        private TextView tvFechas;

        public PublicationViewHolder(View itemView) {
            super(itemView);
            // Inicializa los TextViews
            textViewname = itemView.findViewById(R.id.tvusename);
            tvdescription = itemView.findViewById(R.id.tvdescription);
            tvdescriptiontittle = itemView.findViewById(R.id.tvdescriptiontittle);
            tvFechas = itemView.findViewById(R.id.tvfecha);
        }
    }
}
