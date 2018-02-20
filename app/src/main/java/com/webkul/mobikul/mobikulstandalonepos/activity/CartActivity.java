package com.webkul.mobikul.mobikulstandalonepos.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.adapter.CartProductAdapter;
import com.webkul.mobikul.mobikulstandalonepos.databinding.ActivityCartBinding;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Customer;
import com.webkul.mobikul.mobikulstandalonepos.handlers.CartHandler;
import com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref;
import com.webkul.mobikul.mobikulstandalonepos.helper.Helper;
import com.webkul.mobikul.mobikulstandalonepos.model.CartModel;

public class CartActivity extends BaseActivity {

    public ActivityCartBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CartModel cartData = null;
        if (getIntent().getExtras() != null) {
            cartData = Helper.fromStringToCartModel(AppSharedPref.getCartData(this));
        }
        setCartProducts(cartData);
    }

    private void setCartProducts(CartModel cartData) {
        binding.setData(cartData);
        binding.setHandler(new CartHandler(this, binding));
        if (cartData != null && cartData.getProducts().size() > 0) {
            CartProductAdapter adapter = new CartProductAdapter(this, cartData.getProducts());
            binding.cartProductRv.setAdapter(adapter);
            binding.setVisibility(true);
        } else {
            binding.setVisibility(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCartProducts(Helper.fromStringToCartModel(AppSharedPref.getCartData(this)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.menu_item_search).setVisible(false);
        menu.findItem(R.id.menu_item_scan_barcode).setVisible(false);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getExtras() != null) {
            Customer customer = (Customer) data.getExtras().getSerializable("customer");
            binding.getData().setCustomer(customer);
            AppSharedPref.setCartData(this, Helper.fromCartModelToString(binding.getData()));
        }
    }
}
