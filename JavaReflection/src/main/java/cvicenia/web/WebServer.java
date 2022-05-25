package cvicenia.web;

import com.sun.net.httpserver.HttpServer;
import cvicenia.ServerConfiguration;

import java.io.IOException;
import java.io.OutputStream;

public class WebServer {
    public void startServer() throws IOException {
        HttpServer server = HttpServer.create(ServerConfiguration.getInstance().getServerAdresa(),0);
        server.createContext("/cau").setHandler(exchange -> {
            String response = ServerConfiguration.getInstance().getPrivitaciaSprava();
            exchange.sendResponseHeaders(200,response.length());
            OutputStream respBody = exchange.getResponseBody();
            respBody.write(response.getBytes());
            respBody.flush();
            respBody.close();
        });

        System.out.println(String.format("Server startuje na adrese %s %s"
                ,ServerConfiguration.getInstance().getServerAdresa().getHostName()
                ,ServerConfiguration.getInstance().getServerAdresa().getPort()
        ));

        server.start();
    }
}
