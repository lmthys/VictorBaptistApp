package e.lmandrew.victorbaptistchurch;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.net.ConnectException;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "AndroidClarified";
    private SignInButton googleSignInButton;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth mAuth;
    private boolean isGranted = false;
    private final int REQUEST_INTERNET = 1;
    private Button signIn = null;
    private TextView email = null;
    private TextView pass = null;

    ActivityResultLauncher<Intent> gSA = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK)
                    {
                        Intent data = result.getData();
                        try {
                            // The Task returned from this call is always completed, no need to attach
                            // a listener.
                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            onLoggedIn(account);
                        } catch (ApiException e) {
                            // The ApiException status code indicates the detailed failure reason.
                            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
                        }

                    }
                }
            });

    @Override
    public void onStart() {
        super.onStart();
        if(getIntent().getIntExtra(getString(R.string.logout), 0) == 1){
            /*googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    //On Succesfull signout we navigate the user back to LoginActivity

                }
            });*/
            FirebaseAuth.getInstance().signOut();
        }
        //GoogleSignInAccount alreadyloggedAccount = GoogleSignIn.getLastSignedInAccount(this);
        FirebaseUser curUser = mAuth.getCurrentUser();
        if (curUser != null) {
            Toast.makeText(this, "Already Logged In", Toast.LENGTH_SHORT).show();
            updateMain(curUser);
        } else {
            Log.d(TAG, "Not logged in");
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);

        if(!(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.INTERNET}, REQUEST_INTERNET);
        }
        mAuth = FirebaseAuth.getInstance();

        googleSignInButton = findViewById(R.id.sign_in_button);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(getString(R.string.default_web_client_id))
        .requestEmail()
        .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                gSA.launch(signInIntent);
            }
        });
        signIn = findViewById(R.id.signInButton);
        email = findViewById(R.id.signInEmail);
        pass = findViewById(R.id.signInPass);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.w("UPDATED", "logged in successfully");
                                    mAuth = FirebaseAuth.getInstance();
                                    updateMain(mAuth.getCurrentUser());
                                } else {
                                    Log.w((String) TAG, "createUserWithEmail:failure", task.getException());
                                }
                            }
                        });

            }
        });
    }

    public void createUserClicked(View v)
    {
        Intent i = new Intent(this, NewAccountActivity.class);
        startActivity(i);
    }
    private void onLoggedIn(final GoogleSignInAccount googleSignInAccount) {
        // add to firebase
        AuthCredential credential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            UserProfileChangeRequest photo = new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(googleSignInAccount.getPhotoUrl())
                                    .build();

                            user.updateProfile(photo)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User profile updated.");
                                            }
                                            else {
                                                Log.d(TAG, "User profile not updated.");
                                            }
                                        }
                                    });
                            updateMain(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());


                        }
                    }


                });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_INTERNET:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(LoginActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    public void updateMain(FirebaseUser user){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.GOOGLE_ACCOUNT, user);


        startActivity(intent);
        finish();
    }
}
