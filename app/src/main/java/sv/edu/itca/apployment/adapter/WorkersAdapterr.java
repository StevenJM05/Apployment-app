package sv.edu.itca.apployment.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;

public class WorkersAdapterr extends RecyclerView.Adapter<WorkersAdapterr.WorkerViewHolder> {

    private List<String> profesionList; // Lista de objetos JSON

    // Constructor que recibe la lista de objetos JSON
    public WorkersAdapterr(List<String> profesionList) {
        this.profesionList = profesionList;
    }

    @Override
    public WorkerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Infla el layout de cada item (por ejemplo, simple_list_item_2)
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new WorkerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WorkerViewHolder holder, int position) {
        // Obtiene el trabajador actual de la lista de objetos JSON
        holder.bind(profesionList.get(position));

    }
    @Override
    public int getItemCount() {
        return profesionList.size();
    }

    public static class WorkerViewHolder extends RecyclerView.ViewHolder {
        private TextView TextView;


        public WorkerViewHolder(View itemView) {
            super(itemView);
            // Inicializa los TextViews
      TextView = itemView.findViewById(android.R.id.text1);  // El primer TextView en simple_list_item_2
        }

        public void bind( String profession) {
            // Asigna el nombre completo y la profesión a los TextViews

         TextView.setText(profession);
        }
    }
}
