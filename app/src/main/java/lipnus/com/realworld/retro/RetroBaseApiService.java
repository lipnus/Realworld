package lipnus.com.realworld.retro;


import java.util.HashMap;
import java.util.List;

import lipnus.com.realworld.retro.ResponseBody.Inventory;
import lipnus.com.realworld.retro.ResponseBody.MissionDetail;
import lipnus.com.realworld.retro.ResponseBody.QuestDetail;
import lipnus.com.realworld.retro.ResponseBody.QuestResult;
import lipnus.com.realworld.retro.ResponseBody.ResponseGet2;
import lipnus.com.realworld.retro.ResponseBody.Scenario;
import lipnus.com.realworld.retro.ResponseBody.ScenarioDetail;
import lipnus.com.realworld.retro.ResponseBody.TokenGet;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by sonchangwoo on 2017. 1. 1..
 */

public interface RetroBaseApiService {


    String Base_URL= "https://apitest.doctorhamel.com:443";



    @FormUrlEncoded
    @POST("/test")
    Call<ResponseGet2> postFirst(@FieldMap HashMap<String, Object> parameters);

    @POST("/authorize")
    Call<TokenGet> postAuthorize(@Body HashMap<String, Object> parameters);

    @POST("/scenarios")
    Call<List<Scenario>> postScenarios(@Body HashMap<String, Object> parameters);

    @POST("/scenarios/{id}")
    Call<ScenarioDetail> postScenariosDetail(@Path("id") int id, @Body HashMap<String, Object> parameters);

    @POST("/missions/{id}")
    Call<MissionDetail> postMissionsDetail(@Path("id") int id, @Body HashMap<String, Object> parameters);

    @POST("/quests/{id}")
    Call<QuestDetail> postQuestDetail(@Path("id") int id, @Body HashMap<String, Object> parameters);

    @POST("/quests/{id}/result")
    Call<QuestResult> postQuestResult(@Path("id") int id, @Body HashMap<String, Object> parameters);

    @POST("/inventory")
    Call<List<Inventory>> postInventory(@Body HashMap<String, Object> parameters);





    @FormUrlEncoded
    @Headers({"content-type: application/json"})
    @POST("/authorize")
    Call<TokenGet> postReal2(@Body String jsonStr);

}
