package com.valoresareceber.valoresareceber.Activity.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.se.omapi.Reader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.valoresareceber.valoresareceber.Activity.Activity.Adapter.Adapter;
import com.valoresareceber.valoresareceber.Activity.Activity.Adapter.Adapter_Principal;
import com.valoresareceber.valoresareceber.Activity.Activity.Model.Modelo;
import com.valoresareceber.valoresareceber.R;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import static android.icu.text.Normalizer.NO;
/*

Software Criado por Jeferson dos Santos
CPF:006-420-570-36
Contato: jefersonfire@gmail.com

 */


public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Button add_ID;
    private Button pesquisa_ID;
    private Button listar_ID;
    private Button Recebo_Hoje;
    private Button Ja_recebidas;
    private TextView Total_A_Receber;
    private TextView Total_Pago;
    private TextView Texto;
    private RecyclerView recyclerView_ID;
    private List<Modelo> listaitens = new ArrayList<>();
  //  private AdView AdBanner;
 // private AdView AdBanner;
    private AdView mAdView;

   //    MobileAds.initialize(this, "ca-app-pub-6828909553286825~5817339983");
   // AdRequest adRequest = new AdRequest.Builder().build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add_ID =          (Button) findViewById(R.id.add_ID);


        pesquisa_ID =     (Button) findViewById(R.id.pesquisa_ID);
        listar_ID =       (Button) findViewById(R.id.listar_ID);
        Recebo_Hoje =     (Button) findViewById(R.id.Recebo_Hoje);
        Ja_recebidas =    (Button) findViewById(R.id.Ja_recebidas);
        textView =        (TextView) findViewById(R.id.textView);
        Texto    =        (TextView) findViewById(R.id.Texto);
        Total_A_Receber = (TextView) findViewById(R.id.Total_A_Receber);
        Total_Pago =      (TextView) findViewById(R.id.Total_Pago);
        recyclerView_ID = (RecyclerView) findViewById(R.id.recyclerView_ID);
       // AdBanner =         findViewById(R.id.AdBanner);
      //  AdBanner =          (AdView) findViewById(R.id.AdBanner);

        //mAdView = findViewById(R.id.adView);
        //AdRequest adRequest = new AdRequest.Builder().build();
       // mAdView.loadAd(adRequest);
        MobileAds.initialize(this, "ca-app-pub-6828909553286825~5817339983");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        /*
        * https://www.youtube.com/watch?v=ROKCWZj5OlA&feature=youtu.be
        * https://developers.google.com/admob/android/quick-start?hl=pt-BR#import_the_mobile_ads_sdk
        * */
  //-----------------------------------------------------------------------------------------------------------------------------------------------------//
        //AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
     // AdView adView = new AdView(this);
      //  adView.setAdSize(AdSize.BANNER);



     //  AdRequest adRequest = new AdRequest.Builder().build();
      // AdBanner.loadAd(adRequest);



  //-----------------------------------------------------------------------------------------------------------------------------------------------------//
  final ConverteDatas converteDatas = new ConverteDatas();

  //-----------------------------------------------------------------------------------------------------------------------------------------------------//
        //Criando Banco de dados
        //Construtor do banco de dados
        // textView.setText("Agenda Financeira");

        try {
            //Inicindo banco de dados
            SQLiteDatabase bancoDados = openOrCreateDatabase("Financeiro", MODE_PRIVATE, null);


            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS contasreceber (ID INTEGER PRIMARY KEY AUTOINCREMENT,fone VARCHAR, nome VARCHAR,emissao DATE,vcto DATE, valor FLOAT, valorpgto FLOAT);");

        } catch (Exception e) {
            e.printStackTrace();

        }//fim do try

        //-------------------------------------------------------------------------------------------------------------------------//


        //ROTINA JANA PEGA DT ATUAL E BOTA MENOS TANTOS DIAS

        Integer dias_a_menos = 8; // menos 8 para que ele faça na pratica menos 7 ou seja 7 +1
        Date data_Atual = new Date();
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(data_Atual);
        gc.set(Calendar.DATE, gc.get(Calendar.DATE) - dias_a_menos);
        data_Atual = gc.getTime();
        //---------------------------------------------------//

        Integer dias_a_mais = 1; // menos 8 para que ele faça na pratica menos 7 ou seja 7 +1
        Date data_Mais = new Date();
        GregorianCalendar g_c = new GregorianCalendar();
        g_c.setTime(data_Mais);
        g_c.set(Calendar.DATE, g_c.get(Calendar.DATE) + dias_a_mais);
        data_Mais = g_c.getTime();

        //-----------------------------------------------------//
        //Data Hoje
        Integer dia_a_menos = 1;
        Date data_Hoje = new Date();
        GregorianCalendar g_c_Hoje = new GregorianCalendar();
        g_c_Hoje.setTime(data_Hoje);
        g_c_Hoje.set(Calendar.DATE, g_c_Hoje.get(Calendar.DATE));
        data_Hoje = g_c_Hoje.getTime();




        // Formatando DATA
        //Emissao
        SimpleDateFormat sdff = new SimpleDateFormat("dd/MM/yyyy");
        // "dd/MM/yyyy");
        final String[] dia = {(sdff.format(new Date()))};

        //-------------------------------------------------------------------------//

        SimpleDateFormat dt_Menos_dias = new SimpleDateFormat("dd/MM/yyyy");
        final String DT_MENOS_DIAS = (dt_Menos_dias.format(data_Atual));

        SimpleDateFormat dt_mais_dias = new SimpleDateFormat("dd/MM/yyyy");
        final String DT_Mais_DIAS = (dt_mais_dias.format(data_Mais));
     // textView.setText(DT_Mais_DIAS+"  |  "+DT_MENOS_DIAS);

        SimpleDateFormat dt_Hoje = new SimpleDateFormat("dd/MM/yyyy");
        final String DT_HOJE = (dt_Hoje.format(data_Hoje));

        String TesteData_com_um_a_mais    = converteDatas.ConverteData_BR_Para_Ingles(DT_Mais_DIAS);

        String TesteData_com_Dias_a_menos = converteDatas.ConverteData_BR_Para_Ingles(DT_MENOS_DIAS);

     //   textView.setText(TesteData_com_Dias_a_menos+"   -   "+TesteData_com_um_a_mais);
        //-------------------------------------------------------------------------------------------------------------------------//
        //Calcula os totais
        Totalizadores(DT_MENOS_DIAS);

        //-------------------------------------------------------------------------------------------------------------------------//
             //Configurando o Adaptador

