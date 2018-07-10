package treeparallelbalanced;

import java.util.Stack;

public class ArvoreBalanced {
    volatile No raiz;
    
    public void inserir(int k){
	No n = new No(k);
	inserirAVL(this.raiz, n);
    }
    
    public void inserirAVL(No aComparar, No aInserir){

	if(aComparar == null)
            this.raiz = aInserir;
	else{
            if(aInserir.getChave() < aComparar.getChave()){
		if(aComparar.getEsquerda() == null){
                    aComparar.setEsquerda(aInserir);
                    aInserir.setPai(aComparar);
                    verificarBalanceamento(aComparar);
		}else
                    inserirAVL(aComparar.getEsquerda(), aInserir);
            }else if(aInserir.getChave() > aComparar.getChave()){
		if(aComparar.getDireita() == null){
                    aComparar.setDireita(aInserir);
                    aInserir.setPai(aComparar);
                    verificarBalanceamento(aComparar);
		}else
                    inserirAVL(aComparar.getDireita(), aInserir);
            }else{
		// O nó já existe
            }
	}
    }
    
    public void remover(int k){
	removerAVL(this.raiz, k);
    }

    public void removerAVL(No atual, int k){
	if(atual == null)
            return;
	else{
            if(atual.getChave() > k)
		removerAVL(atual.getEsquerda(), k);
            else if(atual.getChave() < k)
		removerAVL(atual.getDireita(), k);
            else if(atual.getChave() == k)
		removerNoEncontrado(atual);
	}
    }
    
    public void removerNoEncontrado(No aRemover){
	No r;
	if(aRemover.getEsquerda() == null || aRemover.getDireita() == null){
            if(aRemover.getPai() == null){
		this.raiz = null;
		aRemover = null;
		return;
            }
            r = aRemover;
	}else{
            r = sucessor(aRemover);
            aRemover.setChave(r.getChave());
	}
        
	No p;
	if(r.getEsquerda() != null){
            p = r.getEsquerda();
	}else{
            p = r.getDireita();
	}

	if(p != null){
            p.setPai(r.getPai());
	}

	if(r.getPai() == null){
            this.raiz = p;
	}else{
            if(r == r.getPai().getEsquerda()){
		r.getPai().setEsquerda(p);
            }else{
		r.getPai().setDireita(p);
            }
            verificarBalanceamento(r.getPai());
	}
            r = null;
    }
    
    public No buscar(int chaveQueEstaBuscando){ // buscar node com a chaveQueEstaBuscando dada
        // (assume que a arvore não é vazia)
        No nodoAtual = raiz; // começa na raizDaArvore
        while(nodoAtual.chave != chaveQueEstaBuscando){ // enquando não bater,
            if(chaveQueEstaBuscando < nodoAtual.chave) // vai esquerda?
                nodoAtual = nodoAtual.esquerda;
            else // ou vai pra direita?
                nodoAtual = nodoAtual.direita;
            if(nodoAtual == null) // se não tem filho,
                return null; // não pode busca-lo
            }
            return nodoAtual;
    }
    
    public No sucessor(No q){
        if(q.getDireita() != null){
            No r = q.getDireita();
            while(r.getEsquerda() != null) {
				r = r.getEsquerda();
			}
			return r;
		} else {
			No p = q.getPai();
			while (p != null && q == p.getDireita()) {
				q = p;
				p = q.getPai();
			}
			return p;
		}
	}
    
    // * BALANCEAMENTO *    
    public void verificarBalanceamento(No atual){
	setBalanceamento(atual);
        int balanceamento = atual.getBalanceamento();

        if(balanceamento == -2){
            if(altura(atual.getEsquerda().getEsquerda()) >= altura(atual.getEsquerda().getDireita()))
                atual = rotacaoDireita(atual);
            else
                atual = duplaRotacaoEsquerdaDireita(atual);
        }else if(balanceamento == 2){
            if(altura(atual.getDireita().getDireita()) >= altura(atual.getDireita().getEsquerda()))
                atual = rotacaoEsquerda(atual);
            else
                atual = duplaRotacaoDireitaEsquerda(atual);
        }

        if(atual.getPai() != null){
            verificarBalanceamento(atual.getPai());
        }else{
            this.raiz = atual;
        }
    }
    
    private int altura(No atual){
	if(atual == null){
            return -1;
	}
	if(atual.getEsquerda() == null && atual.getDireita() == null){
            return 0;	
	}else if(atual.getEsquerda() == null){
            return 1 + altura(atual.getDireita());
	}else if(atual.getDireita() == null){
            return 1 + altura(atual.getEsquerda());
	}else{
            return 1 + Math.max(altura(atual.getEsquerda()), altura(atual.getDireita()));
	}
    }
    
    private void setBalanceamento(No no){
	no.setBalanceamento(altura(no.getDireita()) - altura(no.getEsquerda()));
    }
    
    public No duplaRotacaoEsquerdaDireita(No inicial){
	inicial.setEsquerda(rotacaoEsquerda(inicial.getEsquerda()));
	return rotacaoDireita(inicial);
    }

    public No duplaRotacaoDireitaEsquerda(No inicial){
	inicial.setDireita(rotacaoDireita(inicial.getDireita()));
	return rotacaoEsquerda(inicial);
    }
    
    public No rotacaoEsquerda(No inicial){
	No direita = inicial.getDireita();
	direita.setPai(inicial.getPai());
        inicial.setDireita(direita.getEsquerda());

	if(inicial.getDireita() != null){
            inicial.getDireita().setPai(inicial);
	}

	direita.setEsquerda(inicial);
	inicial.setPai(direita);

	if(direita.getPai() != null){
            if(direita.getPai().getDireita() == inicial){
		direita.getPai().setDireita(direita);	
            }else if(direita.getPai().getEsquerda() == inicial){
		direita.getPai().setEsquerda(direita);
            }
	}

	setBalanceamento(inicial);
	setBalanceamento(direita);

	return direita;
    }

