package com.example.n.lotte_jjim.Activity.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.n.lotte_jjim.Activity.Activity.OrderActivity1;
import com.example.n.lotte_jjim.Activity.Activity.ProductActivity;
import com.example.n.lotte_jjim.Activity.Adapter.ProductAdapter;
import com.example.n.lotte_jjim.Activity.DataObject.ProductDO;
import com.example.n.lotte_jjim.Activity.GetInfo.GetOrderInfo;
import com.example.n.lotte_jjim.Activity.Util.HttpUtil;
import com.example.n.lotte_jjim.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlaceholderFragment  extends Fragment {

    private static final String TAG = "PlaceholderFragment";

    public RelativeLayout mLayoutProductList;
    public ArrayList<Boolean> mPositionLongCheck;
    private ArrayList<ProductDO> mProductData;
    private View mRootView;
    boolean mShortClickPossible;

    ProductAdapter mProductAdapter;
    ListView mProductListView;
    ArrayList<GetOrderInfo> mPushIntent_Order;

    JSONArray mJJimAddArray;
    JSONArray mOrderNumArray;


    //: fragment argument(ARG)는 섹션 번호를 나타냄
    private static final String ARG_SECTION_NUMBER = "section_number";

    //: 주어진 ARG로 fragment의 instance 반환
    public static PlaceholderFragment newInstance(int sectionNumber) {

        PlaceholderFragment fragment = new PlaceholderFragment();


        Bundle args = new Bundle();

        Log.d(TAG, String.valueOf(sectionNumber));
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
    }

    public PlaceholderFragment() {
        mRootView = null;
        ListLongClickCheck();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_product, container, false);
        Bundle bundle = getArguments();

        int section_number = bundle.getInt(ARG_SECTION_NUMBER);

        Set_AdapterToList(rootView, inflater);

        addProduct(rootView, section_number, inflater);
        Log.d(TAG, "create View");

        return rootView;
    }


    public void Set_AdapterToList(final View rootView, LayoutInflater inflater) {

        mRootView = rootView;
        mProductListView = (ListView) rootView.findViewById(R.id.mList_Product);
        mLayoutProductList = (RelativeLayout) rootView.findViewById(R.id.mLayout_ListProduct);

        ListLongClick();
        ListShortClick();
    }

    public void ListShortClick() {

        mProductListView.setOnItemClickListener(ListViewItemClickListener);
    }

    private AdapterView.OnItemClickListener ListViewItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View clickedView, int position, long id) {

            mPushIntent_Order = new ArrayList<GetOrderInfo>();

            mShortClickPossible = true;
            for (int i = 0; i < 50; i++) {
                if (mPositionLongCheck.get(i) == true) {
                    mShortClickPossible = false;
                }
            }

            if (mShortClickPossible) {
                if (mProductData.get(position).GetProductStock().equals("0")) {
                    CheckState("재고가 없는 상품입니다. 예약 불가합니다.");
                } else {
                        // 02.21 rasp: TODO! Arraylist로 인텐트 넘기는 부분 추가, 테스트 필요

                        String tmp_StoreName = ProductActivity.mGetIntent_StoreName;
                        String tmp_ProductID = mProductData.get(position).GetmProductId();
                        String tmp_ProductName = mProductData.get(position).GetProductName();
                        String tmp_ProductPrice = mProductData.get(position).GetProductPrice();
                        String tmp_StoreEvent = mProductData.get(position).GetProductEvent();
                        String tmp_StoreStock = mProductData.get(position).GetProductStock();

                        mPushIntent_Order.add(new GetOrderInfo(tmp_StoreName, tmp_ProductID, tmp_ProductName, tmp_ProductPrice, tmp_StoreEvent, tmp_StoreStock));

                        Intent mIntent_SelectProduct = new Intent(adapterView.getContext(), OrderActivity1.class);
                        mIntent_SelectProduct.putParcelableArrayListExtra("OrderInfo", mPushIntent_Order);

                        startActivity(mIntent_SelectProduct);
                    }
                }
            }
        }

        ;


        public void ListLongClickCheck() {

            mPositionLongCheck = new ArrayList<Boolean>();

            for (int i = 0; i < 50; i++) {
                mPositionLongCheck.add(false);
            }
        }


        public void ListLongClick() {

            mProductListView.setOnItemLongClickListener(ListViewItemLongClickListener);
        }

        private AdapterView.OnItemLongClickListener ListViewItemLongClickListener = new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                String tmp_StoreName = ProductActivity.mGetIntent_StoreName;
                String tmp_ProductID = mProductData.get(position).GetmProductId();
                String tmp_ProductName = mProductData.get(position).GetProductName();
                String tmp_ProductPrice = mProductData.get(position).GetProductPrice();
                String tmp_StoreEvent = mProductData.get(position).GetProductEvent();
                String tmp_StoreStock = mProductData.get(position).GetProductStock();

                GetOrderInfo mCurrent_OrderInfo = new GetOrderInfo(tmp_StoreName, tmp_ProductID, tmp_ProductName, tmp_ProductPrice, tmp_StoreEvent, tmp_StoreStock);

                Log.d(TAG, "롱클릭갯수확인: " + String.valueOf(ProductActivity.mSelectProductList.size()));

                if (!mProductData.get(position).GetProductStock().equals("0")) {
                    if (mPositionLongCheck.get(position) == false) {
                        if (ProductActivity.mSelectProductList.size() == 0) {
                            ProductActivity.fab.show();

                            ProductActivity.mSelectProductList.add(mCurrent_OrderInfo);
                            view.setBackgroundColor(Color.LTGRAY);
                            mPositionLongCheck.set(position, true);
                            ProductActivity.mHandler.sendEmptyMessage(1);
                        } else {
                            boolean tmpDup = false;
                            for (int k = 0; k < ProductActivity.mSelectProductList.size(); k++) {
                                if (mCurrent_OrderInfo.GetProductName().equals(ProductActivity.mSelectProductList.get(k).GetProductName())) {
                                    tmpDup = true;
                                    ProductActivity.mSelectProductList.remove(k);

                                    if(ProductActivity.mSelectProductList.size() == 0) {
                                        ProductActivity.fab.hide();
                                    }
                                    view.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                                    mPositionLongCheck.set(position, false);
                                    ProductActivity.mHandler.sendEmptyMessage(1);
                                }
                            }

                            if(!tmpDup) {
                                ProductActivity.mSelectProductList.add(mCurrent_OrderInfo);
                                view.setBackgroundColor(Color.LTGRAY);
                                mPositionLongCheck.set(position, true);
                                ProductActivity.mHandler.sendEmptyMessage(1);
                            }
                        }
                    } else {
                        if (ProductActivity.mSelectProductList.size() == 1) {
                            ProductActivity.fab.hide();
                        }
                        for (int k = 0; k < ProductActivity.mSelectProductList.size(); k++) {
                            if (mCurrent_OrderInfo.GetProductName().equals(ProductActivity.mSelectProductList.get(k).GetProductName())) {
                                ProductActivity.mSelectProductList.remove(k);
                            }
                        }
                        view.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                        mPositionLongCheck.set(position, false);
                        ProductActivity.mHandler.sendEmptyMessage(1);
                    }
                } else {
                    CheckState("재고가 없는 상품입니다. 예약 불가합니다.");
                }
                return true;
            }
        };

        public void ListButtonClick() {

            mProductAdapter.setOnClickListener(new ProductAdapter.onButtonClickListener() {
                @Override
                public void onBtnJjim(ProductDO item) {
                    int mStock_SelectStore = Integer.parseInt(item.GetProductStock());

                    if (mStock_SelectStore != 0) {
                        JJimImpossible();
                    } else {
                        String mAlertMsg = "";
                        JJimPossible(item);
                    }
                }
            });
        }


        public void JJimImpossible() {

            AlertDialog.Builder mAlert_JJimImpossible = new AlertDialog.Builder(mRootView.getContext());

            mAlert_JJimImpossible.setTitle("찜 불가 상품")
                    .setMessage("점포에 상품이 남아있습니다.\n재고가 없을 경우에 찜 푸시를 받을 수 있습니다.")
                    .setCancelable(false)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            mAlert_JJimImpossible.show();
        }

        public void JJimPossible(final ProductDO mPosition) {


            AlertDialog.Builder mAlert_CannotReservation = new AlertDialog.Builder(mRootView.getContext());

            mAlert_CannotReservation.setTitle("찜 푸시 받기")
                    .setMessage("상품 입고시 찜 푸시를 받으시겠습니까?")
                    .setCancelable(false)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mJJimAddJSON(mPosition);

                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });

            AlertDialog mAlertDialog = mAlert_CannotReservation.create();
            mAlertDialog.show();
        }

        public void CheckState(String mMsg) {

            AlertDialog.Builder mCheckComplete = new AlertDialog.Builder(mRootView.getContext());
            mCheckComplete.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            mCheckComplete.setMessage(mMsg);
            mCheckComplete.show();
        }

        public void addProduct(View rootView, int section_num, LayoutInflater inflater) {
            mProductData = new ArrayList<ProductDO>();

            Log.d(TAG, "==================come product=========================");

            if (ProductActivity.productDOArrayList.size() - 1 < section_num) {
            } else {
                mProductData.addAll(ProductActivity.productDOArrayList.get(section_num));
            }

            mProductAdapter = new ProductAdapter(rootView.getContext(), mProductData, inflater);
            mProductListView.setAdapter(mProductAdapter);
            ListButtonClick();
        }


        public void mJJimAddJSON(ProductDO position) {
            String JJimAddCheck = null;

            if (ProductActivity.mCurrentUserID != null) {
                mJJimAddArray = new JSONArray();
                toJSON(mJJimAddArray, "userID", ProductActivity.mCurrentUserID);
                toJSON(mJJimAddArray, "productName", position.GetProductName());
                toJSON(mJJimAddArray, "address", ProductActivity.mGetIntent_StoreAddress);
            }
            Log.d(TAG, ProductActivity.mGetIntent_StoreAddress);

            try {

                HttpUtil httpUtil = new HttpUtil("Wait", "JJimAddRequest", mJJimAddArray);
                Log.d(TAG, "데이터 전송 완료");

                httpUtil.start();

                httpUtil.join();

                JSONArray JJimAddResult = httpUtil.getResponse();
                int size = JJimAddResult.length();

                for (int i = 0; i < size; i++) {
                    JJimAddCheck = JJimAddResult.getJSONObject(i).getString("check");
                    Log.i(TAG, "결과 :" + JJimAddCheck);
                }
                if (JJimAddCheck.equals("true")) {
                    String mMsg = "찜 푸시 리스트에 추가되었습니다.";
                    CheckState(mMsg);
                } else if (JJimAddCheck.equals("dup")) {
                    String mMsg = "찜 푸시 리스트에 이미 존재합니다.";
                    CheckState(mMsg);
                } else if (JJimAddCheck.equals("false")) {
                    String mMsg = "찜 푸시 리스트에 추가 실패";
                    CheckState(mMsg);
                }


            } catch (Exception e) {
                e.toString();
            }

        }

        JSONArray toJSON(JSONArray array, String type, String item) {

            JSONObject obj = new JSONObject();
            try {
                obj.put(type, item);
                array = array.put(obj);
                return array;
            } catch (Exception e) {
                return null;
            }
        }
    }




