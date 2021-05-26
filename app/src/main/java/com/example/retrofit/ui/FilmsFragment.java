package com.example.retrofit.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.retrofit.R;
import com.example.retrofit.data.model.Film;
import com.example.retrofit.data.remote.RetrofitBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilmsFragment extends Fragment {

    private RecyclerView recyclerView;
    private FilmsAdapter filmsAdapter;
    private List<Film> films = new ArrayList<>();
    private NavController controller;

    public FilmsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_films, container, false);
        controller = Navigation.findNavController(requireActivity(), R.id.navHostFragment);
        openFilm(view);
        return view;
    }

    private void openFilm(View view) {
        RetrofitBuilder.getInstance().getAllFilm().enqueue(new Callback<List<Film>>() {
            @Override
            public void onResponse(Call<List<Film>> call, Response<List<Film>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    films.clear();
                    films.addAll(response.body());
                    init(view);}
            }

            private void init(View view) {
                recyclerView = view.findViewById(R.id.rv_films);
                filmsAdapter = new FilmsAdapter();
                filmsAdapter.setClickListener(film -> {
                    Bundle bundle = new Bundle();
                    bundle.putString("film", film.getId());
                    controller.navigate(R.id.filmsInfoFragment, bundle);
                });
                filmsAdapter.addToList(films);
                recyclerView.setAdapter(filmsAdapter);
            }

            @Override
            public void onFailure(Call<List<Film>> call, Throwable t) {
            }
        });
    }
}