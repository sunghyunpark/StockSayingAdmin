package api;

import api.response.AuthorListResponse;
import api.response.CommonResponse;
import api.response.SayingListResponse;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface {

    /*
    명언 저자 리스트 API
     */
    @GET("api/author/authorList/{no}")
    Call<AuthorListResponse> getAuthorList(@Path("no") int no);

    /*
    명언 저자 등록
     */
    @FormUrlEncoded
    @POST("api/author/upload/author")
    Call<CommonResponse> registerAuthor(@Field("authorName") String authorName);

    /*
    명언 저자 삭제
     */
    @DELETE("api/author/delete/author/{no}/{authorName}")
    Call<CommonResponse> deleteAuthor(@Path("no") int no,
                                      @Path("authorName") String authorName);

    /*
    명언 등록
     */
    @FormUrlEncoded
    @POST("api/saying/upload/saying")
    Call<CommonResponse> registerSaying(@Field("contents") String contents,
                                        @Field("authorName") String authorName,
                                        @Field("gravityHorizontal") int gravityHorizontal,
                                        @Field("gravityVertical") int gravityVertical,
                                        @Field("textSize") int textSize,
                                        @Field("date") String date);

    /*
    명언 수정
     */
    @FormUrlEncoded
    @PUT("api/saying/edit/saying")
    Call<CommonResponse> editSaying(@Field("no") int no,
                                    @Field("contents") String contents,
                                    @Field("date") String date,
                                    @Field("authorName") String authorName,
                                    @Field("gravityHorizontal") int gravityHorizontal,
                                    @Field("gravityVertical") int gravityVertical,
                                    @Field("textSize") int textSize);

    /*
     명언 삭제
     */
    @DELETE("api/saying/delete/{no}")
    Call<CommonResponse> deleteSaying(@Path("no") int no);

    /*
    명언 리스트 받기
     */
    @GET("api/saying/list/saying/{no}/{sort}")
    Call<SayingListResponse> getSayingList(@Path("no") int no,
                                           @Path("sort") String sort);


}
