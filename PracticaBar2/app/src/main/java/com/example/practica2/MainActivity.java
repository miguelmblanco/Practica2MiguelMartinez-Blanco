package com.example.practica2;
// Autor: Miguel Martinez-Blanco

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    AutoCompleteTextView camarero;
    EditText total;
    CheckBox checkPropina;
    SeekBar seekPropina;
    TextView textoPorcentaje, resultado;
    RadioButton radioEfectivo;
    RatingBar rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // busco todo lo del layout
        camarero = findViewById(R.id.Camarero);
        total = findViewById(R.id.Total);
        checkPropina = findViewById(R.id.CheckPropina);
        seekPropina = findViewById(R.id.SeekPropina);
        textoPorcentaje = findViewById(R.id.TextoPorcentaje);
        radioEfectivo = findViewById(R.id.RadioEfectivo);
        rating = findViewById(R.id.Rating);
        resultado = findViewById(R.id.Resultado);
        Button calcular = findViewById(R.id.BotonCalcular);

        // lista de camareros, sale con 3 letras
        String[] camareros = {"Carlos", "Carmen", "Marcos", "Maria", "Miguel", "Lucia", "Laura"};
        ArrayAdapter<String> adaptador = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, camareros);
        camarero.setAdapter(adaptador);
        camarero.setThreshold(3);

        // texto inicial del porcentaje (10)
        textoPorcentaje.setText(getString(R.string.porcentaje, seekPropina.getProgress()));

        // cuando muevo la barra cambio el texto
        seekPropina.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textoPorcentaje.setText(getString(R.string.porcentaje, progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // boton calcular
        calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcular();
            }
        });
    }

    void calcular() {
        String t = total.getText().toString().trim();

        // si no ha puesto nada
        if (t.equals("")) {
            error(getString(R.string.error_vacio));
            return;
        }

        // si no es un numero (por ejemplo solo un punto o un menos)
        double cuenta;
        try {
            cuenta = Double.parseDouble(t.replace(",", "."));
        } catch (NumberFormatException e) {
            error(getString(R.string.error_formato));
            return;
        }

        // 0 o negativo
        if (cuenta <= 0) {
            error(getString(R.string.error_minimo));
            return;
        }

        // la propina solo si esta marcado el check
        double propina = 0;
        if (checkPropina.isChecked()) {
            propina = cuenta * seekPropina.getProgress() / 100;
        }
        double totalFinal = cuenta + propina;

        // metodo de pago
        String metodo;
        if (radioEfectivo.isChecked()) {
            metodo = getString(R.string.efectivo);
        } else {
            metodo = getString(R.string.tarjeta);
        }

        int estrellas = (int) rating.getRating();

        // si todo va bien lo pongo en blanco
        resultado.setTextColor(Color.WHITE);
        resultado.setText(getString(R.string.resultado, totalFinal, metodo, estrellas));
    }

    // los errores salen en rojo
    void error(String mensaje) {
        resultado.setTextColor(Color.RED);
        resultado.setText(mensaje);
    }
}