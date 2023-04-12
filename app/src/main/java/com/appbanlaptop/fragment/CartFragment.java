package com.appbanlaptop.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.appbanlaptop.R;
import com.appbanlaptop.adapter.CartAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters

    View layoutView;
    RecyclerView recyclerViewCart;
    CartAdapter cartAdapter;

    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layoutView = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerViewCart = layoutView.findViewById(R.id.recyclerViewCart);

        if (isConnected(getContext())) {
            HashMap<Integer, HashMap<String, String>> cart;
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("CartPrefs", Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("cart", "");
            Type type = new TypeToken<HashMap<Integer, HashMap<String, String>>>() {}.getType();
            cart = gson.fromJson(json, type);

            Log.d("CartFrag", "onCreateViell call()");

            cartAdapter = new CartAdapter(cart);

            Log.d("CartFrag", String.valueOf(cart.size()));

            LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerViewCart.setLayoutManager(linearLayoutManager);

            recyclerViewCart.setAdapter(cartAdapter);

        } else {
            Toast.makeText(getContext(), "Connect fail!", Toast.LENGTH_LONG).show();
        }

        return layoutView;
    }

    private boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected())) {
            return true;
        }
        return false;
    }
}