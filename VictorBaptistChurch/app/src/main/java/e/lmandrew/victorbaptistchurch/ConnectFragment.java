package e.lmandrew.victorbaptistchurch;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;


public class ConnectFragment extends Fragment {

    public static final String GOOGLE_ACCOUNT = "google_account";
    public static final String FIRE_ACCOUNT = "fire_account";

    private TextView profileName, profileEmail;
    private ImageView profileImage;
    private Button signOut;
    Activity context;

    public ConnectFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_connect, container, false);

        context = getActivity();

        profileName = rootView.findViewById(R.id.profile_text);
        profileEmail = rootView.findViewById(R.id.profile_email);
        profileImage = rootView.findViewById(R.id.profile_image);
        signOut= rootView.findViewById(R.id.sign_out);

        FirebaseUser user = getArguments().getParcelable(GOOGLE_ACCOUNT);

        Picasso.get().load(user.getPhotoUrl()).centerInside().fit().into(profileImage);
        profileName.setText(user.getDisplayName());
        profileEmail.setText(user.getEmail());

        /*try {
            InputStream is = (InputStream) new URL(user.getPhotoUrl().toString()).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            profileImage.setImageDrawable(d);
        } catch (Exception e) {
            Toast.makeText(context, "Did not make picture" + user.getPhotoUrl().toString(), Toast.LENGTH_LONG).show();
        } */


         signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          /*
          Sign-out is initiated by simply calling the googleSignInClient.signOut API. We add a
          listener which will be invoked once the sign out is the successful
          */
                Intent intent = new Intent(context, LoginActivity.class);
                intent.putExtra(getString(R.string.logout), 1);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        return rootView;
    }


}
