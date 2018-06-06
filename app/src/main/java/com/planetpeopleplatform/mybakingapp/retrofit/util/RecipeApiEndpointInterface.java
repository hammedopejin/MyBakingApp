package com.planetpeopleplatform.mybakingapp.retrofit.util;

import com.planetpeopleplatform.mybakingapp.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Hammedopejin on 5/24/2018.
 */

public interface RecipeApiEndpointInterface {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipes();

}
