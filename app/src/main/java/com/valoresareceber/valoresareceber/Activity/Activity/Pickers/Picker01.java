package com.valoresareceber.valoresareceber.Activity.Activity.Pickers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.valoresareceber.valoresareceber.Activity.Activity.Relatorios;
import com.valoresareceber.valoresareceber.R;

public class Picker01 extends AppCompatActivity {

    private DatePicker DATA;
    private Button grava_ID;
    private Button volta_ID;


    public static final String ARQUIVO_REFERENCIA = "ArquivoReferencia";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picker01);


        volta_ID    = (Button) findViewById(R.id.volta_ID);
        grava_ID    = (Button) findViewById(R.id.grava_ID);
        DATA     = (DatePicker) findViewById(R.id.DATA);

        final SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO_REFERENCIA, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();




        grava_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String RecebeEmissao = DATA.getYear() + "-0" + (DATA.getMonth()+1) + "-0" + DATA.getDayOfMonth();
               // String RecebeInicio = DATA.getYear() + "-" + (DATA.getMonth()+1) + "-" + DATA.getDayOfMonth();
                String TestaDia ="";
                TestaDia = String.valueOf(DATA.getDayOfMonth());
                if(TestaDia.length()==1){
                    TestaDia = "0"+TestaDia;
                }
                String TestaMes ="";

                TestaMes = String.valueOf(DATA.getMonth()+1);
                if(TestaMes.length()==1){
                    TestaMes = "0"+TestaMes;
                }

                String RecebeInicio =  TestaDia+"/" + TestaMes+ "/" + +DATA.getYear();
                editor.putString("Inicio",RecebeInicio);
                editor.commit();
                startActivity( new Intent(Picker01.this,Relatorios.class));
            }//fim da ação
        });//fim do botãonovo

        volta_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putString("Inicio","");
                editor.commit();
                startActivity( new Intent(Picker01.this,Relatorios.class));
            }//fim da ação
        });//fim do botãonovo


    }
}
