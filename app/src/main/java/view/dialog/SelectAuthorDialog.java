package view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;

import com.investmentkorea.android.admin.R;

import java.util.ArrayList;
import java.util.Collections;

import api.ApiClient;
import api.ApiInterface;
import api.response.AuthorListResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.AuthorModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.EndlessRecyclerOnScrollListener;
import util.Util;
import util.adapter.SelectAuthorAdapter;

public class SelectAuthorDialog extends Dialog {

    // 한번에 받아올 데이터 갯수
    private static final int LOAD_DATA_COUNT = 30;

    private ArrayList<AuthorModel> authorModelArrayList;
    private SelectAuthorAdapter selectAuthorAdapter;
    private SelectAuthorListener selectAuthorListener;

    @BindView(R.id.author_recyclerView) RecyclerView authorRecyclerView;

    /*
    isAll -> true - '전체' 항목이 노출, false - '전체' 항목이 미노출
     */
    public SelectAuthorDialog(Context context, SelectAuthorListener selectAuthorListener){
        super(context);
        this.selectAuthorListener = selectAuthorListener;
    }

    public interface SelectAuthorListener{
        void selectAuthor(String authorName);
    }

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 삭제
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.select_author_dialog);

        ButterKnife.bind(this);

        init();
    }

    private void init(){
        authorModelArrayList = new ArrayList<>();

        LinearLayoutManager lL = new LinearLayoutManager(getContext());
        selectAuthorAdapter = new SelectAuthorAdapter(authorModelArrayList, new SelectAuthorAdapter.SelectAuthorAdapterListener() {
            @Override
            public void selectAuthor(String authorName) {
                selectAuthorListener.selectAuthor(authorName);
                dismiss();
            }
            @Override
            public void selectAll(){
                selectAuthorListener.selectAuthor("전체보기");
                dismiss();
            }
        });

        getAuthorList(true, 0);

        // LoadMore 리스너 등록
        EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(lL, LOAD_DATA_COUNT) {
            @Override
            public void onLoadMore(int current_page) {
                if(!authorModelArrayList.isEmpty()){
                    getAuthorList(false, authorModelArrayList.get(authorModelArrayList.size()-1).getNo());
                }
            }
        };
        authorRecyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);

        authorRecyclerView.setLayoutManager(lL);
        authorRecyclerView.setAdapter(selectAuthorAdapter);
    }

    private void getAuthorList(boolean refresh, int no){
        if(refresh){
            authorModelArrayList.clear();
        }

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<AuthorListResponse> call = apiService.getAuthorList(no);
        call.enqueue(new Callback<AuthorListResponse>() {
            @Override
            public void onResponse(Call<AuthorListResponse> call, Response<AuthorListResponse> response) {
                AuthorListResponse authorListResponse = response.body();
                if(authorListResponse.getResult().size() > 0) {
                    for(AuthorModel am : authorListResponse.getResult()){
                        Collections.addAll(authorModelArrayList, am);
                    }
                    selectAuthorAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<AuthorListResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Util.showToast(getContext(), "네트워크 연결상태를 확인해주세요.");
            }
        });
    }

    @OnClick(R.id.cancel_btn) void cancelBtn(){
        dismiss();
    }
}
