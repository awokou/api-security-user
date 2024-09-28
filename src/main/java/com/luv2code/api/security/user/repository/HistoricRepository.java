package com.luv2code.api.security.user.repository;

import com.luv2code.api.security.user.entity.Historic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoricRepository  extends JpaRepository<Historic,Long> {
}
