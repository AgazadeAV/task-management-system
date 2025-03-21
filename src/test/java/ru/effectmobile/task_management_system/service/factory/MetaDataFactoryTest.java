package ru.effectmobile.task_management_system.service.factory;

import org.junit.jupiter.api.Test;
import ru.effectmobile.task_management_system.model.metadata.MetaData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MetaDataFactoryTest {

    private final MetaDataFactory metaDataFactory = new MetaDataFactory();

    @Test
    void createMetaData_ShouldSetTimestamps() {
        MetaData metaData = metaDataFactory.createMetaData();

        assertNotNull(metaData);
        assertNotNull(metaData.getCreatedAt());
        assertNotNull(metaData.getUpdatedAt());
        assertEquals(metaData.getCreatedAt(), metaData.getUpdatedAt());
    }
}
