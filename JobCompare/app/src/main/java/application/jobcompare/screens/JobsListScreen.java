package application.jobcompare.screens;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import application.jobcompare.db.DbManager;
import application.jobcompare.models.Job;
import application.jobcompare.R;

public class JobsListScreen extends ActionScreen {
    private static JobsListScreen instance;

    protected RecyclerView recyclerView;

    protected JobAdapter jobAdapter;

    protected Button compareButton;

    // Public method to get the singleton instance
    public static JobsListScreen getInstance() {
        if (instance == null) {
            synchronized (JobsListScreen.class) {
                if (instance == null) {
                    instance = new JobsListScreen();
                }
            }
        }
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_jobs_list);
        super.onCreate(savedInstanceState);

        compareButton = findViewById(R.id.compareButton);
        compareButton.setEnabled(false);
        recyclerView = findViewById(R.id.listView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        ArrayList<Job> jobs = DbManager.getInstance().getJobs();
        jobAdapter = new JobAdapter(this, jobs);
        recyclerView.setAdapter(jobAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                if (swipeDir == ItemTouchHelper.LEFT) {
                    jobAdapter.removeItem(viewHolder.getAdapterPosition(), viewHolder);
                    jobAdapter.notifyDataSetChanged();
                } else if (swipeDir == ItemTouchHelper.RIGHT) {
                    jobAdapter.editItem(viewHolder.getAdapterPosition());
                    startActivity(new Intent(JobsListScreen.this, JobOfferScreen.class));
                    jobAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }

            }

            @Override
            public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (dX == 0) {
                    super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
                View itemView = viewHolder.itemView;
                Drawable icon;
                Paint backgroundPaint = new Paint();
                float itemViewLeft, itemViewRight = 0;
                int iconSize, iconMargin, iconLeft, iconRight, iconTop, iconBottom = 0;

                if (dX < 0) {
                    // Swipe Right: Delete.
                    icon = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.ic_baseline_delete_24_white);
                    backgroundPaint.setColor(Color.RED);

                    itemViewLeft = itemView.getRight() + dX;
                    itemViewRight = (float)itemView.getRight();
                } else {
                    // Swipe Left: Edit.
                    icon = ContextCompat.getDrawable(recyclerView.getContext(), R.drawable.ic_baseline_edit_24_white);
                    backgroundPaint.setColor(Color.BLUE);

                    itemViewLeft = itemView.getLeft() + dX;
                    itemViewRight = (float)itemView.getLeft();
                }

                // Draw the background
                canvas.drawRect(
                        itemViewLeft,
                        itemView.getTop(),
                        itemViewRight,
                        itemView.getBottom(),
                        backgroundPaint
                );

                // Calculate position of the icon
                iconSize = icon.getIntrinsicWidth();
                iconMargin = (itemView.getHeight() - iconSize) / 2;
                iconTop = itemView.getTop() + (itemView.getHeight() - iconSize) / 2;
                iconBottom = iconTop + iconSize;

                if (dX < 0) {
                    iconLeft = itemView.getRight() - iconMargin - iconSize;
                    iconRight = itemView.getRight() - iconMargin;
                } else {
                    iconLeft = itemView.getLeft() + iconMargin;
                    iconRight = itemView.getLeft() + iconMargin + iconSize;
                }

                // Draw the icon
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                icon.draw(canvas);

                super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);

        if (compareButton != null) {
            if (DbManager.getInstance().getSelectedJobs().size() == 2) {
                compareButton.setEnabled(true);
//                compareButton.setBackground(R.drawable.button_primary);
            }
            compareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!JobsComparisonScreen.isDisabled()) {
                        startActivity(new Intent(JobsListScreen.this, JobsComparisonScreen.class));
                    }
                }
            });
        }
    }

    @Override
    public void cancel() {
        DbManager.getInstance().resetSelected();
        super.cancel();
    }
}
