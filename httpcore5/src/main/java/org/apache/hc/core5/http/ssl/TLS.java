/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.hc.core5.http.ssl;

import java.util.ArrayList;
import java.util.List;

import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.ProtocolVersion;
import org.apache.hc.core5.util.Tokenizer;

/**
 * Supported {@code TLS} protocol versions.
 *
 * @since 5.0
 */
public enum TLS {

    V_1_0("TLSv1",   new ProtocolVersion("TLS", 1, 0)),
    V_1_1("TLSv1.1", new ProtocolVersion("TLS", 1, 1)),
    V_1_2("TLSv1.2", new ProtocolVersion("TLS", 1, 2)),
    V_1_3("TLSv1.3", new ProtocolVersion("TLS", 1, 3));

    public final String id;
    public final ProtocolVersion version;

    TLS(final String id, final ProtocolVersion version) {
        this.id = id;
        this.version = version;
    }

    public boolean isSame(final ProtocolVersion protocolVersion) {
        return version.equals(protocolVersion);
    }

    public boolean isComparable(final ProtocolVersion protocolVersion) {
        return version.isComparable(protocolVersion);
    }

    /**
     * Gets the ID.
     * @return the ID.
     *
     * @since 5.2
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the version.
     * @return the version.
     *
     * @since 5.2
     */
    public ProtocolVersion getVersion() {
        return version;
    }

    public boolean greaterEquals(final ProtocolVersion protocolVersion) {
        return version.greaterEquals(protocolVersion);
    }

    public boolean lessEquals(final ProtocolVersion protocolVersion) {
        return version.lessEquals(protocolVersion);
    }

    public static ProtocolVersion parse(final String s) throws ParseException {
        if (s == null) {
            return null;
        }
        final Tokenizer.Cursor cursor = new Tokenizer.Cursor(0, s.length());
        return TlsVersionParser.INSTANCE.parse(s, cursor, null);
    }

    public static String[] excludeWeak(final String... protocols) {
        if (protocols == null) {
            return null;
        }
        final List<String> enabledProtocols = new ArrayList<>();
        for (final String protocol : protocols) {
            if (isSecure(protocol)) {
                enabledProtocols.add(protocol);
            }
        }
        if (enabledProtocols.isEmpty()) {
            enabledProtocols.add(V_1_2.id);
        }
        return enabledProtocols.toArray(new String[0]);
    }

    /**
     * Check if a given protocol is considered secure and is enabled by default.
     *
     * @return {@code true} if the given protocol is secure and enabled, otherwise return {@code
     * false}.
     * @since 5.2
     */
    public static boolean isSecure(final String protocol) {
        return !protocol.startsWith("SSL") && !protocol.equals(V_1_0.id) && !protocol.equals(V_1_1.id);
    }

}
