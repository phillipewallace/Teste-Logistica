class Nodo {
    Produto produto;
    Nodo esquerda, direita;

    public Nodo(Produto produto) {
        this.produto = produto;
        esquerda = direita = null;
    }
}
