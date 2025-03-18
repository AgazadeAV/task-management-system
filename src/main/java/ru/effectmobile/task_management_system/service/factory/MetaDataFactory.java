package ru.effectmobile.task_management_system.service.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.effectmobile.task_management_system.model.metadata.MetaData;

import java.time.LocalDateTime;

@Slf4j
@Component
public class MetaDataFactory {

    public MetaData createMetaData() {
        LocalDateTime now = LocalDateTime.now();
        log.debug("Creating MetaData with timestamps: createdAt={}, updatedAt={}", now, now);

        MetaData metaData = MetaData.builder()
                .createdAt(now)
                .updatedAt(now)
                .build();

        log.debug("MetaData created successfully: {}", metaData);
        return metaData;
    }
}
