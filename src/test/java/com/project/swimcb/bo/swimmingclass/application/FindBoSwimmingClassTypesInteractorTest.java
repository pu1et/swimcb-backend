package com.project.swimcb.bo.swimmingclass.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassTypesResponse;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassTypesResponse.ClassSubType;
import com.project.swimcb.bo.swimmingclass.application.out.FindBoSwimmingClassTypeDsGateway;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindBoSwimmingClassTypesInteractorTest {

  @InjectMocks
  private FindBoSwimmingClassTypesInteractor interactor;

  @Mock
  private FindBoSwimmingClassTypeDsGateway gateway;

  @Test
  @DisplayName("강습형태/강습구분을 성공적으로 조회한다.")
  void shouldFindSwimmingClassTypesSuccessfully() {
    // given
    val swimmingPoolId = 1L;
    val findClassTypes = FindBoSwimmingClassTypesResponseFactory.create();

    when(gateway.findBoSwimmingClassTypes(anyLong())).thenReturn(findClassTypes);
    // when
    val response = interactor.findBoSwimmingClassTypes(swimmingPoolId);
    // then
    assertThat(response.classTypes()).hasSize(2);
    assertThat(response.classTypes().get(0).classSubTypes())
        .extracting(ClassSubType::classSubTypeId).containsExactly(1L, 2L);
    assertThat(response.classTypes().get(1).classSubTypes())
        .extracting(ClassSubType::classSubTypeId).containsExactly(3L, 4L);

    verify(gateway, only()).findBoSwimmingClassTypes(swimmingPoolId);
  }

  private static class FindBoSwimmingClassTypesResponseFactory {

    private static FindBoSwimmingClassTypesResponse create() {
      return FindBoSwimmingClassTypesResponse.builder()
          .classTypes(List.of(
              FindBoSwimmingClassTypesResponse.ClassType.builder()
                  .classTypeId(1L)
                  .name("DUMMY_CLASS_TYPE_NAME1")
                  .classSubTypes(List.of(
                      FindBoSwimmingClassTypesResponse.ClassSubType.builder()
                          .classSubTypeId(1L)
                          .name("DUMMY_CLASS_SUB_TYPENAME1")
                          .build(),
                      FindBoSwimmingClassTypesResponse.ClassSubType.builder()
                          .classSubTypeId(2L)
                          .name("DUMMY_CLASS_SUB_TYPENAME2")
                          .build()
                  ))
                  .build(),
              FindBoSwimmingClassTypesResponse.ClassType.builder()
                  .classTypeId(2L)
                  .name("DUMMY_CLASS_TYPE_NAME2")
                  .classSubTypes(List.of(
                      FindBoSwimmingClassTypesResponse.ClassSubType.builder()
                          .classSubTypeId(3L)
                          .name("DUMMY_CLASS_SUB_TYPENAME3")
                          .build(),
                      FindBoSwimmingClassTypesResponse.ClassSubType.builder()
                          .classSubTypeId(4L)
                          .name("DUMMY_CLASS_SUB_TYPENAME4")
                          .build()
                  ))
                  .build()
          )).build();
    }
  }
}