    public No rotacaoDireita(No inicial){
	No esquerda = inicial.getEsquerda();
	esquerda.setPai(inicial.getPai());

	inicial.setEsquerda(esquerda.getDireita());

	if(inicial.getEsquerda() != null){
            inicial.getEsquerda().setPai(inicial);
	}

	esquerda.setDireita(inicial);
	inicial.setPai(esquerda);

	if(esquerda.getPai() != null){
            if(esquerda.getPai().getDireita() == inicial){
		esquerda.getPai().setDireita(esquerda);
            }else if(esquerda.getPai().getEsquerda() == inicial){
		esquerda.getPai().setEsquerda(esquerda);
            }
	}

	setBalanceamento(inicial);
	setBalanceamento(esquerda);

	return esquerda;
    }
    
    public void percorrimento(int tipoDePercorrimento){
        switch(tipoDePercorrimento){
            case 1:
                System.out.print("\nPercorrimento em pré-ordem: ");
                percorrerEmPreOrdem(raiz);
                break;
            case 2:
                System.out.print("\nPercorrimento em ordem:  ");
                percorrerEmOrdem(raiz);
                break;
            case 3:
                System.out.print("\nPercorrimento em pós-ordem: ");
                percorrerEmPosOrdem(raiz);
                break;
        }
        System.out.println();
    }

    private void percorrerEmPreOrdem(No raizLocal){
        if(raizLocal != null){
            System.out.print(raizLocal.chave + " ");
            percorrerEmPreOrdem(raizLocal.esquerda);
            percorrerEmPreOrdem(raizLocal.direita);
        }
    }

    private void percorrerEmOrdem(No raizLocal){
        if(raizLocal != null){
            percorrerEmOrdem(raizLocal.esquerda);
            System.out.print(raizLocal.chave + " ");
            percorrerEmOrdem(raizLocal.direita);
        }
    }

    private void percorrerEmPosOrdem(No raizLocal){
        if(raizLocal != null){
            percorrerEmPosOrdem(raizLocal.esquerda);
            percorrerEmPosOrdem(raizLocal.direita);
            System.out.print(raizLocal.chave + " ");
        }
    }
    
    public void mostrarArvore(){
        // push = colocar, pop = retirar
        Stack pilhaGlobal = new Stack();
        pilhaGlobal.push(raiz);
        boolean linhaEVazia = false;
        
        System.out.println();
        
        while(linhaEVazia == false){
            Stack pilhaLocal = new Stack();
            linhaEVazia = true; // se refere ao nivel da arvore

            //for(int j = 0; j < numeroDeNodosEmBranco; j++)
                //System.out.print(' '); // mostra espaço em branco 32 vezes

            while(pilhaGlobal.isEmpty() == false){
                No nodoTemporarioDaImpressao = (No)pilhaGlobal.pop();
                
                if(nodoTemporarioDaImpressao != null){
                    System.out.print(nodoTemporarioDaImpressao.chave + ", ");
                    pilhaLocal.push(nodoTemporarioDaImpressao.esquerda);
                    pilhaLocal.push(nodoTemporarioDaImpressao.direita);

                    if(nodoTemporarioDaImpressao.esquerda != null || nodoTemporarioDaImpressao.direita != null)
                        linhaEVazia = false;
                }else{ // se nodo temporario é nulo
                    System.out.print(" - ");
                    pilhaLocal.push(null);
                    pilhaLocal.push(null);
                }
                //for(int j = 0; j < numeroDeNodosEmBranco * 2 - 2; j++) // imprime 62 espaços em branco
                    //System.out.print(' ');
            } // fim do laço da pilha global não vazia
            System.out.println();
            //numeroDeNodosEmBranco /= 2; // na 1º rodada passa a ser 16
            
            while(pilhaLocal.isEmpty() == false)
                pilhaGlobal.push(pilhaLocal.pop());
        } // fim do loop linhaEVazia é falsa
        System.out.println();
    } // fim mostrarArvore()
    
    /*
    public void printInOrder(){
        printInOrder(raiz);
    }
    private void printInOrder(node<No> tree) {
        if(tree != null){ // se a arvore não é nula
            printInOrder(tree.left); // vai para o lado esquerdo
            System.out.println(" " + tree.val); // quando chegar a um nodo vazio, imprime-o
            printInOrder(tree.right); // aqui voltou, e agora vai para o lado direito
        }
    }*/

    /*
    public void visitDepthFirst(){ // visitar o mais profundo primeiro
      visitDepthFirst(raiz);
    }

    private void visitDepthFirst(node<Tree> tree) {
        Stack<node<Tree>> stack = new Stack<node<Tree>>(); // criou uma pilha
        node<Tree> tp = tree;
        while(( tp != null ) || !stack.isEmpty() ){
            /* follow left branches as much as possible
            and keep pointer to current node 
            while( tp != null ){
                stack.push( tp ); // push = empurrar/colocar, colocar tp na pilha
                tp = tp.left;  /* visit left sub-tree e vai para o lado esquero e seque até o folha
            }
            if( !stack.isEmpty() ){ // entra aqui se a pilha esta vazia
                tp = stack.pop(); // pop = pegar/estourar, tira um elemento da pilha e guarda em tp
                System.out.print(" " + tp.val);
                tp = tp.right;  /* visit right sub-tree 
            }
        }
    }*/
} // fim classe avl