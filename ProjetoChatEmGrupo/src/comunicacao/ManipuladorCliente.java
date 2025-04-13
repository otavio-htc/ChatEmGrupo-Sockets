package comunicacao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;


public class ManipuladorCliente implements Runnable{
    
    public static ArrayList<ManipuladorCliente> clientes =
            new ArrayList<>();
    
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String nomeCliente;
    
    public ManipuladorCliente( Socket socket ){
        
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter( 
                    new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            this.nomeCliente = bufferedReader.readLine();
            clientes.add(this);
            mensagemBroadcast("Servidor: " + nomeCliente
                    + " entrou na conversa!");
            
            
        }catch(IOException e ){
            
            System.out.println("Nao foi possivel conectar-se ao servidor.");
            fecharTudo(socket, bufferedReader, bufferedWriter);
            
        }
        
    }
    
    
    @Override 
    public void run(){
        
        String mensagemDoCliente;
        
        while(socket.isConnected()){
            
            try{
                
                mensagemDoCliente = bufferedReader.readLine();
                mensagemBroadcast(mensagemDoCliente);
                
            } catch (IOException e){
                
                System.out.println("Nao foi possivel "
                        + "comunicar-se entre os clientes.");
                fecharTudo( socket, bufferedReader, bufferedWriter);
                break;
                
            }
            
        }
        
    }
    
    public void mensagemBroadcast( String mensagem ){
        
        for( ManipuladorCliente cliente : clientes ){
            try {
                
                if(!cliente.nomeCliente.equals(nomeCliente)){   
                    cliente.bufferedWriter.write(mensagem);
                    cliente.bufferedWriter.newLine();
                    cliente.bufferedWriter.flush();     
                }
                
            } catch(IOException e){
                System.out.println("Houve um problema "
                        + "na comunicacao broadcast");
                fecharTudo(socket, bufferedReader, bufferedWriter);
            }
            
        }
        
    }
    
    public void removerCliente(){
        
        clientes.remove(this);
        mensagemBroadcast("Servidor: " + nomeCliente +
                " deixou a conversa.");
        
    }
    
    public void fecharTudo( Socket socket, BufferedReader bufferedReader,
            BufferedWriter bufferedWriter){ 
        removerCliente();
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

    
    
}
