package com.firoz.shooply.checkout.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firoz.shooply.R;
import com.firoz.shooply.model.CartModel;
import com.firoz.shooply.util.CartOnclick;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.myViewHolder> {

    Context context;
    List<CartModel> cartModelList;
    CartOnclick cartOnclick;

    public CartAdapter(Context context, List<CartModel> cartModelList, CartOnclick cartOnclick) {
        this.context = context;
        this.cartModelList = cartModelList;
        this.cartOnclick = cartOnclick;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.cart_item,null,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        CartModel cartModel=cartModelList.get(position);
        double r1= Double.parseDouble(cartModel.getProductRate());
        double c1 = Double.parseDouble(cartModel.getQuantity());

        double rate=r1*c1;
        Glide.with(context).load(cartModel.getProductImageLink()).into(holder.item_img);

        holder.item_title.setText(cartModel.getProductName());
        holder.item_rate.setText(String.valueOf(rate));
        holder.count.setText(String.valueOf(cartModel.getQuantity()));
        holder.productDescription.setText(cartModel.getProductDescription());
        holder.mrp.setText(cartModel.getMrp());

        double d = Double.parseDouble(cartModel.getDiscount());
        int discount = (int) d;

        holder.discount.setText("-" + discount + "%");

        holder.count_negative.setOnClickListener(view -> {
            double r= Double.parseDouble(cartModel.getProductRate());
            double c = Double.parseDouble(cartModel.getQuantity());

            if (c>1){
                int i= (int) (c-1);

                holder.count.setText(String.valueOf(i));
                holder.item_rate.setText(String.valueOf((c-1)*r));

                cartModel.setQuantity(String.valueOf((int) (c-1)));
                cartOnclick.decreaseCart(cartModel);
            }
        });

        holder.count_positive.setOnClickListener(view1 -> {
            double r= Double.parseDouble(cartModel.getProductRate());
            double c = Double.parseDouble(cartModel.getQuantity());

            int i= (int) (c+1);
            holder.count.setText(String.valueOf(i));
            holder.item_rate.setText(String.valueOf((c+1)*r));

            cartModel.setQuantity(String.valueOf((int) (c+1)));
            cartOnclick.increaseCart(cartModel);
        });
        holder.menu.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(context, holder.menu);
            //inflating menu from xml resource
            popup.inflate(R.menu.cart_menu);
            //adding click listener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.deletBtn:
                            cartOnclick.deleteCart(cartModel);
                            break;

                    }
                    return false;
                }
            });
            //displaying the popup
            popup.show();
        });

    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        ImageView item_img,menu;
        ImageView count_negative,count_positive;
        TextView item_title,item_rate,count,productDescription,mrp,discount;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            item_img=itemView.findViewById(R.id.item_img);
            menu=itemView.findViewById(R.id.menu);
            item_title=itemView.findViewById(R.id.item_title);
            item_rate=itemView.findViewById(R.id.item_rate);
            count_negative=itemView.findViewById(R.id.count_negative);
            count=itemView.findViewById(R.id.count);
            count_positive=itemView.findViewById(R.id.count_positive);
            productDescription=itemView.findViewById(R.id.productDescription);
            mrp=itemView.findViewById(R.id.mrp);
            discount=itemView.findViewById(R.id.discount);
        }
    }
}
