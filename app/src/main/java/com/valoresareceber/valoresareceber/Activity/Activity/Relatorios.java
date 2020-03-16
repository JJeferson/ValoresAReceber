package com.valoresareceber.valoresareceber.Activity.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.valoresareceber.valoresareceber.Activity.Activity.Pickers.Picker01;
import com.valoresareceber.valoresareceber.Activity.Activity.Pickers.Picker02;
import com.valoresareceber.valoresareceber.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.valoresareceber.valoresareceber.Activity.Activity.Pickers.Picker01.ARQUIVO_REFERENCIA;

public class Relatorios extends AppCompatActivity {

    private Button       B_DT_Inicial;
    private Button       B_DT_Final;
    private Button       B_Em_Aberto;
    private Button       B_Pagas;
    private Button       B_Tudo;
    private Button       Voltar;
    private TextView     DT_Inicial;
    private TextView     DT_Final;

    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relatorios);

        final SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO_REFERENCIA, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();


        B_DT_Inicial      = (Button) findViewById(R.id.B_DT_Inicial);
        B_DT_Final        = (Button) findViewById(R.id.B_DT_Final);
        B_Em_Aberto       = (Button) findViewById(R.id.B_Em_Aberto);
        B_Pagas           = (Button) findViewById(R.id.B_Pagas);
        B_Tudo            = (Button) findViewById(R.id.B_Tudo);
        Voltar            = (Button) findViewById(R.id.Voltar);


        DT_Inicial         = (TextView) findViewById(R.id.DT_Inicial);
        DT_Final           = (TextView) findViewById(R.id.DT_Final);

         editor.putString("TipoRelatorio","");
         editor.commit();
       //  final String RecebeInicio   = "31/08/2018";//sharedPreferences.getString("Inicio","");
        // final String RecebeFim      = "31/08/2018";//sharedPreferences.getString("Fim","");
        final String RecebeInicio   = sharedPreferences.getString("Inicio","");
        final String RecebeFim      = sharedPreferences.getString("Fim","");
        DT_Inicial.setText("Clique ao lado para selecionar um periodo");

        DT_Final.setText("Clique ao lado para selecionar um periodo");

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        //------------------------------------------------------------------------------------//
        MobileAds.initialize(this, "ca-app-pub-6828909553286825~5817339983");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //------------------------------------------------------------------------------------//


        //RecebeFim = "31/08/2018";


         if (RecebeInicio.length()>0){
             DT_Inicial.setText(RecebeInicio);
             }

        if (RecebeFim.length()>0){

            DT_Final.setText(RecebeFim);
             }




        B_DT_Inicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("Inicio","");
                editor.commit();
                startActivity( new Intent(Relatorios.this,Picker01.class));

            }//fim da ação
        });//fim do botãonovo

        B_DT_Final.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putString("Fim","");
                editor.commit();
                startActivity( new Intent(Relatorios.this,Picker02.class));
            }//fim da ação
        });

        B_Em_Aberto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RecebeInicio.length()==0){
                    Toast.makeText(Relatorios.this, "Selecione uma data de Inicio", Toast.LENGTH_SHORT).show();
                }else{
                                               if (RecebeFim.length()==0){
                                                   Toast.makeText(Relatorios.this, "Selecione uma data Final para a pesquisa!", Toast.LENGTH_SHORT).show();

                                               }else{
                                                   //Aqui vai o codigo

                                                   editor.putString("TipoRelatorio","Aberto");
                                                   editor.putString("Fim",RecebeFim);
                                                   editor.putString("Inicio",RecebeInicio);
                                                   editor.commit();
                                                   startActivity( new Intent(Relatorios.this,Preview.class));
                                               }//Fim do segundo else
                }//fim do primeiro else



            }//fim da ação
        });//fim do botãonovo
        B_Pagas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (RecebeInicio.length()==0){
                    Toast.makeText(Relatorios.this, "Selecione uma data de Inicio", Toast.LENGTH_SHORT).show();
                }else{
                    if (RecebeFim.length()==0){
                        Toast.makeText(Relatorios.this, "Selecione uma data Final para a pesquisa!", Toast.LENGTH_SHORT).show();

                    }else{
                        //Aqui vai o codigo
                        editor.putString("TipoRelatorio","Pago");
                        editor.putString("Fim",RecebeFim);
                        editor.putString("Inicio",RecebeInicio);
                        editor.commit();
                        startActivity( new Intent(Relatorios.this,Preview.class));

                    }//Fim do segundo else
                }//fim do primeiro else




            }//fim da ação
        });//fim do botãonovo
        B_Tudo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (RecebeInicio.length()==0){
                    Toast.makeText(Relatorios.this, "Selecione uma data de Inicio", Toast.LENGTH_SHORT).show();
                }else{
                    if (RecebeFim.length()==0){
                        Toast.makeText(Relatorios.this, "Selecione uma data Final para a pesquisa!", Toast.LENGTH_SHORT).show();

                    }else{
                        //Aqui vai o codigo
                        editor.putString("TipoRelatorio","Tudo");
                        editor.putString("Fim",RecebeFim);
                        editor.putString("Inicio",RecebeInicio);
                        editor.commit();
                        startActivity( new Intent(Relatorios.this,Preview.class));

                    }//Fim do segundo else
                }//fim do primeiro else




            }//fim da ação
        });//fim do botãonovo

        Voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("TipoRelatorio","");
                editor.putString("Inicio","");
                editor.putString("Fim","");
                editor.commit();
                startActivity( new Intent(Relatorios.this,Pesquisa_SQLITE.class));

            }//fim da ação
        });//fim do botãonovo









    }//fim do oncreate


    //-------------------------------------------------------------------------------------------------------------------------//
    @Override
    protected void onPause() {
        //Pausando o AdView ao pausar a activity
        mAdView.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Resumindo o AdView ao resumir a activity
        mAdView.resume();
    }

    @Override
    protected void onDestroy() {
        //Destruindo o AdView ao destruir a activity
        mAdView.destroy();
        super.onDestroy();
    }

    //-------------------------------------------------------------------------------------------------------------------------//


}//fim da classe Java
