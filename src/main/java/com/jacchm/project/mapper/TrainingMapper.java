package com.jacchm.project.mapper;

import com.jacchm.project.controller.dto.TrainingCreationDTO;
import com.jacchm.project.controller.dto.TrainingDTO;
import com.jacchm.project.controller.dto.TrainingIdDTO;
import com.jacchm.project.controller.dto.TrainingUpdateDTO;
import com.jacchm.project.model.Training;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TrainingMapper {

  Training mapTrainingCreationDTOToTraining(final TrainingCreationDTO trainingCreationDTO);

  Training mapTrainingUpdateDTOtoTraining(final TrainingUpdateDTO trainingUpdateDTO);

  TrainingIdDTO mapTrainingToTrainingIdDTO(final Training training);

  TrainingDTO mapTrainingToTrainingDTO(final Training training);
}
