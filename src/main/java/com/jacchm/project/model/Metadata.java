package com.jacchm.project.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Metadata {

    @Version
    private Integer version;
    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant createdAt;
    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant modifiedAt;

}
