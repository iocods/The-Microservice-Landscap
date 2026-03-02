package com.iocodes.app.util.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;


@Component
public class ServiceAddressUtil {


    private String serviceAddress = null;
    private final String port;

    public ServiceAddressUtil(@Value("${server.port}") String port) {
        this.port = port;
    }

    String findMyHostname() throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        return inetAddress.getHostName();
    }

    String findMyIpAddress() throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        return inetAddress.getHostAddress();
    }

   public String getServiceAddress() {
        if(serviceAddress == null) {
            try {
                serviceAddress = findMyHostname() + "/" + findMyIpAddress() + ":" + port;
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }
        return serviceAddress;
    }
}
