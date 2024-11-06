package sv.edu.itca.apployment.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PublicationAdapter extends RecyclerView.Adapter<PublicationAdapter.PublicationViewHolder> {

    private List<String> publicationList;

    // Constructor que recibe la lista de publicaciones
    public PublicationAdapter(List<String> publicationList) {
        this.publicationList = publicationList;
    }

    @Override
    public PublicationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Infla el layout para cada ítem de publicación
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new PublicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PublicationViewHolder holder, int position) {
        // Enlaza la publicación correspondiente a la posición actual
        holder.bind(publicationList.get(position));
    }

    @Override
    public int getItemCount() {
        return publicationList.size();
    }

    public static class PublicationViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public PublicationViewHolder(View itemView) {
            super(itemView);
            // Inicializa los TextViews
            textView = itemView.findViewById(android.R.id.text1);
        }

        public void bind(String publication) {
            textView.setText(publication);
        }
    }
}
