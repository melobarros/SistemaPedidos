package com.tabian.sistemapedidos;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;

import com.tabian.sistemapedidos.Fragments.ClienteFragment;
import com.tabian.sistemapedidos.Fragments.ItemFragment;
import com.tabian.sistemapedidos.Fragments.PedidoFragment;
import com.tabian.sistemapedidos.Fragments.ProdutoFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: Starting.");

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new ClienteFragment(), "CLIENTE");
        adapter.addFragment(new ProdutoFragment(), "PRODUTO");
        adapter.addFragment(new ItemFragment(), "ITEM");
        adapter.addFragment(new PedidoFragment(), "PEDIDO");
        viewPager.setAdapter(adapter);
    }

}
