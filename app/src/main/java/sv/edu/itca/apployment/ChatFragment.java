package sv.edu.itca.apployment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import cz.msebera.android.httpclient.Header;
import sv.edu.itca.apployment.adapter.ConversationsAdapter;

public class ChatFragment extends Fragment implements ConversationsAdapter.OnConversationClickListener {

    private RecyclerView recyclerViewConversations;
    private List<String> conversationIds;
    private List<String> workerNames;
    private List<String> lastMessages;
    private ConversationsAdapter adapter;
    private String userId;

    public ChatFragment() {
        // Constructor vacío requerido
    }

    public static ChatFragment newInstance(String userId) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString("userId", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString("userId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerViewConversations = view.findViewById(R.id.recyclerViewConversations);
        recyclerViewConversations.setLayoutManager(new LinearLayoutManager(getContext()));

        conversationIds = new ArrayList<>();
        workerNames = new ArrayList<>();
        lastMessages = new ArrayList<>();
        adapter = new ConversationsAdapter(conversationIds, workerNames, lastMessages, this);
        recyclerViewConversations.setAdapter(adapter);

        fetchConversations();

        return view;
    }

    private void fetchConversations() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://apployment.online/public/api/conversations/" + userId;

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                conversationIds.clear();
                workerNames.clear();
                lastMessages.clear();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject conversation = response.getJSONObject(i);
                        String conversationId = conversation.getString("id");
                        String workerName = conversation.getString("other_user_name");
                        String lastMessage = conversation.optString("last_message", "No messages yet");

                        conversationIds.add(conversationId);
                        workerNames.add(workerName);
                        lastMessages.add(lastMessage);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getContext(), "No se pudieron cargar las conversaciones", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onConversationClick(int position) {
        String conversationId = conversationIds.get(position);
        String nameUser = workerNames.get(position);
        ChatRoomFragment chatDetailFragment = ChatRoomFragment.newInstance(conversationId, userId, nameUser);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, chatDetailFragment)
                .addToBackStack(null)
                .commit();
    }
}
