package com.example.Passenger_Service.service.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.Passenger_Service.model.PassEst;
import com.example.Passenger_Service.model.PassType;
import com.example.Passenger_Service.model.RenewRequest;
import com.example.Passenger_Service.model.RenewRequestStatus;
import com.example.Passenger_Service.repository.PassEstRepository;
import com.example.Passenger_Service.repository.PassTypeRepository;
import com.example.Passenger_Service.repository.RenewRequestRepository;
import com.example.Passenger_Service.service.RenewRequestService;

@Service
public class RenewRequestServiceImpl implements RenewRequestService {

    private static final Logger logger = LoggerFactory.getLogger(RenewRequestServiceImpl.class);
    private final RenewRequestRepository renewRequestRepository;
    private final PassTypeRepository passTypeRepository;
    private final PassEstRepository passEstRepository;

    public RenewRequestServiceImpl(RenewRequestRepository renewRequestRepository, PassTypeRepository passTypeRepository, PassEstRepository passEstRepository) {
        this.renewRequestRepository = renewRequestRepository;
        this.passTypeRepository = passTypeRepository;
        this.passEstRepository = passEstRepository;
    }

    @Override
    public RenewRequest createRenewRequest(String id, String passTypeName) {
        logger.debug("createRenewRequest called with passId='{}', passTypeName='{}'", id, passTypeName);

        if (id == null || id.isBlank()) {
            logger.debug("createRenewRequest failed: passId is null or blank");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pass id is required for renewal");
        }
        if (passTypeName == null || passTypeName.isBlank()) {
            logger.debug("createRenewRequest failed: passTypeName is null or blank");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "passTypeName is required for renewal");
        }

        logger.debug("Checking existence of pass type '{}'", passTypeName);
        if (!passTypeRepository.existsByPassType(passTypeName)) {
            logger.debug("Pass type '{}' does not exist", passTypeName);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pass type does not exist");
        }

        logger.debug("Looking up existing PassEst by id '{}'", id);
        PassEst existing = passEstRepository.findById(id)
                .orElseThrow(() -> {
                    logger.debug("PassEst not found for id '{}'", id);
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Pass record with id '" + id + "' could not be found");
                });

        logger.debug("Found PassEst with id='{}', passId='{}', userId='{}'", existing.getId(), existing.getPassId(), existing.getUserId());
        if (existing.getPassId() == null || existing.getPassId().isBlank()) {
            logger.debug("Stored PassEst has blank passId for id='{}'", id);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Stored pass record does not contain a valid pass type id");
        }

        logger.debug("Loading stored PassType by id='{}'", existing.getPassId());
        PassType storedPassType = passTypeRepository.findById(existing.getPassId())
                .orElseThrow(() -> {
                    logger.debug("PassType not found for id='{}'", existing.getPassId());
                    return new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Pass type id '" + existing.getPassId() + "' stored on this pass could not be found");
                });

        logger.debug("Stored pass type='{}' vs requested passTypeName='{}'", storedPassType.getPassType(), passTypeName);
        if (!storedPassType.getPassType().equals(passTypeName)) {
            logger.debug("Pass type mismatch for passId='{}': stored='{}', requested='{}'", id, storedPassType.getPassType(), passTypeName);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Received passTypeName does not match the pass type stored for this pass");
        }

        LocalDateTime now = LocalDateTime.now();
        logger.debug("Renewal expiry check for passId='{}' expiry='{}' now='{}'", id, existing.getExpiry(), now);
        if (!isExpiryRenewable(existing.getExpiry(), now)) {
            logger.debug("Pass expiry is not within the renewable window for passId='{}'", id);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Pass cannot be renewed until it is expired or within 15 days of expiry");
        }

        logger.debug("Checking for existing waiting renew request for passId='{}'", id);
        if (renewRequestRepository.existsByPassIdAndStatus(id, RenewRequestStatus.WAITING)) {
            logger.debug("Renew request with status WAITING already exists for passId='{}'", id);
            logger.debug("THROWING 409 CONFLICT NOW for passId='{}'", id);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A renew request is already waiting for this pass");
        }

        RenewRequest renewRequest = new RenewRequest(
                UUID.randomUUID().toString(),
                id,
                passTypeName,
                RenewRequestStatus.WAITING);

        logger.debug("Saving RenewRequest for passId='{}', passTypeName='{}'", id, passTypeName);
        RenewRequest savedRequest = renewRequestRepository.save(renewRequest);
        logger.debug("Saved RenewRequest id='{}' with status='{}'", savedRequest.getId(), savedRequest.getStatus());
        return savedRequest;
    }

    private boolean isExpiryRenewable(LocalDateTime expiry, LocalDateTime now) {
        // Allow renewal when the pass is already expired or when the expiry is within 15 days from now.
        return expiry == null || !expiry.isAfter(now) || !expiry.isAfter(now.plusDays(15));
    }

}
