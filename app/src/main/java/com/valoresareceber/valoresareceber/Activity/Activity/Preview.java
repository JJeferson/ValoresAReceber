package com.valoresareceber.valoresareceber.Activity.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.valoresareceber.valoresareceber.Activity.Activity.Adapter.Adapter;
import com.valoresareceber.valoresareceber.Activity.Activity.Model.Modelo;
import com.valoresareceber.valoresareceber.R;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.RED;
import static com.valoresareceber.valoresareceber.Activity.Activity.Pickers.Picker01.ARQUIVO_REFERENCIA;

public class Preview extends AppCompatActivity {

    private TextView     Titulo;
    private TextView     t1;
    private TextView     t2;
    private TextView     t3;
    private TextView     t4;
    private TextView     t5;
    private TextView     t6;
    private TextView     OBS;
    private TextView     OBS1;
    private Button Voltar;

    private RecyclerView recyclerView_ID;
    private List<Modelo> listaitens = new ArrayList<>();

    private AdView mAdView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview);

        final SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO_REFERENCIA, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        final ConverteDatas converteDatas = new ConverteDatas();

        t1     = (TextView) findViewById(R.id.t1);
        t2     = (TextView) findViewById(R.id.t2);
        t3     = (TextView) findViewById(R.id.t3);
        t4     = (TextView) findViewById(R.id.t4);
        t5     = (TextView) findViewById(R.id.t5);
        t6     = (TextView) findViewById(R.id.t6);
        OBS     = (TextView) findViewById(R.id.OBS);
        OBS1     = (TextView) findViewById(R.id.OBS1);
        Titulo = (TextView) findViewById(R.id.Titulo);

        Voltar            = (Button) findViewById(R.id.Voltar);
        recyclerView_ID = (RecyclerView) findViewById(R.id.recyclerView_ID);


        //------------------------------------------------------------------------------------//
        MobileAds.initialize(this, "ca-app-pub-6828909553286825~5817339983");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //------------------------------------------------------------------------------------//

       //********************************************************************************************
        //Adaptador

        //Configurando o RecyclerViwe
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView_ID.setLayoutManager(layoutManager);

        recyclerView_ID.setHasFixedSize(true);
        //Instanciando adaptador
        final Adapter adapter = new Adapter(listaitens);
        recyclerView_ID.setAdapter(adapter);
        //listaitens.clear();


        //********************************************************************************************

        String RecebeInicio    = sharedPreferences.getString("Inicio","");
        String RecebeFim       = sharedPreferences.getString("Fim","");
        String Tipo_Relatorio  = sharedPreferences.getString("TipoRelatorio","");
        RecebeInicio = ConverteDatas.ConverteData_BR_Para_Ingles(RecebeInicio);
        RecebeFim    = ConverteDatas.ConverteData_BR_Para_Ingles(RecebeFim);

        //-----------------------------------------------------------------------------------------------//
        // Construtor do banco de dados
        try {
            //Inicindo banco de dados
            SQLiteDatabase bancoDados = openOrCreateDatabase("Financeiro", MODE_PRIVATE, null);


            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS contasreceber (ID INTEGER PRIMARY KEY AUTOINCREMENT,fone VARCHAR, nome VARCHAR,emissao DATE,vcto DATE, valor FLOAT, valorpgto FLOAT);");

        } catch (Exception e) {
            e.printStackTrace();

        }//fim do try

        //-----------------------------------------------------------------------------------------------//

        Voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("TipoRelatorio","");
                editor.putString("Fim","");
                editor.putString("Inicio","");
                editor.commit();
                startActivity( new Intent(Preview.this,Relatorios.class));


            }//fim da ação
        });//fim do botãonovo
        //-----------------------------------------------------------------------------------------------//



        //evento de click
        recyclerView_ID.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerView_ID,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {


                                final Modelo modelo = listaitens.get(position);

                                // Toast.makeText(getApplicationContext(), "Item pressionado: " + modelo.getID(),Toast.LENGTH_SHORT ).show();

                                //Criando o AlertDialog
                                final AlertDialog.Builder alertDialogPAGAMENTO= new AlertDialog.Builder(Preview.this);
                                //Titulo
                                alertDialogPAGAMENTO.setTitle("Pagamento");
                                //Perguntas
                                alertDialogPAGAMENTO.setMessage("EFETUAR PAGAMENTO?");
                                //Faz não poder sair
                                alertDialogPAGAMENTO.setCancelable(false);
                                final EditText recebeValorpgto = new EditText(Preview.this);
                                //recebeValorpgto.setInputType(InputType.TYPE_CLASS_NUMBER);// diz que meu edittext é numerico
                                alertDialogPAGAMENTO.setView(recebeValorpgto);

                                alertDialogPAGAMENTO.setPositiveButton("PAGAR", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        //PAGAMENTO AQUI

                                        //substitui as virgulas por ponto
                                        String recebeValorPGTOString = recebeValorpgto.getText().toString();
                                        recebeValorPGTOString = recebeValorPGTOString.replaceAll(",", ".");

                                        Float PGTO = Float.valueOf(0);
                                        PGTO = Float.valueOf(recebeValorPGTOString);
                                        try{
                                            //abre o banco
                                            SQLiteDatabase bancoDados = openOrCreateDatabase("Financeiro", MODE_PRIVATE, null);
                                            // bancoDados.execSQL("DELETE FROM contasreceber  WHERE ID="+ID);

                                            bancoDados.execSQL("UPDATE contasreceber  SET valorpgto="+PGTO+"  WHERE ID="+modelo.getID());

                                            Toast.makeText(getApplicationContext(), "PAGAMENTO EFETUADO  " ,Toast.LENGTH_SHORT ).show();
                                            modelo.setValor_PGTO(String.valueOf(PGTO));
                                            adapter.notifyDataSetChanged();

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Toast.makeText(getApplicationContext(), "ERRO PAGAMENTO " ,Toast.LENGTH_SHORT ).show();

                                        }  //fim do try



                                    }

                                });

                                alertDialogPAGAMENTO.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });//fim do cancelar alert Dialog

                                alertDialogPAGAMENTO.create();
                                alertDialogPAGAMENTO.show();



                            }

                            @Override
                            public void onLongItemClick(View view, final int position) {
                                final Modelo modelo = listaitens.get(position);

                                //Criando o AlertDialog
                                final AlertDialog.Builder alertDialogEXCLUSAO= new AlertDialog.Builder(Preview.this);
                                //Titulo
                                alertDialogEXCLUSAO.setTitle("Exclusão");
                                //Perguntas
                                alertDialogEXCLUSAO.setMessage("EXCLUIR?");
                                //Faz não poder sair
                                alertDialogEXCLUSAO.setCancelable(false);
                                alertDialogEXCLUSAO.setPositiveButton("EXCLUIR", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        //EXCLUIR AQUI
                                        Apaga(modelo.getID());
                                        listaitens.remove(position);
                                        adapter.notifyDataSetChanged();


                                    }


                                });

                                alertDialogEXCLUSAO.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });//fim do cancelar alert Dialog

                                alertDialogEXCLUSAO.create();
                                alertDialogEXCLUSAO.show();

                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        }
                )
        );//Fim do evento de lick

        //-------------------------------------------------------------------------------------------------------------------------//



        //-----------------------------------------------------------------------------------------------//

        if (Tipo_Relatorio.equals("Aberto")){
            Titulo.setText("Valores a Receber");

            Titulo.setTextColor(RED);
            OBS.setText("Atenção! Este Relatório se baseia nas datas de vencimento.");
            OBS1.setText("Portanto uma listagem de valores em aberto neste periodo.");
            Abertas(RecebeInicio,RecebeFim);
            adapter.notifyDataSetChanged();
            t1.setText("Periodo da Listagem:");
            t2.setText("Inicio:"+converteDatas.ConverteData_Americano_Para_BR(RecebeInicio));
            t3.setText("Fim:"+converteDatas.ConverteData_Americano_Para_BR(RecebeFim));

            t4.setText("Total a Receber:");
             float TotalAberto= 0;
                                                 //Totalizador
                                                 try {
                                                 SQLiteDatabase bancoDados = openOrCreateDatabase("Financeiro", MODE_PRIVATE, null);
                                                 Cursor cursor = bancoDados.rawQuery("select SUM(valor) from contasreceber where valorpgto=0 and vcto  BETWEEN '"+RecebeInicio+"' and '"+RecebeFim+"'", null);
                                                 int indiceSUMVALOR = cursor.getColumnIndex("SUM(valor)");
                                                 cursor.moveToFirst();
                                                 //Listar
                                                 while (cursor != null) {
                                                 TotalAberto = cursor.getFloat(indiceSUMVALOR);
                                                 cursor.moveToNext();
                                                 }

                                                 } catch (Exception e) {
                                                 e.printStackTrace();

                                                 }  //fim do try

             t5.setText(""+TotalAberto);
             t5.setTextColor(RED);



        }//Fim da condição dos abertos

        if (Tipo_Relatorio.equals("Pago")){
            Titulo.setText("Valores Pagos");
            Titulo.setTextColor(BLUE);
            OBS.setText("Atenção! Este Relatório se baseia nas datas de Emissão.");
            OBS1.setText("Portanto, um comparativo do que era previsto receber e do realmente recebido neste periodo.");
            Pagas(RecebeInicio,RecebeFim);
            adapter.notifyDataSetChanged();
            t1.setText("Periodo da Listagem:");
            t2.setText("Inicio:"+converteDatas.ConverteData_Americano_Para_BR(RecebeInicio));
            t3.setText("Fim:"+converteDatas.ConverteData_Americano_Para_BR(RecebeFim));


            float TotalPrevisto= 0;
            float TotalPGTO=0;


                                                                     //Totalizadores
                                                                      try {
                                                                      SQLiteDatabase bancoDados = openOrCreateDatabase("Financeiro", MODE_PRIVATE, null);
                                                                      Cursor cursor = bancoDados.rawQuery("select SUM(valor) from contasreceber where valorpgto>0 and emissao  BETWEEN '"+RecebeInicio+"' and '"+RecebeFim+"'", null);
                                                                      int indiceSUMVALOR = cursor.getColumnIndex("SUM(valor)");
                                                                      cursor.moveToFirst();
                                                                      //Listar
                                                                      while (cursor != null) {
                                                                      TotalPrevisto= cursor.getFloat(indiceSUMVALOR);
                                                                      cursor.moveToNext();
                                                                      }

                                                                      } catch (Exception e) {
                                                                      e.printStackTrace();

                                                                      }  //fim do try
                                                            //--------------------------------------------------------------------------------//

                                                     try {
                                                     SQLiteDatabase bancoDados = openOrCreateDatabase("Financeiro", MODE_PRIVATE, null);
                                                     Cursor cursor = bancoDados.rawQuery("select SUM(valorpgto) from contasreceber where valorpgto>0 and emissao  BETWEEN '"+RecebeInicio+"' and '"+RecebeFim+"'", null);
                                                     int indiceSUMVALORPGTO = cursor.getColumnIndex("SUM(valorpgto)");
                                                     cursor.moveToFirst();
                                                     //Listar
                                                     while (cursor != null) {
                                                     TotalPGTO= cursor.getFloat(indiceSUMVALORPGTO);
                                                     cursor.moveToNext();
                                                      }

                                                      } catch (Exception e) {
                                                      e.printStackTrace();

                                                      }  //fim do try

        //Exibindo e calculando totais
        t4.setText("Abaixo, Totais:");
        t5.setText("Valor Esperado:"+TotalPrevisto);
        t6.setText("Valor Recebido:"+TotalPGTO);

                                       if(TotalPGTO<TotalPrevisto){
                                           t6.setTextColor(RED);
                                       }else{
                                           t6.setTextColor(BLUE);
                                       }

        }//Fim da condição do Pagos

        if (Tipo_Relatorio.equals("Tudo")){
            Titulo.setText("Todos os Valores, deste periodo.");
            Titulo.setTextColor(BLUE);
            OBS.setText("Atenção! Este Relatório se baseia nas datas de Emissão.");
            OBS1.setText("Portanto, um comparativo do que era previsto receber e do realmente recebido neste periodo.");
            Tudo(RecebeInicio,RecebeFim);
            adapter.notifyDataSetChanged();
            t1.setText("Periodo da Listagem:");

            t2.setText("Inicio:"+converteDatas.ConverteData_Americano_Para_BR(RecebeInicio));
            t3.setText("Fim:"+converteDatas.ConverteData_Americano_Para_BR((RecebeFim)));


            float TUDO_TotalPrevisto= 0;
            float TUDO_TotalPGTO=0;



                                             //Totalizadores
                                             try {
                                             SQLiteDatabase bancoDados = openOrCreateDatabase("Financeiro", MODE_PRIVATE, null);
                                             Cursor cursor = bancoDados.rawQuery("select SUM(valor) from contasreceber where   emissao  BETWEEN '"+RecebeInicio+"' and '"+RecebeFim+"'", null);
                                             int indiceSUMVALOR_TUDO = cursor.getColumnIndex("SUM(valor)");
                                             cursor.moveToFirst();
                                             //Listar
                                             while (cursor != null) {
                                             TUDO_TotalPrevisto= cursor.getFloat(indiceSUMVALOR_TUDO);
                                             cursor.moveToNext();
                                             }

                                            } catch (Exception e) {
                                            e.printStackTrace();

                                            }  //fim do try
                                          //--------------------------------------------------------------------------------//

                                           try {
                                           SQLiteDatabase bancoDados = openOrCreateDatabase("Financeiro", MODE_PRIVATE, null);
                                           Cursor cursor = bancoDados.rawQuery("select SUM(valorpgto) from contasreceber where   emissao  BETWEEN'"+RecebeInicio+"' and '"+RecebeFim+"'", null);
                                           int indiceSUMVALORPGTO_TUDO = cursor.getColumnIndex("SUM(valorpgto)");
                                           cursor.moveToFirst();
                                           //Listar
                                           while (cursor != null) {
                                           TUDO_TotalPGTO= cursor.getFloat(indiceSUMVALORPGTO_TUDO);
                                           cursor.moveToNext();
                                           }

                                           } catch (Exception e) {
                                           e.printStackTrace();

                                           }  //fim do try


            //Exibindo e calculando totais
           // t4.setText("Abaixo, Totais:");
            t4.setText("Valor Esperado:"+TUDO_TotalPrevisto);
            t5.setText("Valor Recebido:"+TUDO_TotalPGTO);

            //Totalizadores
            float TUDO_Diferenca=0;
                       if(TUDO_TotalPrevisto>TUDO_TotalPGTO){
                           TUDO_Diferenca = TUDO_TotalPrevisto-TUDO_TotalPGTO;
                           t6.setText("Saldo Negativo: "+TUDO_Diferenca);
                           t6.setTextColor(RED);
                       }
                       if(TUDO_TotalPGTO>TUDO_TotalPrevisto){
                           TUDO_Diferenca = TUDO_TotalPGTO-TUDO_TotalPrevisto;
                           t6.setText("Saldo Positivo"+TUDO_Diferenca);
                           t6.setTextColor(BLUE);
                       }
                       if(TUDO_TotalPGTO==TUDO_TotalPrevisto){

                           t6.setText("Não houve diferenças.");
                       }


        }//Fim da condição do TUDO






    }//fim do oncreate



    public void Tudo (String Inicio,String Fim) {


        final ConverteDatas converteDatas = new ConverteDatas();
        try {

            SQLiteDatabase bancoDados = openOrCreateDatabase("Financeiro", MODE_PRIVATE, null);
            //Recuperandoos contatos

           // Cursor cursor = bancoDados.rawQuery("select * from contasreceber where  emissao  BETWEEN '"+Inicio+"' and '"+Fim+"''", null);
            Cursor cursor = bancoDados.rawQuery("select * from contasreceber where   emissao  BETWEEN'"+Inicio+"' and '"+Fim+"'", null);

            //Recuperando os ids das colunas
            int indiceColunaID = cursor.getColumnIndex("ID");
            int indiceColunaFone = cursor.getColumnIndex("fone");
            int indiceColunaNome = cursor.getColumnIndex("nome");
            int indiceColunaEmissao = cursor.getColumnIndex("emissao");
            int indiceColunaVCTO = cursor.getColumnIndex("vcto");
            int indiceColunaValor = cursor.getColumnIndex("valor");
            int indiceColunaValorPGTO = cursor.getColumnIndex("valorpgto");


            cursor.moveToFirst();
            //Listar as itens
            while (cursor != null) {
                //Integer.parseInt(cursor.getString(indiceColunaID))

                String MontaEmissao = converteDatas.ConverteData_Americano_Para_BR(cursor.getString(indiceColunaEmissao));
                String MontaVCTO    = converteDatas.ConverteData_Americano_Para_BR(cursor.getString(indiceColunaVCTO));



                Modelo modelo = new Modelo(Integer.parseInt(cursor.getString(indiceColunaID)),
                        "Emissão:" +MontaEmissao,
                        "Nome:" + cursor.getString(indiceColunaNome),
                        "Fone:" + cursor.getString(indiceColunaFone),
                        "Valor:" + cursor.getString(indiceColunaValor),
                        "Vencimento:" + MontaVCTO,
                        "Pagamento:" + cursor.getString(indiceColunaValorPGTO));
                listaitens.add(modelo);
                cursor.moveToNext();

            }

        } catch (Exception e) {
            e.printStackTrace();

        }  //fim do try
    }//Fim do Tudo

    public void Abertas (String Inicio,String Fim) {
        final ConverteDatas converteDatas = new ConverteDatas();

        try {

            SQLiteDatabase bancoDados = openOrCreateDatabase("Financeiro", MODE_PRIVATE, null);
            //Recuperandoos contatos

           // Cursor cursor = bancoDados.rawQuery("select * from contasreceber where valorpgto=0 and vcto>='"+Inicio+"' and vcto<="+Fim+"''", null);

            Cursor cursor = bancoDados.rawQuery("select * from contasreceber where vcto BETWEEN '"+Inicio+"' and '"+Fim+"' and valorpgto=0 ", null);


            //Recuperando os ids das colunas
            int indiceColunaID = cursor.getColumnIndex("ID");
            int indiceColunaFone = cursor.getColumnIndex("fone");
            int indiceColunaNome = cursor.getColumnIndex("nome");
            int indiceColunaEmissao = cursor.getColumnIndex("emissao");
            int indiceColunaVCTO = cursor.getColumnIndex("vcto");
            int indiceColunaValor = cursor.getColumnIndex("valor");
            int indiceColunaValorPGTO = cursor.getColumnIndex("valorpgto");


            cursor.moveToFirst();
            //Listar as itens
            while (cursor != null) {
                //Integer.parseInt(cursor.getString(indiceColunaID))

                String MontaEmissao = converteDatas.ConverteData_Americano_Para_BR(cursor.getString(indiceColunaEmissao));
                String MontaVCTO    = converteDatas.ConverteData_Americano_Para_BR(cursor.getString(indiceColunaVCTO));


                Modelo modelo = new Modelo(Integer.parseInt(cursor.getString(indiceColunaID)),
                        "Emissão:" +MontaEmissao,
                        "Nome:" + cursor.getString(indiceColunaNome),
                        "Fone:" + cursor.getString(indiceColunaFone),
                        "Valor:" + cursor.getString(indiceColunaValor),
                        "Vencimento:" + MontaVCTO,
                        "Pagamento:" + cursor.getString(indiceColunaValorPGTO));
                listaitens.add(modelo);
                cursor.moveToNext();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }  //fim do try
    }//Fim do Abertas


    public void Pagas (String Inicio,String Fim) {
        final ConverteDatas converteDatas = new ConverteDatas();

        try {

            SQLiteDatabase bancoDados = openOrCreateDatabase("Financeiro", MODE_PRIVATE, null);
            //Recuperandoos contatos

            //Cursor cursor = bancoDados.rawQuery("select * from contasreceber where valorpgto>0 and emissao  BETWEEN '"+Inicio+"'and  '"+Fim+"''", null);
            Cursor cursor = bancoDados.rawQuery("select * from contasreceber where valorpgto>0 and emissao  BETWEEN '"+Inicio+"' and '"+Fim+"'", null);

            //Recuperando os ids das colunas
            int indiceColunaID = cursor.getColumnIndex("ID");
            int indiceColunaFone = cursor.getColumnIndex("fone");
            int indiceColunaNome = cursor.getColumnIndex("nome");
            int indiceColunaEmissao = cursor.getColumnIndex("emissao");
            int indiceColunaVCTO = cursor.getColumnIndex("vcto");
            int indiceColunaValor = cursor.getColumnIndex("valor");
            int indiceColunaValorPGTO = cursor.getColumnIndex("valorpgto");


            cursor.moveToFirst();
            //Listar as itens
            while (cursor != null) {
                //Integer.parseInt(cursor.getString(indiceColunaID))

                String MontaEmissao = converteDatas.ConverteData_Americano_Para_BR(cursor.getString(indiceColunaEmissao));
                String MontaVCTO    = converteDatas.ConverteData_Americano_Para_BR(cursor.getString(indiceColunaVCTO));

                Modelo modelo = new Modelo(Integer.parseInt(cursor.getString(indiceColunaID)),
                        "Emissão:" +MontaEmissao,
                        "Nome:" + cursor.getString(indiceColunaNome),
                        "Fone:" + cursor.getString(indiceColunaFone),
                        "Valor:" + cursor.getString(indiceColunaValor),
                        "Vencimento:" + MontaVCTO,
                        "Pagamento:" + cursor.getString(indiceColunaValorPGTO));
                listaitens.add(modelo);
                cursor.moveToNext();

                /*

                Modelo modelo = new Modelo(Integer.parseInt(cursor.getString(indiceColunaID)),
                         modelo = new Modelo(Integer.parseInt(cursor.getString(indiceColunaID)),
                        "Emissão:" +MontaEmissao,
                        "Nome:" + cursor.getString(indiceColunaNome),
                        "Fone:" + cursor.getString(indiceColunaFone),
                        "Valor:" + cursor.getString(indiceColunaValor),
                        "Vencimento:" + MontaVCTO,
                        "Pagamento:" + cursor.getString(indiceColunaValorPGTO));
                listaitens.add(modelo);
                cursor.moveToNext();

                */
            }

        } catch (Exception e) {
            e.printStackTrace();

        }  //fim do try
    }//Fim do Pagas



    public void Apaga(int ID_Excl) {

        try {
            //abre o banco
            SQLiteDatabase bancoDados = openOrCreateDatabase("Financeiro", MODE_PRIVATE, null);
            bancoDados.execSQL("DELETE FROM contasreceber  WHERE ID=" + ID_Excl);
            Toast.makeText(getApplicationContext(), "MOVIMENTAÇÃO EXCLUIDA ", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "ERRO DURANTE EXCLUSÃO ", Toast.LENGTH_SHORT).show();

        }  //fim do try

    }



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


}//fim do Java
