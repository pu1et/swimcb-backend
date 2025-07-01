package com.project.swimcb.bo.freeswimming.application.port.out;

import java.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class DateProvider {

  public LocalDate now() {
    return LocalDate.now();
  }

}
