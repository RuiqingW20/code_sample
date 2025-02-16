package application.jobcompare.screens;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import application.jobcompare.db.DbManager;
import application.jobcompare.models.Job;
import application.jobcompare.R;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder> {

    private ArrayList<Job> jobs;

    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Context viewHolderContext;
        public TextView jobsListTitle;
        public TextView jobsListCompanyName;
        public TextView jobsListIsCurrentJob;
        public TextView jobsListID;
        public TextView jobsScore;
        public CheckBox jobsIsSelected;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(Context viewHolderCtx, View view) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(view);
            this.viewHolderContext = viewHolderCtx;

            jobsListTitle = view.findViewById(R.id.jobsListTitle);
            jobsListCompanyName = view.findViewById(R.id.jobsListCompanyName);
            jobsListIsCurrentJob = view.findViewById(R.id.jobsListIsCurrentJob);
            jobsListID = view.findViewById(R.id.jobsListID);
            jobsScore = view.findViewById(R.id.jobsScore);
            jobsIsSelected = view.findViewById(R.id.jobsIsSelected);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) {
                Job job = jobs.get(position);

                // Are we selecting or deselecting?
                if (!job.isSelected()) {
                    // Selecting ..

                    // Disallow if we have 2 already selected.
                    if (DbManager.getInstance().getSelectedJobs().size() == 2) {
                        Snackbar.make(view, view.getContext().getResources().getString(R.string.jobs_list_selection_error), Snackbar.LENGTH_LONG).show();
                        return;
                    }
                }

                job.setSelected(!job.isSelected());
                DbManager.getInstance().updateJob(job);
                jobsIsSelected.setChecked(job.isSelected());

                if (JobsComparisonScreen.isDisabled()) {
                    ((JobsListScreen)viewHolderContext).compareButton.setEnabled(false);
                } else {
                    ((JobsListScreen)viewHolderContext).compareButton.setEnabled(true);
                }
            }
        }
    }

    public JobAdapter(Context ctx, ArrayList<Job> items) {
        jobs = items;
        context = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View view = inflater.inflate(R.layout.item_list_job, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(context, view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Job job = jobs.get(position);

        // Set item views based on your views and data model
        viewHolder.jobsListTitle.setText(job.getTitle());
        viewHolder.jobsScore.setText(String.valueOf(job.getScore()));
        viewHolder.jobsListCompanyName.setText(job.getCompanyName());
        if (job.isCurrentJob()) {
            viewHolder.jobsListIsCurrentJob.setVisibility(View.VISIBLE);
        } else {
            viewHolder.jobsListIsCurrentJob.setVisibility(View.INVISIBLE);
        }
        viewHolder.jobsListID.setText(Long.toString(job.getID()));
        viewHolder.jobsIsSelected.setChecked(job.isSelected());
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    public void editItem(int position) {
        JobOfferScreen.jobOffer = jobs.get(position);
    }

    public void removeItem(int position, RecyclerView.ViewHolder viewHolder) {
        Job removedJob = jobs.get(position);
        boolean removed = DbManager.getInstance().deleteJob(removedJob.getID());

        if (removed) {
            jobs.remove(position);
            notifyItemRemoved(position);

            Resources resources = viewHolder.itemView.getContext().getResources();

            Snackbar.make(viewHolder.itemView, removedJob + " " + resources.getString(R.string.removed), Snackbar.LENGTH_LONG).setAction(resources.getString(R.string.undo), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    jobs.add(position, removedJob);
                    DbManager.getInstance().addJob(removedJob);
                    notifyItemInserted(position);
                }
            }).show();
        }
    }

}

