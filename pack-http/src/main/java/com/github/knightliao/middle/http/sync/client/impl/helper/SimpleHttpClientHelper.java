package com.github.knightliao.middle.http.sync.client.impl.helper;

import java.net.SocketTimeoutException;
import java.rmi.ServerException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.util.EntityUtils;

import com.github.knightliao.middle.http.common.service.server.IMyHttpServer;
import com.github.knightliao.middle.http.common.service.server.helper.server.ServerStatus;
import com.github.knightliao.middle.http.common.exceptions.NoServerAvailableException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/23 12:20
 */
@Slf4j
public class SimpleHttpClientHelper {

    public static String executeWithLoadBalancer(IMyHttpServer myHttpServer, String path,
                                                 IFunctionWrapper functionWrapper, long numRetries) {

        //
        ServerStatus server = myHttpServer.getServer();
        String response = execute(myHttpServer, server, path, functionWrapper);
        if (StringUtils.isNotEmpty(response)) {
            return response;
        }

        // exchange
        String failedServerId = server.getServerInstance().getId();
        List<ServerStatus> serverStatusList = myHttpServer.getAllAvailable();
        int retries = 0;
        for (retries = 0; retries < numRetries; retries++) {
            server = serverStatusList.get(retries % serverStatusList.size());
            if (failedServerId.equals(server.getServerInstance().getId())) {
                continue;
            }

            response = execute(myHttpServer, server, path, functionWrapper);
            if (StringUtils.isNotEmpty(response)) {
                return response;
            }
        }

        //
        log.error("retry {} time and not ok", retries);
        return null;
    }

    private static String execute(IMyHttpServer myHttpServer, ServerStatus serverStatus, String path,
                           IFunctionWrapper functionWrapper) {

        CloseableHttpResponse closeableHttpResponse = null;

        //
        if (serverStatus == null) {
            throw new NoServerAvailableException(myHttpServer.getName());
        }

        String newUrl = "";

        try {

            //
            newUrl = serverStatus.getServerInstance().getUrl() + path;
            closeableHttpResponse = functionWrapper.execute(newUrl);

            //
            StatusLine statusLine = closeableHttpResponse.getStatusLine();
            if (statusLine.getStatusCode() != 200) {
                log.warn("http status {}", statusLine.getStatusCode());
                throw new ServerException("server internal error " + newUrl);
            }

            //
            HttpEntity entity = closeableHttpResponse.getEntity();
            if (entity != null) {
                String data = EntityUtils.toString(entity);
                EntityUtils.consume(entity);
                return data;
            } else {
                log.warn("http entity null {}", newUrl);
            }

        } catch (ConnectTimeoutException ex) {

            log.warn("ConnectTimeoutException, retry another server " + newUrl, ex);
            serverStatus.connectTimeout();

        } catch (SocketTimeoutException ex) {

            serverStatus.checkAfterException();

        } catch (Exception ex) {

            log.warn("Exception retry another server " + newUrl, ex);
            serverStatus.checkAfterException();
            serverStatus.incrErrors();

        } finally {

            IOUtils.closeQuietly(closeableHttpResponse);
        }

        return null;
    }
}
