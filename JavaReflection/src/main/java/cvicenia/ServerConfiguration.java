package cvicenia;

import java.net.InetSocketAddress;

public class ServerConfiguration {

    private static ServerConfiguration servConf;
    private final InetSocketAddress serverAdresa;
    private final String privitaciaSprava;

    private ServerConfiguration (int port, String sprava) {
        this.privitaciaSprava = sprava;
        this.serverAdresa = new InetSocketAddress("localhost",port);
        if (servConf == null) {
            servConf = this;
        }
    }

    public static ServerConfiguration getInstance() {
        //vdava vytvoreniu tejto premennej nebudem volať new ServerConfiguration zakazdym ked zavolam getInstance
        return servConf;
    }

    public InetSocketAddress getServerAdresa() {
        return serverAdresa;
    }

    public String getPrivitaciaSprava() {
        return privitaciaSprava;
    }
}
