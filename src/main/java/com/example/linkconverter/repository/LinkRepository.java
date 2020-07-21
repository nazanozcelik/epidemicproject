package com.example.linkconverter.repository;


import com.example.linkconverter.model.entity.Links;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkRepository extends JpaRepository<Links, String> {
    Links findOneByWebUrl(String webUrl);

    Links findOneByDeeplink(String deeplink);

    Links findOneByShortlink(String shortlink);
}


