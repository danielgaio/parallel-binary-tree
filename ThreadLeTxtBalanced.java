package treeparallelbalanced;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

// essa classe podia ter a função de simplesmente retornar os valores lidos do txt em um vetor para depois ser inserido na arvore
public class ThreadLeTxtBalanced implements Callable<ArvoreBalanced>{
    ArvoreBalanced localBalancedTree;
    
    public ThreadLeTxtBalanced(ArvoreBalanced BalancedTree){
        this.localBalancedTree = BalancedTree;
    }

    @Override
    public ArvoreBalanced call() throws IOException{
 
        FileReader arqEntrada = null;
        try{
            // * LÊ VALORES DO TXT *
            arqEntrada = new FileReader("valores gerados pela thread.txt");
            BufferedReader leDoTxt = new BufferedReader(arqEntrada);
            String linha1 = leDoTxt.readLine();
            while(linha1 != null){
                localBalancedTree.inserir(Integer.parseInt(linha1));
                linha1 = leDoTxt.readLine();
            }
            arqEntrada.close();
        }catch(FileNotFoundException ex){
            Logger.getLogger(ThreadLeTxtBalanced.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try{
                arqEntrada.close();
            }catch(IOException ex){
                Logger.getLogger(ThreadLeTxtBalanced.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return localBalancedTree;
    }
}
