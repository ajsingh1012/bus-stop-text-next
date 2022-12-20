package com.example.busstoptextnext.ui.home;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busstoptextnext.R;
import com.example.busstoptextnext.activities.MainActivity;
import com.example.busstoptextnext.busStop.BusStop;
import com.example.busstoptextnext.busStop.BusStopAdapter;
import com.example.busstoptextnext.busStop.BusStopController;
import com.example.busstoptextnext.busStop.OnClickInterface;
import com.example.busstoptextnext.databinding.FragmentHomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.HashMap;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private final String TAG = "HomeFragment";
    private BusStopController busStopController;
    private RecyclerView busStopView;
    private BusStopAdapter busStopAdapter;
    private FloatingActionButton sendMsg;
    private OnClickInterface onClickInterface;
    String selectedStopId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
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
        MainActivity mainActivity = (MainActivity) a;
        busStopController = ((MainActivity)a).getBusStopController();
        busStopView = view.findViewById(R.id.busStopView);
        sendMsg = view.findViewById(R.id.send_message);

        onClickInterface = i -> {
            try {
                selectedStopId = busStopController.getStop(i).getNumber();
            } catch (Exception e) {
                selectedStopId = null;
                Toast.makeText(view.getContext(), "No stop selected", Toast.LENGTH_SHORT);
            }
        };

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        busStopAdapter = new BusStopAdapter(busStopController, view.getContext(), onClickInterface);
        busStopView.setLayoutManager(manager);
        busStopView.setAdapter(busStopAdapter);

        // on snapshot listener for the collection
        busStopController.addDataUpdateSnapshotListener(busStopAdapter);

        sendMsg.setOnClickListener(l -> {
            String phoneNumber = ((MainActivity) a).getPhoneNumber();
            try {
                ActivityCompat.requestPermissions(mainActivity,new String[] { Manifest.permission.SEND_SMS}, 1);
                if(selectedStopId != null) {
                    SmsManager.getDefault().sendTextMessage(phoneNumber, null, selectedStopId, null, null);
                } else {
                    Toast.makeText(view.getContext(), "No stop selected", Toast.LENGTH_SHORT);
                }
            } catch (Exception e) {
                AlertDialog.Builder alertDialogBuilder = new
                        AlertDialog.Builder(view.getContext());
                AlertDialog dialog = alertDialogBuilder.create();
                dialog.setMessage(e.getMessage());
                dialog.show();
            }
            busStopAdapter.clearSelection();
            busStopController.clearSelection();
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void clearSelection() {
        busStopAdapter.clearSelection();
    }
}