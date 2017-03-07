package com.tinckay.domain.jpa;

import com.tinckay.domain.base.UpFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by root on 12/26/16.
 */
public interface UpFileRepo extends JpaRepository<UpFile,Long> {

    List<UpFile> findByUserLoginfoKey(String key);

}
