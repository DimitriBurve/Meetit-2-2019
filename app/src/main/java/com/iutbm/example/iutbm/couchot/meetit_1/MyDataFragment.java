package com.iutbm.example.iutbm.couchot.meetit_1;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyDataFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyDataFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String CLEFPREFERENCE = "param2";

    private AppDatabase appdb;
    private CharacterDAO characterDAO;

    private List<Character> characterList = new ArrayList<>();
    private RecyclerView recyclerView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MyDataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyDataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyDataFragment newInstance(String param1, String param2) {
        MyDataFragment fragment = new MyDataFragment();
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
        View v = inflater.inflate(R.layout.fragment_my_data, container, false);

        recyclerView =  v.findViewById(R.id.character_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));

        appdb = AppDatabase.getDatabase(getContext());
        characterDAO = appdb.characterDAO();

        // pour n'exécuter ce code qu'une fois pour toute.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (! sp.getBoolean(getResources().getString(R.string.key_is_db_initialized),false)) {
            prepareCharacterData();
            sp.edit().putBoolean(getResources().getString(R.string.key_is_db_initialized),true).commit();
        }

        (new GetAllCharactersAsyncTask(characterDAO)).execute();
        return v;
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

    public void prepareCharacterData() {
         Character c1, c2, c3;
            c1 = new Character("Jean-François", "Couchot", "http://members.femto-st.fr/jf-couchot/fr", 47.642900f, 6.840027f,"couchot.png");
            c2 = new Character("Raphaël", "Couturier", "http://members.femto-st.fr/raphael-couturier/fr", 47.659518f, 6.813337f,"couturier.png");
            c3 = new Character("Stéphane","Domas", "http://info.iut-bm.univ-fcomte.fr/staff/sdomas/", 47.6387143f, 6.8370225f,"domas.png");
        (new InsertAsyncTask(characterDAO)).execute(c1,c2,c3);
    }

    private class InsertAsyncTask extends AsyncTask<Character, Void, Void> {
        private CharacterDAO dao;
        InsertAsyncTask(CharacterDAO dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(final Character... params) {
            for (Character c : params) {
                this.dao.insertCharacters(c);
            }
            return null;
        }
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
            recyclerView.setAdapter(new CharacterListAdapter(characterList,getContext()));

        }

    }



}
