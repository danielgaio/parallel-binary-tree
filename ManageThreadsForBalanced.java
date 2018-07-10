package treeparallelbalanced;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

public class ManageThreadsForBalanced{
    ExecutorService pool = Executors.newFixedThreadPool(2);
    ArvoreBalanced myTree = new ArvoreBalanced();
    
    public void executar() throws IOException, InterruptedException, ExecutionException{
        
        System.out.print("N de valores: ");
        int numValores = getInt();

        long inicioContagemTempo, tempoDecorrido;
        inicioContagemTempo = System.nanoTime();
       
        int chaveAleatoriaDeNodo, valorMinimoChave = 0, valorMaximoChave = 999999999;
        
        // * GRAVA VALORES GERADOS NO TXT *
        try (FileWriter arqSaida = new FileWriter("valores gerados.txt")) {
            PrintWriter escreveEmTxt = new PrintWriter(arqSaida);
            for(int i = 0; i < numValores; i++){
                chaveAleatoriaDeNodo = ThreadLocalRandom.current().nextInt(valorMinimoChave, valorMaximoChave+1);
                escreveEmTxt.println(chaveAleatoriaDeNodo);
                myTree.inserir(chaveAleatoriaDeNodo);
            }
            arqSaida.close();
        }
    
        // * LÊ VALORES DO TXT *
//        try (FileReader arqEntrada = new FileReader("valores gerados.txt")) {
//            BufferedReader leDoTxt = new BufferedReader(arqEntrada);
//            String linha1 = leDoTxt.readLine(); // leu primeira linha
//            
//            while(linha1 != null){
//                myTree.inserir(Integer.parseInt(linha1));
//                linha1 = leDoTxt.readLine();
//            }
//            arqEntrada.close();
//        }

//        //----------- gera nums em txt com thread -------------
//        ThreadEscreveTxt whriteTxt = new ThreadEscreveTxt(numValores);
//        //----------- gera nums em txt com thread -------------
//        
//        //----------- le nums do txt com thread e insere na arvore -------------
//        ExecutorService pool = Executors.newCachedThreadPool();
//        ThreadLeTxtBalanced readTxt = new ThreadLeTxtBalanced(myTree);
//        Future<ArvoreBalanced> EstruturaArvoreTxt = pool.submit(readTxt);
//        myTree = EstruturaArvoreTxt.get();
//        //----------- le nums do txt com thread e insere na arvore -------------

        tempoDecorrido = System.nanoTime() - inicioContagemTempo;
        System.out.println("Tempo de insercao: " + tempoDecorrido + " ns | " + + tempoDecorrido * Math.pow(10, (-9)) + " s" );

        // registrar o log
        try(PrintWriter saidaParaTxt = new PrintWriter(new FileWriter("log de execução.txt", true))){ // esse true é para não sobreescrever o arquivo
            DateFormat formatoDeData = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date dataAtual = new Date();
            saidaParaTxt.println(formatoDeData.format(dataAtual));
            saidaParaTxt.println("Com balanceamento");
            saidaParaTxt.println("Quantidade: " + numValores);
            saidaParaTxt.println("Tempo de inserção: " + tempoDecorrido * Math.pow(10, (-9)) + " s");
            saidaParaTxt.println();
            saidaParaTxt.close();
        }
        
        while(true){
            System.out.print("Clique a primeira letra da opcao: ");
            System.out.print("mostrar, inserir, buscar, deletar, percorrer ou sair: ");
            int op = getChar();

            switch(op){
                case 'm':
                    myTree.mostrarArvore();
                    break;
                case 'i':
                    System.out.println("1 - Inserção unica, 2 - Inserção paralela");
                    int opInsert = getInt();
                    
                    switch(opInsert){
                        case 1:
                            System.out.print("Entre valor a inserir: ");
                            int valor = getInt();
                            myTree.inserir(valor);
                            break;
                        case 2:
                            System.out.print("Quer inserir quantos dados paralelamente? ");
                            valor = getInt();
                            
                            // aqui, usar uma thread do pool
                            inicioContagemTempo = System.nanoTime();
                            //threadInsert insertWorker = new threadInsert(myTree, valor);
                            //Future<ArvoreBalanced> insercao = pool.submit(insertWorker);
                            //myTree = insercao.get(); // e se colocar na raiz
                            threadInsert2 insertWorker = new threadInsert2(myTree.raiz, valor);
                            insertWorker.start();
                            tempoDecorrido = System.nanoTime() - inicioContagemTempo;
                            System.out.println("Tempo da insercao paralela: " + tempoDecorrido + " ns | " + + tempoDecorrido * Math.pow(10, (-9)) + " s" );
                            
                            break;
                        default:
                            System.out.println("Opção invalida!");
                    }
                    break;
                case 'b':
                    System.out.print("Digite o valor a ser buscado: ");
                    int valor = getInt();
                    No nodoEncontrado = myTree.buscar(valor);

                    if(nodoEncontrado != null)
                        System.out.print("Encontrou: " + nodoEncontrado.chave);
                    else
                        System.out.println("Nao pode buscar valor: " + valor);
                    break;
                case 'd':
                    System.out.print("Entre valor a deletar: ");
                    valor = getInt();
                    myTree.remover(valor);
                    /*boolean conseguiuDeletar;
                    if(conseguiuDeletar){
                        System.out.print("Deletado " + valor + '\n');
                    }else{
                        System.out.print("Nao pode deletar ");
                        System.out.print(valor + '\n');
                    }*/
                    break;
                case 'p':
                    System.out.print("Diga o tipo: 1(pre-ordem), 2(ordem) ou 3(pos-ordem): ");
                    valor = getInt();
                    myTree.percorrimento(valor);
                    break;
                case 's':
                    System.exit(0);
                    pool.shutdown();
                    while (!pool.isTerminated()) {}
                    System.out.println("Finished all threads");
                default:
                    System.out.println("Entrada invalida!");
            } // fim switch
        } // fim while
    } // fim executar
    
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
} // fim classe