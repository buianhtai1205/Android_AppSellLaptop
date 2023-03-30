package com.appbanlaptop.fragment;

import android.content.Context;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appbanlaptop.R;
import com.appbanlaptop.model.Laptop;
import com.appbanlaptop.model.LaptopModel;
import com.appbanlaptop.retrofit.ApiShopLapTop;
import com.appbanlaptop.retrofit.RetrofitClient;
import com.appbanlaptop.utils.Utils;
import com.bumptech.glide.Glide;


import org.commonmark.node.Image;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.AttributeProviderContext;
import org.commonmark.renderer.html.AttributeProviderFactory;
import org.commonmark.renderer.html.HtmlRenderer;

import java.text.DecimalFormat;
import java.util.Map;

import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LaptopDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LaptopDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LaptopDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LaptopDetailFragment.
     */
    // TODO: Rename and change types and number of parameters

    View layoutView;
    ApiShopLapTop apiShopLapTop;
    String id;
    TextView tvLaptopBrandName, tvLaptopName, tvLaptopPrice, tvLaptopSalePrice, tvLaptopCpu;
    TextView tvLaptopRam, tvLaptopRom, tvLaptopScreen, tvLaptopCard, tvLaptopConnector;
    TextView tvLaptopSpecial, tvLaptopOs, tvLaptopWeight, tvLaptopPin, tvLaptopYearLaunch;
    ImageView imageLaptop;
    WebView webView;

    public static LaptopDetailFragment newInstance(String param1, String param2) {
        LaptopDetailFragment fragment = new LaptopDetailFragment();
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
        layoutView = inflater.inflate(R.layout.fragment_laptop_detail, container, false);
        RxJavaPlugins.setErrorHandler(Timber::e);

        apiShopLapTop = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiShopLapTop.class);

        // getArguments from parent
        id = getArguments().getString("id");

        //Anh xa
        AnhXa();

        if (isConnected(getContext())) {
            getLaptopDetail(id);
        } else {
            Toast.makeText(getContext(), "Connect fail!", Toast.LENGTH_LONG).show();
        }


        return layoutView;
    }

    private void getLaptopDetail(String id) {
        Call<LaptopModel> call = apiShopLapTop.getLaptopDetail(id);
        call.enqueue(new Callback<LaptopModel>() {
            @Override
            public void onResponse(Call<LaptopModel> call, Response<LaptopModel> response) {
                LaptopModel laptopModel = response.body();
                if (laptopModel.isSuccess()) {
                    setDataResponseToView(laptopModel);
                }
            }
            @Override
            public void onFailure(Call<LaptopModel> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void setDataResponseToView(LaptopModel laptopModel) {
        Laptop laptop = laptopModel.getResult().get(0);
        tvLaptopBrandName.setText("Laptop / Laptop " + laptop.getBrand().getName());
        Glide.with(layoutView.getContext()).load(laptop.getImage_url()).into(imageLaptop);
        tvLaptopName.setText(laptop.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tvLaptopPrice.setText(decimalFormat.format(laptop.getPrice()) + "đ");
        tvLaptopPrice.setPaintFlags(tvLaptopPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        tvLaptopSalePrice.setText(decimalFormat.format(laptop.getSale_price()) + "đ");
        tvLaptopCpu.setText(laptop.getCpu());
        tvLaptopRam.setText(laptop.getRam());
        tvLaptopRom.setText(laptop.getRom());
        tvLaptopScreen.setText(laptop.getScreen());
        tvLaptopCard.setText(laptop.getCard());
        tvLaptopSpecial.setText(laptop.getSpecial());
        tvLaptopOs.setText(laptop.getOs());
        tvLaptopWeight.setText(Float.toString(laptop.getWeight()));
        tvLaptopPin.setText(laptop.getPin());
        tvLaptopYearLaunch.setText(String.valueOf(laptop.getYear_launch()));
        tvLaptopConnector.setText(laptop.getConnector());

        //markdown
        String markdown = laptop.getDescription();
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().attributeProviderFactory(context -> new CustomAttributeProvider()).build();
        String html = renderer.render(document);

        webView.setWebViewClient(new WebViewClient());
        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
    }

    static class CustomAttributeProvider implements AttributeProvider {
        public void setAttributes(Node node, String tagName, Map<String, String> attributes) {
            if (node instanceof Image) {
                attributes.put("width", "100%");
                attributes.put("height", "auto");
            }
        }
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

    private void AnhXa() {
        tvLaptopBrandName = layoutView.findViewById(R.id.tvLaptopBrandName);
        tvLaptopName = layoutView.findViewById(R.id.tvLaptopName);
        tvLaptopPrice = layoutView.findViewById(R.id.tvLaptopPrice);
        tvLaptopSalePrice = layoutView.findViewById(R.id.tvLaptopSalePrice);
        tvLaptopCpu = layoutView.findViewById(R.id.tvLaptopCpu);
        tvLaptopRam = layoutView.findViewById(R.id.tvLaptopRam);
        tvLaptopRom = layoutView.findViewById(R.id.tvLaptopRom);
        tvLaptopScreen = layoutView.findViewById(R.id.tvLaptopScreen);
        tvLaptopConnector = layoutView.findViewById(R.id.tvLaptopConnector);
        tvLaptopCard = layoutView.findViewById(R.id.tvLaptopCard);
        tvLaptopSpecial = layoutView.findViewById(R.id.tvLaptopSpecial);
        tvLaptopOs = layoutView.findViewById(R.id.tvLaptopOs);
        tvLaptopWeight = layoutView.findViewById(R.id.tvLaptopWeight);
        tvLaptopPin = layoutView.findViewById(R.id.tvLaptopPin);
        tvLaptopYearLaunch = layoutView.findViewById(R.id.tvLaptopYearLaunch);
        imageLaptop = layoutView.findViewById(R.id.imageLaptop);

        webView = layoutView.findViewById(R.id.webView);
    }
}