//textView.setText(""+DT_Mais_DIAS);
//textView.setText(""+DT_MENOS_DIAS);


        //Configurando o RecyclerViwe
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView_ID.setLayoutManager(layoutManager);

        recyclerView_ID.setHasFixedSize(true);
        //Instanciando adaptador
        final Adapter adapter = new Adapter(listaitens);

       // final Adapter_Principal adapter = new Adapter_Principal(listaitens);



        recyclerView_ID.setAdapter(adapter);
       // listaitens.clear();
        AlimentaLista_Devendo(DT_MENOS_DIAS);
        adapter.notifyDataSetChanged();


        listar_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listaitens.clear();
                AlimentaLista(DT_MENOS_DIAS);
                adapter.notifyDataSetChanged();

            }//fim da ação
        });//fim do botãonovo

        Recebo_Hoje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listaitens.clear();
                AlimentaLista_Vence_Hoje(DT_HOJE);
                adapter.notifyDataSetChanged();

            }//fim da ação
        });//fim do botãonovo
        Ja_recebidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listaitens.clear();
                AlimentaLista_Pagos(DT_MENOS_DIAS);
                adapter.notifyDataSetChanged();

            }//fim da ação
        });//fim do botãonovo

        pesquisa_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //     startActivity( new Intent(Nova_Movimentacao.this,NovaMovPicker_VCTO.class));
                //   startActivity(new Intent(Nova_Movimentacao.this, Seleciona_Cliente.class));
                startActivity(new Intent(MainActivity.this, Pesquisa_SQLITE.class));

            }//fim da ação
        });//fim do botãonovo




        //---------------------------------------------------------------------------------------------------------------------------//
        //   EVENTO ON CLICK//


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
                                final AlertDialog.Builder alertDialogPAGAMENTO = new AlertDialog.Builder(MainActivity.this);
                                //Titulo
                                alertDialogPAGAMENTO.setTitle("Pagamento");
                                //Perguntas
                                alertDialogPAGAMENTO.setMessage("EFETUAR PAGAMENTO?");
                                //Faz não poder sair
                                alertDialogPAGAMENTO.setCancelable(false);
                                final EditText recebeValorpgto = new EditText(MainActivity.this);
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
                                        if(recebeValorpgto.length()==0){
                                            PGTO= Float.valueOf(0);
                                            try {
                                                //abre o banco
                                                SQLiteDatabase bancoDados = openOrCreateDatabase("Financeiro", MODE_PRIVATE, null);
                                                // bancoDados.execSQL("DELETE FROM contasreceber  WHERE ID="+ID);

                                                bancoDados.execSQL("UPDATE contasreceber  SET valorpgto=" + PGTO + "  WHERE ID=" + modelo.getID());

                                                Toast.makeText(getApplicationContext(), "PAGAMENTO EFETUADO  ", Toast.LENGTH_SHORT).show();
                                                modelo.setValor_PGTO("Pagamento:" + String.valueOf(PGTO));
                                                adapter.notifyDataSetChanged();
                                                //Calcula os totais
                                                Totalizadores(DT_MENOS_DIAS);


                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Toast.makeText(getApplicationContext(), "ERRO PAGAMENTO ", Toast.LENGTH_SHORT).show();

                                            }  //fim do try

                                        }
                                                           else {
                                            PGTO = Float.valueOf(recebeValorPGTOString);
                                            //Esta fechando sozinho se passo nada no valor a pagar

                                            try {
                                                //abre o banco
                                                SQLiteDatabase bancoDados = openOrCreateDatabase("Financeiro", MODE_PRIVATE, null);
                                                // bancoDados.execSQL("DELETE FROM contasreceber  WHERE ID="+ID);

                                                bancoDados.execSQL("UPDATE contasreceber  SET valorpgto=" + PGTO + "  WHERE ID=" + modelo.getID());

                                                Toast.makeText(getApplicationContext(), "PAGAMENTO EFETUADO  ", Toast.LENGTH_SHORT).show();
                                                modelo.setValor_PGTO("Pagamento:" + String.valueOf(PGTO));
                                                adapter.notifyDataSetChanged();
                                                //Calcula os totais
                                                Totalizadores(DT_MENOS_DIAS);


                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Toast.makeText(getApplicationContext(), "ERRO PAGAMENTO ", Toast.LENGTH_SHORT).show();

                                            }  //fim do try

                                                           }//Fim do Else

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
                                final AlertDialog.Builder alertDialogEXCLUSAO = new AlertDialog.Builder(MainActivity.this);
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
                                        //Calcula os totais
                                        Totalizadores(DT_MENOS_DIAS);


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


        //------------------------------------------------------------------------------------------------------------------------------//

