package ap.behrouzi.smartr.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ap.behrouzi.smartr.AddNormalReminderActivity;
import ap.behrouzi.smartr.R;

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
        AppCompatTextView appCompatTextView = view.findViewById(R.id.sec);
        appCompatTextView.setText("Hi: " + getArguments().getInt("day",0));
        FloatingActionButton floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener( v-> {
            Intent intent = new Intent(getActivity(), AddNormalReminderActivity.class);
            intent.putExtra("from", getArguments().getInt("day", 0));
            startActivity(intent);
        });
        return view;
    }
}
