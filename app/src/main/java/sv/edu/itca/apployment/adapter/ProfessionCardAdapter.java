package sv.edu.itca.apployment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sv.edu.itca.apployment.R;

public class ProfessionCardAdapter extends RecyclerView.Adapter<ProfessionCardAdapter.ProfessionViewHolder> {

    private List<String> professionList;
    private List<String> workersIds;
    private List<String> professions;
    private List<String> cities;
    private Context context;
    private OnWorkerClickListener clickListener;

    public interface OnWorkerClickListener {
        void onWorkerClick(String workerId);
    }

    public ProfessionCardAdapter(List<String> professionList, List<String> workersIds, List<String> professions, List<String> cities, Context context, OnWorkerClickListener clickListener) {
        this.professionList = professionList;
        this.workersIds = workersIds;
        this.professions = professions;
        this.cities = cities;
        this.context = context;
        this.clickListener = clickListener;
    }

    @Override
    public ProfessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.worker_item, parent, false);
        return new ProfessionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProfessionViewHolder holder, int position) {
        holder.fullNameText.setText(professionList.get(position));
        holder.professionText.setText(professions.get(position));
        holder.cityText.setText(cities.get(position));

        holder.itemView.setOnClickListener(v -> {
            String workerId = workersIds.get(position);
            clickListener.onWorkerClick(workerId);
        });
    }

    @Override
    public int getItemCount() {
        return professionList.size();
    }

    public static class ProfessionViewHolder extends RecyclerView.ViewHolder {
        private TextView fullNameText;
        private TextView professionText;
        private TextView cityText;

        public ProfessionViewHolder(View itemView) {
            super(itemView);
            fullNameText = itemView.findViewById(R.id.textViewWorkerName);
            professionText = itemView.findViewById(R.id.textViewProfession);
            cityText = itemView.findViewById(R.id.textViewCity);
        }
    }
}
