package app.hacela.chamatablebanking.ui.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import app.hacela.chamatablebanking.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupChatActivity extends AppCompatActivity {

    private static final String TAG = "GroupChatActivity";

    private static final String MESSAGE = "message";

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @BindView(R.id.et_message)
    EditText etMessage;
    @BindView(R.id.et_send_message)
    FloatingActionButton etSendMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.et_send_message)
    public void onViewClicked() {
        String message = etMessage.getText().toString().trim();
        Map<String, Object> messageText = new HashMap<>();
        messageText.put(MESSAGE, message);
        firestore.collection("Chats").document("Group Chat").set(messageText)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(GroupChatActivity.this, "Message Sent Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(GroupChatActivity.this, "Error encountered", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
