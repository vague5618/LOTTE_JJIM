package com.example.n.lotte_jjim.Activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.n.lotte_jjim.Activity.DataObject.JJimDO;
import com.example.n.lotte_jjim.R;

import java.util.ArrayList;


public class JJimAdapter extends BaseAdapter {

    private static final String TAG = "JJimAdapter";

    Context mContext;
    int mLayout;
    ArrayList<JJimDO> mJJimDO;
    LayoutInflater mInflater;

    //rasp: 버튼 클릭 리스너
    public interface onButtonClickListener {
        void onBtnDeleteJJim (JJimDO item);
    }

    private onButtonClickListener adptCallback = null;

    public void setOnClickListener(onButtonClickListener callback) {
        adptCallback = callback;
    }
    //rasp:


    public JJimAdapter(Context mContext, int mLayout, ArrayList<JJimDO> mJJimDO) {

        this.mContext = mContext;
        this.mLayout = mLayout;
        this.mJJimDO = mJJimDO;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mJJimDO.size();
    }

    @Override
    public Object getItem(int position) {
        return mJJimDO.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.list_jjim, null);
        }

        //: 리스트 뷰 번호 받아오기
        final JJimDO mJJimData = mJJimDO.get(position);
        if (mJJimData != null) {
            ImageButton mBtn_JJimDelete = (ImageButton)convertView.findViewById(R.id.mBtn_JJimDelete);

            mBtn_JJimDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (adptCallback != null) {
                        adptCallback.onBtnDeleteJJim(mJJimData);
                    }
                }
            });


            mBtn_JJimDelete.setFocusable(false);
        }

        TextView mJJimStoreName = (TextView)convertView.findViewById(R.id.mText_JJimStore);
        TextView mJJimProductName = (TextView)convertView.findViewById(R.id.mText_JJimProduct);
        ImageView mJJimProductImg = (ImageView)convertView.findViewById(R.id.mImage_JJimProductImg);

        mJJimStoreName.setText(mJJimDO.get(position).GetJJimStoreName());
        mJJimProductName.setText(mJJimDO.get(position).GetJJimProductName());
        mJJimProductImg.setImageResource(mJJimDO.get(position).GetJJimProductImgId());

        return convertView;
    }
}
