package com.md09.pharmapoly.network;



import com.md09.pharmapoly.Models.District;
import com.md09.pharmapoly.Models.DistrictRequest;
import com.md09.pharmapoly.Models.GHNResponse;
import com.md09.pharmapoly.Models.Province;
import com.md09.pharmapoly.Models.Ward;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GHNService {
    @GET("shiip/public-api/master-data/province")
    Call<GHNResponse<ArrayList<Province>>> getListProvince();
    @POST("shiip/public-api/master-data/district")
    Call<GHNResponse<ArrayList<District>>> getListDistrict(@Body DistrictRequest districtRequest);
    @GET("shiip/public-api/master-data/ward")
    Call<GHNResponse<ArrayList<Ward>>> getListWard(@Query("district_id") int district_id);
//    @POST("shiip/public-api/v2/shipping-order/create")
//    Call<GHNResponse<GHNOrderRespone>> GHNOrder(@Body GHNOrderRequest ghnOrderRequest);
}
