package com.project.swimcb.notice.adapter.in;

import com.project.swimcb.notice.adapter.out.FindNoticesResponseMapper;
import com.project.swimcb.notice.application.port.in.FindNoticesUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "공지사항")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class FindNoticesController {

  private final FindNoticesUseCase useCase;
  private final FindNoticesResponseMapper mapper;

  @Operation(summary = "공지사항 리스트 조회")
  @GetMapping
  public FindNoticesResponse findNotices(
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    val notices = useCase.findNotices(PageRequest.of(page - 1, size));
    return mapper.toResponse(notices);
  }

}
