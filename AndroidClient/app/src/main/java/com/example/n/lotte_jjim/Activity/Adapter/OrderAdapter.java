package com.example.n.lotte_jjim.Activity.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.n.lotte_jjim.Activity.DataObject.OrderDO;
import com.example.n.lotte_jjim.R;

import java.util.ArrayList;

public class OrderAdapter extends BaseAdapter {

    private static final String TAG = "OrderAdapter";

    Context mContext;
    int mLayout;
    ArrayList<OrderDO> mOrderDO;
    LayoutInflater mInflater;

    //black:
    Button mBtn_OrderNumPlus;
    Button mBtn_OrderNumMinus;
    TextView mOrderInputNum;
    TextView mOrderStoreName;
    TextView mOrderProductName;
    TextView mOrderProductEvent;
    TextView mOrderProductPrice;
    ImageView mOrderProductImage;



    public OrderAdapter(Context mContext, int mLayout, ArrayList<OrderDO> mOrderDO) {
        this.mContext = mContext;
        this.mLayout = mLayout;
        this.mOrderDO = mOrderDO;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mOrderDO.size();
    }

    @Override
    public Object getItem(int position) {
        return mOrderDO.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        convertView = mInflater.inflate(R.layout.list_order, null);

        mOrderInputNum = (TextView) convertView.findViewById(R.id.mText_OrderNum);
        mBtn_OrderNumPlus = (Button) convertView.findViewById(R.id.mBtn_Plus);
        mBtn_OrderNumMinus = (Button) convertView.findViewById(R.id.mBtn_Minus);

        mOrderStoreName = (TextView) convertView.findViewById(R.id.mText_CurrentStore);
        mOrderProductName = (TextView) convertView.findViewById(R.id.mText_CurrentProduct);
        mOrderProductEvent = (TextView) convertView.findViewById(R.id.mText_CurrentEvent);
        mOrderProductPrice = (TextView) convertView.findViewById(R.id.mText_CurrentPrice);
        mOrderProductImage = (ImageView) convertView.findViewById(R.id.mImage_CurrentProductImg);

        mOrderInputNum.setText(mOrderDO.get(position).GetOrderInputNum());
        mOrderStoreName.setText(mOrderDO.get(position).GetOrderStoreName());
        mOrderProductName.setText(mOrderDO.get(position).GetOrderProductName());
        mOrderProductEvent.setText(mOrderDO.get(position).GetOrderProductEvent());
        mOrderProductPrice.setText(mOrderDO.get(position).GetOrderProductPrice());
        mOrderProductImage.setImageResource(mOrderDO.get(position).GetOrderProductImgId());


        final OrderDO mOrderData = mOrderDO.get(position);

        if (mOrderData != null) {
            mBtn_OrderNumPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int mMaxInput = Integer.parseInt(mOrderData.GetOrderProductStock());
                    int mTmpInput = Integer.parseInt(mOrderData.GetOrderInputNum());

                    if (mTmpInput < mMaxInput) {
                        mTmpInput++;
                        Log.d(TAG, String.valueOf(mTmpInput));
                        mOrderData.SetOrderInputNum(String.valueOf(mTmpInput));
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(v.getContext(), "주문 가능한 수량을 초과하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            mBtn_OrderNumMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v ) {

                    int mTmpInput = Integer.parseInt(mOrderData.GetOrderInputNum());

                    if (mTmpInput > 0) {
                        mTmpInput--;
                        Log.d(TAG, String.valueOf(mTmpInput));
                        mOrderData.SetOrderInputNum(String.valueOf(mTmpInput));
                        notifyDataSetChanged();
                    }
                }
            });

            mBtn_OrderNumPlus.setFocusable(false);
            mBtn_OrderNumMinus.setFocusable(false);
        }
        return convertView;
    }
}

