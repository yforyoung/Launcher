package com.example.y.launcher.adapter;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.y.launcher.R;
import java.util.List;

public class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.ViewHolder> {
    private List<ScanResult> wifiList;
    private OnItemClickListener onItemClickListener;

    public WifiAdapter(List<ScanResult> wifiList) {
        this.wifiList = wifiList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wifi, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, (Integer) v.getTag());
                }
            }
        });

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ScanResult sr = wifiList.get(position);
        holder.wifiName.setText(sr.SSID);
        sr.level= WifiManager.calculateSignalLevel(sr.level,4);
        setWifiIcon(holder, sr.level, sr.capabilities);
        holder.itemView.setTag(position);
    }

    private void setWifiIcon(ViewHolder holder, int level, String capabilities) {
        if (capabilities.contains("WPA")) {
            holder.wifiState.setText("加密");
            switch (level) {
                case 0:
                    holder.wifiIc.setImageResource(R.drawable.ic_wifi_1_bar_lock);
                    break;
                case 1:
                    holder.wifiIc.setImageResource(R.drawable.ic_wifi_2_bar_lock);
                    break;
                case 2:
                    holder.wifiIc.setImageResource(R.drawable.ic_wifi_3_bar_lock);
                    break;
                case 3:
                    holder.wifiIc.setImageResource(R.drawable.ic_wifi_full_lock);
                    break;
            }
        } else {
            holder.wifiState.setText("开放");
            switch (level) {
                case 0:
                    holder.wifiIc.setImageResource(R.drawable.ic_wifi_1_bar);
                    break;
                case 1:
                    holder.wifiIc.setImageResource(R.drawable.ic_wifi_2_bar);
                    break;
                case 2:
                    holder.wifiIc.setImageResource(R.drawable.ic_wifi_3_bar);
                    break;
                case 3:
                    holder.wifiIc.setImageResource(R.drawable.ic_wifi_full_unlock);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return wifiList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView wifiIc;
        TextView wifiName, wifiState;

        ViewHolder(View itemView) {
            super(itemView);
            wifiIc = itemView.findViewById(R.id.wifi_icon);
            wifiName = itemView.findViewById(R.id.wifi_name);
            wifiState = itemView.findViewById(R.id.wifi_state);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int tag);
    }
}
