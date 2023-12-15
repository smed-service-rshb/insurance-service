package ru.softlab.efr.services.insurance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.softlab.efr.services.insurance.repositories.ExtractRepository;

import java.util.concurrent.CountDownLatch;

@Service
public class ExtractServiceLock {

    @Autowired
    private ExtractRepository extractRepository;

    @Transactional
    public void lockExtract(String uuid, CountDownLatch lockTaken, CountDownLatch lockMustBeReleased) {
        extractRepository.findByUuidWithWriteLock(uuid);
        lockTaken.countDown();
        try {
            lockMustBeReleased.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
