package com.example.isabe.capstone_wineapp;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isabe.capstone_wineapp.database.AppDatabase;
import com.example.isabe.capstone_wineapp.database.FavoriteEntry;
import com.example.isabe.capstone_wineapp.object.Wine;
import com.example.isabe.capstone_wineapp.widget.WineWidgetProvider;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.isabe.capstone_wineapp.utilities.NetworkUtils.isOnline;

/**
 * Created by isabe on 7/20/2018.
 */

public class WineDetailsFragment extends Fragment {
    private static final String LOG_TAG = WineDetailsFragment.class.getSimpleName();
    private static final int RC_SIGN_IN = 1;
    private Unbinder unbinder;
    private static WineAdapter mWineAdapter;
    public Wine mWinePOJO;
    @BindBool(R.bool.isTablet)
    boolean tabletSize;
    public static final String WINE_INDEX = "wine_index";
    private int mCurrentCheckedPosition = 0;
    @BindView(R.id.image_wine)
    ImageView mWineImage;

    @BindView(R.id.tv_wine_varietal)
    TextView mWineVarietal;

    @BindView(R.id.tv_wine_name)
    TextView mWineName;

    @BindView(R.id.tv_wine_vintage)
    TextView mWineVintage;

    @BindView(R.id.tv_wine_region)
    TextView mWineRegion;

    @BindView(R.id.tv_wine_price)
    TextView mWinePrice;

    private String code;
    private String wineName;
    private String wineRegion;
    private String wineVarietal;
    private Double winePrice;
    private Double totalPrice = 0.00;
    private String wineVIntage;
    private String wineImage;
    private Double wineRank;
    private GoogleSignInClient mGoogleSignInClient;
    @BindView(R.id.sign_in_button)
    SignInButton mSignInButton;
    private TextView mEmailTextView;
    private TextView mWordingTextView;
    private String userEmail;
    private int noBottles = 0;

    @BindView(R.id.tv_qty_number)
    TextView mQtyTv;

    @BindView(R.id.tv_total_cost)
    TextView mTotalCost;

    @BindView(R.id.user_data)
    TextView mUserData;

    @BindView(R.id.sign_out)
    Button mSignOut;

    @BindView(R.id.tv_wine_rank)
    TextView mWineRank;

    @BindView(R.id.fab_save)
    FloatingActionButton mSaveButton;

    @BindView(R.id.fab_share)
    FloatingActionButton mShareButton;

    private static AppDatabase appDatabase;
    private static Context context = null;

