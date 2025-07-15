package com.example.prm392_v1.ui.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_v1.R;
import com.example.prm392_v1.data.model.UserDto;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final List<UserDto> userList;
    private final OnUserActionListener listener;

    // Interface to handle button clicks in the activity
    public interface OnUserActionListener {
        void onChangeRoleClicked(UserDto user);
        void onBlockStatusChanged(UserDto user);
    }

    public UserAdapter(List<UserDto> userList, OnUserActionListener listener) {
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserDto user = userList.get(position);
        holder.bind(user, listener);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvUsername, tvUserRole, tvUserStatus;
        private final Button btnChangeRole, btnBlockUser;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvUserRole = itemView.findViewById(R.id.tvUserRole);
            tvUserStatus = itemView.findViewById(R.id.tvUserStatus);
            btnChangeRole = itemView.findViewById(R.id.btnChangeRole);
            btnBlockUser = itemView.findViewById(R.id.btnBlockUser);
        }

        public void bind(final UserDto user, final OnUserActionListener listener) {
            tvUsername.setText(user.Username);
            tvUserRole.setText("Role: " + user.Role);

            // Update status text and color
            if (user.Status) {
                tvUserStatus.setText("Status: Active");
                tvUserStatus.setTextColor(Color.parseColor("#4CAF50")); // Green
                btnBlockUser.setText("Block");
                btnBlockUser.setTextColor(Color.RED);
            } else {
                tvUserStatus.setText("Status: Blocked");
                tvUserStatus.setTextColor(Color.RED);
                btnBlockUser.setText("Unblock");
                btnBlockUser.setTextColor(Color.parseColor("#4CAF50")); // Green
            }

            // Set click listeners
            btnChangeRole.setOnClickListener(v -> listener.onChangeRoleClicked(user));
            btnBlockUser.setOnClickListener(v -> listener.onBlockStatusChanged(user));
        }
    }
}