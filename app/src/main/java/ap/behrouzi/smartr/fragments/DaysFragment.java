package ap.behrouzi.smartr.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import ap.behrouzi.smartr.AddNormalReminderActivity;
import ap.behrouzi.smartr.R;
import ap.behrouzi.smartr.adapters.RemindersAdapter;
import ap.behrouzi.smartr.dataModels.Reminders;
import ap.behrouzi.smartr.database.DatabaseHelper;

public class DaysFragment extends Fragment {

    private int day;

    public static DaysFragment newInstance(int day) {
        DaysFragment daysFragment = new DaysFragment();

        Bundle args = new Bundle();
        args.putInt("day", day);
        daysFragment.setArguments(args);

        return daysFragment;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.row_days, container,false);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener( v-> {
            Intent intent = new Intent(getActivity(), AddNormalReminderActivity.class);
            intent.putExtra("from", getArguments().getInt("day", 0));
            startActivity(intent);
        });
        RecyclerView recyclerView = view.findViewById(R.id.days_recycler);

        ArrayList<Reminders> reminders = new ArrayList<>();

        DatabaseHelper db = new DatabaseHelper(getContext());
        Cursor cursor = db.readAllData();
        if (cursor.getCount() == 0) {
            Toast.makeText(getActivity(), "No Data.", Toast.LENGTH_SHORT).show();
        }else {
            while (cursor.moveToNext()) {
                reminders.add(new Reminders(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(7),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(8),
                        cursor.getString(4),
                        cursor.getString(3),
                        cursor.getString(8)
                        )
                );
            }
        }

        RemindersAdapter remindersAdapter = new RemindersAdapter(getContext(),reminders);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));


        recyclerView.setAdapter(remindersAdapter);

        remindersAdapter.setOnItemCheckedListener(new RemindersAdapter.onItemCheckedListener() {
            @Override
            public void onItemChecked(Reminders reminders, boolean isChecked) {
                Toast.makeText(getActivity(), "Checked: " + isChecked, Toast.LENGTH_SHORT).show();
            }
        });

        remindersAdapter.setOnDeletedListener(new RemindersAdapter.onDeletedListener() {
            @Override
            public void onDeleted(Reminders reminders) {
                Toast.makeText(getActivity(), "Delete", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
