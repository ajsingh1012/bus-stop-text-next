package com.example.busstoptextnext.ui.dashboard;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busstoptextnext.R;
import com.example.busstoptextnext.activities.MainActivity;
import com.example.busstoptextnext.busStop.BusStop;
import com.example.busstoptextnext.busStop.BusStopAdapter;
import com.example.busstoptextnext.busStop.BusStopController;
import com.example.busstoptextnext.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private final String TAG = "DashboardFragment";
    private BusStopController busStopController;
    private RecyclerView busStopView;
    private BusStopAdapter busStopAdapter;
    private EditText descriptionEditText, numberEditText;
    private ImageButton addButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Activity a = getActivity();
        if (a == null) {
            Log.i(TAG, "Fragment is not associated with an activity!");
            return;
        }
        busStopController = ((MainActivity)a).getBusStopController();
        busStopView = view.findViewById(R.id.busStopView);
        descriptionEditText = view.findViewById(R.id.editTextTextPersonName);
        numberEditText = view.findViewById(R.id.editTextNumberDecimal);
        addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(l -> {
            busStopController.addEdit(
                    new BusStop(
                            descriptionEditText.getText().toString(),
                            numberEditText.getText().toString()
                    )
            );
            descriptionEditText.setText("");
            numberEditText.setText("");
        });

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        busStopAdapter = new BusStopAdapter(busStopController, view.getContext());
        busStopView.setLayoutManager(manager);
        busStopView.setAdapter(busStopAdapter);

        // drag to delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            /**
             * This method is called when the item is moved
             * @param recyclerView
             * @param viewHolder
             * @param target
             * @return
             */
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            /**
             * Creates swipe to delete functionality
             * @param viewHolder
             * @param direction
             */
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // below line is to get the position
                // of the item at that position.

                int position = viewHolder.getAdapterPosition();
                Log.d(TAG, "Swiped " + busStopController.getStop(position).getDescription() + " at position " + position);
                busStopController.delete(position);
            }
            // at last we are adding this
            // to our recycler view.
        }).attachToRecyclerView(busStopView);

        // on snapshot listener for the collection
        busStopController.addDataUpdateSnapshotListener(busStopAdapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}