package ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.widget.*;

import client.model.*;
import client.request.*;
import client.response.*;
import net.ServerProxy;
import jordanrj.familymap.R;

public class LoginFragment extends Fragment implements GetDataTask.Listener
{
    private static final int REQUEST_CODE_LOGGEDIN = 0;
    private User mUser;

    private EditText mHostField;
    private EditText mPortField;
    private EditText mUserNameField;
    private EditText mPasswordField;
    private EditText mFirstNameField;
    private EditText mLastNameField;
    private EditText mEmailField;
    private RadioGroup mRadioGenderGroup;
    private RadioButton mRadioGenderButton;
    private Button mSignInButton;
    private Button mRegisterButton;

    private String serverHost;
    private String serverPort;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;

    private boolean hostFieldEmpty;
    private boolean portFieldEmpty;
    private boolean userNameFieldEmpty;
    private boolean passwordFieldEmpty;
    private boolean firstNameFieldEmpty;
    private boolean lastNameFieldEmpty;
    private boolean emailFieldEmpty;
    private boolean genderSelected;
    //private boolean loggedIn;

    public LoginFragment()
    {
        hostFieldEmpty = true;
        portFieldEmpty = true;
        userNameFieldEmpty = true;
        passwordFieldEmpty = true;
        firstNameFieldEmpty = true;
        lastNameFieldEmpty = true;
        emailFieldEmpty = true;
        genderSelected = false;
        //loggedIn = false;
    }

