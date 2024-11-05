package sv.edu.itca.apployment.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ProfessionAdapter extends RecyclerView.Adapter<ProfessionAdapter.ProfessionViewHolder> {

    private List<String> professionList; // Mantiene la lista de Strings

    // Constructor que recibe la lista de Strings
    public ProfessionAdapter(List<String> professionList) {
        this.professionList = professionList;
    }

    @Override
    public ProfessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Infla el layout para cada ítem
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ProfessionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProfessionViewHolder holder, int position) {
        // Enlaza el String correspondiente a la posición actual
        holder.bind(professionList.get(position));
    }

    @Override
    public int getItemCount() {
        return professionList.size();
    }

    public static class ProfessionViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public ProfessionViewHolder(View itemView) {
            super(itemView);
            // Inicializa el TextView
            textView = itemView.findViewById(android.R.id.text1);
        }

        public void bind(String profession) {
            // Establece el texto del TextView
            textView.setText(profession);
        }
    }
}
