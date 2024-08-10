package com.n1nt3nd0.videoservice.repository;

import com.n1nt3nd0.videoservice.model.VideoFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoFileRepository extends JpaRepository<VideoFile, Long> {
}