/*
        // Formatando DATA
        //Emissao
        SimpleDateFormat sdff = new SimpleDateFormat("dd-MM-yyyy");
        // "dd/MM/yyyy");
        final String[] dia = {(sdff.format(new Date()))};
*/
        //-------------------------------------------------------------------------------------------------------------------------//


        add_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Criando o AlertDialog
                final AlertDialog.Builder alertDialogEmissao = new AlertDialog.Builder(MainActivity.this);
                //Titulo
                alertDialogEmissao.setTitle("Novo Financeiro");
                //Perguntas
                alertDialogEmissao.setMessage("Emissão:");

                final EditText recebeEmissao = new EditText(MainActivity.this);
                recebeEmissao.setInputType(InputType.TYPE_CLASS_NUMBER);//diz que o edittext é apenas numerico
                alertDialogEmissao.setView(recebeEmissao);


                //-----------------------------
                dia[0] = dia[0].replaceAll("-", "/");

                recebeEmissao.setText(dia[0]);

                //Faz não poder sair
                alertDialogEmissao.setCancelable(false);

                //Botões
                alertDialogEmissao.setPositiveButton("Proximo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        String emissao = "";

                        emissao =  converteDatas.ConverteData_BR_Para_Ingles(recebeEmissao.getText().toString());

                        //Criando o AlertDialog
                        final AlertDialog.Builder alertDialogNome = new AlertDialog.Builder(MainActivity.this);
                        //Titulo
                        alertDialogNome.setTitle("Novo Financeiro");

                        final EditText recebeNome = new EditText(MainActivity.this);
                        alertDialogNome.setView(recebeNome);
                        //Perguntas
                        alertDialogNome.setMessage("Nome:");
                        alertDialogNome.setCancelable(false);


                        final String finalEmissao = emissao;
                        alertDialogNome.setPositiveButton("Proximo", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String nome = "";
                                nome = recebeNome.getText().toString();


                                //Criando o AlertDialog
                                final AlertDialog.Builder alertDialogFone = new AlertDialog.Builder(MainActivity.this);
                                //Titulo
                                alertDialogFone.setTitle("Novo Financeiro");
                                //Perguntas
                                alertDialogFone.setMessage("Fone:");
                                final EditText recebeFone = new EditText(MainActivity.this);
                                recebeFone.setInputType(InputType.TYPE_CLASS_NUMBER);//diz que o edittext é apenas numerico
                                alertDialogFone.setView(recebeFone);

                                alertDialogFone.setCancelable(false);
                                final String finalNome = nome;
                                alertDialogFone.setPositiveButton("Proximo", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String fone = "";
                                        fone = recebeFone.getText().toString();


                                        //Criando o AlertDialog
                                        final AlertDialog.Builder alertDialogValor = new AlertDialog.Builder(MainActivity.this);
                                        //Titulo
                                        alertDialogValor.setTitle("Novo Financeiro");
                                        //Perguntas
                                        alertDialogValor.setMessage("Valor:");
                                        final EditText recebeValor = new EditText(MainActivity.this);
                                        alertDialogValor.setView(recebeValor);


                                        alertDialogValor.setCancelable(false);
                                        final String finalFone = fone;
                                        alertDialogValor.setPositiveButton("Proximo", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                String valor = "";
                                                valor = recebeValor.getText().toString();

                                                //substitui as virgulas por ponto

                                                valor = valor.replaceAll(",", ".");


                                                //Criando o AlertDialog
                                                final AlertDialog.Builder alertDialogDT_VCTO = new AlertDialog.Builder(MainActivity.this);

                                                //Titulo
                                                alertDialogDT_VCTO.setTitle("Novo Financeiro");
                                                //Perguntas
                                                alertDialogDT_VCTO.setMessage("Data VCTO:");
                                                final EditText recebeDT_VCTO = new EditText(MainActivity.this);
                                                recebeDT_VCTO.setInputType(InputType.TYPE_CLASS_NUMBER);//diz que o edittext é apenas numerico
                                                recebeDT_VCTO.setText(dia[0]);
                                                alertDialogDT_VCTO.setView(recebeDT_VCTO);


                                                alertDialogDT_VCTO.setCancelable(false);
                                                final String finalValor = valor;
                                                alertDialogDT_VCTO.setPositiveButton("GRAVA", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {


                                                        String vcto = "";
                                                        vcto =  converteDatas.ConverteData_BR_Para_Ingles(recebeDT_VCTO.getText().toString());



                                                        //-------------------------------------//


                                                        Insert(finalFone, finalEmissao, finalNome, vcto, finalValor);
                                                        //Tornando o lançamento visivel a lista, atualizando ela da inserção
                                                        String ExibeEmissao;
                                                        String ExibeVCTO;
                                                              ExibeEmissao  = converteDatas.ConverteData_Americano_Para_BR(finalEmissao);
                                                              ExibeVCTO     = converteDatas.ConverteData_Americano_Para_BR(vcto);

                                                        Modelo modelo = new Modelo(0,
                                                                "Emissão:" + ExibeEmissao,
                                                                "Nome:" + finalNome,
                                                                "Fone:" + finalFone,
                                                                "Valor:" + finalValor,
                                                                "Vencimento:" + ExibeVCTO,
                                                                "Pagamento:" + 0);
                                                        //Calcula os totais
                                                        Totalizadores(DT_MENOS_DIAS);
                                                        listaitens.add(modelo);

                                                        listaitens.clear();;
                                                        AlimentaLista_Devendo(DT_MENOS_DIAS);
                                                        adapter.notifyDataSetChanged();



                                                    }
                                                });//fim do botão gravar do Dialog

                                                alertDialogDT_VCTO.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                    }
                                                });//fim do cancelar alert Dialog

                                                alertDialogDT_VCTO.create();
                                                alertDialogDT_VCTO.show();


                                            }
                                        });//fim do botão gravar do Dialog

                                        alertDialogValor.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });//fim do cancelar alert Dialog

                                        alertDialogValor.create();
                                        alertDialogValor.show();

                                    }
                                });//fim do botão gravar do Dialog

                                alertDialogFone.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });//fim do cancelar alert Dialog

                                alertDialogFone.create();
                                alertDialogFone.show();
                            }
                        });//fim do botão gravar do Dialog

                        alertDialogNome.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });//fim do cancelar alert Dialog

                        alertDialogNome.create();
                        alertDialogNome.show();


                    }
                });//fim do botão gravar do Dialog

                alertDialogEmissao.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });//fim do cancelar alert Dialog

                //deixa visivel
                alertDialogEmissao.create();
                alertDialogEmissao.show();


            }//fim da ação
        });//fim do botão

        //-------------------------------------------------------------------------------------------------------------------------//



    }//Fim do Oncreate

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
    public void AlimentaLista(String DT_Final) {
        Texto.setText("Todos os valores a partir de :" + DT_Final);
        final ConverteDatas converteDatas = new ConverteDatas();
        DT_Final = ConverteDatas.ConverteData_BR_Para_Ingles(DT_Final);

        try {
            SQLiteDatabase bancoDados = openOrCreateDatabase("Financeiro", MODE_PRIVATE, null);
            //Recuperandoos contatos

          //  Cursor cursor = bancoDados.rawQuery("SELECT * FROM contasreceber Where emissao>='" + DT_Final + "'  order by ID  DESC ", null);

            Cursor cursor = bancoDados.rawQuery("SELECT * FROM contasreceber where emissao>='"+DT_Final+"' order by ID  DESC ", null);



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
                        "Emissão:" + MontaEmissao,
                        "Nome:" + cursor.getString(indiceColunaNome),
                        "Fone:" + cursor.getString(indiceColunaFone),
                        "Valor:" + cursor.getString(indiceColunaValor),
                        "Vencimento:" +MontaVCTO,
                        "Pagamento:" + cursor.getString(indiceColunaValorPGTO));
                listaitens.add(modelo);
                cursor.moveToNext();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }  //fim do try
    }//Fim do Alimenta lista

    public void AlimentaLista_Devendo(String DT_Final) {


        final ConverteDatas converteDatas = new ConverteDatas();
        DT_Final = ConverteDatas.ConverteData_BR_Para_Ingles(DT_Final);
        try {

            SQLiteDatabase bancoDados = openOrCreateDatabase("Financeiro", MODE_PRIVATE, null);
            //Recuperandoos contatos

          Cursor cursor = bancoDados.rawQuery("SELECT * FROM contasreceber Where emissao>='"+DT_Final+"' and valorpgto=0  order by ID  DESC", null);




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
    }//Fim do Alimenta lista


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

    public void Insert(String Fone, String Emissao, String Nome, String VCTO, String Valor) {


        try {

            //abre o banco
            SQLiteDatabase bancoDados = openOrCreateDatabase("Financeiro", MODE_PRIVATE, null);
            bancoDados.execSQL("INSERT INTO contasreceber (fone,nome,emissao,vcto,valor,valorpgto) " +
                    "VALUES ('" + Fone + "','" + Nome + "','" + Emissao + "','" + VCTO + "','" + Valor + "','" + 0 + "')");

            Toast.makeText(MainActivity.this, "Cadastrado com sucesso", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "!!ERRO NO CADASTRO!!!", Toast.LENGTH_SHORT).show();
        }  //fim do try

    }

    public void update(int ID, Float Valor_PGTO) {

        try {
            //abre o banco
            SQLiteDatabase bancoDados = openOrCreateDatabase("Financeiro", MODE_PRIVATE, null);
            // bancoDados.execSQL("DELETE FROM contasreceber  WHERE ID="+ID);

            bancoDados.execSQL("UPDATE contasreceber  SET valorpgto=" + Valor_PGTO + "  WHERE ID=" + ID);

            Toast.makeText(getApplicationContext(), "PAGAMENTO EFETUADO  ", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "ERRO PAGAMENTO ", Toast.LENGTH_SHORT).show();

        }  //fim do try

    }

    /************************************************/
    public void Totalizadores (String  DT_MENOS_DIAS) {
        //-------------------------------------------------------------------------------------------------------------------------//
        //Totalizadores
        Float Total_Nao_Pagos=null;

        final ConverteDatas converteDatas = new ConverteDatas();
        DT_MENOS_DIAS = ConverteDatas.ConverteData_BR_Para_Ingles(DT_MENOS_DIAS);

        try

        {
            SQLiteDatabase bancoDados = openOrCreateDatabase("Financeiro", MODE_PRIVATE, null);

            //Contas a pagar é 2
            //Cursor cursor = bancoDados.rawQuery("select SUM(valorpgto) from contasreceber where tipo_lanca="+2, null);


            //"+TesteRecebeData+"
           Cursor cursor = bancoDados.rawQuery("select SUM(valor) from contasreceber Where emissao>='" + DT_MENOS_DIAS + "' and valorpgto=0", null);
          //  Cursor cursor = bancoDados.rawQuery("select SUM(valor) from contasreceber Where valorpgto=0", null);

            int indiceColunaSUM2 = cursor.getColumnIndex("SUM(valor)");
            cursor.moveToFirst();
            //Listar as itens
            while (cursor != null) {
                Total_Nao_Pagos = cursor.getFloat(indiceColunaSUM2);
                cursor.moveToNext();
            }
        } catch(
                Exception e)

        {
            e.printStackTrace();
        }  //fim do try

        //-------------------------------------------------------------------------------------------------------------------------//
        //-------------------------------------------------------------------------------------------------------------------------//
        //Totalizadores
        Float Total_Pagos = null;

        try

        {
            SQLiteDatabase bancoDados = openOrCreateDatabase("Financeiro", MODE_PRIVATE, null);

            //Contas a pagar é 2
            //Cursor cursor = bancoDados.rawQuery("select SUM(valorpgto) from contasreceber where tipo_lanca="+2, null);


            //"+TesteRecebeData+"
            Cursor cursor = bancoDados.rawQuery("select SUM(valorpgto) from contasreceber Where emissao>='" + DT_MENOS_DIAS + "' and valorpgto>0", null);
          //  Cursor cursor = bancoDados.rawQuery("select SUM(valorpgto) from contasreceber Where  valorpgto>0", null);

            int indiceColunaSUM2 = cursor.getColumnIndex("SUM(valorpgto)");
            cursor.moveToFirst();
            //Listar as itens
            while (cursor != null) {

                Total_Pagos = cursor.getFloat(indiceColunaSUM2);
                cursor.moveToNext();
            }
        } catch(
                Exception e)

        {
            e.printStackTrace();
        }  //fim do try

        //-------------------------------------------------------------------------------------------------------------------------//
        Total_Pago.setText("  Valores Recebidos:"+Total_Pagos);
        Total_A_Receber.setText("Valores a Receber:"+Total_Nao_Pagos);
        //------------------------------------------------------------------------------------------------------------------------//

       }//fim da classe
    /*************************************************/



    public void AlimentaLista_Vence_Hoje(String DT_HJ) {
        Texto.setText("Receber Hoje :" + DT_HJ);
        final ConverteDatas converteDatas = new ConverteDatas();
        DT_HJ = ConverteDatas.ConverteData_BR_Para_Ingles(DT_HJ);
        // Total_Pago.setText(""+DT_HJ);
        try {
            SQLiteDatabase bancoDados = openOrCreateDatabase("Financeiro", MODE_PRIVATE, null);
            //Recuperandoos contatos

            //  Cursor cursor = bancoDados.rawQuery("SELECT * FROM contasreceber Where emissao>='" + DT_Final + "'  order by ID  DESC ", null);

            Cursor cursor = bancoDados.rawQuery("SELECT * FROM contasreceber where  vcto  BETWEEN'"+DT_HJ+"' and '"+DT_HJ+"' and valorpgto=0 order by ID  DESC ", null);



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
                        "Emissão:" + MontaEmissao,
                        "Nome:" + cursor.getString(indiceColunaNome),
                        "Fone:" + cursor.getString(indiceColunaFone),
                        "Valor:" + cursor.getString(indiceColunaValor),
                        "Vencimento:" +MontaVCTO,
                        "Pagamento:" + cursor.getString(indiceColunaValorPGTO));
                listaitens.add(modelo);
                cursor.moveToNext();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }  //fim do try
    }//Fim do Alimenta lista



    public void AlimentaLista_Pagos(String DT_Final) {
        Texto.setText("Valores Recebidos a partir de :" + DT_Final);

        final ConverteDatas converteDatas = new ConverteDatas();
        DT_Final = ConverteDatas.ConverteData_BR_Para_Ingles(DT_Final);

        try {

            SQLiteDatabase bancoDados = openOrCreateDatabase("Financeiro", MODE_PRIVATE, null);
            //Recuperandoos contatos

            Cursor cursor = bancoDados.rawQuery("SELECT * FROM contasreceber Where emissao>='"+DT_Final+"' and valorpgto>0  order by ID  DESC", null);




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
    }//Fim do Alimenta lista



}//Fim da classe Java
