package sv.edu.itca.apployment.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sv.edu.itca.apployment.R;

public class ProfessionCardAdapter extends RecyclerView.Adapter<ProfessionCardAdapter.ProfessionViewHolder> {

    private List<String> professionList; // Lista de profesiones o datos

    // Constructor para recibir la lista de profesiones
    public ProfessionCardAdapter(List<String> professionList) {
        this.professionList = professionList;
    }

    @Override
    public ProfessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflar el layout de cada card
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profession, parent, false);
        return new ProfessionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProfessionViewHolder holder, int position) {
        // Enlazar los datos a las vistas
        holder.bind(professionList.get(position));
    }

    @Override
    public int getItemCount() {
        return professionList.size();
    }

    // ViewHolder para enlazar los elementos del CardView
    public static class ProfessionViewHolder extends RecyclerView.ViewHolder {
        private TextView fullNameText;
        private TextView professionText;

        public ProfessionViewHolder(View itemView) {
            super(itemView);
            // Inicializamos los TextViews
            fullNameText = itemView.findViewById(R.id.fullNameText);
            professionText = itemView.findViewById(R.id.professionText);
        }

        public void bind(String profession) {
            // Asumimos que cada 'profession' es un String con "Nombre Completo - Profesión"
            String[] parts = profession.split(" - ");
            if (parts.length > 1) {
                fullNameText.setText(parts[0]); // Nombre completo
                professionText.setText(parts[1]); // Profesión
            }
        }
    }
}
