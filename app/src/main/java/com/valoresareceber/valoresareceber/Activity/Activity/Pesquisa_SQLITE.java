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
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.valoresareceber.valoresareceber.Activity.Activity.Adapter.Adapter;
import com.valoresareceber.valoresareceber.Activity.Activity.Model.Modelo;
import com.valoresareceber.valoresareceber.Activity.Activity.Pickers.Picker01;
import com.valoresareceber.valoresareceber.R;

import java.util.ArrayList;
import java.util.List;

import static com.valoresareceber.valoresareceber.Activity.Activity.Pickers.Picker01.ARQUIVO_REFERENCIA;

public class Pesquisa_SQLITE extends AppCompatActivity {

    private RecyclerView recyclerView_P_ID;
    private SearchView   search_ID;
    private Button       Voltar;
    private Button       N_Pagos;
    private Button       Pagos;
    private Button       Tudo_ID;
    private Button       Rel_ID;

    private AdView mAdView;

// private SearchView searchViewPesquisa;

    private List<Modelo> listaitens = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pesquisa_sqlite);


        recyclerView_P_ID = (RecyclerView) findViewById(R.id.recyclerView_P_ID);
        search_ID         = (SearchView) findViewById(R.id.search_ID);
        Voltar            = (Button) findViewById(R.id.Voltar);
        N_Pagos           = (Button) findViewById(R.id.N_Pagos);
        Pagos             = (Button) findViewById(R.id.Pagos);
        Tudo_ID           = (Button) findViewById(R.id.Tudo_ID);
        Rel_ID            = (Button) findViewById(R.id.Rel_ID);

        final SharedPreferences sharedPreferences = getSharedPreferences(ARQUIVO_REFERENCIA, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        final ConverteDatas converteDatas = new ConverteDatas();

        //Configurando o Adaptador

        //Configurando o RecyclerViwe
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView_P_ID.setLayoutManager(layoutManager);

        recyclerView_P_ID.setHasFixedSize(true);
        //Instanciando adaptador
        final Adapter adapter = new Adapter(listaitens);
        recyclerView_P_ID.setAdapter(adapter);

        listaitens.clear();

        //------------------------------------------------------------------------------------//
        MobileAds.initialize(this, "ca-app-pub-6828909553286825~5817339983");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //------------------------------------------------------------------------------------//
        search_ID.setQueryHint("Buscar por nome...");
        final String[] textoDigitado = {""};
        search_ID.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                //  String textoDigitado = query.toUpperCase();
                //  AlimentaPesquisa(textoDigitado);
                //  adapter.notifyDataSetChanged();

                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {

                listaitens.clear();
                // String textoDigitado = newText.toUpperCase();
                textoDigitado[0] = newText.toUpperCase();
                AlimentaPesquisa(textoDigitado[0]);
                adapter.notifyDataSetChanged();

                return true;
            }
        });

        //---------------------------------------------------------------------------------------------------------------------------//
        Rel_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("Inicio","");
                editor.putString("Fim","");
                editor.commit();
                startActivity( new Intent(Pesquisa_SQLITE.this,Relatorios.class));
            }//fim da ação
        });//fim do botãonovo
        //---------------------------------------------------------------------------------------------------------------------------//
        Tudo_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaitens.clear();
                AlimentaLista();
                adapter.notifyDataSetChanged();

            }//fim da ação
        });//fim do botãonovo

        N_Pagos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaitens.clear();
                adapter.notifyDataSetChanged();
                if (textoDigitado.length>0){
                    String Pesquisa = textoDigitado[0];
                    AlimentaPesquisa_Com_Nome_NaoPagos(Pesquisa);
                    adapter.notifyDataSetChanged();

                }else{
                    AlimentaPesquisa_SemNome_PGTO_Zero();
                    adapter.notifyDataSetChanged();
                }


            }//fim da ação
        });//fim do botãonovo


        Pagos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaitens.clear();
                adapter.notifyDataSetChanged();
                if (textoDigitado.length>0){
                    String Pesquisa = textoDigitado[0];
                    AlimentaPesquisa_Com_NomePagos(Pesquisa);
                    adapter.notifyDataSetChanged();

                }else{
                    AlimentaPesquisa_SemNome_Pagos();
                    adapter.notifyDataSetChanged();
                }


            }//fim da ação
        });//fim do botãonovo

        //---------------------------------------------------------------------------------------------------------------------------//

        Voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaitens.clear();
                AlimentaLista();
                adapter.notifyDataSetChanged();
                startActivity( new Intent(Pesquisa_SQLITE.this,MainActivity.class));
            }//fim da ação
        });//fim do botãonovo



        //---------------------------------------------------------------------------------------------------------------------------//

        //evento de click
        recyclerView_P_ID.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerView_P_ID,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {


                                final Modelo modelo = listaitens.get(position);

                                // Toast.makeText(getApplicationContext(), "Item pressionado: " + modelo.getID(),Toast.LENGTH_SHORT ).show();

                                //Criando o AlertDialog
                                final AlertDialog.Builder alertDialogPAGAMENTO= new AlertDialog.Builder(Pesquisa_SQLITE.this);
                                //Titulo
                                alertDialogPAGAMENTO.setTitle("Pagamento");
                                //Perguntas
                                alertDialogPAGAMENTO.setMessage("EFETUAR PAGAMENTO?");
                                //Faz não poder sair
                                alertDialogPAGAMENTO.setCancelable(false);
                                final EditText recebeValorpgto = new EditText(Pesquisa_SQLITE.this);
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
                                final AlertDialog.Builder alertDialogEXCLUSAO= new AlertDialog.Builder(Pesquisa_SQLITE.this);
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
        //Criando Banco de dados
        //Construtor do banco de dados


        try {
            //Inicindo banco de dados
            SQLiteDatabase bancoDados = openOrCreateDatabase("Financeiro", MODE_PRIVATE, null);


            bancoDados.execSQL("CREATE TABLE IF NOT EXISTS contasreceber (ID INTEGER PRIMARY KEY AUTOINCREMENT,fone VARCHAR, nome VARCHAR,emissao DATE,vcto DATE, valor FLOAT, valorpgto FLOAT);");

        } catch (Exception e) {
            e.printStackTrace();

        }//fim do try



    }//fim do oncreate



    public void AlimentaPesquisa(String Pesquisa) {

        final ConverteDatas converteDatas = new ConverteDatas();
        Pesquisa="%"+Pesquisa+"%";

        try {

            SQLiteDatabase bancoDados = openOrCreateDatabase("Financeiro", MODE_PRIVATE, null);
            //Recuperandoos contatos


            Cursor cursor = bancoDados.rawQuery("select * from contasreceber where nome like "+"'"+Pesquisa+"' order by ID  DESC ", null);

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
    }//fim do alimenta pesquisa
//----------------------------------------------------------------------------------------------------------------------------
    //Pequisa nao pagos
    public void AlimentaPesquisa_Com_Nome_NaoPagos(String Pesquisa) {
        final ConverteDatas converteDatas = new ConverteDatas();
        Pesquisa="%"+Pesquisa+"%";

        try {

            SQLiteDatabase bancoDados = openOrCreateDatabase("Financeiro", MODE_PRIVATE, null);
            //Recuperandoos contatos


            Cursor cursor = bancoDados.rawQuery("select * from contasreceber where nome like "+"'"+Pesquisa+"' and valorpgto=0 order by ID  DESC ", null);

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
    }//fim do alimenta pesquisa

    public void AlimentaPesquisa_SemNome_PGTO_Zero() {

        final ConverteDatas converteDatas = new ConverteDatas();
        try {

            SQLiteDatabase bancoDados = openOrCreateDatabase("Financeiro", MODE_PRIVATE, null);
            //Recuperandoos contatos

            Cursor cursor = bancoDados.rawQuery("select * from contasreceber where valorpgto=0", null);
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


//-----------------------------------------------------------------------------------------------------------------
    //PesquisaPAgos
public void AlimentaPesquisa_Com_NomePagos(String Pesquisa) {
    final ConverteDatas converteDatas = new ConverteDatas();
    Pesquisa="%"+Pesquisa+"%";

    try {

        SQLiteDatabase bancoDados = openOrCreateDatabase("Financeiro", MODE_PRIVATE, null);
        //Recuperandoos contatos

        Cursor cursor = bancoDados.rawQuery("select * from contasreceber where nome like "+"'"+Pesquisa+"' and valorpgto>0 order by ID  DESC ", null);

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
}//fim do alimenta pesquisa

    public void AlimentaPesquisa_SemNome_Pagos() {

        final ConverteDatas converteDatas = new ConverteDatas();
        try {

            SQLiteDatabase bancoDados = openOrCreateDatabase("Financeiro", MODE_PRIVATE, null);
            //Recuperandoos contatos

            Cursor cursor = bancoDados.rawQuery("select * from contasreceber where valorpgto>0", null);
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

//-----------------------------------------------------------------------------------------------------------------

    public void AlimentaLista() {
        final ConverteDatas converteDatas = new ConverteDatas();

        try {

            SQLiteDatabase bancoDados = openOrCreateDatabase("Financeiro", MODE_PRIVATE, null);
            //Recuperandoos contatos

            Cursor cursor = bancoDados.rawQuery("SELECT * FROM contasreceber ", null);
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

    public void Apaga(int ID_Excl){

        try{
            //abre o banco
            SQLiteDatabase bancoDados = openOrCreateDatabase("Financeiro", MODE_PRIVATE, null);
            bancoDados.execSQL("DELETE FROM contasreceber  WHERE ID="+ID_Excl);
            Toast.makeText(getApplicationContext(), "MOVIMENTAÇÃO EXCLUIDA " ,Toast.LENGTH_SHORT ).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "ERRO DURANTE EXCLUSÃO " ,Toast.LENGTH_SHORT ).show();

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




}//fim da classe java
