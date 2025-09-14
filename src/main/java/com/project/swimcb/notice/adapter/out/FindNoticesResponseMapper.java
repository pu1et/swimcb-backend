package com.project.swimcb.notice.adapter.out;

import com.project.swimcb.notice.adapter.in.FindNoticesResponse;
import com.project.swimcb.notice.domain.Notice;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class FindNoticesResponseMapper {

  public FindNoticesResponse toResponse(@NonNull Page<Notice> notices) {
    return null;
  }

}
