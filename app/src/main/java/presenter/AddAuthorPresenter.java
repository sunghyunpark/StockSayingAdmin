package presenter;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

import api.ApiClient;
import api.ApiInterface;
import api.response.AuthorListResponse;
import api.response.CommonResponse;
import base.presenter.BasePresenter;
import model.AuthorModel;
import presenter.view.AddAuthorView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Util;

public class AddAuthorPresenter extends BasePresenter<AddAuthorView>{
    private Context context;
    private ApiInterface apiService;
    private ArrayList<AuthorModel> authorModelArrayList;

    public AddAuthorPresenter(Context context, AddAuthorView view, ArrayList<AuthorModel> authorModelArrayList){
        super(view);
        this.context = context;
        this.authorModelArrayList = authorModelArrayList;
        this.apiService = ApiClient.getClient().create(ApiInterface.class);
    }

    public void getAuthorList(boolean refresh, int no){
        if(refresh)
            authorModelArrayList.clear();

        Call<AuthorListResponse> call = apiService.getAuthorList(no);
        call.enqueue(new Callback<AuthorListResponse>() {
            @Override
            public void onResponse(Call<AuthorListResponse> call, Response<AuthorListResponse> response) {
                AuthorListResponse authorListResponse = response.body();
                if(authorListResponse.getResult().size() > 0){
                    for(AuthorModel am : authorListResponse.getResult()){
                        Collections.addAll(authorModelArrayList, am);
                    }
                    getView().setAuthorList();
                }
            }

            @Override
            public void onFailure(Call<AuthorListResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Util.showToast(context, "네트워크 연결상태를 확인해주세요.");
            }
        });
    }

    public void registerAuthor(String authorName){
        Call<CommonResponse> call = apiService.registerAuthor(authorName);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse commonResponse = response.body();
                if(commonResponse.getCode() == 200) {
                    getAuthorList(true, 0);
                    Util.showToast(context, "등록되었습니다.");
                }else{
                    Util.showToast(context, commonResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Util.showToast(context, "네트워크 연결상태를 확인해주세요.");
            }
        });
    }

    public void deleteAuthor(int no, String authorName){
        Call<CommonResponse> call = apiService.deleteAuthor(no, authorName);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse commonResponse = response.body();
                if(commonResponse.getCode() == 200) {
                    Util.showToast(context, "삭제되었습니다.");
                }else{
                    Util.showToast(context, commonResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Util.showToast(context, "네트워크 연결상태를 확인해주세요.");
            }
        });
    }
}
