package presenter;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

import api.ApiClient;
import api.ApiInterface;
import api.response.SayingListResponse;
import base.presenter.BasePresenter;
import model.SayingModel;
import presenter.view.MainView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Util;

public class MainPresenter extends BasePresenter<MainView>{

    private Context context;
    private ArrayList<SayingModel> sayingModelArrayList;
    private ApiInterface apiService;

    public MainPresenter(Context context, MainView view, ArrayList<SayingModel> sayingModelArrayList){
        super(view);
        this.context = context;
        this.sayingModelArrayList = sayingModelArrayList;
        this.apiService = ApiClient.getClient().create(ApiInterface.class);
    }

    public void getSayingList(boolean refresh, int no, String sort){
        if(refresh)
            sayingModelArrayList.clear();

        Call<SayingListResponse> call = apiService.getSayingList(no, sort);
        call.enqueue(new Callback<SayingListResponse>() {
            @Override
            public void onResponse(Call<SayingListResponse> call, Response<SayingListResponse> response) {
                SayingListResponse sayingListResponse = response.body();
                if(sayingListResponse.getResult().size() > 0) {
                    for(SayingModel sm : sayingListResponse.getResult()){
                        Collections.addAll(sayingModelArrayList, sm);
                    }
                }
                getView().setSayingList();
            }

            @Override
            public void onFailure(Call<SayingListResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e("tag", t.toString());
                Util.showToast(context, "네트워크 연결상태를 확인해주세요.");
            }
        });
    }

}
