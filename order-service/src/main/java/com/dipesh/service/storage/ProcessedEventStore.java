package com.dipesh.service.storage;

public interface ProcessedEventStore
{
    boolean isDuplicate(String consumer, String eventKey);
}
