package edu.uncc.assessment03;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.uncc.assessment03.R;
import edu.uncc.assessment03.models.User;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private ArrayList<User> userList;
    private DeleteUserListener deleteUserListener;

    public UserAdapter(ArrayList<User> userList, DeleteUserListener deleteUserListener) {
        this.userList = userList;
        this.deleteUserListener = deleteUserListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User currentUser = userList.get(position);

        holder.textViewName.setText(currentUser.getName());
        holder.textViewAge.setText(currentUser.getAge() + " years old");
        holder.textViewState.setText(currentUser.getState().getName() + ", " + currentUser.getState().getAbbreviation());
        holder.textViewCredit.setText(String.valueOf(currentUser.getCreditScore()));

        setCreditScoreImage(currentUser.getCreditScore(), holder.imageViewCredit);

        holder.trashImageView.setOnClickListener(v -> {
            deleteUserListener.onUserDelete(currentUser);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    private void setCreditScoreImage(int creditScore, ImageView imageView) {
        if (creditScore >= 800) {
            imageView.setImageResource(R.drawable.excellent);
        } else if (creditScore >= 740) {
            imageView.setImageResource(R.drawable.very_good);
        } else if (creditScore >= 670) {
            imageView.setImageResource(R.drawable.good);
        } else if (creditScore >= 580) {
            imageView.setImageResource(R.drawable.fair);
        } else {
            imageView.setImageResource(R.drawable.poor);
        }
    }

    public void updateUserList(ArrayList<User> updatedUsers) {
        this.userList = updatedUsers;
        notifyDataSetChanged();
    }

    public interface DeleteUserListener {
        void onUserDelete(User user);
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewAge;
        TextView textViewState;
        TextView textViewCredit;
        ImageView imageViewCredit;
        ImageView trashImageView;

        public UserViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewAge = itemView.findViewById(R.id.textViewAge);
            textViewState = itemView.findViewById(R.id.textViewState);
            textViewCredit = itemView.findViewById(R.id.textViewCredit);
            imageViewCredit = itemView.findViewById(R.id.imageViewCredit);
            trashImageView = itemView.findViewById(R.id.trashImageView);
        }
    }
}


