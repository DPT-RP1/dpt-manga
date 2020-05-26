package com.sony.dpt.network;

import org.apache.ftpserver.ConnectionConfigFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.ConcurrentLoginPermission;
import org.apache.ftpserver.usermanager.impl.TransferRatePermission;
import org.apache.ftpserver.usermanager.impl.WritePermission;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FtpMount {

    private final FtpServer ftpServer;

    public FtpMount(final String path, int port) {

        // Create folder if not exists
        File root = new File(path);
        root.mkdirs();

        FtpServerFactory serverFactory = new FtpServerFactory();

        ConnectionConfigFactory connectionConfigFactory = new ConnectionConfigFactory();
        connectionConfigFactory.setAnonymousLoginEnabled(true);

        serverFactory.setConnectionConfig(connectionConfigFactory.createConnectionConfig());

        BaseUser user = new BaseUser();
        user.setName("anonymous");
        user.setHomeDirectory(path);
        user.setEnabled(true);
        user.setMaxIdleTime(60);

        List<Authority> ADMIN_AUTHORITIES = new ArrayList<>();
        ADMIN_AUTHORITIES.add(new WritePermission());
        ADMIN_AUTHORITIES.add(new ConcurrentLoginPermission(10, 10));
        ADMIN_AUTHORITIES.add(new TransferRatePermission(Integer.MAX_VALUE, Integer.MAX_VALUE));
        user.setAuthorities(ADMIN_AUTHORITIES);

        try {
            serverFactory.getUserManager().save(user);
        } catch (FtpException e) {
            e.printStackTrace();
        }

        ListenerFactory factory = new ListenerFactory();
        factory.setPort(port);

        serverFactory.addListener("default", factory.createListener());
        serverFactory.getFtplets().put(FTPLetImpl.class.getName(), new FTPLetImpl());
        ftpServer = serverFactory.createServer();
    }

    public void mount() throws FtpException {
        ftpServer.start();
    }

}

