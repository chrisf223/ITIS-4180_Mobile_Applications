package edu.uncc.assessment04.fragments.users;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import edu.uncc.assessment04.databinding.FragmentPassCodeBinding;
import edu.uncc.assessment04.models.User;

public class PassCodeFragment extends Fragment {
    private static final String ARG_PARAM_USER = "ARG_PARAM_USER";
    private User mUser;

    public PassCodeFragment() {
        // Required empty public constructor
    }

    public static PassCodeFragment newInstance(User user) {
        PassCodeFragment fragment = new PassCodeFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUser = (User) getArguments().getSerializable(ARG_PARAM_USER);
        }
    }

    FragmentPassCodeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPassCodeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Enter Passcode");
        binding.textViewName.setText("For " + mUser.getName());

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPassCodeCancel();
            }
        });

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passcode = binding.editTextPasscode.getText().toString().trim();
                if(passcode.equals(mUser.getPasscode())) {
                    // Passcode is correct
                    mListener.onPasscodeSuccessful(mUser);
                } else {
                    // Passcode is incorrect
                    Toast.makeText(getContext(), "Incorrect Passcode", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    PassCodeListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof PassCodeListener) {
            mListener = (PassCodeListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement PassCodeListener");
        }
    }

    public interface PassCodeListener {
        void onPasscodeSuccessful(User user);
        void onPassCodeCancel();
    }
}