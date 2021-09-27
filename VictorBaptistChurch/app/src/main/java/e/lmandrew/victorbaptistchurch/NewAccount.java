package e.lmandrew.victorbaptistchurch;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.net.URI;
import java.util.concurrent.Executor;

public class NewAccount {
    private static final Object TAG = "ERROR";
    private String email = null;
    private String first_name = null;
    private String last_name = null;
    private String phone_number = null;
    private String passWord = null;
    private Uri profile_pic = null;
    private FirebaseAuth mAuth;

    public NewAccount()
    {

    }

    public NewAccount(String email, String first_name, String last_name, String phone_number, String pass, Uri image){
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_number = phone_number;
        profile_pic = image;
        passWord = pass;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setProfile_pic(Uri profile_pic) {
        this.profile_pic = profile_pic;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getEmail() {
        return email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public Uri getProfile_pic() {
        return profile_pic;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getPassWord() {
        return passWord;
    }


}