    public User getmUser() { return mUser; }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            serverHost = getArguments().getString("serverHost");
            serverPort = getArguments().getString("serverPort");
        }
        mUser = new User();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        view = wireUpWidgets(view);
        setListeners();

        return view;
    }

    private View wireUpWidgets(View view)
    {
        mHostField = view.findViewById(R.id.serverHostField);
        mPortField = view.findViewById(R.id.serverPortField);
        mUserNameField = view.findViewById(R.id.userNameField);
        mPasswordField = view.findViewById(R.id.passwordField);
        mFirstNameField = view.findViewById(R.id.firstNameField);
        mLastNameField = view.findViewById(R.id.lastNameField);
        mEmailField = view.findViewById(R.id.emailField);

        mRadioGenderGroup = view.findViewById(R.id.radioGender);
        int selectedID = mRadioGenderGroup.getCheckedRadioButtonId();
        mRadioGenderButton = view.findViewById(selectedID);
        mRadioGenderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radioMale) {
                    gender = "male";
                }
                else if (i == R.id.radioFemale) {
                    gender = "female";
                }
                genderSelected = true;
                if (!anyEmptyFields()) mRegisterButton.setEnabled(true);
            }
        });

        mSignInButton = view.findViewById(R.id.signInButton);
        mSignInButton.setEnabled(false);

        mRegisterButton = view.findViewById(R.id.registerButton);
        mRegisterButton.setEnabled(false);

        return view;
    }

    private void setListeners()
    {
        mHostField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { return; }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { return; }

            @Override
            public void afterTextChanged(Editable editable) {
                mSignInButton.setEnabled(false);
                mRegisterButton.setEnabled(false);
                if (editable.length() > 0)
                {
                    hostFieldEmpty = false;
                    serverHost = editable.toString();
                    if (!hostFieldEmpty && !portFieldEmpty && !userNameFieldEmpty && !passwordFieldEmpty) mSignInButton.setEnabled(true);
                    else mSignInButton.setEnabled(false);
                    if (!anyEmptyFields()) mRegisterButton.setEnabled(true);
                    else mRegisterButton.setEnabled(false);
                }
                else
                {
                    hostFieldEmpty = true;
                    setmSignInButton(false);
                    setmRegisterButtonButton(false);
/*
                    if (!hostFieldEmpty && !portFieldEmpty && !userNameFieldEmpty && !passwordFieldEmpty) mSignInButton.setEnabled(true);
                    else mSignInButton.setEnabled(false);
                    if (!anyEmptyFields()) mRegisterButton.setEnabled(true);
                    else mRegisterButton.setEnabled(false);
*/
                }
            }
        });
        mPortField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { return; }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { return; }

            @Override
            public void afterTextChanged(Editable editable) {
                mSignInButton.setEnabled(false);
                mRegisterButton.setEnabled(false);
                if (editable.length() > 0)
                {
                    portFieldEmpty = false;
                    serverPort = editable.toString();
                    if (!hostFieldEmpty && !userNameFieldEmpty && !passwordFieldEmpty) mSignInButton.setEnabled(true);
                    else mSignInButton.setEnabled(false);
                    if (!anyEmptyFields()) mRegisterButton.setEnabled(true);
                    else mRegisterButton.setEnabled(false);
                }
                else
                {
                    portFieldEmpty = true;
                    setmSignInButton(false);
                    setmRegisterButtonButton(false);
/*
                    if (!hostFieldEmpty && !portFieldEmpty && !userNameFieldEmpty && !passwordFieldEmpty) mSignInButton.setEnabled(true);
                    else mSignInButton.setEnabled(false);
                    if (!anyEmptyFields()) mRegisterButton.setEnabled(true);
                    else mRegisterButton.setEnabled(false);
*/
                }
            }
        });
        mUserNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { return; }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { return; }

            @Override
            public void afterTextChanged(Editable editable) {
                mSignInButton.setEnabled(false);
                mRegisterButton.setEnabled(false);
                if (editable.length() > 0)
                {
                    userNameFieldEmpty = false;
                    userName = editable.toString();
                    if (!hostFieldEmpty && !portFieldEmpty && !passwordFieldEmpty) mSignInButton.setEnabled(true);
                    else mSignInButton.setEnabled(false);
                    if (!anyEmptyFields()) mRegisterButton.setEnabled(true);
                    else mRegisterButton.setEnabled(false);
                }
                else
                {
                    userNameFieldEmpty = true;
                    mSignInButton.setEnabled(false);
                    if (!anyEmptyFields()) mRegisterButton.setEnabled(true);
                    else mRegisterButton.setEnabled(false);
                }
            }
        });
        mPasswordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { return; }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { return; }

            @Override
            public void afterTextChanged(Editable editable) {
                mSignInButton.setEnabled(false);
                mRegisterButton.setEnabled(false);
                if (editable.length() > 0)
                {
                    passwordFieldEmpty = false;
                    password = editable.toString();
                    if (!hostFieldEmpty && !portFieldEmpty && !userNameFieldEmpty) mSignInButton.setEnabled(true);
                    else mSignInButton.setEnabled(false);
                    if (!anyEmptyFields()) mRegisterButton.setEnabled(true);
                    else mRegisterButton.setEnabled(false);
                }
                else
                {
                    passwordFieldEmpty = true;
                    mSignInButton.setEnabled(false);
                    if (!anyEmptyFields()) mRegisterButton.setEnabled(true);
                    else mRegisterButton.setEnabled(false);
                }
            }
        });
        mFirstNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { return; }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { return; }

            @Override
            public void afterTextChanged(Editable editable) {
                mSignInButton.setEnabled(false);
                mRegisterButton.setEnabled(false);
                if (editable.length() > 0)
                {
                    firstNameFieldEmpty = false;
                    firstName = editable.toString();
                    if (!hostFieldEmpty && !portFieldEmpty && !userNameFieldEmpty && !passwordFieldEmpty) mSignInButton.setEnabled(true);
                    else mSignInButton.setEnabled(false);
                    if (!anyEmptyFields()) mRegisterButton.setEnabled(true);
                    else mRegisterButton.setEnabled(false);
                }
                else
                {
                    firstNameFieldEmpty = true;
                    if (!hostFieldEmpty && !portFieldEmpty && !userNameFieldEmpty && !passwordFieldEmpty) mSignInButton.setEnabled(true);
                    else mSignInButton.setEnabled(false);
                    mRegisterButton.setEnabled(false);
                }
            }
        });
        mLastNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { return; }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { return; }

            @Override
            public void afterTextChanged(Editable editable) {
                mSignInButton.setEnabled(false);
                mRegisterButton.setEnabled(false);
                if (editable.length() > 0)
                {
                    lastNameFieldEmpty = false;
                    lastName = editable.toString();
                    if (!hostFieldEmpty && !portFieldEmpty && !userNameFieldEmpty && !passwordFieldEmpty) mSignInButton.setEnabled(true);
                    else mSignInButton.setEnabled(false);
                    if (!anyEmptyFields()) mRegisterButton.setEnabled(true);
                    else mRegisterButton.setEnabled(false);
                }
                else
                {
                    lastNameFieldEmpty = true;
                    if (!hostFieldEmpty && !portFieldEmpty && !userNameFieldEmpty && !passwordFieldEmpty) mSignInButton.setEnabled(true);
                    else mSignInButton.setEnabled(false);
                    mRegisterButton.setEnabled(false);
                }
            }
        });
        mEmailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { return; }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { return; }

            @Override
            public void afterTextChanged(Editable editable) {
                mSignInButton.setEnabled(false);
                mRegisterButton.setEnabled(false);
                if (editable.length() > 0)
                {
                    emailFieldEmpty = false;
                    email = editable.toString();
                    if (!hostFieldEmpty && !portFieldEmpty && !userNameFieldEmpty && !passwordFieldEmpty) mSignInButton.setEnabled(true);
                    else mSignInButton.setEnabled(false);
                    if (!anyEmptyFields()) mRegisterButton.setEnabled(true);
                    else mRegisterButton.setEnabled(false);
                }
                else
                {
                    emailFieldEmpty = true;
                    if (!hostFieldEmpty && !portFieldEmpty && !userNameFieldEmpty && !passwordFieldEmpty) mSignInButton.setEnabled(true);
                    else mSignInButton.setEnabled(false);
                    mRegisterButton.setEnabled(false);
                }
            }
        });

        mSignInButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                signInButtonClicked();
            }
        });
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerButtonClicked();
            }
        });
    }

    public boolean anyEmptyFields()
    {
        return !(!hostFieldEmpty && !portFieldEmpty && !userNameFieldEmpty && !passwordFieldEmpty
                && !firstNameFieldEmpty && !lastNameFieldEmpty && !emailFieldEmpty && genderSelected);
    }

    public void setmSignInButton(boolean b) {
        mSignInButton.setEnabled(b);
    }

    public void setmRegisterButtonButton(boolean b) {
        mRegisterButton.setEnabled(b);
    }

    public void runGetDataTask() {
        DataRequest gdRequest = new DataRequest();
        gdRequest.setAuthTokenStr(Model.instance().getAuthToken().getAuthToken());
        gdRequest.setServerHost(serverHost);
        gdRequest.setServerPort(serverPort);

        GetDataTask gdTask = new GetDataTask(LoginFragment.this);
        gdTask.execute(gdRequest);
    }

    public void displayToast(String message) {
        Toast.makeText(getActivity(),
                message,
                Toast.LENGTH_SHORT).show();

        //((MainActivity)getActivity()).swapFragment();

        if (!hostFieldEmpty && !portFieldEmpty && !userNameFieldEmpty && !passwordFieldEmpty) mSignInButton.setEnabled(true);
        else mSignInButton.setEnabled(false);
        if (!anyEmptyFields()) mRegisterButton.setEnabled(true);
        else mRegisterButton.setEnabled(false);
    }

    private void signInButtonClicked()
    {
        try
        {
            setmSignInButton(false);
            setmRegisterButtonButton(false);

            LoginTask loginTask = new LoginTask();
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUserName(userName);
            loginRequest.setPassword(password);

            loginTask.execute(loginRequest);
        }
        catch (Exception e)
        {
            Log.e("LoginFragment", e.getMessage(), e);
        }
    }

    private void registerButtonClicked()
    {
        try
        {
            setmSignInButton(false);
            setmRegisterButtonButton(false);

            RegisterTask task = new RegisterTask();
            RegisterRequest request = new RegisterRequest();
            request.setUserName(userName);
            request.setPassword(password);
            request.setFirstName(firstName);
            request.setLastName(lastName);
            request.setEmail(email);
            request.setGender(gender);

            task.execute(request);
        }
        catch (Exception e)
        {
            Log.e("LoginFragment", e.getMessage(), e);
        }
    }

    public class LoginTask extends AsyncTask<LoginRequest, Void, LoginResponse>
    {
        public LoginTask() {}
        @Override
        protected LoginResponse doInBackground(LoginRequest... requests)
        {
            ServerProxy servProxy = new ServerProxy();
            LoginResponse response = new LoginResponse();
            for (LoginRequest request: requests)
            {
                response = servProxy.login(serverHost, serverPort, request);
            }
            return response;
        }

        @Override
        protected void onPostExecute(LoginResponse response)
        {
            Model.instance().setLoggedInValue(false);

            if (response.getMessage() == null || response.getMessage().isEmpty())
            {
                Model.instance().setLoggedInValue(true);

                AuthToken authToken = new AuthToken();
                authToken.setAuthToken(response.getAuthToken());
                Model.instance().setAuthToken(authToken);
                mUser.setPersonID(response.getPersonID());

                runGetDataTask();
            }
            else
            {
/*
                Toast.makeText(LoginFragment.this.getContext(),
                        response.getMessage(),
                        Toast.LENGTH_SHORT).show();
*/
                displayToast(response.getMessage());
            }
        }
    }

    public class RegisterTask extends AsyncTask<RegisterRequest, Void, RegisterResponse>
    {
        public RegisterTask() {}
        @Override
        protected RegisterResponse doInBackground(RegisterRequest... requests)
        {
            ServerProxy servProxy = new ServerProxy();
            RegisterResponse response = new RegisterResponse();
            for (RegisterRequest request: requests)
            {
                response = servProxy.register(serverHost, serverPort, request);
            }
            return response;
        }

        @Override
        protected void onPostExecute(RegisterResponse response)
        {
            Model.instance().setLoggedInValue(false);

            if (response.getMessage() == null || response.getMessage().isEmpty())
            {
                Model.instance().setLoggedInValue(true);

                AuthToken authToken = new AuthToken();
                authToken.setAuthToken(response.getAuthToken());
                Model.instance().setAuthToken(authToken);
                mUser.setPersonID(response.getPersonID());

                runGetDataTask();
            }
            else
            {
/*
                Toast.makeText(LoginFragment.this.getContext(),
                        response.getMessage(),
                        Toast.LENGTH_SHORT).show();
*/
                displayToast(response.getMessage());
            }
        }
    }

}