package com.example.busdevelop.buses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentSender;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import java.util.ArrayList;
import java.util.List;


/**
 * A login screen that offers login via email/password.

 */
public class LoginActivity extends Activity implements LoaderCallbacks<Cursor>,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private Usuario mUsuario;

    private GoogleApiClientSing mGoogleApiClient;
    private ConnectionResult mConnectionResult;
    private ProgressDialog mConnectionProgressDialog;
    private static final int REQUEST_CODE_RESOLVE_ERR = 9000;

    private static final String TAG = "MainFragment";
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };
    private UiLifecycleHelper uiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUsuario = null;
        setUp();

        uiHelper = new UiLifecycleHelper(this, callback);
        //uiHelper.onCreate(savedInstanceState);

        mGoogleApiClient = GoogleApiClientSing.getInstancia();
        mGoogleApiClient.setGoogleApiClient(new GoogleApiClient.Builder(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build());

        // Progress bar to be displayed if the connection failure is not resolved.
        mConnectionProgressDialog = new ProgressDialog(this);
        mConnectionProgressDialog.setMessage("Signing in...");

        this.findViewById(R.id.googleplus_sign_in_button).setOnClickListener(this);

        // start Facebook Login
        Session.openActiveSession(this, true, new Session.StatusCallback() {

            // callback when session changes state
            @Override
            public void call(Session session, SessionState state, Exception exception) {
                if (session.isOpened()) {

                    // make request to the /me API
                    Request.newMeRequest(session, new Request.GraphUserCallback() {

                        // callback after Graph API response with user object
                        @Override
                        public void onCompleted(GraphUser user, Response response) {
                            if (user != null) {
                                Toast.makeText(getBaseContext(), "Hola " + user.getName() + "!", Toast.LENGTH_SHORT).show();

                            }

                        }
                    }).executeAsync();
                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        uiHelper.onActivityResult(requestCode, responseCode, intent);
        Session.getActiveSession().onActivityResult(this, requestCode, responseCode, intent);
        if (requestCode == REQUEST_CODE_RESOLVE_ERR) {
            Log.i("tag", "requestCode == REQUEST_CODE_RESOLVE_ERR. responseCode = " + responseCode);
            if(responseCode == Activity.RESULT_OK) {
                if(mGoogleApiClient != null) {
                    if (!mGoogleApiClient.getGoogleApiClient().isConnecting()) {
                        mGoogleApiClient.getGoogleApiClient().connect();
                    }
                }
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mConnectionProgressDialog.dismiss();
        Toast.makeText(getBaseContext(), "Bienvenido " + Plus.PeopleApi
                .getCurrentPerson(mGoogleApiClient.getGoogleApiClient()).getDisplayName(), Toast.LENGTH_SHORT).show();
        //iniciarMainActivity();

    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
        if (mConnectionProgressDialog.isShowing()) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
                } catch (IntentSender.SendIntentException e) {
                    mGoogleApiClient.getGoogleApiClient().connect();
                }
            }
        }
        mConnectionResult = connectionResult;

    }

    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.googleplus_sign_in_button) {
            if(mGoogleApiClient != null) {
                if(!mGoogleApiClient.getGoogleApiClient().isConnected()) {
                    if (mConnectionResult == null) {
                        mConnectionProgressDialog.show();
                        mGoogleApiClient.getGoogleApiClient().connect();

                    } else {
                        try {
                            mConnectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
                        } catch (IntentSender.SendIntentException e) {
                            // Try connecting again.
                            mConnectionResult = null;
                            mGoogleApiClient.getGoogleApiClient().connect();
                        }
                    }
                }
            }
        }
    }

    private void setUp() {
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mLoginButton = (Button) findViewById(R.id.Login);
        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mRegisterButton = (Button) findViewById(R.id.Register);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegister();
            }
        });

        mProgressView = findViewById(R.id.login_progress);
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login, container, false);
        LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
        return view;

    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");
        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // For scenarios where the main activity is launched and user
        // session is not null, the session state change notification
        // may not be triggered. Trigger it if it's open/closed.
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }

        uiHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = getCurrentEmail();
        String password = getCurrentPassword();

        boolean cancel = false;
        View focusView = null;

        //Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            Log.d("tiene:",email+" "+password);
            mAuthTask.execute();
        }
    }

    private void makeInvisible(){
        View appearLogin = findViewById(R.id.Register);
        appearLogin.setVisibility(View.INVISIBLE);
        appearLogin = findViewById(R.id.Login);
        appearLogin.setVisibility(View.INVISIBLE);
        appearLogin = findViewById(R.id.email);
        appearLogin.setVisibility(View.INVISIBLE);
        appearLogin = findViewById(R.id.password);
        appearLogin.setVisibility(View.INVISIBLE);
    }

    private void makeVisible(){
        View appearLogin = findViewById(R.id.Register);
        appearLogin.setVisibility(View.VISIBLE);
        appearLogin = findViewById(R.id.Login);
        appearLogin.setVisibility(View.VISIBLE);
        appearLogin = findViewById(R.id.email);
        appearLogin.setVisibility(View.VISIBLE);
        appearLogin = findViewById(R.id.password);
        appearLogin.setVisibility(View.VISIBLE);

        RelativeLayout relative = (RelativeLayout) findViewById(R.id.login_view);
        relative.setBackgroundResource(R.drawable.bus_logo);
    }

    /*Returns the current value at the email field*/
    public String getCurrentEmail(){
        try {
            return mEmailView.getText().toString();
        }catch(NullPointerException e){
            return null;
        }
    }

    /*Returns the current value at the password field*/
    public String getCurrentPassword(){
        try{
            return mPasswordView.getText().toString();
        }catch(NullPointerException e){
            return null;
        }
    }

    public boolean isEmailValid(String email) {
        return email.contains("@");
    }

    public boolean isPasswordValid(String password) {
        return password.length() > 2;
    }
    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProgressView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public void iniciarMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /** Metodo para pasar a a la actividad CrearCuenta*/
    public void goToRegister() {
        Intent i = new Intent(this, CrearCuentaActivity.class);
        if(getCurrentEmail()!=null){
            i.putExtra("emailIngresado", getCurrentEmail());
        }
        startActivity(i);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    public boolean validarDatos(String value,String email, String password){
        UserLoginTask u = new UserLoginTask(email,password);
        return u.validateData(value);
    }
    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String LOG_TAG = LoginActivity.class.getSimpleName();
        private final String mEmail;
        private final String mPassword;
        private boolean mValid = false;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            // Will contain the raw JSON response as a string.
            String usersJsonStr = null;

            // HAce un post a request para obtener el token
            mUsuario = new Usuario();
            usersJsonStr = mUsuario.obtenerToken(mEmail, mPassword);

            mValid = validateData(usersJsonStr);
            if(mValid)
                saveUser();

            /*Solo para debugging
            String a = ""+mValid;
            Log.d(LOG_TAG, a);*/
            return mValid;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

        //Valida si los datos están dentro del json de todos los usuarios
        private boolean validateData(String jsonString){
            boolean existe;

            if(jsonString.contains("token")){
                existe = true;
                return existe;
            }else{
                existe = false;
                return existe;
            }



        }

        public boolean validUser(){
            return mValid;
        }

        //Guarda dentro del SharedPreferences el email del usuario para saber que ya ingreso correctamente
        private void saveUser(){
            SharedPreferences settings = getSharedPreferences("MyPrefsFile", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("UserEmail",mEmail);
            editor.putBoolean("SinRegistrar",false);
            editor.putString("UserPass",mPassword);
            editor.commit();
        }
    }
}
