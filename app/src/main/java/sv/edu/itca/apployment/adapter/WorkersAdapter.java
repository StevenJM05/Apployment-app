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
    private Context context;

    public WorkersAdapter(List<String> workersList, Context context) {
        this.workersList = workersList;
        this.context = context;
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
