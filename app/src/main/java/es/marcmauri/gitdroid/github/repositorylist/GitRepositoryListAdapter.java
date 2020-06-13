package es.marcmauri.gitdroid.github.repositorylist;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import es.marcmauri.gitdroid.R;
import es.marcmauri.gitdroid.github.viewmodel.GitRepositoryBasicModel;

public class GitRepositoryListAdapter extends RecyclerView.Adapter<GitRepositoryListAdapter.RepositoryListItemViewHolder> {

    private final String TAG = GitRepositoryListAdapter.class.getName();
    private List<GitRepositoryBasicModel> repositoryList;


    public GitRepositoryListAdapter(List<GitRepositoryBasicModel> repositoryList) {
        this.repositoryList = repositoryList;
    }

    @NonNull
    @Override
    public RepositoryListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder() called");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.git_repository_list_item, parent, false);

        return new RepositoryListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RepositoryListItemViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder() called");
        holder.bind(repositoryList.get(position));
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount() called");
        return repositoryList.size();
    }


    public static class RepositoryListItemViewHolder extends RecyclerView.ViewHolder {

        private TextView tvRepoName;
        private TextView tvRepoCreatedAt;
        private TextView tvRepoUpdatedAt;
        private TextView tvRepoDescription;

        public RepositoryListItemViewHolder(@NonNull View itemView) {
            super(itemView);

            this.tvRepoName = itemView.findViewById(R.id.tv_repository_name);
            this.tvRepoCreatedAt = itemView.findViewById(R.id.tv_repository_created_at);
            this.tvRepoUpdatedAt = itemView.findViewById(R.id.tv_repository_updated_at);
            this.tvRepoDescription = itemView.findViewById(R.id.tv_repository_description);
        }

        public void bind(final GitRepositoryBasicModel repository) {
            this.tvRepoName.setText(repository.getName());
            this.tvRepoDescription.setText(repository.getDescription());

            SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
            String createdAt = dateFormat.format(repository.getCreatedAt());
            String updatedAt = dateFormat.format(repository.getUpdatedAt());
            this.tvRepoCreatedAt.setText(String.format("Created at:  %s", createdAt));
            this.tvRepoUpdatedAt.setText(String.format("Updated at:  %s", updatedAt));
        }
    }
}
