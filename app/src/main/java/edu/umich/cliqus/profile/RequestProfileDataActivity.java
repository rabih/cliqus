package edu.umich.cliqus.profile;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.umich.cliqus.R;
import edu.umich.cliqus.UserInformation;

public class RequestProfileDataActivity extends AppCompatActivity {
    private static final String TAG = "CliqUs";

    @BindView(R.id.input_first_name)
    EditText _firstNameText;
    @BindView(R.id.input_last_name) EditText _lastNameText;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.input_gender) EditText _genderText;
    /// @BindView(R.id.input_dob) EditText _dobtext;
    @BindView(R.id.btn_save_profile) Button _saveDataButton;

    private FirebaseAuth mAuth;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String gender;
    private String dob;
    private String phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_profile_data);
        ButterKnife.bind(this);

        _saveDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createProfile();
            }
        });

    }


    public boolean validate() {
        boolean valid = true;

        firstName = _firstNameText.getText().toString();
        lastName = _lastNameText.getText().toString();
        email = _emailText.getText().toString();
        password = _passwordText.getText().toString();
        gender = _genderText.getText().toString();

        if (firstName.isEmpty() || firstName.length() < 3) {
            _firstNameText.setError("at least 3 characters");
            valid = false;
        } else {
            _firstNameText.setError(null);
        }

        if (lastName.isEmpty() || lastName.length() < 3) {
            _lastNameText.setError("at least 3 characters");
            valid = false;
        } else {
            _lastNameText.setError(null);
        }

        if (lastName.isEmpty()) {
            _genderText.setError("enter your gender");
            valid = false;
        } else {
            _genderText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 5 || password.length() > 16) {
            _passwordText.setError("between 5 and 16 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    public void createProfile() {
        mAuth = FirebaseAuth.getInstance();

        if(validate()) {
            Profile profile = new Profile(firstName, lastName, gender, " ", email, " ");

            FirebaseUser user = mAuth.getCurrentUser();
            FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference myRef;
            FirebaseAuth.AuthStateListener mAuthListener;
            myRef = mFirebaseDatabase.getReference();

            if(user != null) {
                String userID = user.getUid();

                //signUp();   //attempt to sign up if first time user.

                Log.w("CliqUs", firstName);
                Log.w("CliqUs", lastName);
                Log.w("CliqUs", gender);
                Log.w("CliqUs", email);

                myRef.child("users").child(userID).setValue(profile);
                this.finish();
            } else
                Log.w(TAG, "KILL ME NOW");
        }
    }


}