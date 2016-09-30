package com.example.n.lotte_jjim.Activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.n.lotte_jjim.Activity.DataObject.OrderListDO;
import com.example.n.lotte_jjim.R;

import java.util.ArrayList;


public class OrderListAdapter extends BaseAdapter {

    private static final String TAG = "OrderListAdapter";

    Context mContext;
    int mLayout;
    ArrayList<OrderListDO> mOrderListDO;
    LayoutInflater mInflater;

    //rasp: 버튼 클릭 리스너
    public interface onButtonClickListener {
        void onBtnConfirmOrderList (OrderListDO item);
        void onBtnDeleteOrderList (OrderListDO item);
    }

    private onButtonClickListener adptCallback = null;

    public void setOnClickListener(onButtonClickListener callback) {
        adptCallback = callback;
    }


    public OrderListAdapter(Context mContext, int mLayout, ArrayList<OrderListDO> mOrderListDO) {
        this.mContext = mContext;
        this.mLayout = mLayout;
        this.mOrderListDO = mOrderListDO;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {

        return mOrderListDO.size();
    }

    @Override
    public Object getItem(int position) {

        return mOrderListDO.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.list_orderlist, null);
        }

        //: 리스트 뷰 번호 받아오기
        final OrderListDO mOrderListData = mOrderListDO.get(position);
        if (mOrderListData != null) {
            ImageButton mBtn_OrderListConfirm = (ImageButton)convertView.findViewById(R.id.mBtn_OrderListConfirm);
            ImageButton mBtn_OrderListDelete = (ImageButton)convertView.findViewById(R.id.mBtn_OrderListDelete);

            mBtn_OrderListConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (adptCallback != null) {
                        adptCallback.onBtnConfirmOrderList(mOrderListData);
                    }
                }
            });
            mBtn_OrderListDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (adptCallback != null) {
                        adptCallback.onBtnDeleteOrderList(mOrderListData);
                    }
                }
            });

            mBtn_OrderListConfirm.setFocusable(false);
            mBtn_OrderListDelete.setFocusable(false);
        }


        TextView mOrderNo = (TextView)convertView.findViewById(R.id.mText_OrderNo);
        TextView mOrderListStoreName = (TextView)convertView.findViewById(R.id.mText_OrderStoreName);
        TextView mOrderPrice = (TextView)convertView.findViewById(R.id.mText_OrderPrice);

        mOrderNo.setText(mOrderListDO.get(position).GetOrderNo());
        mOrderListStoreName.setText(mOrderListDO.get(position).GetOrderStoreName());
        mOrderPrice.setText(mOrderListDO.get(position).GetOrderSalesPrice());

        return convertView;
    }
}