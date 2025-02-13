package com.example.assignment_05;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;
import java.util.regex.Pattern;


public class Edit_Fragment extends Fragment {

    private static final String ARG_PARAM_PROFILE = "param1";

    private User mUser;
    
    public Edit_Fragment() {
        // Required empty public constructor
    }

    public static Edit_Fragment newInstance(User user) {
        Edit_Fragment fragment = new Edit_Fragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_PROFILE, user);
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUser = (User) getArguments().getSerializable(ARG_PARAM_PROFILE);
        }
    }

    TextView name;
    TextView email;
    RadioGroup radioGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = view.findViewById(R.id.user_name);
        email = view.findViewById(R.id.user_email);
        radioGroup = view.findViewById(R.id.radioGroup);

        name.setText(mUser.getName());
        email.setText(mUser.getEmail());

        if ("Student".equals(mUser.getRole())) {
            ((RadioButton) view.findViewById(R.id.studentRadio)).setChecked(true);
        } else if ("Employee".equals(mUser.getRole())) {
            ((RadioButton) view.findViewById(R.id.employeeRadio)).setChecked(true);
        }
        else {
            ((RadioButton) view.findViewById(R.id.otherRadio)).setChecked(true);
        }

        view.findViewById(R.id.editSubmitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter your name", Toast.LENGTH_SHORT).show();
                }
                else if (email.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter your email", Toast.LENGTH_SHORT).show();
                }
                else if (!isValidEmail(email.getText().toString())) {
                    Toast.makeText(getActivity(), "Please enter a valid email", Toast.LENGTH_SHORT).show();
                }
                else {
                    String userName = name.getText().toString();
                    String userEmail = email.getText().toString();

                    int selectedRoleId = radioGroup.getCheckedRadioButtonId();
                    RadioButton selectedRoleButton = view.findViewById(selectedRoleId);
                    String userRole = selectedRoleButton.getText().toString();

                    User user = new User(userName, userEmail, userRole);
                    mListener.sendUserData(user);
                    mListener.returnToProfile(user);
                }
            }
        });
        view.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.cancelEdit();
            }
        });
    }

    Edit_Fragment.EditListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (Edit_Fragment.EditListener) context;
    }

    public interface EditListener {
        void returnToProfile(User user);
        void sendUserData(User user);
        void cancelEdit();
    }

    public static boolean isValidEmail(String email) {

        // Regular expression to match valid email formats
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        // Compile the regex
        Pattern p = Pattern.compile(emailRegex);

        // Check if email matches the pattern
        return email != null && p.matcher(email).matches();
    }
}