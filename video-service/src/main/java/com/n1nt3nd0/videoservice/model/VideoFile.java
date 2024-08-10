package com.n1nt3nd0.videoservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "t_video_service")
public class VideoFile {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "video_owner_email")
    private String videoOwnerEmail;
    @Column(name = "video_name")
    private String videoName;
    @Column(name = "description", nullable = true)
    private String description;
    @Column(name = "url")
    private String url;
}
