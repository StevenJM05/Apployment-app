package sv.edu.itca.apployment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import sv.edu.itca.apployment.R;

public class WorkersAdapter extends RecyclerView.Adapter<WorkersAdapter.WorkerViewHolder> {
    private List<String> workersList;
    private List<String> workersIds;  // Cambiado a List<Integer> para que coincida con la clase
    private Context context;
    private OnWorkerClickListener listener;

    // Interfaz para manejar los clics en los elementos
    public interface OnWorkerClickListener {
        void onWorkerClick(int workerId);
    }

    public WorkersAdapter(List<String> workersList, List<String> workersIds, Context context, OnWorkerClickListener listener) {
        this.workersList = workersList;
        this.workersIds = workersIds;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WorkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_worker, parent, false);
        return new WorkerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkerViewHolder holder, int position) {
        String workerName = workersList.get(position);
        holder.textViewWorkerName.setText(workerName);

        // Configura el clic en el elemento
        holder.itemView.setOnClickListener(v -> {
            int workerId = Integer.parseInt(workersIds.get(position));  // Obtén el ID del trabajador
            listener.onWorkerClick(workerId);  // Llama al método de la interfaz
        });
    }

    @Override
    public int getItemCount() {
        return workersList.size();
    }

    public static class WorkerViewHolder extends RecyclerView.ViewHolder {
        TextView textViewWorkerName;

        public WorkerViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewWorkerName = itemView.findViewById(R.id.textViewWorkerName);
        }
    }
}
