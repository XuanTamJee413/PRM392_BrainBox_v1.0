package com.example.prm392_v1.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_v1.R;
import com.example.prm392_v1.data.model.DocumentDto;

public class DocumentActionNewAdapter extends ListAdapter<DocumentDto, DocumentActionNewAdapter.DocumentViewHolder> {
    private OnItemClickListener itemClickListener;
    private OnUpdateClickListener updateClickListener;
    private OnDeleteClickListener deleteClickListener;

    public DocumentActionNewAdapter() {
        super(DIFF_CALLBACK);
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentDto document);
    }

    public interface OnUpdateClickListener {
        void onUpdateClick(DocumentDto document);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(DocumentDto document);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setOnUpdateClickListener(OnUpdateClickListener listener) {
        this.updateClickListener = listener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }

    private static final DiffUtil.ItemCallback<DocumentDto> DIFF_CALLBACK = new DiffUtil.ItemCallback<DocumentDto>() {
        @Override
        public boolean areItemsTheSame(@NonNull DocumentDto oldItem, @NonNull DocumentDto newItem) {
            return oldItem.DocId == newItem.DocId;
        }

        @Override
        public boolean areContentsTheSame(@NonNull DocumentDto oldItem, @NonNull DocumentDto newItem) {
            // Explicitly compare fields to avoid relying on equals()
            return (oldItem.Title != null ? oldItem.Title.equals(newItem.Title) : newItem.Title == null) &&
                    (oldItem.Content != null ? oldItem.Content.equals(newItem.Content) : newItem.Content == null) &&
                    oldItem.IsPublic == newItem.IsPublic &&
                    oldItem.Views == newItem.Views &&
                    oldItem.CreatedAt == newItem.CreatedAt &&
                    (oldItem.Author != null && newItem.Author != null ?
                            oldItem.Author.Username.equals(newItem.Author.Username) :
                            oldItem.Author == null && newItem.Author == null);
        }
    };

    @NonNull
    @Override
    public DocumentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_document_action, parent, false);
        return new DocumentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentViewHolder holder, int position) {
        DocumentDto document = getItem(position);
        holder.bind(document);
    }

    class DocumentViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvAuthor;
        private final Button btnUpdate;
        private final Button btnDelete;

        public DocumentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_document_title);
            tvAuthor = itemView.findViewById(R.id.tv_document_author);
            btnUpdate = itemView.findViewById(R.id.btn_update);
            btnDelete = itemView.findViewById(R.id.btn_delete);

            itemView.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(getItem(getAdapterPosition()));
                }
            });

            btnUpdate.setOnClickListener(v -> {
                if (updateClickListener != null) {
                    updateClickListener.onUpdateClick(getItem(getAdapterPosition()));
                }
            });

            btnDelete.setOnClickListener(v -> {
                if (deleteClickListener != null) {
                    deleteClickListener.onDeleteClick(getItem(getAdapterPosition()));
                }
            });
        }

        public void bind(DocumentDto document) {
            tvTitle.setText(document.Title);
            tvAuthor.setText(document.Author != null ? document.Author.Username : "Ẩn danh");
        }
    }
}