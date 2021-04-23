package com.example.authorization_server.dto;


import com.example.authorization_server.entity.enums.PermissionType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
public class Consent {
    private Long consentId;
    private Set<PermissionType> permissions;
}
