package es.marcmauri.gitdroid.github.gitrepositories;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.marcmauri.gitdroid.R;
import es.marcmauri.gitdroid.github.viewmodel.GitRepositoryBasicModel;

public class GitRepositoryListAdapter extends RecyclerView.Adapter<GitRepositoryListAdapter.RepositoryListItemViewHolder> {

    private static final String TAG = GitRepositoryListAdapter.class.getName();
    private List<GitRepositoryBasicModel> repositoryList;
    private OnItemClickListener listener;


    public GitRepositoryListAdapter(List<GitRepositoryBasicModel> repositoryList, OnItemClickListener listener) {
        this.repositoryList = repositoryList;
        this.listener = listener;
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
        holder.bind(this.repositoryList.get(position), this.listener);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount() called");
        return repositoryList.size();
    }


    public static class RepositoryListItemViewHolder extends RecyclerView.ViewHolder {

        private CardView cardviewItem;
        private TextView tvRepoName;
        private TextView tvOwnerName;
        private TextView tvRepoDescription;

        public RepositoryListItemViewHolder(@NonNull View itemView) {
            super(itemView);

            this.cardviewItem = itemView.findViewById(R.id.cardview_repository_item);
            this.tvRepoName = itemView.findViewById(R.id.tv_repository_name);
            this.tvOwnerName = itemView.findViewById(R.id.tv_repository_owner_name);
            this.tvRepoDescription = itemView.findViewById(R.id.tv_repository_description);
        }

        public void bind(final GitRepositoryBasicModel currentRepository, final OnItemClickListener listener) {
            // Set texts on recycler item
            this.tvRepoName.setText(currentRepository.getName());
            this.tvRepoDescription.setText(currentRepository.getDescription());
            this.tvOwnerName.setText(currentRepository.getOwnerName());

            // Set dates on recycler item
            //SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
            //String createdAt = (currentRepository.getCreatedAt() != null) ? dateFormat.format(currentRepository.getCreatedAt()) : "";
            //String updatedAt = (currentRepository.getUpdatedAt() != null) ? dateFormat.format(currentRepository.getUpdatedAt()) : "";
            // Set on recycler item click listener

            this.cardviewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i(TAG, "RecyclerView on click listener for repository " + currentRepository.getName());
                    listener.onRepositoryClick(currentRepository);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onRepositoryClick(GitRepositoryBasicModel repository);
    }
}
