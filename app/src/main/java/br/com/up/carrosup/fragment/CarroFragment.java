package br.com.up.carrosup.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.parceler.Parcels;

import br.com.up.carrosup.R;
import br.com.up.carrosup.activity.CarroActivity;
import br.com.up.carrosup.domain.Carro;
import br.com.up.carrosup.utils.ImageUtils;
import livroandroid.lib.utils.IntentUtils;

/**
 * Fragment que mostra os dados do carro
 */
public class CarroFragment extends BaseFragment implements OnMapReadyCallback {
    protected ImageView img;
    protected TextView tNome;
    protected TextView tDesc;
    protected TextView tLatLng;
    private GoogleMap map;
    protected Carro carro;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Recebe o carro por parâmetro
        carro = Parcels.unwrap(getArguments().getParcelable("carro"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carro, container, false);
        initViews(view);
        // Mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }

    protected void initViews(View view) {
        img = (ImageView) view.findViewById(R.id.img);
        tNome = (TextView) view.findViewById(R.id.tNome);
        tDesc = (TextView) view.findViewById(R.id.tDesc);
        tLatLng = (TextView) view.findViewById(R.id.tLatLng);
        if (getArguments() != null) {
            setCarro(carro);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        if (carro != null && map != null) {
            // Ativa o botão para mostrar minha localização
            map.setMyLocationEnabled(true);
            // Cria o objeto LatLng com a coordenada da  fábrica
            double lat = carro.getLatitude();
            double lng = carro.getLongitude();
            if (lat > 0 && lng > 0) {
                LatLng location = new LatLng(lat, lng);
                // Posiciona o mapa na coordenada da fábrica (zoom = 13)
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location, 13);
                //map.moveCamera(update);
                map.animateCamera(update, 2000, null);
                // Marcador no local da fábrica
                map.addMarker(new MarkerOptions()
                        .title(carro.nome).snippet(carro.desc).position(location));
            }
            // Tipo do mapa: MAP_TYPE_NORMAL,
            // MAP_TYPE_TERRAIN, MAP_TYPE_HYBRID and MAP_TYPE_NONE map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    private void setCarro(Carro c) {
        if (c != null) {
            if (img != null) {
                ImageUtils.setImage(getContext(), c.urlFoto, img);
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showVideo();
                    }
                });
            }
            tNome.setText(c.nome);
            tDesc.setText(c.desc);
            if (tLatLng != null) {
                tLatLng.setText(String.format("%s/%s", c.latitude, c.longitude));
            }
        }
        // Imagem do Header na Toolbar
        CarroActivity activity = (CarroActivity) getActivity();
        activity.setAppBarInfo(c);
    }

    protected void showVideo() {
        // Abre o vídeo no Player de Vídeo Nativo
        if (carro.urlVideo != null && carro.urlVideo.trim().length() > 0) {
            if (URLUtil.isValidUrl(carro.urlVideo)) {
                IntentUtils.showVideo(getContext(), carro.urlVideo);
            } else {
                toast(getString(R.string.msg_url_invalida));
            }
        } else {
            toast(getString(R.string.msg_carro_sem_video));
        }
    }
}