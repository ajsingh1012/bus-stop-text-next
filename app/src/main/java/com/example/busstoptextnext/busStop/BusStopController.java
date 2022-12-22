package com.example.busstoptextnext.busStop;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class BusStopController {
    private final String TAG = "BusStopController";
    private final String fireStorePath = "BusStops";

    private FirebaseFirestore db;
    private CollectionReference busStopCollection;
    private ArrayList<BusStop> busStops;
    private ArrayList<Boolean> selection;
    private String userPath, phoneNumber;

    public BusStopController(FirebaseFirestore db, String userPath) {
        this.db = db;
        this.busStopCollection = db.collection(userPath + "/" + fireStorePath);
        this.busStops = new ArrayList<>();
        this.selection = new ArrayList<>();
        this.userPath = userPath;
        getNumberFromDB();
    }

    public void addDataUpdateSnapshotListener(BusStopAdapter busStopAdapter){
        busStopCollection.addSnapshotListener((queryDocumentSnapshots, error) -> {
            // Clear the old list
            busStops.clear();
            selection.clear();

            if (queryDocumentSnapshots == null) {
                return;
            }
            for(QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                BusStop busStopToAdd = doc.toObject(BusStop.class);
                busStops.add(busStopToAdd); // Adding the bus stop attributes from FireStore
                selection.add(false);
            }

            busStopAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
        });
    }

    public void getNumberFromDB() {
        db.document(userPath).get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            phoneNumber = (String) document.getData().get("localNo");
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
        ).addOnFailureListener(e -> {
            Log.e(TAG, e.getMessage());
        });
    }

    public void setNumber(String number) {
        if(number.isBlank() || number.length() < 5) {
            throw new IllegalArgumentException("Phone number must be at least 5 digits!");
        }

        this.phoneNumber = number;
        db.document(userPath).update("localNo", number);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void addEdit(BusStop busStop){
        // Adds if id is null else edits
        String id = busStop.getNumber();
        if (id == null){
            UUID uuid = UUID.randomUUID();
            id = uuid.toString();
            busStop.setNumber(id);
        }
        busStopCollection.document(id).set(busStop);
    }

    public void delete(int position) {

        // Get the swiped item at a particular position.
        BusStop deletedStop = busStops.get(position);
        String description = deletedStop.getDescription();
        String number = deletedStop.getNumber();
        busStopCollection
                .document(number)
                .delete()
                .addOnSuccessListener(unused -> Log.i(TAG, "Successfully deleted stop: " + description));
    }

    public BusStop getStop(int position) {
        return busStops.get(position);
    }

    public void setBusStops(ArrayList<BusStop> busStops) {
        this.busStops = busStops;
    }

    public boolean setSelection(int i, boolean toSet) {
        clearSelection();
        // set selection to i
        selection.set(i, toSet);

        Log.i(TAG, Arrays.toString(selection.toArray()));

        // return whether a selection is currently made or not
        return toSet;
    }

    public int getSelectionPosition() {
        for(int i = 0; i < selection.size(); i++) {
            if(selection.get(i)) {
                return i;
            }
        }
        return -1;
    }

    public boolean getSelection(int i) {
        return selection.get(i);
    }

    public void clearSelection() {
        // clear all
        for(int j = 0; j < selection.size(); j++) {
            selection.set(j, false);
        }
    }

    public int size() {
        return busStops.size();
    }
}
