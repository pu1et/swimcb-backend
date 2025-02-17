package com.project.swimcb.bo.swimmingclass.adapter.in;

import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassTypesResponse.ClassSubType;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassTypesResponse.ClassType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO")
@RestController
@RequestMapping("/api/bo/swimming-classes/class-types")
@RequiredArgsConstructor
public class FindBoSwimmingClassTypesController {

  @Operation(summary = "클래스 데이터 관리 - 클래스 강습형태/구분 리스트 조회")
  @GetMapping
  public FindBoSwimmingClassTypesResponse findBoSwimmingClassSubType() {
    return FindBoSwimmingClassTypesResponse.builder()
        .classTypes(
            List.of(
                ClassType.builder()
                    .classTypeId(1L)
                    .name("단체강습")
                    .classSubTypes(
                        List.of(
                            ClassSubType.builder().classSubTypeId(1L).name("기초").build(),
                            ClassSubType.builder().classSubTypeId(2L).name("초급").build(),
                            ClassSubType.builder().classSubTypeId(3L).name("중급").build(),
                            ClassSubType.builder().classSubTypeId(4L).name("상급").build(),
                            ClassSubType.builder().classSubTypeId(5L).name("연수").build(),
                            ClassSubType.builder().classSubTypeId(6L).name("마스터즈").build()
                        )
                    ).build(),
                ClassType.builder()
                    .classTypeId(2L)
                    .name("키즈")
                    .classSubTypes(
                        List.of(
                            ClassSubType.builder().classSubTypeId(7L).name("병아리반").build(),
                            ClassSubType.builder().classSubTypeId(8L).name("유아 5세").build()
                        )
                    ).build(),
                ClassType.builder()
                    .classTypeId(3L)
                    .name("레슨")
                    .classSubTypes(List.of(
                        ClassSubType.builder().classSubTypeId(9L).name("1:1 레슨").build()
                    )).build(),
                ClassType.builder()
                    .classTypeId(4L)
                    .name("아쿠아로빅")
                    .classSubTypes(List.of()).build()
            )
        )
        .build();
  }
}

