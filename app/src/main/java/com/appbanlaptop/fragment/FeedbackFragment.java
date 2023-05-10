package com.appbanlaptop.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appbanlaptop.R;
import com.appbanlaptop.model.OrderModel;
import com.appbanlaptop.retrofit.ApiShopLapTop;
import com.appbanlaptop.retrofit.RetrofitClient;
import com.appbanlaptop.utils.Utils;
import com.bumptech.glide.Glide;

import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedbackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedbackFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View layoutView;

    ImageView imageViewLaptop;
    TextView tvLaptopName;
    AppCompatRatingBar ratingBar;
    EditText etReview;
    Button btnSend;

    ApiShopLapTop apiShopLapTop;
    int star;

    public FeedbackFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeedbackFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedbackFragment newInstance(String param1, String param2) {
        FeedbackFragment fragment = new FeedbackFragment();
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

        RxJavaPlugins.setErrorHandler(Timber::e);

        apiShopLapTop = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiShopLapTop.class);

        // Inflate the layout for this fragment
        layoutView = inflater.inflate(R.layout.fragment_feedback, container, false);

        AnhXa();

        if (isConnected(getContext())) {
            setData();
            chooseStar();
            sendFeedback();
        } else {
            Toast.makeText(getContext(), "Connect fail!", Toast.LENGTH_LONG).show();
        }

        return layoutView;
    }

    private void setData() {
        // Lấy dữ liệu từ bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            String name = bundle.getString("name");
            String image_url = bundle.getString("image_url");
            tvLaptopName.setText(name);
            Glide.with(getContext()).load(image_url).into(imageViewLaptop);
        }
    }

    private void sendFeedback() {
        btnSend.setOnClickListener(view -> {
            // Lấy dữ liệu từ bundle
            Bundle bundle = getArguments();
            if (bundle != null) {
                int detailId = bundle.getInt("detail_id");
                int laptopId = bundle.getInt("laptop_id");
                String user_fullname = bundle.getString("user_fullname");
                String user_avatar = bundle.getString("user_avatar");
                String comment = etReview.getText().toString();

                Call<OrderModel> call = apiShopLapTop.sendFeedback(
                        String.valueOf(detailId),
                        String.valueOf(star),
                        comment,
                        user_fullname,
                        user_avatar,
                        String.valueOf(laptopId)
                );
                call.enqueue(new Callback<OrderModel>() {
                    @Override
                    public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                        OrderModel orderModel = response.body();
                        if (orderModel.isSuccess()) {
                            Toast.makeText(getContext(), orderModel.getMessage(), Toast.LENGTH_LONG).show();
                            Navigation.findNavController(view).navigate(R.id.action_feedbackFragment_to_menuOrderHistory);
                        } else {
                            Toast.makeText(getContext(), orderModel.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderModel> call, Throwable t) {
                        call.cancel();
                        Toast.makeText(getContext(), "Lỗi kết nối đến Server!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void chooseStar() {
        star = (int) ratingBar.getRating();
        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, b) -> {
            star = (int) rating;
        });
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
        imageViewLaptop = layoutView.findViewById(R.id.imageViewLaptop);
        tvLaptopName = layoutView.findViewById(R.id.tvLaptopName);
        ratingBar = layoutView.findViewById(R.id.ratingBar);
        etReview = layoutView.findViewById(R.id.etReview);
        btnSend = layoutView.findViewById(R.id.btnSend);
    }
}