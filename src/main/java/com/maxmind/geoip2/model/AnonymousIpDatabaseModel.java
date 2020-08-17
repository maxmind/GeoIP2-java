package com.maxmind.geoip2.model;

import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;

public class AnonymousIpDatabaseModel {
    private final Boolean isAnonymous;
    private final Boolean isAnonymousVpn;
    private final Boolean isHostingProvider;
    private final Boolean isPublicProxy;
    private final Boolean isTorExitNode;

    @MaxMindDbConstructor
    public AnonymousIpDatabaseModel (
        @MaxMindDbParameter(name="is_anonymous") Boolean isAnonymous,
        @MaxMindDbParameter(name="is_anonymous_vpn") Boolean isAnonymousVpn,
        @MaxMindDbParameter(name="is_hosting_provider") Boolean isHostingProvider,
        @MaxMindDbParameter(name="is_public_proxy") Boolean isPublicProxy,
        @MaxMindDbParameter(name="is_tor_exit_node") Boolean isTorExitNode
    ) {
        this.isAnonymous = isAnonymous;
        this.isAnonymousVpn = isAnonymousVpn;
        this.isHostingProvider = isHostingProvider;
        this.isPublicProxy = isPublicProxy;
        this.isTorExitNode = isTorExitNode;
    }

    public boolean getIsAnonymous() {
        if (this.isAnonymous == null) {
            return false;
        }
        return this.isAnonymous;
    }

    public boolean getIsAnonymousVpn() {
        if (this.isAnonymousVpn == null) {
            return false;
        }
        return this.isAnonymousVpn;
    }

    public boolean getIsHostingProvider() {
        if (this.isHostingProvider == null) {
            return false;
        }
        return this.isHostingProvider;
    }

    public boolean getIsPublicProxy() {
        if (this.isPublicProxy == null) {
            return false;
        }
        return this.isPublicProxy;
    }

    public boolean getIsTorExitNode() {
        if (this.isTorExitNode == null) {
            return false;
        }
        return this.isTorExitNode;
    }
}
