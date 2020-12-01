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

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ap.behrouzi.smartr.R;
import ap.behrouzi.smartr.dataModels.Reminders;

public class RemindersAdapter extends RecyclerView.Adapter<RemindersAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Reminders> reminders;
    private onItemCheckedListener checkedListener;
    private onDeletedListener deletedListener;
    public RemindersAdapter(Context context, ArrayList<Reminders> reminder) {
        this.reminders = reminder;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.reminder_card, parent, false);
        return new MyViewHolder(view, checkedListener,deletedListener, this.reminders);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.reminderName.setText(reminders.get(position).getName());
        holder.reminderDateTime.setText(reminders.get(position).getDate() + reminders.get(position).getTime());
        Log.e("LIST", "onBindViewHolder: " + reminders.get(position).getDone());
        holder.isDone.setChecked(reminders.get(position).getDone().equals("yes"));
        if (holder.isDone.isChecked()) {
            holder.reminderBackgroundLinearLayout.setBackgroundColor(Color.rgb(33,150,243));
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

        public MyViewHolder(@NonNull View itemView,final onItemCheckedListener listener,onDeletedListener deletedListener, final ArrayList<Reminders> reminders) {
            super(itemView);
            reminderName = itemView.findViewById(R.id.reminder_name);
            reminderDateTime = itemView.findViewById(R.id.reminder_date_time);
            isDone = itemView.findViewById(R.id.completed_check_box);
            delete = itemView.findViewById(R.id.delete_reminder);
            reminderBackgroundLinearLayout = itemView.findViewById(R.id.reminder_background);

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
}
