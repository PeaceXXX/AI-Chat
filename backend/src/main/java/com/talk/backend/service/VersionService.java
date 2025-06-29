package com.talk.backend.service;

import com.talk.backend.model.Version;
import com.talk.backend.repository.VersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VersionService {
    @Autowired
    private VersionRepository versionRepository;

    @Transactional
    public void saveOrUpdateVersion(String versionStr) {
        Version version = versionRepository.findById(1L).orElse(new Version());
        version.setVersion(versionStr);
        versionRepository.save(version);
    }

    public String getVersion() {
        return versionRepository.findById(1L).map(Version::getVersion).orElse(null);
    }
} 