class BST {
    Nodo raiz;

    public BST() {
        raiz = null;
    }

    public void inserir(Produto produto) {
        raiz = inserirRec(raiz, produto);
    }

    private Nodo inserirRec(Nodo raiz, Produto produto) {
        if (raiz == null) {
            raiz = new Nodo(produto);
            return raiz;
        }
        if (produto.codigo < raiz.produto.codigo)
            raiz.esquerda = inserirRec(raiz.esquerda, produto);
        else if (produto.codigo > raiz.produto.codigo)
            raiz.direita = inserirRec(raiz.direita, produto);
        return raiz;
    }

    public Produto buscar(int codigo) {
        return buscarRec(raiz, codigo);
    }

    private Produto buscarRec(Nodo raiz, int codigo) {
        if (raiz == null || raiz.produto.codigo == codigo)
            return raiz != null ? raiz.produto : null;
        if (raiz.produto.codigo > codigo)
            return buscarRec(raiz.esquerda, codigo);
        return buscarRec(raiz.direita, codigo);
    }

    public void remover(int codigo) {
        raiz = removerRec(raiz, codigo);
    }

    private Nodo removerRec(Nodo raiz, int codigo) {
        if (raiz == null) return raiz;
        if (codigo < raiz.produto.codigo)
            raiz.esquerda = removerRec(raiz.esquerda, codigo);
        else if (codigo > raiz.produto.codigo)
            raiz.direita = removerRec(raiz.direita, codigo);
        else {
            if (raiz.esquerda == null)
                return raiz.direita;
            else if (raiz.direita == null)
                return raiz.esquerda;
            raiz.produto = minValue(raiz.direita);
            raiz.direita = removerRec(raiz.direita, raiz.produto.codigo);
        }
        return raiz;
    }

    private Produto minValue(Nodo raiz) {
        Produto minValue = raiz.produto;
        while (raiz.esquerda != null) {
            minValue = raiz.esquerda.produto;
            raiz = raiz.esquerda;
        }
        return minValue;
    }

    public void listarProdutos() {
        listarProdutosRec(raiz);
    }

    private void listarProdutosRec(Nodo raiz) {
        if (raiz != null) {
            listarProdutosRec(raiz.esquerda);
            System.out.println(raiz.produto);
            listarProdutosRec(raiz.direita);
        }
    }
}
