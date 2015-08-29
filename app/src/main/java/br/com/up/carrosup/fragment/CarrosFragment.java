package br.com.up.carrosup.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.parceler.Parcels;

import java.util.List;
import br.com.up.carrosup.R;
import br.com.up.carrosup.activity.CarroActivity;
import br.com.up.carrosup.domain.Carro;
import br.com.up.carrosup.domain.CarroService;
import br.com.up.carrosup.fragment.adapter.CarroAdapter;
import livroandroid.lib.fragment.BaseFragment;
/**
 * Created by ricardo on 12/06/15.
 */
public class CarrosFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private List<Carro> carros;
    private String tipo;
    private SwipeRefreshLayout swipeLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.tipo = getArguments().getString("tipo");
        }
        // Para inflar itens de menu na toolbar
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carros, container, false);
        // Lista
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // Swipe to Refresh
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeToRefresh);
        swipeLayout.setOnRefreshListener(OnRefreshListener());
        swipeLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);

        // FAB
        view.findViewById(R.id.fabAddCarro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snack(recyclerView,"Depois vamos adicionar um carro.");
            }
        });

        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listaCarros(false);
    }
    private void listaCarros(boolean refresh) {
        carros = CarroService.getCarros(getContext(),tipo);
        recyclerView.setAdapter(new CarroAdapter(getContext(), carros, onClickCarro()));
    }
    protected CarroAdapter.CarroOnClickListener onClickCarro() {
        return new CarroAdapter.CarroOnClickListener() {
            @Override
            public void onClickCarro(CarroAdapter.CarrosViewHolder holder, int idx) {
                Carro c = carros.get(idx);
                Intent intent = new Intent(getContext(), CarroActivity.class);
                intent.putExtra("carro", Parcels.wrap(c));

                // Transição animada
                ImageView img = (ImageView) holder.img;
                String key = getString(R.string.transition_key);
                ActivityOptionsCompat opts = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(), img, key);
                ActivityCompat.startActivity(getActivity(), intent, opts.toBundle());
            }
            @Override
            public void onLongClickCarro(CarroAdapter.CarrosViewHolder holder, int idx) {
                toast("long click");
            }
        };
    }
    private SwipeRefreshLayout.OnRefreshListener OnRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listaCarros(true);
                swipeLayout.setRefreshing(false);
            }
        };
    }
}