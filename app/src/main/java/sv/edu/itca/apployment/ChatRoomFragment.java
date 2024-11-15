package sv.edu.itca.apployment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import sv.edu.itca.apployment.adapter.MessagesAdapter;
import sv.edu.itca.apployment.modelos.Message;

public class ChatRoomFragment extends Fragment {

    private static final String ARG_CONVERSATION_ID = "conversationId";
    private static final String ARG_USER_ID = "userId";
    private String conversationId;
    private String userId;
    private String nameUser;

    private RecyclerView recyclerViewMessages;
    private EditText editTextMessage;
    private ImageButton buttonSendMessage;
    private List<Message> messages;
    private MessagesAdapter messagesAdapter;

    public ChatRoomFragment() {
        // Constructor vacío requerido
    }

    public static ChatRoomFragment newInstance(String conversationId, String userId, String nameUser) {
        ChatRoomFragment fragment = new ChatRoomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CONVERSATION_ID, conversationId);
        args.putString(ARG_USER_ID, userId);
        args.putString("nameUser", nameUser);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            conversationId = getArguments().getString(ARG_CONVERSATION_ID);
            userId = getArguments().getString(ARG_USER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_room, container, false);

        recyclerViewMessages = view.findViewById(R.id.recyclerViewMessages);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(getContext()));

        editTextMessage = view.findViewById(R.id.editTextMessage);
        buttonSendMessage = view.findViewById(R.id.buttonSendMessage);

        messages = new ArrayList<>();
        messagesAdapter = new MessagesAdapter(messages);
        recyclerViewMessages.setAdapter(messagesAdapter);
        nameUser = getArguments().getString("nameUser");
        ((TextView) view.findViewById(R.id.chatTitle)).setText(nameUser);

        fetchMessages();

        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        return view;
    }

    private void fetchMessages() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://apployment.online/public/api/conversations/" + conversationId + "/messages";

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                messages.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject messageObj = response.getJSONObject(i);
                        String messageContent = messageObj.getString("message_content");
                        int senderId = messageObj.getInt("sender_id");

                        boolean isSentByCurrentUser = senderId == Integer.parseInt(userId);
                        messages.add(new Message(messageContent, isSentByCurrentUser));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                messagesAdapter.notifyDataSetChanged();
                recyclerViewMessages.scrollToPosition(messages.size() - 1);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getContext(), "No se pudieron cargar los mensajes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage() {
        String messageContent = editTextMessage.getText().toString().trim();
        if (messageContent.isEmpty()) {
            Toast.makeText(getContext(), "Escribe un mensaje", Toast.LENGTH_SHORT).show();
            return;
        }

        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://apployment.online/public/api/conversations/" + conversationId + "/messages";
        JSONObject params = new JSONObject();

        try {
            params.put("sender_id", userId);
            params.put("message_content", messageContent);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        client.post(getContext(), url, new StringEntity(params.toString(), "UTF-8"), "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                messages.add(new Message(messageContent, true));
                messagesAdapter.notifyDataSetChanged();
                recyclerViewMessages.scrollToPosition(messages.size() - 1);
                editTextMessage.setText("");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getContext(), "No se pudo enviar el mensaje", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
