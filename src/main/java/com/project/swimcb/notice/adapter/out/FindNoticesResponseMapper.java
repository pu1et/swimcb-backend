package com.project.swimcb.notice.adapter.out;

import com.project.swimcb.notice.adapter.in.FindNoticesResponse;
import com.project.swimcb.notice.adapter.in.FindNoticesResponse.NoticeResponse;
import com.project.swimcb.notice.domain.Notice;
import lombok.NonNull;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

@Service
public class FindNoticesResponseMapper {

  public FindNoticesResponse toResponse(@NonNull Page<Notice> notices) {
    val response = notices.getContent()
        .stream()
        .map(i -> NoticeResponse.builder()
            .noticeId(i.noticeId())
            .title(i.title())
            .content(i.content())
            .date(i.date())
            .build()
        ).toList();

    return new FindNoticesResponse(
        new PageImpl<>(response, notices.getPageable(), notices.getTotalElements())
    );
  }

}
