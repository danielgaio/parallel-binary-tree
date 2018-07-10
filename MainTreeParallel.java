package treeparallelbalanced;

import java.io.*;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

class MainTreeParallel{
    
    public static void main(String[] args){
     
        ManageThreadsForBalanced balancedWork = new ManageThreadsForBalanced();
        //ArvoreBalanced myTree = new ArvoreBalanced();
        
        try { 
            balancedWork.executar();
        } catch (IOException | InterruptedException | ExecutionException ex) {
            Logger.getLogger(MainTreeParallel.class.getName()).log(Level.SEVERE, null, ex);
        }
     
    }
    
    public static String getString() throws IOException{
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        String s = br.readLine();
        return s;
    }

    public static char getChar() throws IOException{
        String s = getString();
        return s.charAt(0);
    }

    public static int getInt() throws IOException{
        String s = getString();
        return Integer.parseInt(s);
    }
} // fim da classe