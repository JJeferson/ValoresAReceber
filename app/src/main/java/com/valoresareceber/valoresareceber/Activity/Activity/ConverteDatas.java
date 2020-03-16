package com.valoresareceber.valoresareceber.Activity.Activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConverteDatas {


    public static String ConverteData_BR_Para_Ingles(String RecebeEntrada){

        String r="";
        Date AjusteFormato;
        SimpleDateFormat formatoBR = new SimpleDateFormat("dd/MM/yyyy");

        SimpleDateFormat formatoAmaericano = new SimpleDateFormat("yyyy-MM-dd");

        try {
            AjusteFormato =  formatoBR.parse(RecebeEntrada);

            r = (formatoAmaericano.format(AjusteFormato));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //  int r = 0;
       // r= n1+n2+n3;

        return(r);
    }

    public static String ConverteData_Americano_Para_BR(String RecebeEntrada){

        String r="";
        Date AjusteFormato;
        SimpleDateFormat formatoBR = new SimpleDateFormat("dd/MM/yyyy");

        SimpleDateFormat formatoAmaericano = new SimpleDateFormat("yyyy-MM-dd");

        try {
            AjusteFormato =  formatoAmaericano.parse(RecebeEntrada);

            r = (formatoBR.format(AjusteFormato));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //  int r = 0;
        // r= n1+n2+n3;

        return(r);
    }



}//fim do java
