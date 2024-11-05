package sv.edu.itca.apployment.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sv.edu.itca.apployment.R;

public class SkillAdapter extends RecyclerView.Adapter<SkillAdapter.SkillViewHolder> {

    private List<String> skillList;

    public SkillAdapter(List<String> skillList) {
        this.skillList = skillList;
    }

    @NonNull
    @Override
    public SkillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_skill, parent, false);
        return new SkillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SkillViewHolder holder, int position) {
        String skill = skillList.get(position);
        holder.skillNameTextView.setText(skill);
    }

    @Override
    public int getItemCount() {
        return skillList.size();
    }

    static class SkillViewHolder extends RecyclerView.ViewHolder {
        TextView skillNameTextView;

        SkillViewHolder(View itemView) {
            super(itemView);
            skillNameTextView = itemView.findViewById(R.id.skill_name);
        }
    }
}
