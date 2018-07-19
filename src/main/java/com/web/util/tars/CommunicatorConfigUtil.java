package com.web.util.tars;

import com.qq.tars.client.Communicator;
import com.qq.tars.client.CommunicatorConfig;
import com.qq.tars.client.CommunicatorFactory;
import com.web.servant.center.TraderServantPrx;

public abstract class CommunicatorConfigUtil {

    private static CommunicatorConfig cfg = new CommunicatorConfig();

    public static TraderServantPrx getProxy(){
        cfg.setLocator("tars.tarsregistry.QueryObj@tcp -h 192.168.3.189 -p 17890");
        Communicator communicator = CommunicatorFactory.getInstance().getCommunicator(cfg);
        // = communicator.stringToProxy(TraderServantPrx.class,"Center.EFServer.TraderServantAObj");
        String strConnecter = "Center.EFServer.TraderServantAObj@tcp -h 192.168.3.167 -p 21111";
        TraderServantPrx proxy = communicator.stringToProxy(TraderServantPrx.class, strConnecter);
        return proxy;
    }
}
