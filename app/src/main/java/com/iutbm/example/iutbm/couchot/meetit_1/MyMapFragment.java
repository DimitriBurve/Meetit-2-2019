package com.iutbm.example.iutbm.couchot.meetit_1;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyMapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyMapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GoogleApiClient mGoogleApiClient = null;

    private AppDatabase appdb;
    private CharacterDAO characterDAO;

    private List<Character> characterList = new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    private static int REQUEST_LOCATION=1;
    private Location mLastLocation;
    private boolean mRequestingLocationUpdates;
    private LocationRequest mLocationRequest;

    public MyMapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyMapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyMapFragment newInstance(String param1, String param2) {
        MyMapFragment fragment = new MyMapFragment();
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
        if (mGoogleApiClient == null){
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_my_map, container, false);
        appdb = AppDatabase.getDatabase(getContext());
        characterDAO = appdb.characterDAO();
        (new MyMapFragment.GetAllCharactersAsyncTask(characterDAO)).execute();
        View root = inflater.inflate(R.layout.fragment_my_map, container, false);
        SupportMapFragment mapFragment= (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng[] latLngsTab = new LatLng[characterList.size()];
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (int i=0;i<latLngsTab.length;i++){
            latLngsTab[i]= new LatLng(characterList.get(i).getLatitude(),characterList.get(i).getLongitude());
            googleMap.addMarker(new MarkerOptions().position(latLngsTab[i]).title("Marker for "+characterList.get(i).getFamilyname()+" "+characterList.get(i).getFirstname()).alpha(0.5f));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLngsTab[i]));
            builder.include(latLngsTab[i]);
        }

        LatLngBounds bounds = builder.build();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,150));

//        LatLng sydney = new LatLng(-34, 151);
//        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }
    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
        } else {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                String lats = "" + mLastLocation.getLatitude();
                String longs = "" + mLastLocation.getLongitude();
                Toast.makeText(getActivity(), lats + " " + longs, Toast.LENGTH_LONG).show();
            }
        }
        if (mRequestingLocationUpdates){
            startLocationUpdates();
        }else {
            stopLocationUpdates();
        }
    }
    private void requestLocationPermission(){
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_LOCATION);
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(getActivity(), location.getLatitude() + " " + location.getLongitude(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        setLocationParameters();
        if (mGoogleApiClient.isConnected()){
            if (mRequestingLocationUpdates) {
                startLocationUpdates();
            } else {
                stopLocationUpdates();
            }
        }
    }

    private void setLocationParameters() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean myBool = preferences.getBoolean(getResources().getString(R.string.key_location_switch) , false);
        int valueDelay = Integer.parseInt(preferences.getString(getResources().getString(R.string.key_search_delay), "10"));
        mRequestingLocationUpdates = myBool;
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(valueDelay);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
    }
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class GetAllCharactersAsyncTask extends AsyncTask<Void, Void, Void> {
        private CharacterDAO mAsyncTaskDao;
        ArrayList<Character> characters;

        GetAllCharactersAsyncTask(CharacterDAO dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            characters = new ArrayList<>(mAsyncTaskDao.getAllCharaters());
            return null;
        }
        @Override
        protected void onPostExecute(Void voids){
            characterList = characters;
//            recyclerView.setAdapter(new CharacterListAdapter(characterList,getContext()));

        }

    }
}
