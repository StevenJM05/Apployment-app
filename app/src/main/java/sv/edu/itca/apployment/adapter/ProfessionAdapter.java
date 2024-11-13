package sv.edu.itca.apployment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ProfessionAdapter extends RecyclerView.Adapter<ProfessionAdapter.ProfessionViewHolder> {

    private List<String> professionList;

    public ProfessionAdapter(Context context, List<String> professionList, List<String> publicationIdList, List<String> titulosList, List<String> descripcionesList, List<String> fechasList, List<String> usersnamesList) {
        this.professionList = professionList;
    }

    @Override
    public ProfessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ProfessionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProfessionViewHolder holder, int position) {
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
            textView = itemView.findViewById(android.R.id.text1);
        }

        public void bind(String profession) {
            textView.setText(profession);
        }
    }
}
