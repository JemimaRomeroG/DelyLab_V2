package pe.edu.ulima.pm.delylab;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


import pe.edu.ulima.pm.delylab.adapters.MenuListAdapter;
import pe.edu.ulima.pm.delylab.model.Menu;
import pe.edu.ulima.pm.delylab.model.RestaurantModel;

public class RestaurantMenuActivity extends AppCompatActivity implements MenuListAdapter.MenuListClickListener {
    private List<Menu> menuList = null;
    private MenuListAdapter menuListAdapter;
    private List<Menu> itemInCartList;
    private  int totalItemInCart = 0 ;
    private TextView buttonCheckout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);


        RestaurantModel restaurantModel = getIntent().getParcelableExtra("RestaurantModel");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(restaurantModel.getName());
        actionBar.setSubtitle(restaurantModel.getAddress());
        actionBar.setDisplayHomeAsUpEnabled(true);

         menuList = restaurantModel.getMenus();
         initRecyclerView();

          buttonCheckout = findViewById(R.id.buttonCheckout);
        buttonCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(itemInCartList != null && itemInCartList.size() <= 0 ){
                        Toast.makeText(RestaurantMenuActivity.this,"Porfavor agregar productos en el carro de compras.",Toast.LENGTH_SHORT).show();
                        return;

                    }

                restaurantModel.setMenus(itemInCartList);
                Intent i = new Intent(RestaurantMenuActivity.this, PlaceYourOrderActivity.class);
                i.putExtra("RestaurantModel",restaurantModel);
                startActivityForResult(i,1000) ;
            }
        });


    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        menuListAdapter = new MenuListAdapter(menuList,this);
        recyclerView.setAdapter(menuListAdapter);

    }

    // funcion para al dar click muestre la cantidad de prodcuto que se han añadido y se va  a pagar en el boton checkout
    @Override
    public void onAddToCartClick(Menu menu) {
        if (itemInCartList == null){
            itemInCartList = new ArrayList<>();
        }

        itemInCartList.add(menu);
        totalItemInCart = 0;

        for (Menu m :  itemInCartList){
            totalItemInCart =  totalItemInCart + m.getTotalInCart();
        }
        buttonCheckout.setText("PAGAR ("+totalItemInCart+") PRODUCTOS");

    }
    //para actualizar la cantidad de prodcutos a pagar en el boton checkout
    @Override
    public void onUpdateCarClick(Menu menu) {
            if(itemInCartList.contains(menu)){
                int index  = itemInCartList.indexOf(menu);
                itemInCartList.remove(menu);
                itemInCartList.add(index,menu);

                totalItemInCart = 0;

                for (Menu m :  itemInCartList){
                    totalItemInCart =  totalItemInCart + m.getTotalInCart();

                }
                buttonCheckout.setText("PAGAR ("+totalItemInCart+") PRODUCTOS");
            }


    }
    // remover o solo mostrar el boton original si no hay ningun prodcuto añadir
    @Override
    public void onRemoveFromCartClick(Menu menu) {

        if(itemInCartList.contains(menu)){
             itemInCartList.remove(menu);
            totalItemInCart = 0;

            for (Menu m :  itemInCartList){
                totalItemInCart =  totalItemInCart + m.getTotalInCart();

            }
            buttonCheckout.setText("PAGAR ("+totalItemInCart+") PRODUCTOS");

        }

    }
    // para la flecha de regresar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home :
                finish();

            default:
                //do nothing

        }
        return super.onOptionsItemSelected(item);
    }

    //para ver los resultados aca esta la opcion para pagar y verirficar los prodcutos que se han añadido para pagar
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1000 && resultCode == Activity.RESULT_OK ){

                finish();

        }
    }
}