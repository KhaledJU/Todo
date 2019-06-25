package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class RealMain extends AppCompatActivity {
    private EditText editNewTask;
    private Button buttonLogout;
    private Button buttonAdd;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ListView listView;
    private String TAG = "RealMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_main);
        editNewTask = findViewById(R.id.EditNewTask);
        buttonLogout = findViewById(R.id.ButtonLogout);
        buttonAdd = findViewById(R.id.ButtonAdd);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        listView = findViewById(R.id.List_view);

        collectData();
        //String email = getCurrentUser();

        //textInfo.setText("Welcome "+email);

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent=new Intent(RealMain.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(buttonAdd == null || TextUtils.isEmpty(buttonAdd.getText().toString()))
                    Toast.makeText(RealMain.this,"Please insert task first",Toast.LENGTH_SHORT).show();
                else {
                    createNewTask(editNewTask.getText().toString());
                }

            }
        });

    }

    private void collectData() {
        db.collection("todos")
                .whereEqualTo("userId",mAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot data : task.getResult()) {
                                //insertToDoList(data.get("task").toString());
                                Log.d(TAG,data.get("task").toString());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void insertToDoList(String task) {
        TextView textView = new TextView(this);
        textView.setText(task);
        listView.addView(textView);
    }

    private void createNewTask(String newTask) {
        // Create a new task
        Map<String, Object> task = new HashMap<>();
        task.put("userId", mAuth.getUid());
        task.put("task", newTask);

        // Add a new task
        db.collection("todos")
                .add(task)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private String getCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();

            return email;
        }
        return "";
    }
}
