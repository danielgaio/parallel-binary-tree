package treeparallelbalanced;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class threadInsert2 extends Thread{
    private int valor;
    No no;
    
    
    threadInsert2(No no, int valor){
        //this.myLocalTree = myTree;
        this.no = no;
        this.valor = valor;
    }
    ArvoreBalanced myLocalTree = new ArvoreBalanced();

    @Override
    public void run(){
        FileWriter insercoesParalelas = null;
        // * GRAVA VALORES GERADOS NO TXT *
        try {
            insercoesParalelas = new FileWriter("valores inserção paralela.txt");
            PrintWriter escreveEmTxt2 = new PrintWriter(insercoesParalelas);
            int valorMinimoChave = 0, valorMaximoChave = 999999999, chaveAleatoriaDeNodo;
            for(int i = 0; i < valor; i++){ 
                chaveAleatoriaDeNodo = ThreadLocalRandom.current().nextInt(valorMinimoChave, valorMaximoChave+1);
                escreveEmTxt2.println(chaveAleatoriaDeNodo);
                //myLocalTree.inserir(chaveAleatoriaDeNodo);
            }
            insercoesParalelas.close();
        } catch (IOException ex) {
            Logger.getLogger(threadInsert.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                insercoesParalelas.close();
            } catch (IOException ex) {
                Logger.getLogger(threadInsert.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
            
        // * LÊ VALORES DO TXT *
        try (FileReader arqEntrada = new FileReader("valores inserção paralela.txt")) {
            BufferedReader leDoTxt = new BufferedReader(arqEntrada);
            String linha1 = leDoTxt.readLine(); // leu primeira linha

            while(linha1 != null){
                myLocalTree.inserir(Integer.parseInt(linha1));
                linha1 = leDoTxt.readLine();
            }
            arqEntrada.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(threadInsert2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(threadInsert2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}