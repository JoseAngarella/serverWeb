package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class MyThread extends Thread {
    private Socket s;


    public MyThread(Socket s) {
        this.s = s;
    }

    public String traduzioneEstensioni(String resource){
        String contentType="";
        String extension = resource.substring(resource.lastIndexOf(".") + 1);
                    System.out.println(extension);
                    switch (extension) {
                        case "png":
                            contentType = "image/png";
                            break;
                        case "css":
                            contentType = "text/css";
                            break;
                        case "html":
                        case "htm":
                            contentType = "text/html";
                            break;
                        case "jpg":
                        case "jpeg":
                            contentType = "image/jpg";
                            break;
                        case "js":
                            contentType="application/javascript";
                            break;
                        default:
                            contentType = "text/plain";
                            break;
                    }
        return contentType;
    }

    public void inviaFile(File file, DataOutputStream out, String version, String resource){
             try {
                String contentType=traduzioneEstensioni(resource);
                    
                    out.writeBytes(version + " 200 OK\n");
                    out.writeBytes("Content-Type: " + contentType + "\n");
                    out.writeBytes("Content-Length: " + file.length() + "\n");
                    out.writeBytes("\n");
                    InputStream input = new FileInputStream(file);
                    byte[] buf = new byte[8192];
                    int n;
                    while ((n = input.read(buf)) != -1) {
                        out.write(buf, 0, n);
                    }
                    input.close();
                    System.out.println("fine");
             } catch (Exception e) {
                e.printStackTrace();
             }       
                    

    }

    public String ottieniRisorsa(String resource){
        if(resource.equals("/")){
            resource="/index.html";
        }
        //+ possibili futuri controlli
        return resource;
    }

    public void run(){
        try {
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                DataOutputStream out = new DataOutputStream(s.getOutputStream());


                String[] firstline = in.readLine().split(" ");
                String method = firstline[0];
                String resource = firstline[1];
                String version = firstline[2];
                System.out.println(method + " " + resource + " " + version);

                String header;

                do {
                    header = in.readLine();
                    // System.out.println(header);
                } while (!header.isEmpty());

                resource=ottieniRisorsa(resource);

                File file = new File("htdocs" + resource);

                //se file esiste
                if (file.exists()) {
                    inviaFile(file, out, version, resource);
                    
                    // se il file non esiste
                } else {
                    File file_error = new File("htdocs/not_found_error.html");
                    inviaFile(file_error, out , version, "not_found_error.html");

                }
                s.close();


            

        } catch (Exception e) {
            e.printStackTrace();

        }
  
    }
}
