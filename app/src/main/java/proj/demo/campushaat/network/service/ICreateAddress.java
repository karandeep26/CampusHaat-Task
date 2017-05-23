package proj.demo.campushaat.network.service;

import proj.demo.campushaat.network.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by stpl on 5/23/2017.
 */

public interface ICreateAddress {
    @POST("/createAddress")
    Call<ResponseBody> createAddress(@Header("Content-Type") String content_type,@Body String body);

}
