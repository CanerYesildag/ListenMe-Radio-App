package com.example.app.radioapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NextCustomAdapter extends BaseAdapter {
    private Context context;


    public NextCustomAdapter(Context context) {

        this.context = context;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }
    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getCount() {
        return NextActivity.favs.size();
    }

    @Override
    public Object getItem(int position) {
        return NextActivity.favs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lv_item, null, true);
            holder.tvRadio =  convertView.findViewById(R.id.temp);
            //holder.tvFavs= convertView.findViewById(R.id.isfav);
            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }
        holder.tvRadio.setText(NextActivity.favs.get(position).getChannel());
        //holder.tvFavs.setText(String.valueOf(NextActivity.favs.get(position).isFaved()));
        return convertView;
    }

    private class ViewHolder {
        private TextView tvRadio,tvFavs;

    }

}
