package sv.edu.itca.apployment.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class WorkersAdapter extends RecyclerView.Adapter<WorkersAdapter.WorkerViewHolder> {

    private List<String> workersList;

    public WorkersAdapter(List<String> workersList) {
        this.workersList = workersList;
    }

    @Override
    public WorkerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new WorkerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WorkerViewHolder holder, int position) {
        holder.bind(workersList.get(position));
    }

    @Override
    public int getItemCount() {
        return workersList.size();
    }

    public static class WorkerViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public WorkerViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }

        public void bind(String workerName) {
            textView.setText(workerName);
        }
    }
}
