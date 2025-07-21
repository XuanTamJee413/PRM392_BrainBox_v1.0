package com.example.prm392_v1.ui.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_v1.R;
import com.example.prm392_v1.data.model.Comment;
import com.example.prm392_v1.data.model.DocumentDetail;
import com.example.prm392_v1.data.network.ApiService;
import com.example.prm392_v1.data.network.RetrofitClient;
import com.example.prm392_v1.data.model.ODataResponse;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocumentDetailAdapter extends ListAdapter<DocumentDetail, DocumentDetailAdapter.DetailViewHolder> {
    private final ApiService apiService;

    public DocumentDetailAdapter() {
        super(DIFF_CALLBACK);
        apiService = RetrofitClient.getApiService(null);
    }

    private static final DiffUtil.ItemCallback<DocumentDetail> DIFF_CALLBACK = new DiffUtil.ItemCallback<DocumentDetail>() {
        @Override
        public boolean areItemsTheSame(@NonNull DocumentDetail oldItem, @NonNull DocumentDetail newItem) {
            return oldItem.DocDetailId == newItem.DocDetailId;
        }

        @Override
        public boolean areContentsTheSame(@NonNull DocumentDetail oldItem, @NonNull DocumentDetail newItem) {
            return oldItem.ImageUrl.equals(newItem.ImageUrl) &&
                    (oldItem.Caption != null ? oldItem.Caption.equals(newItem.Caption) : newItem.Caption == null);
        }
    };

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_document_detail, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        DocumentDetail detail = getItem(position);
        holder.bind(detail);
    }

    class DetailViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView caption;
        RecyclerView recyclerViewComments;
        CommentAdapter commentAdapter;

        public DetailViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_detail);
            caption = itemView.findViewById(R.id.text_caption);
            recyclerViewComments = itemView.findViewById(R.id.recycler_view_comments);
            recyclerViewComments.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            commentAdapter = new CommentAdapter();
            recyclerViewComments.setAdapter(commentAdapter);
        }

        public void bind(DocumentDetail detail) {
            if (detail.ImageUrl != null && !detail.ImageUrl.isEmpty()) {
                new LoadImageTask(imageView).execute(detail.ImageUrl);
            } else {
                imageView.setImageResource(R.drawable.ic_placeholder);
            }
            caption.setText(detail.Caption != null ? detail.Caption : "");

            loadComments(detail.DocDetailId);
        }

        private void loadComments(int docDetailId) {
            apiService.getCommentsByDocDetail("DocumentDetail/DocDetailId eq " + docDetailId).enqueue(new Callback<ODataResponse<Comment>>() {
                @Override
                public void onResponse(Call<ODataResponse<Comment>> call, Response<ODataResponse<Comment>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        commentAdapter.submitList(response.body().value);
                    }
                }

                @Override
                public void onFailure(Call<ODataResponse<Comment>> call, Throwable t) {
                }
            });
        }
    }

    private static class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private final ImageView imageView;

        public LoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap bitmap = null;
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
                input.close();
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageResource(R.drawable.ic_placeholder);
            }
        }
    }
}