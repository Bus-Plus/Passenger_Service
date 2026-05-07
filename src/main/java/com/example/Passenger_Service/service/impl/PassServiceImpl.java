package com.example.Passenger_Service.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.Passenger_Service.dto.PassDto;
import com.example.Passenger_Service.dto.PassResponseDto;
import com.example.Passenger_Service.dto.UserPassesDto;
import com.example.Passenger_Service.model.PassEst;
import com.example.Passenger_Service.model.PassType;
import com.example.Passenger_Service.model.User;
import com.example.Passenger_Service.repository.PassEstRepository;
import com.example.Passenger_Service.repository.PassTypeRepository;
import com.example.Passenger_Service.service.PassService;
import com.example.Passenger_Service.service.UserService;

@Service
public class PassServiceImpl implements PassService {

    private final PassEstRepository passEstRepository;
    private final PassTypeRepository passTypeRepository;
    private final UserService userService;

    public PassServiceImpl(PassEstRepository passEstRepository, PassTypeRepository passTypeRepository, UserService userService) {
        this.passEstRepository = passEstRepository;
        this.passTypeRepository = passTypeRepository;
        this.userService = userService;
    }

    @Override
    public Optional<PassResponseDto> getPassDetailsByPassId(String passId) {
        return passEstRepository.findByPassId(passId)
                .flatMap(passEst -> buildResponse(passEst, passId));
    }

    @Override
    public Optional<PassResponseDto> getPassDetailsByPassIdForUser(String passId, String userId) {
        return userService.findByUserId(userId)
                .flatMap(user -> passEstRepository.findByPassId(passId)
                        .filter(passEst -> passEst.getUserId().equals(user.getId()))
                        .flatMap(passEst -> buildResponse(passEst, passId)));
    }

    @Override
    public Optional<UserPassesDto> getPassesByUserId(String userId) {
        return userService.findByUserId(userId)
                .map(this::buildUserPassesDto);
    }

    private UserPassesDto buildUserPassesDto(User user) {
        List<PassDto> passes = passEstRepository.findAllByUserId(user.getId())
                .stream()
                .map(this::buildPassDto)
                .collect(Collectors.toList());

        return new UserPassesDto(user.getId(), passes);
    }

    private PassDto buildPassDto(PassEst passEst) {
        Optional<PassType> passType = passTypeRepository.findById(passEst.getPassId());
        String passTypeName = passType.map(PassType::getPassType).orElse(null);

        return new PassDto(
                passEst.getPassId(),
                passTypeName,
                passEst.getFromLocation(),
                passEst.getToLocation(),
                passEst.getCreatedAt(),
                passEst.getExpiry());
    }

    private Optional<PassResponseDto> buildResponse(PassEst passEst, String passId) {
        Optional<PassType> passType = passTypeRepository.findById(passId);

        if (passType.isEmpty()) {
            return Optional.empty();
        }

        PassDto passDto = new PassDto(
                passId,
                passType.get().getPassType(),
                passEst.getFromLocation(),
                passEst.getToLocation(),
                passEst.getCreatedAt(),
                passEst.getExpiry());

        return Optional.of(new PassResponseDto(passEst.getUserId(), passDto));
    }
}
