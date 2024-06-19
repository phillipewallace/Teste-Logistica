import java.sql.*;

public class BancoDeDados {
    private Connection conexao;

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/inventario";
        String usuario = "root";
        String senha = "12345678";
    
        try {
            BancoDeDados banco = new BancoDeDados(url, usuario, senha);
            // Restante do código para interagir com o banco de dados...
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    public BancoDeDados(String url, String usuario, String senha) throws SQLException {
        conexao = DriverManager.getConnection(url, usuario, senha);
    }

    public void inserirProduto(Produto produto) throws SQLException {
        String sql = "INSERT INTO produtos (codigo, nome, preco) VALUES (?, ?, ?)";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setInt(1, produto.codigo);
        stmt.setString(2, produto.nome);
        stmt.setDouble(3, produto.preco);
        stmt.executeUpdate();
    }

    public Produto buscarProduto(int codigo) throws SQLException {
        String sql = "SELECT * FROM produtos WHERE codigo = ?";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setInt(1, codigo);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return new Produto(rs.getInt("codigo"), rs.getString("nome"), rs.getDouble("preco"));
        }
        return null;
    }

    public void removerProduto(int codigo) throws SQLException {
        String sql = "DELETE FROM produtos WHERE codigo = ?";
        PreparedStatement stmt = conexao.prepareStatement(sql);
        stmt.setInt(1, codigo);
        stmt.executeUpdate();
    }

    public void listarProdutos() throws SQLException {
        String sql = "SELECT * FROM produtos";
        Statement stmt = conexao.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            Produto produto = new Produto(rs.getInt("codigo"), rs.getString("nome"), rs.getDouble("preco"));
            System.out.println(produto);
        }
    }
}
