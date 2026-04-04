package com.example.Passenger_Service.service.impl;

import java.util.UUID;

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
               if (id == null || id.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pass id is required for renewal");
        }
        if (passTypeName == null || passTypeName.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "passTypeName is required for renewal");
        }
        if (!passTypeRepository.existsByPassType(passTypeName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pass type does not exist");
        }

        PassEst existing = passEstRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Pass record with id '" + id + "' could not be found"));
        if (existing.getPassId() == null || existing.getPassId().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Stored pass record does not contain a valid pass type id");
        }

        PassType storedPassType = passTypeRepository.findById(existing.getPassId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Pass type id '" + existing.getPassId() + "' stored on this pass could not be found"));

        if (!storedPassType.getPassType().equals(passTypeName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Received passTypeName does not match the pass type stored for this pass");
        }

        RenewRequest renewRequest = new RenewRequest(
                UUID.randomUUID().toString(),
                id,
                passTypeName,
                RenewRequestStatus.WAITING);
        return renewRequestRepository.save(renewRequest);
    }

}
