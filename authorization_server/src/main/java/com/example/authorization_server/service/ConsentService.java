package com.example.authorization_server.service;


import com.example.authorization_server.entity.AccountEntity;
import com.example.authorization_server.entity.ClientPermissionEntity;
import com.example.authorization_server.entity.ConsentEntity;
import com.example.authorization_server.entity.UserEntity;
import com.example.authorization_server.entity.enums.PermissionType;
import com.example.authorization_server.repository.AccountRepository;
import com.example.authorization_server.repository.ClientPermissionsRepository;
import com.example.authorization_server.repository.ConsentRepository;
import com.example.authorization_server.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ConsentService {
    @Resource
    private ConsentRepository consentRepository;
    @Resource
    private ClientPermissionsRepository clientPermissionsRepository;
    @Resource
    private UserRepository userRepository;
    @Resource
    private AccountRepository accountRepository;

    @Transactional
    public Long saveConsent(ConsentEntity consent) {
        ConsentEntity res = consentRepository.save(consent);
        return res.getId();
    }

    @Transactional
    public List<AccountEntity> findUserAccounts(Long userId) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            return accountRepository.findAllByUser(user);
        }
        return Collections.EMPTY_LIST;
    }

    @Transactional
    public void saveClientPermissions (List<PermissionType> permissionTypes) {
        Set<ClientPermissionEntity> permissionEntities = permissionTypes.stream()
            .map(permissionType -> {
                ClientPermissionEntity entity = new ClientPermissionEntity();
                return entity.setPermissionType(permissionType);
            })
            .collect(Collectors.toSet());
        clientPermissionsRepository.saveAll(permissionEntities);
    }
}
