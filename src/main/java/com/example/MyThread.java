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

    public void run(){
        try {
            while(true){

            
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                DataOutputStream out = new DataOutputStream(s.getOutputStream());
                String[] firstline = in.readLine().split(" ");
                String method = firstline[0];
                String resource = firstline[1];
                String version = firstline[2];
                System.out.println(method + " " + resource + " " + version);

                String header;
                String contentType;

                do {
                    header = in.readLine();
                    // System.out.println(header);
                } while (!header.isEmpty());

                if(resource.equals("/")){
                    resource="/index.html";
                }

                System.out.println(resource);
                File file = new File("htdocs" + resource);

                //se file esiste
                if (file.exists()) {

                    String extension = resource.substring(resource.lastIndexOf(".") + 1);
                    System.out.println(extension);
                    switch (extension) {
                        case "css":
                            contentType = "text/css";
                            break;
                        case "html":
                            contentType = "text/html";
                            break;
                        case "jpg":
                            contentType = "image/jpg";
                            break;
                        default:
                            contentType = "text/plain";
                            break;
                    }
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

                    // se il file non esiste
                } else {
                    File file_not_found_error = new File("htdocs/not_found_error.html");
                    out.writeBytes(version + " 404 Not Found\n");
                    out.writeBytes("Content-Type: text/html\n");
                    out.writeBytes("Content-Length: " + file_not_found_error.length() + "\n");
                    out.writeBytes("\n");

                    InputStream input = new FileInputStream(file_not_found_error);

                    byte[] buf = new byte[8192];

                    int n;

                    while ((n = input.read(buf)) != -1) {
                        out.write(buf, 0, n);

                    }

                    input.close();

                }
            }

            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
