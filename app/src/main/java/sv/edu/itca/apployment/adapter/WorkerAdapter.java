package sv.edu.itca.apployment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

import sv.edu.itca.apployment.R;
import sv.edu.itca.apployment.modelos.Worker;

public class WorkerAdapter extends RecyclerView.Adapter<WorkerAdapter.WorkerViewHolder> {
    private List<Worker> workerList;
    private Context context;

    public WorkerAdapter(List<Worker> workerList, Context context) {
        this.workerList = workerList;
        this.context = context;
    }

    @NonNull
    @Override
    public WorkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.worker_item, parent, false);
        return new WorkerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkerViewHolder holder, int position) {
        Worker worker = workerList.get(position);
        holder.txtName.setText(worker.getName());
        holder.txtProfession.setText(worker.getProfession());
        holder.txtCity.setText(worker.getCity());

        // Cargar la imagen usando Glide
        Glide.with(context)
                .load(worker.getPhotoUrl())
                .placeholder(R.drawable.user_avatar)
                .into(holder.imgProfilePhoto);
    }

    @Override
    public int getItemCount() {
        return workerList.size();
    }

    public static class WorkerViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtProfession, txtCity;
        ImageView imgProfilePhoto;

        public WorkerViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtProfession = itemView.findViewById(R.id.txtProfession);
            txtCity = itemView.findViewById(R.id.txtCity);
            imgProfilePhoto = itemView.findViewById(R.id.imgProfilePhoto);
        }
    }
}
