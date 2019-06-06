import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorSocket extends Thread {

    private static Vector clientes;
    private Socket conexao;
    private String nomeCliente;
    private static List clienteNomes = new ArrayList();

    public ServidorSocket(Socket socket) {
        this.conexao = socket;
    }

    public boolean clienteExistente(String newName) {
        for (int i = 0; i < clienteNomes.size(); i++) {
            if (clienteNomes.get(i).equals(newName)) {
                return true;
            }
        }
        clienteNomes.add(newName);
        return false;
    }

    public void remove(String oldName) {
        for (int i = 0; i < clienteNomes.size(); i++) {
            if (clienteNomes.get(i).equals(oldName)) {
                clienteNomes.remove(oldName);
            }
        }
    }

    public static void main(String args[]) {
        clientes = new Vector();
        try {
            ServerSocket server = new ServerSocket(9999);
            System.out.println("Servidor Online !!!");
            while (true) {
                Socket conexao = server.accept();
                Thread t = new ServidorSocket(conexao);
                t.start();
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }

    public void run() {
        try {
            BufferedReader entrada
                    = new BufferedReader(new InputStreamReader(this.conexao.getInputStream()));

            PrintStream saida = new PrintStream(this.conexao.getOutputStream());
            this.nomeCliente = entrada.readLine();
            if (clienteExistente(this.nomeCliente)) {
                saida.println("Este nome ja existe! Conecte novamente com outro Nome.");
                clientes.add(saida);
                this.conexao.close();
                return;
            } else {
                System.out.println(this.nomeCliente + " : Conectado ao Servidor!");
            }
            if (this.nomeCliente == null) {
                return;
            }
            clientes.add(saida);
            String msg = entrada.readLine();
            while (msg != null && !(msg.trim().equals(""))) {
                if(msg.equalsIgnoreCase("null")){
                    break;
                }
                enviarMsgTodos(saida, " escreveu: ", msg);
                msg = entrada.readLine();
            }
            System.out.println(this.nomeCliente + " saiu do chat");
            enviarMsgTodos(saida, " saiu", " do chat!");
            remove(this.nomeCliente);
            clientes.remove(saida);
            this.conexao.close();
        } catch (IOException e) {
            System.out.println("Erro na Conexão !!!" + " IOException: " + e);
        }

    }

    public void enviarMsgTodos(PrintStream saida, String acao, String msg) throws IOException {
        Enumeration e = clientes.elements();
        while (e.hasMoreElements()) {
            PrintStream chat = (PrintStream) e.nextElement();
            if (chat != saida) {
                chat.println(this.nomeCliente + acao + msg);
            }
        }
    }
}
