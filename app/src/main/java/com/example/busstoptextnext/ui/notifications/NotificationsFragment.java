package com.example.busstoptextnext.ui.notifications;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.SmsManager;
import android.widget.Toast;

import com.example.busstoptextnext.R;
import com.example.busstoptextnext.activities.MainActivity;
import com.example.busstoptextnext.busStop.BusStopAdapter;
import com.example.busstoptextnext.busStop.BusStopController;
import com.example.busstoptextnext.busStop.OnClickInterface;
import com.example.busstoptextnext.databinding.FragmentHomeBinding;
import com.example.busstoptextnext.databinding.FragmentNotificationsBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private final String TAG = "NotificationsFragment";
    private EditText busEditText;
    private Button busUpdateButton;
    private BusStopController busStopController;
    private FloatingActionButton updateNumber;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
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
        busEditText = view.findViewById(R.id.editBusPhone);
        busUpdateButton = view.findViewById(R.id.busNumberButton);

        busUpdateButton.setOnClickListener(view1 -> {
            try {
                busStopController.setNumber(busEditText.getText().toString());
                busEditText.setText("");
            } catch(Exception e) {
                Toast.makeText(view1.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}