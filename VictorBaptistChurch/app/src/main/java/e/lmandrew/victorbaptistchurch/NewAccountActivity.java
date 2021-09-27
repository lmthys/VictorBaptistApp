package e.lmandrew.victorbaptistchurch;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.security.Permission;

public class NewAccountActivity extends AppCompatActivity {
    private static final Object TAG = "ERROR";
    private TextView firstName = null;
    private TextView lastName = null;
    private TextView pass = null;
    private TextView email  = null;
    private TextView number = null;
    private ImageButton profile = null;
    private Uri selectedUri = null;
    private CharSequence req = "Password and Email Required";
    private FirebaseAuth mAuth = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newaccount);
        Button submit = findViewById(R.id.submitAccountButton);

        firstName = findViewById(R.id.editTextTextPersonName);
        lastName = findViewById(R.id.editTextTextPersonName2);
        pass = findViewById(R.id.editTextTextPassword);
        email = findViewById(R.id.editTextTextEmailAddress);
        number = findViewById(R.id.editTextPhone);

        submit.setOnClickListener(view -> {
            if(pass.getText().toString().equals("") || email.getText().toString().equals(""))
            {
                Toast.makeText(NewAccountActivity.this, req, Toast.LENGTH_SHORT).show();
            }
            else
            {
                NewAccount nA = new NewAccount();
                nA.setEmail(email.getText().toString());
                nA.setPassWord(pass.getText().toString());
                if(!firstName.getText().toString().equals(""))
                    nA.setFirst_name(firstName.getText().toString());
                if(!lastName.getText().toString().equals(""))
                    nA.setLast_name(lastName.getText().toString());
                if(!number.getText().toString().equals(""))
                    nA.setPhone_number(number.getText().toString());
                if(selectedUri != null)
                    nA.setProfile_pic(selectedUri);
                updateFirebase(nA);
                Intent intent = new Intent(NewAccountActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void setImageButton(View v){
        profile = findViewById(R.id.imageProfileButton);
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
        }
        // TODO this is breaking
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_PICK);
        ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK)
                    {
                        selectedUri = result.getData().getData();
                        if(selectedUri != null)
                            profile.setImageURI(selectedUri);
                    }
                }
        );
        resultLauncher.launch(i);
    }
    public void updateFirebase(NewAccount nA) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(nA.getEmail(), nA.getPassWord())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.w("UPDATED", "updated firebase successfully");
                        } else {
                            Log.w((String) TAG, "createUserWithEmail:failure", task.getException());
                        }
                    }
                });
        FirebaseUser user = mAuth.getCurrentUser();
        if(nA.getPhone_number() != null && user != null)
        {
            //TODO
        }
        if(nA.getProfile_pic() != null && user != null)
        {
            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(nA.getProfile_pic())
                    .build();
            user.updateProfile(profileChangeRequest)
                    .addOnCompleteListener(task -> Log.w("UPDATED", "Photo was successfully updated"));
        }
        if(nA.getLast_name() != null && nA.getLast_name() != null && user != null)
        {
            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nA.getLast_name() + " " + nA.getLast_name())
                    .build();
            user.updateProfile(profileChangeRequest)
                    .addOnCompleteListener(task -> Log.w("UPDATED", "Display name was successfully updated"));
        }
    }


}
