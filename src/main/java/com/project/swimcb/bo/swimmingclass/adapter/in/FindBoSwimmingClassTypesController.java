package com.project.swimcb.bo.swimmingclass.adapter.in;

import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassTypesResponse.SubType;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassTypesResponse.Type;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO")
@RestController
@RequestMapping("/api/bo/swimming-classes/types")
@RequiredArgsConstructor
public class FindBoSwimmingClassTypesController {

  @Operation(summary = "클래스 데이터 관리 - 클래스 강습형태/구분 리스트 조회")
  @GetMapping
  public FindBoSwimmingClassTypesResponse findBoSwimmingClassSubType() {
    return FindBoSwimmingClassTypesResponse.builder()
        .types(
            List.of(
                Type.builder()
                    .typeId(1L)
                    .name("단체강습")
                    .subTypes(
                        List.of(
                            SubType.builder().subTypeId(1L).name("기초").build(),
                            SubType.builder().subTypeId(2L).name("초급").build(),
                            SubType.builder().subTypeId(3L).name("중급").build(),
                            SubType.builder().subTypeId(4L).name("상급").build(),
                            SubType.builder().subTypeId(5L).name("연수").build(),
                            SubType.builder().subTypeId(6L).name("마스터즈").build()
                        )
                    ).build(),
                Type.builder()
                    .typeId(2L)
                    .name("키즈")
                    .subTypes(
                        List.of(
                            SubType.builder().subTypeId(7L).name("병아리반").build(),
                            SubType.builder().subTypeId(8L).name("유아 5세").build()
                        )
                    ).build(),
                Type.builder()
                    .typeId(3L)
                    .name("레슨")
                    .subTypes(List.of(
                        SubType.builder().subTypeId(9L).name("1:1 레슨").build()
                    )).build(),
                Type.builder()
                    .typeId(4L)
                    .name("아쿠아로빅")
                    .subTypes(List.of()).build()
            )
        )
        .build();
  }
}