    private static final int DEFAULT_WINE_ID = -1;
    private int mWineId = DEFAULT_WINE_ID;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mWinePOJO = args.getParcelable(WineListFragment.WINE_SELECTION);
            assert mWinePOJO != null;
            code = mWinePOJO.getmCode();
            wineName = mWinePOJO.getmWineName();
            wineRegion = mWinePOJO.getmWineRegion();
            wineVarietal = mWinePOJO.getmWineVarietal();
            winePrice = mWinePOJO.getmWinePrice();
            wineVIntage = mWinePOJO.getmWineVintage();
            wineImage = mWinePOJO.getmWineImage();
            wineRank = mWinePOJO.getmWineRank();
        }

        Activity activity = this.getActivity();

        CollapsingToolbarLayout collapsingToolbarLayout = activity.findViewById(R.id.collapsing_toolbar_layout);
        if (collapsingToolbarLayout != null) {
            collapsingToolbarLayout.setTitle(mWinePOJO.getmWineName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();

        View rootView = inflater.inflate(R.layout.fragment_details_wine, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        setUpDetailViews();

        configureGoogleSignIn();

        saveWineName(mWinePOJO.getmWineName(), mWinePOJO.getmWineImage());
        appDatabase = AppDatabase.getInstance(getActivity().getApplicationContext());
        return rootView;
    }

    public void setUpDetailViews() {
        if (wineImage != null && !wineImage.isEmpty()) {
            Picasso.get().load(wineImage).into(mWineImage);
        } else {
            Picasso.get()
                    .load(R.drawable.wine_png9490)
                    .placeholder(R.drawable.wine_png9490)
                    .error(R.drawable.wine_png9490)
                    .into(mWineImage);
        }

        mWineName.setText(wineName);
        mWinePrice.setText((winePrice).toString());
        if (!wineVarietal.isEmpty()) {
            mWineVarietal.setText(wineVarietal);
        } else {
            mWineVarietal.setText(R.string.missing_varietal);
        }
        if (!wineVarietal.isEmpty()) {
            mWineRegion.setText(wineRegion);
        } else {
            mWineRegion.setText(R.string.region_missing);
        }
        mWineVintage.setText(wineVIntage);
        if (!wineRank.toString().isEmpty()) {
            mWineRank.setText(String.valueOf(wineRank));
        } else {
            mWineRank.setText(R.string.no_rank);
        }
    }

    public void configureGoogleSignIn() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        updateUserData(account);
    }

    @OnClick(R.id.sign_in_button)
    public void onClick(View view) {
        signIn();

        Log.d(LOG_TAG, "User signed in " + userEmail);
    }


    public void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            int statusCode = result.getStatus().getStatusCode();

            Log.d(LOG_TAG, "OnActivityResult reached." + statusCode);
        }
    }

    public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUserData(account);
        } catch (ApiException e) {

            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(LOG_TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUserData(null);
            e.printStackTrace();
        }
    }

    public void updateUserData(GoogleSignInAccount account) {
        if (account != null) {
            userEmail = account.getEmail();
            String displayName = account.getDisplayName();
            mSignInButton.setVisibility(View.GONE);
            mUserData.setVisibility(View.VISIBLE);
            mSignOut.setVisibility(View.VISIBLE);
            mUserData.setText(displayName);
            Log.d(LOG_TAG, "User email is " + userEmail);
        }
    }

    @OnClick(R.id.sign_out)
    public void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mSignInButton.setVisibility(View.VISIBLE);
                        mSignOut.setVisibility(View.GONE);
                        mUserData.setVisibility(View.GONE);
                    }
                });
    }

    //This email will send order confirmation to youru Gmail, according to your Google Sign-In account.
    @OnClick(R.id.button_order)
    public void orderEmail() {
        String senderEmail = "isabella.velkova@gmail.com";
        String recipient = userEmail;
        Intent gmailIntent = new Intent(Intent.ACTION_SEND, Uri.fromParts(
                "mailto", senderEmail, null));
        gmailIntent.setType("text/plain");
        gmailIntent.putExtra(Intent.EXTRA_SUBJECT, "Wine Order Confirmation Email");
        gmailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{recipient});
        gmailIntent.putExtra(Intent.EXTRA_TEXT, "You just ordered " +
                noBottles + " of " + wineName);
        startActivity(Intent.createChooser(gmailIntent, "Send confirmation"));
    }


    //Quantity selection code
    @OnClick(R.id.increase_qty)
    public void increment(View view) {
        noBottles = noBottles + 1;
        displayQuantity(noBottles);
        totalPrice = calculatePrice();
        displayPrice(totalPrice);
    }

    private Double calculatePrice() {
        totalPrice = noBottles * winePrice;
        return totalPrice;
    }

    @OnClick(R.id.decrease_qty)
    public void decrement(View view) {
        if (noBottles > 0) {
            noBottles = noBottles - 1;
            displayQuantity(noBottles);
            totalPrice = calculatePrice();
            displayPrice(totalPrice);
        }
    }

    private void displayQuantity(int noBottles) {
        mQtyTv.setText(String.valueOf(noBottles));
    }

    private void displayPrice(double totalPrice) {
        String convertPriceToString = NumberFormat.getCurrencyInstance().format(totalPrice);
        mTotalCost.setText(convertPriceToString);
    }

    @OnClick(R.id.fab_save)
    void saveWine() {

        new AddFavoriteWIne().execute();
        Toast.makeText(getActivity(), getString(R.string.save_item_1) + wineName + getString(R.string.save_item_2), Toast.LENGTH_LONG).show();

        /**     String image = wineImage;
         String name = wineName;
         String region = wineRegion;

         final FavoriteEntry favoriteEntry = new FavoriteEntry(name, image, region);
         AppExecutors.getInstance().diskIO().execute(new Runnable() {
        @Override public void run() {
        appDatabase.wineDao().insertWine(favoriteEntry);
        //getActivity().finish();
        }
        });

         Toast.makeText(getActivity(), getString(R.string.save_item_1) + wineName + getString(R.string.save_item_2), Toast.LENGTH_LONG).show();
         **/
    }

    @OnClick(R.id.fab_share)
    void shareWine() {
        startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                .setType("text/plain")
                .setText(getString(R.string.share_text))
                .getIntent(), getString(R.string.intent_share)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public class AddFavoriteWIne extends AsyncTask<List<FavoriteEntry>, Void, List<FavoriteEntry>> {

        @Override
        protected List<FavoriteEntry> doInBackground(List<FavoriteEntry>[] lists) {

            String image = wineImage;
            String name = wineName;
            String region = wineRegion;

            final FavoriteEntry favoriteEntry = new FavoriteEntry(name, image, region);
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    appDatabase.wineDao().insertWine(favoriteEntry);
                }
            });

            return null;
        }
    }

    private void saveWineName(String wineWidgetName, String wineStringImage){
        Activity activity = getActivity();
        SharedPreferences sharedPreferences = activity.getSharedPreferences(
                getString(R.string.prefs_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.wine_name_prefs), wineWidgetName);
        mWineName.setText(wineName);

        editor.commit();

        Intent intent = new Intent(activity, WineWidgetProvider.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        int appWidgetIds[] = AppWidgetManager.getInstance(activity.getApplication())
                .getAppWidgetIds(new ComponentName(activity.getApplication(),
                        WineWidgetProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        activity.sendBroadcast(intent);

        Log.i(LOG_TAG, getString(R.string.saved_shared_prefs));

    }
}
