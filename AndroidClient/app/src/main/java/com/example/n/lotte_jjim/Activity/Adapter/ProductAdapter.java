package com.example.n.lotte_jjim.Activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.n.lotte_jjim.Activity.Activity.ProductActivity;
import com.example.n.lotte_jjim.Activity.DataObject.ProductDO;
import com.example.n.lotte_jjim.R;

import java.util.ArrayList;


public class ProductAdapter extends BaseAdapter {

    private static final String TAG = "ProductAdapter";

    Context mContext;
    ArrayList<ProductDO> mProductDO;
    LayoutInflater mInflater;

    //rasp: 버튼 클릭 리스너
    public interface onButtonClickListener {
        void onBtnJjim (ProductDO item);
    }

    private onButtonClickListener adptCallback = null;

    public void setOnClickListener(onButtonClickListener callback) {
        adptCallback = callback;
    }


    public ProductAdapter(Context mContext,ArrayList<ProductDO> mProductDO, LayoutInflater mInflater) {
        this.mContext = mContext;
        this.mProductDO = mProductDO;
        this.mInflater = mInflater;
    }

    @Override
    public int getCount() {
        return mProductDO.size();
    }

    @Override
    public Object getItem(int position) {
        return mProductDO.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = mInflater.inflate(R.layout.list_product, null);

        final ProductDO mProductData = mProductDO.get(position);
        if(mProductData != null) {
            ImageButton mBtn_JJim = (ImageButton) convertView.findViewById(R.id.mBtn_Jjim);

            mBtn_JJim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (adptCallback != null) {
                        adptCallback.onBtnJjim(mProductData);
                    }
                }
            });

            mBtn_JJim.setFocusable(false);
        }

        ImageView mProductImg = (ImageView) convertView.findViewById(R.id.mImage_ProductImage);
        TextView mProductName = (TextView) convertView.findViewById(R.id.mText_ProductName);
        TextView mProductStock = (TextView) convertView.findViewById(R.id.mText_ProductStock);
        TextView mProductPrice = (TextView) convertView.findViewById(R.id.mText_ProductPrice);
        TextView mProductEvent = (TextView) convertView.findViewById(R.id.mText_ProductEvent);


        // TODO! 사이즈 줄이는거 ㅠㅠ
        mProductImg.setImageResource(mProductDO.get(position).GetmProductImage());
        mProductName.setText(mProductDO.get(position).GetProductName());
        mProductStock.setText(mProductDO.get(position).GetProductStock());
        mProductPrice.setText(mProductDO.get(position).GetProductPrice());
        mProductEvent.setText(mProductDO.get(position).GetProductEvent());


        for(int j=0; j< ProductActivity.mSelectProductList.size(); j++) {

            if (mProductDO.get(position).GetProductName().equals(ProductActivity.mSelectProductList.get(j).GetProductName())) {
                convertView.setBackgroundColor(mContext.getResources().getColor(R.color.lightGray));
            }
        }

        return convertView;
    }
}