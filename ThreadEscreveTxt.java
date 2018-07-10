package treeparallelbalanced;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadEscreveTxt implements Runnable{
    private int numValores;
    
    public ThreadEscreveTxt(int numValores){
        this.numValores = numValores;
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run(){
        try{
            try ( // * GRAVA VALORES GERADOS NO TXT *
                FileWriter arqSaida = new FileWriter("valores gerados pela thread.txt")){
                PrintWriter escreveEmTxt = new PrintWriter(arqSaida);
                int valorMinimoChave = 0, valorMaximoChave = 999999999, chaveAleatoriaDeNodo;
                for(int i = 0; i < numValores; i++){
                    chaveAleatoriaDeNodo = ThreadLocalRandom.current().nextInt(valorMinimoChave, valorMaximoChave+1);
                    escreveEmTxt.println(chaveAleatoriaDeNodo);
                }
            }
            
        }catch(IOException ex){
            Logger.getLogger(ThreadEscreveTxt.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}