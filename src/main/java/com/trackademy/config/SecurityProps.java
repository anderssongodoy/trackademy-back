package com.trackademy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import lombok.Getter;

@Component
@Getter
public class SecurityProps {
    
    @Value("${security.google.audience:}")
    private String googleAudience;
    
    @Value("${security.azure.tenant-id:${AZURE_AD_TENANT_ID:common}}")
    private String azureTenantId;
    
    @Value("${security.azure.client-id:${AZURE_AD_CLIENT_ID:}}")
    private String azureClientId;
}
