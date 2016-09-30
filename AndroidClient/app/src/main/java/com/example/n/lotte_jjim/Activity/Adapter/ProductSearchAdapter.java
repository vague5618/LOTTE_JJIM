package com.example.n.lotte_jjim.Activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.n.lotte_jjim.Activity.DataObject.ProductSearchDO;
import com.example.n.lotte_jjim.R;

import java.util.ArrayList;

public class ProductSearchAdapter extends BaseAdapter {

    private static final String TAG = "ProductSearchAdapter";

    Context mContext;
    int mLayout;
    ArrayList<ProductSearchDO> mProductSearchData;
    LayoutInflater mInflater;

    public ProductSearchAdapter(Context mContext, int mLayout, ArrayList<ProductSearchDO> mProductData) {
        this.mContext = mContext;
        this.mLayout = mLayout;
        this.mProductSearchData = mProductData;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mProductSearchData.size();
    }

    @Override
    public Object getItem(int position) {
        return mProductSearchData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_product_search, null);
        }

        ImageView mProductImg = (ImageView) convertView.findViewById(R.id.mImage_SearchProduct);
        TextView mProductName = (TextView) convertView.findViewById(R.id.mText_ProductName);

        mProductImg.setImageResource(mProductSearchData.get(position).GetProductImgId());
        mProductName.setText(mProductSearchData.get(position).GetProductName());

        return convertView;
    }
}