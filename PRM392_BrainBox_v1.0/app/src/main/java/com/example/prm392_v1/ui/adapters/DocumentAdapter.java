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
import com.example.prm392_v1.data.model.DocumentDto;

import java.util.List;

public class DocumentAdapter extends ListAdapter<DocumentDto, DocumentAdapter.DocumentViewHolder> {
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(DocumentDto document);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {this.listener = listener;}
    public DocumentAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<DocumentDto> DIFF_CALLBACK = new DiffUtil.ItemCallback<DocumentDto>() {
        @Override
        public boolean areItemsTheSame(@NonNull DocumentDto oldItem, @NonNull DocumentDto newItem) {
            return oldItem.DocId == newItem.DocId;
        }

        @Override
        public boolean areContentsTheSame(@NonNull DocumentDto oldItem, @NonNull DocumentDto newItem) {
            return oldItem.Title.equals(newItem.Title)
                    && oldItem.Views == newItem.Views
                    && (oldItem.Author != null && newItem.Author != null
                    ? oldItem.Author.Username.equals(newItem.Author.Username)
                    : oldItem.Author == null && newItem.Author == null);
        }
    };

    @NonNull
    @Override
    public DocumentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_document, parent, false);
        return new DocumentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentViewHolder holder, int position) {
        DocumentDto doc = getItem(position);
        holder.bind(doc);
    }

    class DocumentViewHolder extends RecyclerView.ViewHolder {
        TextView title, author, views;

        public DocumentViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_doc_title);
            author = itemView.findViewById(R.id.text_doc_author);
            views = itemView.findViewById(R.id.text_doc_views);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
                }
            });
        }

        public void bind(DocumentDto doc) {
            title.setText(doc.Title);
            author.setText(doc.Author != null ? doc.Author.Username : "Ẩn danh");
            views.setText(doc.Views + " lượt xem");
        }
    }
}