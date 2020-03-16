package com.valoresareceber.valoresareceber.Activity.Activity.Adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.valoresareceber.valoresareceber.Activity.Activity.ConverteDatas;
import com.valoresareceber.valoresareceber.Activity.Activity.Model.Modelo;
import com.valoresareceber.valoresareceber.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder>{








    private List<Modelo> listaitens;


    public Adapter(List<Modelo> lista) {
        this.listaitens = lista;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Linka o layout que for feito com esta classe java para gerar o adapter
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_lista,parent,false);

        return new MyViewHolder(itemLista);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {

        final ConverteDatas converteDatas = new ConverteDatas();






                   //Aqui é onde vai mostrar os dados
        Modelo modelo = listaitens.get(position);

        String TestaDT_VCTO = modelo.getVCTO().replaceAll("Vencimento:","" );
        String TestaDT_Emissao = modelo.getEmissao().replaceAll("Emissão:","" );

        String MontaEmissao = converteDatas.ConverteData_Americano_Para_BR(TestaDT_Emissao);
        String MontaVCTO    = converteDatas.ConverteData_Americano_Para_BR(TestaDT_VCTO);


        myViewHolder.dt_VCTO.setText("Venc. : "+TestaDT_VCTO);
        myViewHolder.Emissao.setText("Emissão: "+TestaDT_Emissao);
        myViewHolder.Nome.setText(modelo.getNome());
        myViewHolder.Fone.setText(modelo.getFone());
        myViewHolder.Valor.setText(modelo.getValor());
        myViewHolder.valor_PGTO.setText(modelo.getValor_PGTO());
        myViewHolder.ID.setText(""+modelo.getID());

        //---------------------------

        // Formatando DATA
        //Pegando data de HOje
        SimpleDateFormat sdff = new SimpleDateFormat("dd/MM/yyyy");
        final String Hoje = (sdff.format(new Date()));

        //----------------------------
             //If não pago
       if (modelo.getValor_PGTO().equals("Pagamento:0") || modelo.getValor_PGTO().equals("Pagamento:0.0")) {

           //Formatando o vencimento
          // String TestaDT_PGTO = modelo.getVCTO().replaceAll("Vencimento:","" );
           SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
           Date TestaPGTO;
           Date Hoje_Data;
           //Convertendo primeira string em data
           try {
               TestaPGTO =  formato.parse(TestaDT_VCTO);
              // myViewHolder.valor_PGTO.setText(""+TestaPGTO);
                                  //Convertendo segunda string em data
                                  try {
                                       Hoje_Data =  formato.parse(Hoje);
                                      // myViewHolder.valor_PGTO.setText(""+Hoje_Data);

                                      //Se a data de Hoje for Maior que o  PGTO
                                      if (Hoje_Data.after(TestaPGTO)){
                                       myViewHolder.valor_PGTO.setBackgroundColor(Color.RED);
                                      }
                                      //Se vence hoje
                                      if (Hoje_Data.equals(TestaPGTO)){
                                          myViewHolder.valor_PGTO.setBackgroundColor(Color.YELLOW);
                                      }
                                      //Se não venceu ainda
                                      if (Hoje_Data.before(TestaPGTO)){
                                          myViewHolder.valor_PGTO.setBackgroundColor(Color.GREEN);
                                      }

                                  } catch (ParseException e) {
                                   e.printStackTrace();
                                  }//fim do segundo try





           } catch (ParseException e) {
               e.printStackTrace();
           }//fim do primeiro try




       }//Fim do if não pago
 //-------------------------------------------------------------------------------------------------------
        else {
           //Pagos
           myViewHolder.valor_PGTO.setBackgroundColor(Color.BLUE);
       }
 //-------------------------------------------------------------------------------------------------------


    }//Fim do Holder

    @Override
    public int getItemCount() {
        //Diz quantos itens mostar na lista
       //return 6;
        return listaitens.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
              //Link com o layout dessa forma
         TextView ID;
         TextView Emissao;
         TextView Nome;
         TextView Fone;
         TextView Valor;
         TextView dt_VCTO;
         TextView valor_PGTO;


         public MyViewHolder(@NonNull View itemView) {
             super(itemView);
             ID         = itemView.findViewById(R.id.ID);
             Emissao    = itemView.findViewById(R.id.emissao_ID);
             Nome       = itemView.findViewById(R.id.nome_ID);
             Fone       = itemView.findViewById(R.id.fone_Id);
             Valor      = itemView.findViewById(R.id.valor_ID);
             dt_VCTO    = itemView.findViewById(R.id.dt_VCTO_ID);
             valor_PGTO = itemView.findViewById(R.id.valor_PGTO_ID);

         }
     }


}
