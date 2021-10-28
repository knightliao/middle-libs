package com.github.knightliao.middle.springboot.ext.bean;

import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;

import lombok.Builder;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/10/29 00:20
 */
@Slf4j
@Builder
public class TomcatConfigBean {

    @Setter
    private Integer asyncTimeout;

    @Setter
    private Integer connectCount;

    @Setter
    private Integer keepAliveCount;

    private EmbeddedServletContainerFactory servletContainer() {

        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
        factory.addConnectorCustomizers(new TomcatConnectorCustomizer() {
            @Override
            public void customize(org.apache.catalina.connector.Connector connector) {

                connector.setAsyncTimeout(asyncTimeout);

                Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();

                protocol.setMaxConnections(connectCount);
                protocol.setMaxKeepAliveRequests(keepAliveCount);

                log.info("keepAliveCount={} connectCount={} asyncTimeout={}", keepAliveCount, connectCount,
                        asyncTimeout);
            }

        });

        return factory;
    }
}
