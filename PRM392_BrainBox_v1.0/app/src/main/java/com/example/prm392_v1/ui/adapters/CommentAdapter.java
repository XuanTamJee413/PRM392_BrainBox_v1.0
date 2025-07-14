package com.example.prm392_v1.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_v1.R;
import com.example.prm392_v1.data.model.Comment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CommentAdapter extends ListAdapter<Comment, CommentAdapter.CommentViewHolder> {
    public CommentAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Comment> DIFF_CALLBACK = new DiffUtil.ItemCallback<Comment>() {
        @Override
        public boolean areItemsTheSame(@NonNull Comment oldItem, @NonNull Comment newItem) {
            return oldItem.CommentId == newItem.CommentId;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Comment oldItem, @NonNull Comment newItem) {
            return oldItem.Content.equals(newItem.Content) &&
                    oldItem.CreatedAt == newItem.CreatedAt &&
                    (oldItem.User != null && newItem.User != null &&
                            oldItem.User.username.equals(newItem.User.username));
        }
    };

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = getItem(position);
        holder.bind(comment);
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView userTextView;
        TextView contentTextView;
        TextView timeTextView;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            userTextView = itemView.findViewById(R.id.text_comment_user);
            contentTextView = itemView.findViewById(R.id.text_comment_content);
            timeTextView = itemView.findViewById(R.id.text_comment_time);
        }

        public void bind(Comment comment) {
            userTextView.setText(comment.User != null ? comment.User.username : "Anonymous");
            contentTextView.setText(comment.Content);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            timeTextView.setText(sdf.format(new Date(comment.CreatedAt)));
        }
    }
}