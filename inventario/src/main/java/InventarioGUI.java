import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class InventarioGUI extends JFrame {
    private BST arvore;
    private BancoDeDados banco;
    private JTextArea areaTexto;
    private JLabel statusBar;

    public InventarioGUI() {
        try {
            banco = new BancoDeDados("jdbc:mysql://localhost:3306/inventario", "usuario", "senha");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        arvore = new BST();
        setTitle("Sistema de Inventário");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Menu
        JMenuBar menuBar = new JMenuBar();
        JMenu menuArquivo = new JMenu("Arquivo");
        JMenuItem menuItemSair = new JMenuItem("Sair");
        menuItemSair.addActionListener(e -> System.exit(0));
        menuArquivo.add(menuItemSair);
        menuBar.add(menuArquivo);
        setJMenuBar(menuBar);

        // Barra de Ferramentas
        JToolBar toolBar = new JToolBar();
        JButton botaoInserir = new JButton("Inserir");
        JButton botaoBuscar = new JButton("Buscar");
        JButton botaoRemover = new JButton("Remover");
        JButton botaoListar = new JButton("Listar");
        toolBar.add(botaoInserir);
        toolBar.add(botaoBuscar);
        toolBar.add(botaoRemover);
        toolBar.add(botaoListar);
        add(toolBar, BorderLayout.NORTH);

        // Painel Principal
        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel labelCodigo = new JLabel("Código:");
        JLabel labelNome = new JLabel("Nome:");
        JLabel labelPreco = new JLabel("Preço:");
        JTextField campoCodigo = new JTextField(10);
        JTextField campoNome = new JTextField(20);
        JTextField campoPreco = new JTextField(10);

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        painelPrincipal.add(labelCodigo, gbc);

        gbc.gridx = 1;
        painelPrincipal.add(campoCodigo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        painelPrincipal.add(labelNome, gbc);

        gbc.gridx = 1;
        painelPrincipal.add(campoNome, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        painelPrincipal.add(labelPreco, gbc);

        gbc.gridx = 1;
        painelPrincipal.add(campoPreco, gbc);

        add(painelPrincipal, BorderLayout.CENTER);

        areaTexto = new JTextArea(15, 50);
        areaTexto.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaTexto);
        add(scrollPane, BorderLayout.SOUTH);

        // Barra de Status
        statusBar = new JLabel("Pronto");
        add(statusBar, BorderLayout.SOUTH);

        // Ações dos Botões
        botaoInserir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int codigo = Integer.parseInt(campoCodigo.getText());
                    String nome = campoNome.getText();
                    double preco = Double.parseDouble(campoPreco.getText());
                    Produto produto = new Produto(codigo, nome, preco);
                    arvore.inserir(produto);
                    banco.inserirProduto(produto);
                    statusBar.setText("Produto inserido com sucesso.");
                    campoCodigo.setText("");
                    campoNome.setText("");
                    campoPreco.setText("");
                    listarProdutos();
                } catch (NumberFormatException ex) {
                    statusBar.setText("Erro: Verifique os campos de entrada.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    statusBar.setText("Erro ao inserir produto no banco de dados.");
                }
            }
        });

        botaoBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int codigo = Integer.parseInt(campoCodigo.getText());
                    Produto produto = arvore.buscar(codigo);
                    if (produto != null) {
                        areaTexto.setText(produto.toString());
                        statusBar.setText("Produto encontrado.");
                    } else {
                        areaTexto.setText("Produto não encontrado");
                        statusBar.setText("Produto não encontrado.");
                    }
                } catch (NumberFormatException ex) {
                    statusBar.setText("Erro: Código inválido.");
                }
            }
        });

        botaoRemover.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int codigo = Integer.parseInt(campoCodigo.getText());
                    arvore.remover(codigo);
                    banco.removerProduto(codigo);
                    statusBar.setText("Produto removido com sucesso.");
                    campoCodigo.setText("");
                    listarProdutos();
                } catch (NumberFormatException ex) {
                    statusBar.setText("Erro: Código inválido.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    statusBar.setText("Erro ao remover produto do banco de dados.");
                }
            }
        });

        botaoListar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarProdutos();
                statusBar.setText("Listagem completa.");
            }
        });
    }

    private void listarProdutos() {
        areaTexto.setText("");
        arvore.listarProdutos();
        try {
            banco.listarProdutos();
        } catch (SQLException e) {
            e.printStackTrace();
            statusBar.setText("Erro ao listar produtos.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new InventarioGUI().setVisible(true);
            }
        });
    }
}
