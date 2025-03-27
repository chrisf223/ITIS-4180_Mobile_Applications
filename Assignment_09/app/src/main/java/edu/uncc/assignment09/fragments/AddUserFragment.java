package edu.uncc.assignment09.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import edu.uncc.assignment09.R;
import edu.uncc.assignment09.databinding.FragmentAddUserBinding;
import edu.uncc.assignment09.models.State;
import edu.uncc.assignment09.models.User;

public class AddUserFragment extends Fragment {
    public AddUserFragment() {
        // Required empty public constructor
    }

    FragmentAddUserBinding binding;
    State selectedState;

    public void setSelectedState(State selectedState) {
        this.selectedState = selectedState;
        updateStateUI();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddUserBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Add User");

        updateStateUI();

        binding.buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoSelectState();
            }
        });

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAndSubmitForm();
            }
        });

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.cancelAddOrSelection();
            }
        });
    }

    private void updateStateUI() {
        if (binding != null) {
            if(selectedState == null){
                binding.textViewState.setText("N/A");
            } else {
                binding.textViewState.setText(selectedState.getName());
            }
        }
    }

    private void validateAndSubmitForm() {
        String name = binding.editTextName.getText().toString().trim();
        String ageStr = binding.editTextAge.getText().toString().trim();
        String creditScoreStr = binding.editTextCreditScore.getText().toString().trim();

        // Validate name
        if(name.isEmpty()){
            Toast.makeText(getActivity(), "Enter Name!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate age
        if(ageStr.isEmpty()){
            Toast.makeText(getActivity(), "Enter Age!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int age = Integer.parseInt(ageStr);
            if(age < 0 || age > 150){
                Toast.makeText(getActivity(), "Age must be between 0 and 150!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate credit score
            if(creditScoreStr.isEmpty()){
                Toast.makeText(getActivity(), "Enter Credit Score!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int creditScore = Integer.parseInt(creditScoreStr);
                if(creditScore < 300 || creditScore > 850){
                    Toast.makeText(getActivity(), "Credit Score must be between 300 and 850!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate state
                if (selectedState == null){
                    Toast.makeText(getActivity(), "Select State!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // All validations passed - create user
                User user = new User(name, age, creditScore, selectedState.getStateSummary());
                mListener.sendBackNewUser(user);

            } catch (NumberFormatException ex) {
                Toast.makeText(getActivity(), "Enter valid Credit Score!", Toast.LENGTH_SHORT).show();
            }

        } catch (NumberFormatException ex) {
            Toast.makeText(getActivity(), "Enter valid Age!", Toast.LENGTH_SHORT).show();
        }
    }

    AddUserListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddUserListener) {
            mListener = (AddUserListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement AddUserListener");
        }
    }

    public interface AddUserListener {
        void sendBackNewUser(User user);
        void cancelAddOrSelection();
        void gotoSelectState();
    }
}