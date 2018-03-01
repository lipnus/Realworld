package lipnus.com.realworld.retro;


import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.List;

import lipnus.com.realworld.retro.ResponseBody.MissionDetail;
import lipnus.com.realworld.retro.ResponseBody.QuestDetail;
import lipnus.com.realworld.retro.ResponseBody.QuestResult;
import lipnus.com.realworld.retro.ResponseBody.ResponseGet2;
import lipnus.com.realworld.retro.ResponseBody.Scenario;
import lipnus.com.realworld.retro.ResponseBody.ScenarioDetail;
import lipnus.com.realworld.retro.ResponseBody.TokenGet;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sonchangwoo on 2017. 1. 6..
 */

public class RetroClient {

    private RetroBaseApiService apiService;
    public static String baseUrl = RetroBaseApiService.Base_URL;
    private static Context mContext;
    private static Retrofit retrofit;

    private static class SingletonHolder {
        private static RetroClient INSTANCE = new RetroClient(mContext);
    }

    public static RetroClient getInstance(Context context) {
        if (context != null) {
            mContext = context;
        }
        return SingletonHolder.INSTANCE;
    }

    private RetroClient(Context context) {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }

    public RetroClient createBaseApi() {
        apiService = create(RetroBaseApiService.class);
        return this;
    }

    /**
     * create you ApiService
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     */
    public  <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }


    public void postFirst(HashMap<String, Object> parameters, final RetroCallback callback) {
        apiService.postFirst(parameters).enqueue(new Callback<ResponseGet2>() {
            @Override
            public void onResponse(Call<ResponseGet2> call, Response<ResponseGet2> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseGet2> call, Throwable t) {
                callback.onError(t);
            }
        });
    }


    //토큰
    public void postAuthorize(HashMap<String, Object> parameters, final RetroCallback callback) {
        apiService.postAuthorize(parameters).enqueue(new Callback<TokenGet>() {
            @Override
            public void onResponse(Call<TokenGet> call, Response<TokenGet> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<TokenGet> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    //시나리오
    public void postScenarios(HashMap<String, Object> parameters, final RetroCallback callback) {
        apiService.postScenarios(parameters).enqueue(new Callback<List<Scenario>>() {
            @Override
            public void onResponse(Call<List<Scenario>> call, Response<List<Scenario>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Scenario>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    //시나리오 상세
    public void postScenariosDetail(int id, HashMap<String, Object> parameters, final RetroCallback callback) {
        apiService.postScenariosDetail(id, parameters).enqueue(new Callback<ScenarioDetail>() {
            @Override
            public void onResponse(Call<ScenarioDetail> call, Response<ScenarioDetail> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<ScenarioDetail> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    //미션상세
    public void postMissionsDetail(int id, HashMap<String, Object> parameters, final RetroCallback callback) {
        apiService.postMissionsDetail(id, parameters).enqueue(new Callback<MissionDetail>() {
            @Override
            public void onResponse(Call<MissionDetail> call, Response<MissionDetail> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<MissionDetail> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    //퀘스트상세
    public void postQuestDetail(int id, HashMap<String, Object> parameters, final RetroCallback callback) {
        apiService.postQuestDetail(id, parameters).enqueue(new Callback<QuestDetail>() {
            @Override
            public void onResponse(Call<QuestDetail> call, Response<QuestDetail> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<QuestDetail> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    //퀘스트결과등록
    public void postQuestResult(int id, HashMap<String, Object> parameters, final RetroCallback callback) {
        apiService.postQuestResult(id, parameters).enqueue(new Callback<QuestResult>() {
            @Override
            public void onResponse(Call<QuestResult> call, Response<QuestResult> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.code(), response.body());
                } else {
                    callback.onFailure(response.code());
                }
            }

            @Override
            public void onFailure(Call<QuestResult> call, Throwable t) {
                callback.onError(t);
            }
        });
    }



}
