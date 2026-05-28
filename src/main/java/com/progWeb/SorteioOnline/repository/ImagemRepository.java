package com.progWeb.SorteioOnline.repository;

import com.progWeb.SorteioOnline.model.ImagemModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagemRepository extends JpaRepository<ImagemModel, Long> {
}