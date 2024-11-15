package sv.edu.itca.apployment.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sv.edu.itca.apployment.R;

public class ConversationsAdapter extends RecyclerView.Adapter<ConversationsAdapter.ConversationViewHolder> {

    private List<String> conversationIds;
    private List<String> workerNames;
    private List<String> lastMessages;
    private OnConversationClickListener listener;

    public interface OnConversationClickListener {
        void onConversationClick(int position);
    }

    public ConversationsAdapter(List<String> conversationIds, List<String> workerNames, List<String> lastMessages, OnConversationClickListener listener) {
        this.conversationIds = conversationIds;
        this.workerNames = workerNames;
        this.lastMessages = lastMessages;
        this.listener = listener;
    }

    @Override
    public ConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation, parent, false);
        return new ConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ConversationViewHolder holder, int position) {
        String workerName = workerNames.get(position);
        String lastMessage = lastMessages.get(position);
        holder.textViewWorkerName.setText(workerName);
        holder.textViewLastMessage.setText(lastMessage);
        holder.itemView.setOnClickListener(v -> listener.onConversationClick(position));
    }

    @Override
    public int getItemCount() {
        return conversationIds.size();
    }

    public class ConversationViewHolder extends RecyclerView.ViewHolder {
        TextView textViewWorkerName, textViewLastMessage;

        public ConversationViewHolder(View itemView) {
            super(itemView);
            textViewWorkerName = itemView.findViewById(R.id.textViewWorkerName);
            textViewLastMessage = itemView.findViewById(R.id.textViewLastMessage);
        }
    }
}
