package e.lmandrew.victorbaptistchurch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertController;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AwanaFragment extends Fragment {

    Activity context;

    private String postTitle;

    private String postSummary;

    private Button postBtn;

    private StorageReference storage;

    private FirebaseDatabase database;

    private DatabaseReference databaseReference;

    private FirebaseAuth firebaseAuth;

    private DatabaseReference mDatabaseUsers;

    private FirebaseUser mCurrentUser;

    private RecyclerView post_list;

    private FirebaseAuth.AuthStateListener mAuthListener;



    public AwanaFragment(){

    }

    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.awana_fragment, container, false);

        context = getActivity();

        post_list = rootView.findViewById(R.id.posts);
        post_list.setLayoutManager(new LinearLayoutManager(context));
        post_list.setHasFixedSize(true);

        //put database into list
        postBtn = rootView.findViewById(R.id.postButt);

        storage = FirebaseStorage.getInstance().getReference();

        databaseReference = database.getInstance().getReference().child("Awana_posts");

        firebaseAuth = FirebaseAuth.getInstance();

        mCurrentUser = firebaseAuth.getCurrentUser();

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(mCurrentUser==null){
                    //Intent loginIntent = new Intent(MainActivity.this, MainActivity.class);
                    //loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    //startActivity(loginIntent);
                }
            }
        };
        // posting to Firebase
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("New Post");

                // Set up the input
                final EditText title = new EditText(context);
                title.setHint("Title");
                final EditText summary = new EditText(context);
                summary.setHint("Summary");

                //Specify the type of input expected
                title.setInputType(InputType.TYPE_CLASS_TEXT);
                summary.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);

                layout.addView(title);
                layout.addView(summary);

                builder.setView(layout);

                builder.setPositiveButton("Post", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        postTitle = title.getText().toString();
                        postSummary = summary.getText().toString();
                        Toast.makeText(context, "POSTING...", Toast.LENGTH_LONG).show();

                        if (!TextUtils.isEmpty(postTitle) && !TextUtils.isEmpty(postSummary)){
                            final DatabaseReference newPost = databaseReference.push();
                            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    newPost.child("title").setValue(postTitle);
                                    newPost.child("Summ").setValue(postSummary);
                                    newPost.child("uid").setValue(mCurrentUser.getUid());
                                    newPost.child("username").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(context, "Failed to Post"+databaseError, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
                // do a check for empty fields

            }
        });


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
        FirebaseRecyclerAdapter<Post_Item, PostViewHolder> FBRA = new FirebaseRecyclerAdapter<Post_Item, PostViewHolder>(
                Post_Item.class,
                R.layout.post_item,
                PostViewHolder.class,
                databaseReference
        )
        {
            @Override
            protected void populateViewHolder(PostViewHolder viewHolder, Post_Item model, int position) {
                final String post_key = getRef(position).getKey().toString();
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImageUrl(getApplicationContext(), model.getImageUrl());
                viewHolder.setUserName(model.getUsername());
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent singleActivity = new Intent(MainActivity.this, SinglePostActivity.class);
                        singleActivity.putExtra(“PostID”, post_key);
                        startActivity(singleActivity);
                    } });}};
        recyclerView.setAdapter(FBRA);
    }

}
