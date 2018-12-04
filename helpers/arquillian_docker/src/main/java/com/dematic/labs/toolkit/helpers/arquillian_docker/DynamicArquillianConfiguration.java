/*
 * Copyright 2018 Dematic, Corp.
 * Licensed under the MIT Open Source License: https://opensource.org/licenses/MIT
 */

package com.dematic.labs.toolkit.helpers.arquillian_docker;

import com.dematic.labs.toolkit.helpers.test_util.DockerHelper;
import org.jboss.arquillian.config.descriptor.api.ContainerDef;
import org.jboss.arquillian.config.descriptor.api.ProtocolDef;
import org.jboss.arquillian.container.spi.Container;
import org.jboss.arquillian.container.spi.ContainerRegistry;
import org.jboss.arquillian.container.spi.client.protocol.ProtocolDescription;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.spi.LoadableExtension;
import org.jboss.arquillian.core.spi.ServiceLoader;

import javax.annotation.Nonnull;

public class DynamicArquillianConfiguration implements LoadableExtension {
    @Override
    public void register(LoadableExtension.ExtensionBuilder builder) {
        builder.observer(LoadContainerConfiguration.class);
    }

    public static final class LoadContainerConfiguration {
        /**
         * Method which observes {@link ContainerRegistry}. Gets called by Arquillian at startup time.
         *
         * @param registry      contains containers defined in arquillian.xml
         * @param serviceLoader
         */
        public void registerInstance(@Observes @Nonnull final ContainerRegistry registry, ServiceLoader serviceLoader) {
            final Container arquillianContainer = registry.getContainers().iterator().next();
            ContainerDef containerConfiguration = arquillianContainer.getContainerConfiguration();
            containerConfiguration.property("managementPort", DockerHelper.getPort("container.configuration.managementPort"));

            final ProtocolDef protocolConfiguration = arquillianContainer.getProtocolConfiguration(new ProtocolDescription("Servlet 3.0"));
            protocolConfiguration.property("port", DockerHelper.getPort("servlet.port"));
        }
    }
}
