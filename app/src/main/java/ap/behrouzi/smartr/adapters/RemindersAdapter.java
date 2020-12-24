package ap.behrouzi.smartr.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Random;

import ap.behrouzi.smartr.R;
import ap.behrouzi.smartr.dataModels.Reminders;

public class RemindersAdapter extends RecyclerView.Adapter<RemindersAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Reminders> reminders;
    private onItemCheckedListener checkedListener;
    private onDeletedListener deletedListener;
    private onEditListener editListener;
    private int rnd = 0;
    public RemindersAdapter(Context context, ArrayList<Reminders> reminder) {
        this.reminders = reminder;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        rnd = (int) (Math.random()*10000+1)%2;
        if (rnd == 1) {
            view = inflater.inflate(R.layout.reminder_card, parent, false);
        }else {
            view = inflater.inflate(R.layout.reminder_card2, parent, false);
        }
        return new MyViewHolder(view, checkedListener,deletedListener, this.reminders, editListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.reminderName.setText(reminders.get(position).getName());
        holder.reminderDateTime.setText(reminders.get(position).getTime());
        Log.e("LIST", "onBindViewHolder: " + reminders.get(position).getDone());
        holder.isDone.setChecked(reminders.get(position).getDone().equals("yes"));
        if (holder.isDone.isChecked()) {
            holder.reminderBackgroundLinearLayout.setBackgroundColor(Color.rgb(33,150,243));
        }

        if (!reminders.get(position).getIsLocational().equals("yes")) {
            holder.edit.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView reminderName, reminderDateTime;
        private AppCompatCheckBox isDone;
        private AppCompatImageButton delete;
        private LinearLayout reminderBackgroundLinearLayout;
        private AppCompatImageButton edit;

        public MyViewHolder(@NonNull View itemView,final onItemCheckedListener listener,onDeletedListener deletedListener, final ArrayList<Reminders> reminders, onEditListener editListener) {
            super(itemView);
            if (rnd == 1) {
                reminderName = itemView.findViewById(R.id.reminder_name);
                reminderDateTime = itemView.findViewById(R.id.reminder_date_time);
                isDone = itemView.findViewById(R.id.completed_check_box);
                delete = itemView.findViewById(R.id.delete_reminder);
                reminderBackgroundLinearLayout = itemView.findViewById(R.id.reminder_background);
                edit = itemView.findViewById(R.id.edit_reminder);

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(deletedListener != null) {
                            int position = getAdapterPosition();
                            if(position != RecyclerView.NO_POSITION) {
                                deletedListener.onDeleted(reminders.get(position), getAdapterPosition());
                            }
                        }
                    }
                });

                isDone.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onItemChecked(reminders.get(position), isChecked);
                            if (isChecked) {
                                reminderBackgroundLinearLayout.setBackgroundColor(Color.rgb(33,150,243));
                            }else {
                                reminderBackgroundLinearLayout.setBackgroundColor(Color.argb(48,236,236,236));
                            }
                        }
                    }
                });

                edit.setOnClickListener( v -> {
                    if (editListener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            editListener.onEdit(reminders.get(position), position);
                        }
                    }
                });
            }else {
                reminderName = itemView.findViewById(R.id.reminder_name_r2);
                reminderDateTime = itemView.findViewById(R.id.reminder_date_time_r2);
                isDone = itemView.findViewById(R.id.completed_check_box_r2);
                delete = itemView.findViewById(R.id.delete_reminder_r2);
                RelativeLayout reminderBackgroundLinearLayout2 = itemView.findViewById(R.id.reminder_background_r2);
                edit = itemView.findViewById(R.id.edit_reminder_r2);

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(deletedListener != null) {
                            int position = getAdapterPosition();
                            if(position != RecyclerView.NO_POSITION) {
                                deletedListener.onDeleted(reminders.get(position), getAdapterPosition());
                            }
                        }
                    }
                });

                isDone.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if(listener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onItemChecked(reminders.get(position), isChecked);
                            if (isChecked) {
                                reminderBackgroundLinearLayout2.setBackgroundColor(Color.rgb(33,150,243));
                            }else {
                                reminderBackgroundLinearLayout2.setBackgroundColor(Color.argb(48,236,236,236));
                            }
                        }
                    }
                });

                edit.setOnClickListener( v -> {
                    if (editListener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            editListener.onEdit(reminders.get(position), position);
                        }
                    }
                });
            }
        }
    }

    public interface onItemCheckedListener {
        void onItemChecked(Reminders reminders, boolean isChecked);
    }

    public void setOnItemCheckedListener(onItemCheckedListener listener) {
        checkedListener = listener;
    }


    public interface onDeletedListener {
        void onDeleted(Reminders reminders, int position);
    }

    public void setOnDeletedListener(onDeletedListener listener) {
        deletedListener = listener;
    }

    public interface onEditListener {
        void onEdit(Reminders reminders, int position);
    }

    public void setOnEditListener(onEditListener listener) {
        editListener = listener;
    }
}
