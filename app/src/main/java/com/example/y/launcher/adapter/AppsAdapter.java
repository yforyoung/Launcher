package com.example.y.launcher.adapter;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.y.launcher.R;
import com.example.y.launcher.util.AnimateUtil;

import java.util.List;

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.ViewHolder> implements View.OnFocusChangeListener {

    private List<ResolveInfo> infos;
    private PackageManager manager;
    private OnItemClickListener onItemClickListener;

    public AppsAdapter(List<ResolveInfo> infos, PackageManager manager) {
        this.infos = infos;
        this.manager = manager;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        AnimateUtil.onFocusAnimation(v,hasFocus);
    }

    public interface OnItemClickListener{
        void onItemClick(View v,int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_application,parent,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener!=null){
                    onItemClickListener.onItemClick(v, (Integer) v.getTag());
                }
            }
        });
        view.setOnFocusChangeListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.appName.setText(infos.get(position).loadLabel(manager));
        holder.appIcon.setImageDrawable(infos.get(position).loadIcon(manager));
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return infos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView appIcon;
        TextView appName;
        ViewHolder(View itemView) {
            super(itemView);
            appIcon=itemView.findViewById(R.id.app_icon);
            appName=itemView.findViewById(R.id.app_name);
        }
    }
}
