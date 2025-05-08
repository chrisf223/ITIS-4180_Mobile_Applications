package edu.uncc.assignment11.fragments.auth;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import edu.uncc.assignment11.databinding.FragmentLoginBinding;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {
    public LoginFragment() {
        // Required empty public constructor
    }

    FragmentLoginBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("User Login");
        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.editTextEmail.getText().toString();
                String password = binding.editTextPassword.getText().toString();
                if(email.isEmpty()){
                    Toast.makeText(getActivity(), "Enter Name!!", Toast.LENGTH_SHORT).show();
                } else if(password.isEmpty()){
                    Toast.makeText(getActivity(), "Enter Password!!", Toast.LENGTH_SHORT).show();
                } else {
                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                    String url = "https://www.theappsdr.com/api/login";

                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("LoginResponse", response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                if (status.equals("ok")) {
                                    String token = jsonObject.getString("token");

                                    // Save the token to SharedPreferences
                                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                    preferences.edit().putString("token", token).apply();

                                    // Login success, move to the next screen
                                    mListener.onLoginSuccessful();
                                } else {
                                    showErrorDialog("Login failed.");
                                }
                            } catch (JSONException e) {
                                Log.e("JSONError", "Error parsing login response", e);
                                showErrorDialog("Error parsing response: " + e.getMessage());
                            }
                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            showErrorDialog("Network error occurred.");
                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("email", email);
                            params.put("password", password);
                            return params;
                        }
                    };

                    queue.add(request);

                }
            }
        });

        binding.buttonCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoSignUpUser();
            }
        });

    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Login Failed")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    LoginListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof LoginListener) {
            mListener = (LoginListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement LoginListener");
        }
    }

    public interface LoginListener {
        void gotoSignUpUser();
        void onLoginSuccessful();
    }
}