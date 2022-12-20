package com.example.busstoptextnext.busStop;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.busstoptextnext.R;

import java.util.ArrayList;
import java.util.HashMap;

public class BusStopAdapter extends RecyclerView.Adapter<BusStopAdapter.ViewHolder> {
    private BusStopController busStopController;
    private boolean isHome;
    private Context context;
    private OnClickInterface onClickInterface;
    private HashMap<Integer, BusStopAdapter.ViewHolder> holderList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView description, number;
        private final View content;
        //private boolean isSelected;

        public ViewHolder(View view) {
            super(view);
            description = view.findViewById(R.id.descriptionText);
            number = view.findViewById(R.id.idText);
            content = view;
            //isSelected = false;
        }

        public TextView getDescription() {
            return description;
        }

        public TextView getNumber() {
            return number;
        }

        public View getContent() {
            return content;
        }

        //public boolean getSelected() { return isSelected; }

        //public void setSelected(boolean isSelected) { this.isSelected = isSelected; }
    }

    public BusStopAdapter(BusStopController busStopController, Context context) {
        this.busStopController = busStopController;
        this.isHome = false;
        this.context = context;
    }

    public BusStopAdapter(BusStopController busStopController, Context context, OnClickInterface onClickInterface) {
        this(busStopController, context);
        //this.busStopController = busStopController;
        this.isHome = true;
        //this.context = context;
        this.onClickInterface = onClickInterface;
        this.holderList = new HashMap<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.bus_stop_in_list, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        if(isHome) {
            if (!holderList.containsKey(position)) {
                holderList.put(position, viewHolder);
            }
        }

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getDescription().setText(busStopController.getStop(position).getDescription());
        viewHolder.getNumber().setText(busStopController.getStop(position).getNumber());

        if(isHome) {
            showSelection(viewHolder, position);

            viewHolder.getContent().setOnClickListener(l -> {
                clearSelection();
                boolean isSelected = busStopController.setSelection(position, !busStopController.getSelection(position));
                if(isSelected) {
                    this.onClickInterface.onClick(position);
                } else {
                    this.onClickInterface.onClick(-1);
                }
                showSelection(viewHolder, position);
                notifyDataSetChanged();
            });
        }
    }

    @Override
    public int getItemCount() {
        return busStopController.size();
    }

    public void clearSelection() {
        for(int pos : holderList.keySet()) {
            holderList.get(pos).getContent().setBackgroundColor(Color.TRANSPARENT);
        }
        //busStopController.clearSelection();
        this.onClickInterface.onClick(-1);
    }

    public void showSelection(ViewHolder viewHolder, int position) {
        //if (viewHolder.getSelected()) {
        if (busStopController.getSelection(position)) {
            viewHolder.getContent().setBackgroundColor(context.getResources().getColor(R.color.purple_500));
            //this.onClickInterface.onClick(position);
        } else {
            viewHolder.getContent().setBackgroundColor(Color.TRANSPARENT);
            //holderList.get(position).getContent().setBackgroundColor(Color.TRANSPARENT);
            //this.onClickInterface.onClick(-1);
        }
    }
}
