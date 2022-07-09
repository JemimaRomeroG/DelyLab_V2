package pe.edu.ulima.pm.delylab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

import pe.edu.ulima.pm.delylab.adapters.RestaurantListAdapter;
import pe.edu.ulima.pm.delylab.model.RestaurantModel;

public class MainActivity2 extends AppCompatActivity  implements RestaurantListAdapter.RestaurantListClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        List<RestaurantModel>   restaurantModelList = getRestaurantData();
        initRecyclerView(restaurantModelList);
    }
    //va a mostrar la lista de restarurantes en un reciclados

    private void initRecyclerView(List<RestaurantModel>   restaurantModelList){
        RecyclerView recyclerView=  findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RestaurantListAdapter adapter = new RestaurantListAdapter(restaurantModelList,this);

        recyclerView.setAdapter(adapter);

    }

    //obtener el listado de json a traves de una clase
    private List<RestaurantModel> getRestaurantData(){
            InputStream is = getResources().openRawResource(R.raw.restaurent);
            Writer writer = new StringWriter();
            char [] buffer  = new char[1024];


            try {
                    Reader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                    int n;
                    while(( n = reader.read(buffer)) != -1){

                        writer.write(buffer,0,n);
                    }


            }catch (Exception e){}

            String jsonStr = writer.toString();
            Gson gson = new Gson();
            RestaurantModel[] restaurantModels = gson.fromJson(jsonStr,RestaurantModel[].class);
            List<RestaurantModel> restList = Arrays.asList(restaurantModels);

            return  restList;

    }

    @Override
    public void onItemClick(RestaurantModel restaurantModel) {
            Intent intent = new Intent(MainActivity2.this,RestaurantMenuActivity.class);
            intent.putExtra("RestaurantModel",restaurantModel);
            startActivity(intent);

    }
}