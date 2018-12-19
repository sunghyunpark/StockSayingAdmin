package com.investmentkorea.android.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.SayingModel;
import presenter.MainPresenter;
import presenter.view.MainView;
import util.EndlessRecyclerOnScrollListener;
import util.adapter.SayingAdapter;
import view.RegisterActivity;
import view.dialog.SelectAuthorDialog;

public class MainActivity extends BaseActivity implements MainView{

    private static final int REQUEST_WRITE = 1000;
    private static final int REQUEST_DETAIL = 2000;
    private static final int RESULT_DELETE = 3000;
    private static final int LOAD_DATA_COUNT = 10;

    private int detailPosition = -1;
    private String sortMode = "all";
    private MainPresenter mainPresenter;
    private ArrayList<SayingModel> sayingModelArrayList;
    private SayingAdapter sayingAdapter;
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;

    @BindView(R.id.saying_recyclerView) RecyclerView sayingRecyclerView;
    @BindView(R.id.empty_tv) TextView emptyTv;
    @BindView(R.id.sort_tv) TextView sortTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        init();
    }

    private void init(){
        sayingModelArrayList = new ArrayList<>();
        mainPresenter = new MainPresenter(getApplicationContext(), this, sayingModelArrayList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        sayingAdapter = new SayingAdapter(getApplicationContext(), sayingModelArrayList, new SayingAdapter.SayingAdapterListener() {
            @Override
            public void clickItem(SayingModel sayingModel, int position) {
                detailPosition = position;
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                intent.putExtra("isRegister", false);
                intent.putExtra("SayingModel", sayingModel);
                startActivityForResult(intent, REQUEST_DETAIL);
            }
        });

        // 최초로 한번 명언 리스트를 불러온다.
        mainPresenter.getSayingList(true, 0, "all");

        // LoadMore 리스너 등록
        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager, LOAD_DATA_COUNT) {
            @Override
            public void onLoadMore(int current_page) {
                if(!sayingModelArrayList.isEmpty()){
                    mainPresenter.getSayingList(false, sayingModelArrayList.get(sayingModelArrayList.size()-1).getNo(), sortMode);
                }
            }
        };
        sayingRecyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);

        sayingRecyclerView.setLayoutManager(linearLayoutManager);
        sayingRecyclerView.setAdapter(sayingAdapter);
    }

    @Override
    public void setSayingList(){
        if(sayingModelArrayList.size() > 0){
            emptyTv.setVisibility(View.GONE);
            sayingRecyclerView.setVisibility(View.VISIBLE);
            sayingAdapter.notifyDataSetChanged();
        }else{
            emptyTv.setVisibility(View.VISIBLE);
            sayingRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_WRITE) {
            // 글쓰기 후 돌아왔을 경우
            if(resultCode == Activity.RESULT_OK){
                sortMode = "all";
                mainPresenter.getSayingList(true, 0, sortMode);
                endlessRecyclerOnScrollListener.reset(0, true);
            }
        }else if(requestCode == REQUEST_DETAIL){
            // 글 상세 화면 진입 후 돌아온 경우
            if(resultCode == Activity.RESULT_OK){
                sayingModelArrayList.set(detailPosition, (SayingModel) data.getExtras().getSerializable("SayingModel"));
                sayingAdapter.notifyDataSetChanged();
            }else if(resultCode == RESULT_DELETE){
                // 글 삭제 후 돌아온경우
                sayingAdapter.onItemDismiss(detailPosition);
            }
        }
    }

    @OnClick({R.id.register_btn, R.id.sort_tv}) void onClick(View v){
        switch (v.getId()){
            case R.id.register_btn:
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                intent.putExtra("isRegister", true);
                startActivityForResult(intent, REQUEST_WRITE);
                break;
            case R.id.sort_tv:
                SelectAuthorDialog selectAuthorDialog = new SelectAuthorDialog(this, new SelectAuthorDialog.SelectAuthorListener() {
                    @Override
                    public void selectAuthor(String authorName) {
                        sortTv.setText(authorName);
                        sortMode = (authorName.equals("전체보기") ? "all" : authorName);
                        mainPresenter.getSayingList(true, 0, sortMode);
                        endlessRecyclerOnScrollListener.reset(0, true);
                    }
                });
                selectAuthorDialog.show();
                break;
        }
    }
}
