package br.com.up.carrosup.activity;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import br.com.up.carrosup.R;
import br.com.up.carrosup.domain.Carro;

public class CarroActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carro);

        setupToolbar();

        Carro c = Parcels.unwrap(getIntent().getExtras().getParcelable("carro"));

        getSupportActionBar().setTitle(c.nome);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tDesc = (TextView) findViewById(R.id.tDesc);
        ImageView img = (ImageView) findViewById(R.id.img);
        ViewCompat.setTransitionName(img, getString(R.string.transition_key));

//        tNome.setText("Carro: " + c.nome);
        tDesc.setText(c.desc);
        Picasso.with(this).load(c.urlFoto).into(img);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_carro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
