package ru.effectmobile.task_management_system.service.factory;

import org.springframework.stereotype.Component;
import ru.effectmobile.task_management_system.model.metadata.MetaData;

import java.time.LocalDateTime;

@Component
public class MetaDataFactory {

    public MetaData createMetaData() {
        return MetaData.builder()
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
