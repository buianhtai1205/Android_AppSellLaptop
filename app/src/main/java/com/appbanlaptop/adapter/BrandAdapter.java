package com.appbanlaptop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appbanlaptop.R;
import com.appbanlaptop.model.Brand;
import com.bumptech.glide.Glide;

import java.util.List;

public class BrandAdapter extends BaseAdapter {
    List<Brand> arrayBrand;
    Context context;

    public BrandAdapter(List<Brand> arrayBrand, Context context) {
        this.arrayBrand = arrayBrand;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrayBrand.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public class ViewHolder {
        TextView name;
        ImageView image_url;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_brand, null);
            viewHolder.name = view.findViewById(R.id.item_brand_name);
            viewHolder.image_url = view.findViewById(R.id.item_brand_image);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();

        }
        viewHolder.name.setText(arrayBrand.get(i).getName());
        Glide.with(context).load(arrayBrand.get(i).getImage_url()).into(viewHolder.image_url);
        return view;
    }
}
