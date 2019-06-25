package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RealMain extends AppCompatActivity {
    private EditText editNewTask;
    private Button buttonLogout;
    private ImageView imageAdd;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<List_item> tasks;
    private List<String> taskId;


    private String TAG = "RealMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_main);
        editNewTask = findViewById(R.id.EditNewTask);
        buttonLogout = findViewById(R.id.ButtonLogout);
        imageAdd = findViewById(R.id.ImageAdd);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tasks = new ArrayList<List_item>();
        taskId = new ArrayList<String>();

        collectData();


        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent=new Intent(RealMain.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageAdd == null || TextUtils.isEmpty(editNewTask.getText().toString()))
                    Toast.makeText(RealMain.this,"Please insert task first",Toast.LENGTH_SHORT).show();
                else {
                    createNewTask(editNewTask.getText().toString());
                    editNewTask.setText("");
                    collectData();
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
                            //listView.getEmptyView();
                            tasks = new ArrayList<>();
                            taskId = new ArrayList<>();
                            for (QueryDocumentSnapshot data : task.getResult()) {
                                insertToList(data);
                                Log.d(TAG,data.get("task").toString());
                            }
                            updateUI();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void updateUI() {
        adapter = new List_Adapter(tasks,this);
        recyclerView.setAdapter(adapter);
    }

    private void insertToList(QueryDocumentSnapshot data) {
        tasks.add(new List_item(data.get("task").toString(),(boolean) data.get("done")));
        taskId.add(data.getId());
    }

    private void createNewTask(String newTask) {
        // Create a new task
        Map<String, Object> task = new HashMap<>();
        task.put("userId", mAuth.getUid());
        task.put("task", newTask);
        task.put("done", false);

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

        collectData();
    }

    private void getCurrentUser() {
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


        }
        
    }

    public void DeleteClicked(View view) {
        View Row = (View) view.getParent();
        RecyclerView cycle = (RecyclerView) Row.getParent();
        int position = cycle.getChildLayoutPosition(Row);

        deleteTask(position);
        tasks.remove(position);
        taskId.remove(position);
        updateUI();
    }

    private void deleteTask(int position) {
        // [START delete_document]
        db.collection("todos").document(taskId.get(position))
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
        // [END delete_document]
    }

    public void CheckClicked(View view) {
        CheckBox checkBox = (CheckBox) view;
        View Row = (View) view.getParent();
        RecyclerView cycle = (RecyclerView) Row.getParent();
        int position = cycle.getChildLayoutPosition(Row);
        if(checkBox.isChecked())
            updateTask(position,true);
        else
            updateTask(position,false);

    }

    private void updateTask(int position, boolean done) {
        // [START delete_document]
        db.collection("todos").document(taskId.get(position))
                .update("done",done)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
        // [END delete_document]
    }
}
