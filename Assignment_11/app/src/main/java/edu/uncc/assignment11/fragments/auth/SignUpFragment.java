package edu.uncc.assignment11.fragments.auth;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import edu.uncc.assignment11.databinding.FragmentSignupBinding;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

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


public class SignUpFragment extends Fragment {
    public SignUpFragment() {
        // Required empty public constructor
    }

    FragmentSignupBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSignupBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("User Sign Up");

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.gotoLogin();
            }
        });

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname = binding.editTextFirstName.getText().toString().trim();
                String lname = binding.editTextLastName.getText().toString().trim();
                String email = binding.editTextEmail.getText().toString().trim();
                String password = binding.editTextPassword.getText().toString().trim();

                if(fname.isEmpty()){
                    Toast.makeText(getContext(), "First Name is required", Toast.LENGTH_SHORT).show();
                } else if(lname.isEmpty()){
                    Toast.makeText(getContext(), "Last Name is required", Toast.LENGTH_SHORT).show();
                } else if(email.isEmpty()){
                    Toast.makeText(getContext(), "Email is required", Toast.LENGTH_SHORT).show();
                } else if(password.isEmpty()){
                    Toast.makeText(getContext(), "Password is required", Toast.LENGTH_SHORT).show();
                } else {
                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                    String url = "https://www.theappsdr.com/api/signup";

                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("SignUpResponse", response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                if (status.equals("ok")) {
                                    String token = jsonObject.getString("token");

                                    // Save token to SharedPreferences
                                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                    preferences.edit().putString("token", token).apply();

                                    // Notify the activity
                                    mListener.onSignUpSuccessful();
                                } else {
                                    showErrorDialog("Registration failed.");
                                }
                            } catch (JSONException e) {
                                Log.e("JSONError", "SignUp response not JSON", e);
                                showErrorDialog("Error parsing response: " + e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("VolleyError", "SignUp request failed", error);
                            showErrorDialog("Network error occurred.");
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("fname", fname);
                            params.put("lname", lname);
                            params.put("email", email);
                            params.put("password", password);
                            return params;
                        }
                    };

                    queue.add(request);

                }
            }
        });
    }

    SignUpListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof SignUpListener) {
            mListener = (SignUpListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SignUpListener");
        }
    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Sign Up Failed")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    public interface SignUpListener {
        void gotoLogin();
        void onSignUpSuccessful();
    }
}