package comunicacao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;


public class Cliente {
  
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String nome;
    
    public Cliente( Socket socket, String nome ){
        
        try{
            
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
            this.nome = nome;
            
        }catch( IOException e ){
            
            System.out.println("Nao foi possivel iniciar o cliente.");
            fecharTudo(socket, bufferedReader, bufferedWriter);
            
        }
        
        
    }
    
    public void mandarMensagem(){
        
        try{
            
            bufferedWriter.write(nome);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            
            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()){
                String mensagem = scanner.nextLine();
                bufferedWriter.write(nome + ": " + mensagem);
                bufferedWriter.newLine();
                bufferedWriter.flush();
                
                
            }
            
        } catch(IOException e){
            
            System.out.println("Nao foi possivel enviar mensagens.");
            fecharTudo(socket, bufferedReader, bufferedWriter);
            
        }
        
        
    }
    
    
    public void ouvirMensagem(){
        
        new Thread( new Runnable(){
            
            @Override
            public void run(){
                
                String mensagemDaConversa;
                
                while(socket.isConnected()){
                    
                    try{
                        
                        mensagemDaConversa = bufferedReader.readLine();
                        System.out.println(mensagemDaConversa);
                        
                    } catch (IOException e){
                        
                        fecharTudo(socket, bufferedReader, bufferedWriter);
                    }  
                }                         
            }
            
            
        }).start();         
    }
    
    public void fecharTudo( Socket socket, BufferedReader bufferedReader,
            BufferedWriter bufferedWriter){ 
        try{
            
            if(bufferedReader != null ){
                bufferedReader.close();
                
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
                
            }
            
            if(socket != null){
                socket.close();
                
            }
            
        }catch ( IOException e ){
           System.out.println("Nao foi possivel encerrar a conexao.");
           e.printStackTrace();   
        }
        
        
    }
    
    public static void main(String[] args) throws IOException{
        
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Entre com um nome "
                + "para a conversa em grupo:");
        
        String nome = scanner.nextLine();
        
        Socket socket = new Socket("127.0.0.1", 7776);
        
        Cliente cliente = new Cliente(socket, nome);     
        cliente.ouvirMensagem();
        cliente.mandarMensagem();
        
    }
    
    
}
