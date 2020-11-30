package ap.behrouzi.smartr.fragments;

import android.content.Context;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

import ap.behrouzi.smartr.AddNormalReminderActivity;
import ap.behrouzi.smartr.R;
import ap.behrouzi.smartr.adapters.RemindersAdapter;
import ap.behrouzi.smartr.dataModels.Reminders;
import ap.behrouzi.smartr.database.DatabaseHelper;

public class DaysFragment extends Fragment {

    private int day;
    private RecyclerView recyclerView;
    private ArrayList<Reminders> reminders;
    private DatabaseHelper db;
    private RemindersAdapter remindersAdapter;
    private LottieAnimationView lottieAnimationView;
    private AppCompatTextView   emptyTextView;
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
        lottieAnimationView = view.findViewById(R.id.empty_animations);
        emptyTextView = view.findViewById(R.id.empty_text);
        recyclerView = view.findViewById(R.id.days_recycler);

        reminders = new ArrayList<>();

        db = new DatabaseHelper(getContext());
        Cursor cursor = db.readAllData();
        if (cursor.getCount() == 0) {
            lottieAnimationView.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.VISIBLE);
        }else {
            lottieAnimationView.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.GONE);
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
                        cursor.getString(8),
                        cursor.getInt(0)
                        )
                );
            }
        }

        remindersAdapter = new RemindersAdapter(getContext(),reminders);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));


        recyclerView.setAdapter(remindersAdapter);

        remindersAdapter.setOnItemCheckedListener((reminders, isChecked) ->{
            db.changeAlarmStatus(reminders.getId(), (isChecked)?"yes":"no");
        });

        remindersAdapter.setOnDeletedListener((reminders1, position) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
            builder.setTitle("پاک کردن هشدار")
                    .setMessage("درصورت حذف کردن هشدار شما قادر به برگرداندن آن نیستید!")
                    .setPositiveButton("حذف", (dialog, which) -> {
                        Toast.makeText(getActivity(), "هشدار " + reminders1.getName()  + " با موفقیت پاک شد. ", Toast.LENGTH_SHORT).show();
                        db.removeRow(reminders1.getId());
                        reminders.remove(position);
                        remindersAdapter.notifyItemRemoved(position);
                        remindersAdapter.notifyItemRangeChanged(position, reminders.size());
                        onResume();
                    })
                    .setNegativeButton("انصراف", (dialog, which) -> {

                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        reminders.clear();

        Cursor cursor = db.readAllData();
        if (cursor.getCount() == 0) {
            lottieAnimationView.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.VISIBLE);
        }else {
            lottieAnimationView.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.GONE);
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
                                cursor.getString(8),
                                cursor.getInt(0)
                        )
                );
            }
        }

        recyclerView.setAdapter(remindersAdapter);
    }
}
