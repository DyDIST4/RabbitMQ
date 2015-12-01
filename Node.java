import com.rabbitmq.client.*;

import java.io.IOException;
import java.io.*;
import java.util.*;
public class Node {

  private static final String TASK_QUEUE_NAME = "action";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    final Connection connection = factory.newConnection();
    final Channel channel = connection.createChannel();

    channel.queueDeclare(TASK_QUEUE_NAME, false, false, false, null);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    channel.basicQos(1);

    final Consumer consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        String message = new String(body, "UTF-8");

        System.out.println(" [x] Received '" + message + "'");
        try {
          doWork(message);
        } finally {
          System.out.println(" [x] Done");
          channel.basicAck(envelope.getDeliveryTag(), false);
        }
      }
    };
    channel.basicConsume(TASK_QUEUE_NAME, false, consumer);
  }

  private static void doWork(String task) {
	int length = task.length();
	String delimitador=" ";
	String[]palabras=task.split(delimitador);
	String action=palabras[0];

	switch(action){
	case "echo":
	try{
	echo(task);}
	catch(IOException e){
}
	break;
	case "ping":
	try{
	ping(palabras[1]);
	}
	catch(IOException e){
	}
	break;
	case "remove":
        try{
        remove(palabras[1]);
        }
        catch(IOException e){
        }
        break;

default:
break;
}
}

	public static void echo(String valor)throws IOException{	
	String cmdline=valor;
	String delimitador=" ";
        String[]palabras=valor.split(delimitador);
        String texto=palabras[1];
	String fichero="/home/rabbit/"+palabras[2];
	Process p=Runtime.getRuntime().exec(cmdline);
	PrintWriter writer=new PrintWriter(fichero,"UTF-8");
	writer.println(texto);
	writer.close();
	try{
	p.waitFor();}
	catch(InterruptedException a){
	}
	BufferedReader reader=new BufferedReader(new InputStreamReader(p.getInputStream()));
	String line="";
	try{	
	while((line=reader.readLine())!=null){
	
	System.out.println(" "+line);
	}
	}
	catch(IOException e){
	System.out.println("error");	
	}
	}


public static void ping(String domain)throws IOException{        
        String command="ping -c 3 "+domain;
	
        Process p;
        try{
	p=Runtime.getRuntime().exec(new String[]{"sh","-c",command});
 try{
        p.waitFor();}
        catch(InterruptedException a){
        }

	BufferedReader reader=new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line="";
        while((line=reader.readLine())!=null){

System.out.println(line);   
	}	
	}
        
        catch(IOException e){
        System.out.println("error");    
        }

}

public static void remove(String fichero)throws IOException{        
        String command="rm /home/rabbit/"+fichero;
        
        Process p;
        try{
        p=Runtime.getRuntime().exec(new String[]{"sh","-c",command});
 try{
        p.waitFor();}
        catch(InterruptedException a){
        }

        BufferedReader reader=new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line="";
        while((line=reader.readLine())!=null){

System.out.println(line);   
        }       
        }
        
        catch(IOException e){
        System.out.println("error");  
}
}

}
