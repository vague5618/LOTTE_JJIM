package com.example.n.lotte_jjim.Activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.n.lotte_jjim.Activity.DataObject.OrderDetailDO;
import com.example.n.lotte_jjim.R;

import java.util.ArrayList;


public class OrderDetailAdapter extends BaseAdapter {

    private static final String TAG = "OrderListAdapter";

    Context mContext;
    int mLayout;
    ArrayList<OrderDetailDO> mOrderDetailDO;
    LayoutInflater mInflater;


    public OrderDetailAdapter(Context mContext, int mLayout, ArrayList<OrderDetailDO> mOrderDetailDO) {
        this.mContext = mContext;
        this.mLayout = mLayout;
        this.mOrderDetailDO = mOrderDetailDO;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {

        return mOrderDetailDO.size();
    }

    @Override
    public Object getItem(int position) {

        return mOrderDetailDO.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.list_orderdetail, null);
        }


        TextView mProductName = (TextView)convertView.findViewById(R.id.mText_OrderProductName);
        TextView mProductPrice = (TextView)convertView.findViewById(R.id.mText_OrderProductPrice);
        TextView mOrderProductEvent = (TextView)convertView.findViewById(R.id.mText_OrderProductEvent);
        TextView mProductOrderNum = (TextView)convertView.findViewById(R.id.mText_OrderProductNum);
        ImageView  mProductImg = (ImageView)convertView.findViewById(R.id.mImage_OrderProductImg);

        mProductName.setText(mOrderDetailDO.get(position).GetOrderProductName());
        mProductPrice.setText(mOrderDetailDO.get(position).GetOrderProductPrice());
        mOrderProductEvent.setText(mOrderDetailDO.get(position).GetOrderProductEvent());
        mProductOrderNum.setText(mOrderDetailDO.get(position).GetOrderProductNum());
        mProductImg.setImageResource(mOrderDetailDO.get(position).GetOrderProductImg());

        return convertView;
    }
}