import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ClienteSocket extends Thread {

    private Socket conexao;

    public ClienteSocket(Socket socket) {
        this.conexao = socket;
    }

    public static void main(String args[]) {
        try {
            Socket socket = new Socket("127.0.0.1", 9999);
            PrintStream saida = new PrintStream(socket.getOutputStream());
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Digite seu nome: ");
            String nome = teclado.readLine();
            saida.println(nome.toUpperCase());
            Thread thread = new ClienteSocket(socket);
            thread.start();
            String msg;
            while (true) {
                System.out.print("Digite uma Mensagem: ");
                msg = teclado.readLine();
                saida.println(msg);
            }
        } catch (IOException e) {
            System.out.println("Falha na Conexao !!!" + " IOException: " + e);
        }
    }


    public void run() {
        try {
            BufferedReader entrada
                    = new BufferedReader(new InputStreamReader(this.conexao.getInputStream()));
            String msg;
            while (true) {
                msg = entrada.readLine();
                if (msg == null) {
                    System.out.println("Conexão encerrada!");
                    System.exit(0);
                }
                System.out.println();
                System.out.println(msg);
                System.out.print("Responder > ");
            }
        } catch (IOException e) {
            System.out.println("Erro na Conexão !!!"
                    + " IOException: " + e);
        }
    }
}
