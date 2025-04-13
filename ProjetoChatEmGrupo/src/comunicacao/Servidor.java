package comunicacao;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Servidor {
    
    private ServerSocket ssServidor;
    
    public Servidor ( ServerSocket ssSocket ){ 
        this.ssServidor = ssSocket;  
    }
    
    public void iniciarServidor(){
        
        try{
            
            while(!ssServidor.isClosed()){
                
                Socket sCliente = ssServidor.accept();
                System.out.println("Um novo cliente conectou-se.");
                ManipuladorCliente mCliente = new ManipuladorCliente(sCliente);
                
                Thread thread = new Thread(mCliente);
                
                thread.start();
                
                
            }
            
            
        }catch (IOException e){
            
            System.out.println("Nao foi possivel iniciar o servidor.");
            e.printStackTrace();
            
        }
        
    }
    
    public void fecharServidor(){
        
        try{
            
            if(ssServidor != null){
                
                ssServidor.close();
                
            }
            
        } catch (IOException e){
            
            System.out.println("Nao foi possivel fechar o servidor.");
            e.printStackTrace();
            
        }
        
        
    }
    
    public static void main(String[] args) throws IOException{
        
        ServerSocket ssServidor = new ServerSocket(7776);
        Servidor servidor = new Servidor(ssServidor);
        servidor.iniciarServidor();
        
    }
    
    
}
