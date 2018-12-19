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
import presenter.view.RegisterView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Util;

public class RegisterPresenter extends BasePresenter<RegisterView>{

    private Context context;
    private ArrayList<AuthorModel> authorModelArrayList;
    private ApiInterface apiService;

    public RegisterPresenter(Context context, RegisterView view, ArrayList<AuthorModel> authorModelArrayList){
        super(view);
        this.context = context;
        this.authorModelArrayList = authorModelArrayList;
        this.apiService = ApiClient.getClient().create(ApiInterface.class);
    }

    /*
    * 명언 신규 등록
     */
    public void registerSaying(String contents, String date, String authorName, int gravityHorizontal, int gravityVertical, int textSize){
        Call<CommonResponse> call = apiService.registerSaying(contents, date, authorName, gravityHorizontal, gravityVertical, textSize);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse authorListResponse = response.body();
                if(authorListResponse.getCode() == 200){
                    Util.showToast(context, "명언이 등록되었습니다.");
                }else{
                    Util.showToast(context, "명언 등록을 실패하였습니다.");
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

    public void editSaying(int no, String contents, String date, String authorName, int gravityHorizontal, int gravityVertical, int textSize){
        Call<CommonResponse> call = apiService.editSaying(no, contents, date, authorName, gravityHorizontal, gravityVertical, textSize);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse commonResponse = response.body();
                if(commonResponse.getCode() == 200){
                    Util.showToast(context, "명언이 수정되었습니다.");
                }else{
                    Util.showToast(context, "명언 수정을 실패하였습니다.");
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

    public void deleteSaying(int no){
        Call<CommonResponse> call = apiService.deleteSaying(no);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse commonResponse = response.body();
                if(commonResponse.getCode() == 200){
                    Util.showToast(context, "명언이 삭제되었습니다.");
                    getView().deleteSaying();
                }else{
                    Util.showToast(context, "명언 삭제가 실패되었습니다.");
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
