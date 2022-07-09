package pe.edu.ulima.pm.delylab.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import pe.edu.ulima.pm.delylab.R;
import pe.edu.ulima.pm.delylab.RestaurantMenuActivity;
import pe.edu.ulima.pm.delylab.model.Menu;
import pe.edu.ulima.pm.delylab.model.RestaurantModel;

public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.MyViewHolder>  {


    private List<Menu> menuList;
    private MenuListClickListener  clickListener;
    private AlertDialog.Builder builder;

    // Se esta creando funciones separadas por si se quiere actualizar los datos desde el exterior
    public MenuListAdapter(List<Menu> menuList, MenuListClickListener clickListener){
        this.menuList = menuList;
        this .clickListener = clickListener;

    }


    public void updateData (List<RestaurantModel> restaurantModelList){
        this.menuList = menuList;
        notifyDataSetChanged();

    }

    //vamos a inflar el recyclerview

    @NonNull
    @Override
    public MenuListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_recycler_row,parent,false);
        return  new MyViewHolder(view);
    }

    // Esta trayendo todas las variables del JSON , para que se muestre en el  layout recycler_Row
    @Override
    public void onBindViewHolder(@NonNull MenuListAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.menuName.setText(menuList.get(position).getName());
        holder.menuPrice.setText("Precio S/."+menuList.get(position).getPrice());
        //boton para agregar productos al carrito
        holder.addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Menu menu  = menuList.get(position);
                    menu.setTotalInCart(1);
                clickListener.onAddToCartClick(menu);
                holder.addMoreLayout.setVisibility(View.VISIBLE);
                holder.addToCartButton.setVisibility(View.GONE);
                holder.tvCount.setText(menu.getTotalInCart()+"");
            }
        });
        // boton cuando se quiera quitar productos
        holder.imageMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Menu menu  = menuList.get(position);
                int total = menu.getTotalInCart();
                total--;
                if (total>0){
                    menu.setTotalInCart(total);
                    clickListener.onUpdateCarClick(menu);
                    holder.tvCount.setText(total+"");

                }else{
                    holder.addMoreLayout.setVisibility(View.GONE);
                    holder.addToCartButton.setVisibility(View.VISIBLE);
                    menu.setTotalInCart(total);
                    clickListener.onRemoveFromCartClick(menu);

                }

            }
        });
        //boton para agregar elementos al carrito solo hasta 10 elementos
        holder.imageAddOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Menu menu  = menuList.get(position);
                int total = menu.getTotalInCart();
                total++;
                if (total <= 10 ){
                    menu.setTotalInCart(total);
                    clickListener.onUpdateCarClick(menu);
                    holder.tvCount.setText(total+"");

                }

            }
        });

       /* holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onAddToCartClick(menuList.get(position));
            }
        });*/


        Glide.with(holder.thumbImage)
                .load(menuList.get(position).getUrl())
                .into(holder.thumbImage);



    }


    @Override
    public int getItemCount() {
        return menuList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView menuName;
        TextView  menuPrice;
        TextView  addToCartButton;
        ImageView thumbImage;
        ImageView imageMinus;
        ImageView imageAddOne;
        TextView tvCount;
        LinearLayout addMoreLayout;
        public MyViewHolder(View view) {
            super(view);
            menuName = view.findViewById(R.id.menuName);
            menuPrice = view.findViewById(R.id.menuPrice);
            addToCartButton = view.findViewById(R.id.addToCartButton);
            thumbImage = view.findViewById(R.id.thumbImage);
            imageMinus = view.findViewById(R.id.imageMinus);
            imageAddOne =  view.findViewById(R.id.imageAddOne);
            tvCount = view.findViewById(R.id.tvCount);
            addMoreLayout = view.findViewById(R.id.addMoreLayout);
        }
    }

    public interface MenuListClickListener {
        public void onAddToCartClick(Menu menu);
        public void onUpdateCarClick(Menu menu);
        public void onRemoveFromCartClick(Menu menu);

    }
}